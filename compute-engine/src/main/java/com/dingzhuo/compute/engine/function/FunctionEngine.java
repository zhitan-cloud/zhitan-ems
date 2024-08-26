package com.dingzhuo.compute.engine.function;


import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;

/**
 * @author fanxinfu
 */
public class FunctionEngine {

  private static ThreadLocal<FunctionEngine> function = ThreadLocal
      .withInitial(FunctionEngine::new);
  private FelEngine felEngine;

  private FunctionEngine() {
    felEngine = new FelEngineImpl();
    felEngine.addFun(CustomFunction.accumulate);
    felEngine.addFun(CustomFunction.get);
    felEngine.addFun(CustomFunction.limitPeriodAlarm);
    felEngine.addFun(CustomFunction.limitRealtimeAlarm);
  }

  public static FelEngine getInstance() {
    return function.get().felEngine;
  }
}
