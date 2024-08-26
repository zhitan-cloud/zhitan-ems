package com.dingzhuo.energy.dataservice.data.influxdb;

import com.jsoniter.JsonIterator;
import com.jsoniter.spi.Decoder;

import java.io.IOException;
import java.time.Instant;

public class DecodeInstant implements Decoder {

  @Override
  public Object decode(JsonIterator jsonIterator) throws IOException {
    return Instant.ofEpochMilli(jsonIterator.readLong());
  }
}
