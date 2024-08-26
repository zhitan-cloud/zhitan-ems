package com.dingzhuo.compute.engine.config;

import akka.dispatch.Envelope;
import akka.dispatch.UnboundedStablePriorityMailbox;
import java.util.Comparator;

public class ExecutePrioMailBox extends UnboundedStablePriorityMailbox {

  public ExecutePrioMailBox(Comparator<Envelope> cmp, int initialCapacity) {
    super(cmp, initialCapacity);
  }

  public ExecutePrioMailBox(Comparator<Envelope> cmp) {
    super(cmp);
  }
}
