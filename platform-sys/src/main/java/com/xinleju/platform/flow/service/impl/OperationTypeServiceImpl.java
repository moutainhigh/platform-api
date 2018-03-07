package com.xinleju.platform.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.OperationTypeDao;
import com.xinleju.platform.flow.dto.OperationTypeDto;
import com.xinleju.platform.flow.entity.OperationType;
import com.xinleju.platform.flow.service.OperationTypeService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class OperationTypeServiceImpl extends  BaseServiceImpl<String,OperationType> implements OperationTypeService{
	

	@Autowired
	private OperationTypeDao operationTypeDao;

	@Override
	public List<OperationTypeDto> queryAllObjectDtoList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("delflag", 0);
		List<OperationType> templist = operationTypeDao.queryList(paramMap);
		List<OperationTypeDto> resultList = new ArrayList<OperationTypeDto>();
		for(OperationType obj : templist){
			OperationTypeDto dto = new OperationTypeDto();
			BeanUtils.copyProperties(obj, dto);
			resultList.add(dto);
		}
		return resultList;
	}
	

}
