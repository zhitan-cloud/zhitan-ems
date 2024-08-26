package com.dingzhuo.energy.project.basicSetup.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dingzhuo.energy.project.basicSetup.domain.SysSvgInfo;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.basicSetup.mapper.SysEquipmentfileMapper;
import com.dingzhuo.energy.project.basicSetup.domain.SysEquipmentfile;
import com.dingzhuo.energy.project.basicSetup.service.ISysEquipmentfileService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 组态图Service业务层处理
 *
 * @author sys
 * @date 2020-02-24
 */
@Service
public class SysEquipmentfileServiceImpl implements ISysEquipmentfileService {

  @Autowired
  private SysEquipmentfileMapper equipmentfileMapper;

  @Override
  public void saveEquipmentFile(SysEquipmentfile sysEquipmentfile) {
    equipmentfileMapper.saveEquipmentFile(sysEquipmentfile);
  }

  @Override
  public void saveSettingInfo(String nodeId, List<SysSvgInfo> svgInfo) {
    equipmentfileMapper.saveSettingInfo(nodeId, svgInfo);
  }

  @Override
  public SysEquipmentfile getConfigure(String nodeId) {
    SysEquipmentfile sysEquipmentfile = equipmentfileMapper.getConfigure(nodeId);
    List<SysSvgInfo> infos = getConfigureTag(nodeId);
    if (sysEquipmentfile != null) {
      sysEquipmentfile.setInfoList(infos);
    }

    return sysEquipmentfile;
  }

  @Override
  public List<SysSvgInfo> getConfigureTag(String nodeId) {
    return equipmentfileMapper.getConfigureTag(nodeId);
  }
}
