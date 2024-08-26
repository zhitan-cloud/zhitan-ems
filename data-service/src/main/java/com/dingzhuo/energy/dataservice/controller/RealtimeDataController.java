package com.dingzhuo.energy.dataservice.controller;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.domain.TagValueResult;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rtdb")
public class RealtimeDataController extends BaseController {

  @Autowired
  private RealtimeDatabaseService realtimeDatabaseService;

  @Autowired
  private IEnergyIndexService energyIndexService;

  @GetMapping("/retrieve/{tagCodes}")
  @ResponseBody
  public AjaxResult getLiveData(@PathVariable("tagCodes") String[] tagCodes) {
    List<String> codes = new ArrayList<>(Arrays.asList(tagCodes));
    List<TagValue> tagValues = realtimeDatabaseService.retrieve(codes);
    List<EnergyIndex> energyIndices = energyIndexService.getEnergyIndexMeterByCodes(codes);
    Map<String, EnergyIndex> mapValue = energyIndices.stream()
        .collect(Collectors.toMap(EnergyIndex::getCode, m -> m));
    List<TagValueResult> results = new ArrayList<>();
    if (!tagValues.isEmpty()) {
      tagValues.forEach(value -> {
        TagValueResult result = new TagValueResult();
        if (mapValue.containsKey(value.getTagCode())) {
          EnergyIndex energyIndex = mapValue.get(value.getTagCode());
          result.setTagName(energyIndex.getName());
          result.setUnitId(energyIndex.getUnitId());
          result.setMeteName(energyIndex.getMeterName());
        }

        result.setTagCode(value.getTagCode());
        result.setValue(value.getValue());
        result.setDataTime(value.getDataTime());
        result.setQuality(value.getQuality());
        results.add(result);
      });
    }
    return AjaxResult.success(results);
  }
}
