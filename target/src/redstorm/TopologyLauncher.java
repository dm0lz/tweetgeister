package redstorm;

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import redstorm.storm.jruby.JRubyBolt;
import redstorm.storm.jruby.JRubySpout;


public class TopologyLauncher extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'java'\n" +
            "\n" +
            "# see https://github.com/colinsurprenant/redstorm/issues/7\n" +
            "module Backtype\n" +
            "  java_import 'backtype.storm.Config'\n" +
            "end\n" +
            "\n" +
            "java_import 'backtype.storm.LocalCluster'\n" +
            "java_import 'backtype.storm.StormSubmitter'\n" +
            "java_import 'backtype.storm.topology.TopologyBuilder'\n" +
            "java_import 'backtype.storm.tuple.Fields'\n" +
            "java_import 'backtype.storm.tuple.Tuple'\n" +
            "java_import 'backtype.storm.tuple.Values'\n" +
            "\n" +
            "java_import 'redstorm.storm.jruby.JRubyBolt'\n" +
            "java_import 'redstorm.storm.jruby.JRubySpout'\n" +
            "\n" +
            "java_package 'redstorm'\n" +
            "\n" +
            "# TopologyLauncher is the application entry point when launching a topology. Basically it will \n" +
            "# call require on the specified Ruby topology class file path and call its start method\n" +
            "class TopologyLauncher\n" +
            "\n" +
            "  java_signature 'void main(String[])'\n" +
            "  def self.main(args)\n" +
            "    unless args.size > 1\n" +
            "      puts(\"Usage: redstorm local|cluster topology_class_file_name\")\n" +
            "      exit(1)\n" +
            "    end\n" +
            "\n" +
            "    env = args[0].to_sym\n" +
            "    class_path = args[1]\n" +
            "\n" +
            "    launch_path = Dir.pwd\n" +
            "    $:.unshift File.expand_path(launch_path)\n" +
            "    $:.unshift File.expand_path(launch_path + '/lib')\n" +
            "    $:.unshift File.expand_path(launch_path + '/target/lib')\n" +
            "\n" +
            "    require \"#{class_path}\" \n" +
            "\n" +
            "    topology_name = RedStorm::Configuration.topology_class.respond_to?(:topology_name) ? \"/#{RedStorm::Configuration.topology_class.topology_name}\" : ''\n" +
            "    puts(\"RedStorm v#{RedStorm::VERSION} starting topology #{RedStorm::Configuration.topology_class.name}#{topology_name} in #{env.to_s} environment\")\n" +
            "    RedStorm::Configuration.topology_class.new.start(class_path, env)\n" +
            "  end\n" +
            "\n" +
            "  private \n" +
            "\n" +
            "  def self.camel_case(s)\n" +
            "    s.to_s.gsub(/\\/(.?)/) { \"::#{$1.upcase}\" }.gsub(/(?:^|_)(.)/) { $1.upcase }\n" +
            "  end\n" +
            "end\n" +
            "").toString();
        __ruby__.executeScript(source, "/home/fetcher/.rvm/gems/jruby-1.6.7.2/bundler/gems/redstorm-ddff6b65aed9/lib/red_storm/topology_launcher.rb");
        RubyClass metaclass = __ruby__.getClass("TopologyLauncher");
        metaclass.setRubyStaticAllocator(TopologyLauncher.class);
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: TopologyLauncher");
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private TopologyLauncher(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new TopologyLauncher(ruby, metaClass);
    }
        
    /**
     * Default constructor. Invokes this(Ruby, RubyClass) with the classloader-static
     * Ruby and RubyClass instances assocated with this class, and then invokes the
     * no-argument 'initialize' method in Ruby.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public TopologyLauncher() {
        this(__ruby__, __metaclass__);
        RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "initialize");
    }

    
    public static void main(String[] args) {
        IRubyObject ruby_args = JavaUtil.convertJavaToRuby(__ruby__, args);
        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __metaclass__, "main", ruby_args);
        return;

    }

    
    public static Object camel_case(Object s) {
        IRubyObject ruby_s = JavaUtil.convertJavaToRuby(__ruby__, s);
        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __metaclass__, "camel_case", ruby_s);
        return (Object)ruby_result.toJava(Object.class);

    }

}
