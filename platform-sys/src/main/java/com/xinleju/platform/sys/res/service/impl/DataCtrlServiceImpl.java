package com.xinleju.platform.sys.res.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.res.dao.DataCtrlDao;
import com.xinleju.platform.sys.res.dto.DataCtrlDto;
import com.xinleju.platform.sys.res.dto.DataItemDto;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.DataPointDto;
import com.xinleju.platform.sys.res.entity.DataCtrl;
import com.xinleju.platform.sys.res.entity.DataItem;
import com.xinleju.platform.sys.res.service.DataCtrlService;
import com.xinleju.platform.sys.res.service.DataItemService;
import com.xinleju.platform.sys.res.service.DataPointService;

/**
 * @author admin
 * 
 * 
 */
@Service
public class DataCtrlServiceImpl extends  BaseServiceImpl<String,DataCtrl> implements DataCtrlService{
	

	@Autowired
	private DataCtrlDao dataCtrlDao;
	@Autowired
	private DataItemService dataItemService;

	@Override
	public List<DataNodeDto> queryListDataItem(String id) throws Exception {
	List<DataItemDto> dataItemDto = dataCtrlDao.querylistDataItem(id);
	List<DataNodeDto>list=new ArrayList<DataNodeDto>();
	for (DataItemDto dataItem : dataItemDto) {
		DataNodeDto dataNode=new DataNodeDto();
		String ctrlId = dataItem.getCtrlId();
		BeanUtils.copyProperties(dataItem, dataNode);
		dataNode.setParentId(ctrlId);
		list.add(dataNode);
	}
		return list;
	}

	@Override
	public DataCtrlDto getDataPointAll(String id) throws Exception {
		DataCtrl dataCtrl = dataCtrlDao.getObjectById(id);
		DataCtrlDto dataCtrlDto =new DataCtrlDto();
		BeanUtils.copyProperties(dataCtrl, dataCtrlDto);
		List<DataItemDto> dataItemDto = dataCtrlDao.querylistDataItem(id);	
		for (DataItemDto dataItem : dataItemDto) {
			List<DataPointDto> pointDtoList = dataItemService.getDataPointDtoList(dataItem.getId());
			dataItem.setList(pointDtoList);
		}
		dataCtrlDto.setList(dataItemDto);
		return dataCtrlDto;
	}
	

}
