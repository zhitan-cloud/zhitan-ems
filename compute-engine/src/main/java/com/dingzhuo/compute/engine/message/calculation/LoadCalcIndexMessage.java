package com.dingzhuo.compute.engine.message.calculation;

import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.energy.data.model.domain.IndexStorage;

/**
 * @author fanxinfu
 */
public class LoadCalcIndexMessage extends BaseActorMessage {

  private IndexStorage indexStorage;

  public LoadCalcIndexMessage(IndexStorage indexStorage) {
    super(ActorUtil.buildActorId(indexStorage));
    this.indexStorage = indexStorage;
  }

  public IndexStorage getIndexStorage() {
    return indexStorage;
  }

  public void setIndexStorage(IndexStorage indexStorage) {
    this.indexStorage = indexStorage;
  }
}
