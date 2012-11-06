$:.unshift File.dirname(__FILE__) + '/../../../'

require 'bundler/setup'
require 'redis'
require 'thread'
require 'lib/tweitgeist/twitter/twitter_stream'
require 'config/twitter_reader'
require "tweetstream"
require "pry"

module Tweitgeist

  class TwitterReader
    attr_accessor :config

    STATS_INTERVAL = 10
    
    def initialize
      @redis = Redis.new(:host => "localhost", :port => 6379, :driver => :hiredis)
      @tweets = Queue.new
      @tweets_count = 0
      @tweets_count_mutex = Mutex.new
      @flusher = detach_flusher
    end

    def start
      #stream = TwitterStream.new(:path => '/1/statuses/sample.json', :auth => "#{CONFIG[:twitter_user]}:#{CONFIG[:twitter_pwd]}")
      
        TweetStream.configure do |config|
          config.consumer_key       = "qSXgYnfl9TQHL0rcRw"
          config.consumer_secret    = "zhzEDv12a87RimkvaisEi5IZvqISzEVmf2gDIJQCuw"
          config.oauth_token        = "308762265-ahoa4FzpufTaPRghxHsChqwTwMRIRaJfHkmkn5Ip"
          config.oauth_token_secret = "UwAWZJlhSGiBNgbHtDqiS2ILZJuprd091rUNbETE"
          config.auth_method        = :oauth
        end
      
      @stream ||= TweetStream::Client.new
              
      puts("TwitterReader starting")

      i = 0
      #stream.on_item do |item|
      #  @tweets << item
      #  @tweets_count_mutex.synchronize {@tweets_count += 50} if (i += 1) % 50 == 0
      #end

      #stream.on_error {|message| puts("stream error=#{message}")}
      #stream.on_failure {|message| puts("stream failure=#{message}")}
      #stream.on_reconnect {|timeout, retries| puts("stream reconnect timeout=#{timeout}, retries=#{retries}")}

      puts("opening stream connection")
      #begin
      #@stream = TweetStream::Client.new
        #@stream = Twitter::JSONStream.connect(@options)
        @stream.userstream do |status|
          @tweets << status.to_hash
          #binding.pry
          puts status.text
        end
        #TweetStream::Client.new.sample do |status|
        ## The status object is a special Hash with
        ## method access to its keys.
        #  puts "#{status.text}"
        #end
      #ensure
      #  puts("closing stream connection")
      #  @stream.stop
      #end
    end     

    private

    def detach_flusher
      Thread.new do
        Thread.current.abort_on_exception = true

        previous_tweets_count = @tweets_count_mutex.synchronize {@tweets_count}
        stats_start = Time.now.to_i
        loop do
          sleep(1)

          if (size = @tweets.size) > 0
            @redis.pipelined do
              binding.pry
              #size.times.each {@redis.rpush("twitter_stream", @tweets.pop)}
              size.times.each {@redis.set("twitter_stream", @tweets.pop)}
            end
          end

          if (elapsed = (Time.now.to_i - stats_start)) >= STATS_INTERVAL
            tweets_count = @tweets_count_mutex.synchronize {@tweets_count} 
            rate = (tweets_count - previous_tweets_count) / elapsed
            previous_tweets_count = tweets_count
            stats_start = Time.now.to_i
            @redis.rpush("stream_rate", rate)

            puts("twitter reader: rate=#{rate}/s, queue size=#{size}, tweets count=#{tweets_count}")
          end
        end
      end
    end

  end
end

loop do
  begin
    Tweitgeist::TwitterReader.new.start
  rescue
    puts("TwitterReader exception=##{$!.inspect}")
  end
end