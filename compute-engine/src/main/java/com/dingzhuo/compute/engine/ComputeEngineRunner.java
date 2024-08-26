package com.dingzhuo.compute.engine;

import akka.actor.ActorSystem;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import com.dingzhuo.compute.engine.actor.alarm.AlarmTimerActor;
import com.dingzhuo.compute.engine.actor.alarm.LoadAlarmActor;
import com.dingzhuo.compute.engine.actor.alarm.PeriodAlarmActor;
import com.dingzhuo.compute.engine.actor.alarm.RealtimeAlarmActor;
import com.dingzhuo.compute.engine.actor.device.DeviceStatusActor;
import com.dingzhuo.compute.engine.actor.device.DeviceStatusTimerActor;
import com.dingzhuo.compute.engine.actor.device.LoadDeviceStatusActor;
import com.dingzhuo.compute.engine.actor.indexcalc.CalculationIndexActor;
import com.dingzhuo.compute.engine.actor.indexcalc.LoadIndexActor;
import com.dingzhuo.compute.engine.actor.indexcalc.SavePeriodActor;
import com.dingzhuo.compute.engine.actor.indexcalc.TimerActor;
import com.dingzhuo.compute.engine.actor.monitor.RecastDataActor;
import com.dingzhuo.compute.engine.config.CalcMessageExtractor;
import com.dingzhuo.compute.engine.config.EngineArgOption;
import com.dingzhuo.compute.engine.utils.SpringAkkaExtension;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component
public class ComputeEngineRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final ActorSystem actorSystem;
  private final SpringAkkaExtension akkaExt;

  public ComputeEngineRunner(
      ActorSystem actorSystem, SpringAkkaExtension springAkkaExtension) {
    this.actorSystem = actorSystem;
    this.akkaExt = springAkkaExtension;
  }

  @Override
  public void run(String... args) {
    EngineArgOption argOption = new EngineArgOption();
    CmdLineParser cmdLineParser = new CmdLineParser(argOption);
    if (args.length == 0) {
      showHelp(cmdLineParser);
      return;
    }

    try {
      cmdLineParser.parseArgument(args);
    } catch (CmdLineException e) {
      logger.error(e.getLocalizedMessage());
    }

    if (argOption.isCalc()) {
      actorSystem
          .actorOf(akkaExt.props(CalculationIndexActor.ACTOR_NAME),
              CalculationIndexActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(TimerActor.ACTOR_NAME), TimerActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(LoadIndexActor.ACTOR_NAME), LoadIndexActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(SavePeriodActor.ACTOR_NAME), SavePeriodActor.ACTOR_NAME);
    }

    if (argOption.isAlarm()) {
      actorSystem.actorOf(akkaExt.props(LoadAlarmActor.ACTOR_NAME), LoadAlarmActor.ACTOR_NAME);
      actorSystem
          .actorOf(akkaExt.props(RealtimeAlarmActor.ACTOR_NAME), RealtimeAlarmActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(PeriodAlarmActor.ACTOR_NAME), PeriodAlarmActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(AlarmTimerActor.ACTOR_NAME), AlarmTimerActor.ACTOR_NAME);
    }

    if (argOption.isDevice()) {
      actorSystem.actorOf(akkaExt.props(LoadDeviceStatusActor.ACTOR_NAME),
          LoadDeviceStatusActor.ACTOR_NAME);
      actorSystem.actorOf(akkaExt.props(DeviceStatusTimerActor.ACTOR_NAME),
          DeviceStatusTimerActor.ACTOR_NAME);
      actorSystem
          .actorOf(akkaExt.props(DeviceStatusActor.ACTOR_NAME), DeviceStatusActor.ACTOR_NAME);
    }

    if (argOption.isRecast()) {
      actorSystem.actorOf(akkaExt.props(RecastDataActor.ACTOR_NAME), RecastDataActor.ACTOR_NAME);
    }
  }

  private void showHelp(CmdLineParser cmdLineParser) {
    System.out.println("参数说明：");
    cmdLineParser.printUsage(System.out);
  }
}
