package com.dingzhuo.energy.dataservice.controller;

import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dataService")
public class PeriodDataController {

  @Autowired
  private PeriodDataService periodDataService;

  @GetMapping
  @ResponseBody
  public AjaxResult test() {
    DataItem item = periodDataService.getDataByIndex("b2f4b0b7-19e3-4dc2-9e4c-ce7a8ba1710e", "ddd");
    return AjaxResult.success(item);
  }

}
