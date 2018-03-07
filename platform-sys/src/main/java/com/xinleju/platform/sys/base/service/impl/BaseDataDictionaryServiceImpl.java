package com.xinleju.platform.sys.base.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.base.dao.BaseDataDictionaryDao;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryDto;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryItemDto;
import com.xinleju.platform.sys.base.entity.BaseDataDictionary;
import com.xinleju.platform.sys.base.entity.BaseDataDictionaryItem;
import com.xinleju.platform.sys.base.service.BaseDataDictionaryItemService;
import com.xinleju.platform.sys.base.service.BaseDataDictionaryService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BaseDataDictionaryServiceImpl extends  BaseServiceImpl<String,BaseDataDictionary> implements BaseDataDictionaryService{
	

	@Autowired
	private BaseDataDictionaryDao baseDataDictionaryDao;
	@Autowired
	private BaseDataDictionaryItemService baseDataDictionaryItemService;
	@Override
	public BaseDataDictionaryDto getBaseDataDictionaryAndItem(String id)
			throws Exception {
		BaseDataDictionary baseDataDictionary = baseDataDictionaryDao.getObjectById(id);
		BaseDataDictionaryDto baseDataDictionaryDto =new BaseDataDictionaryDto();
		BeanUtils.copyProperties(baseDataDictionary, baseDataDictionaryDto);
		List<BaseDataDictionaryItemDto> list=baseDataDictionaryItemService.getItemListByDictionaryId(id);
		baseDataDictionaryDto.setList(list);
		return baseDataDictionaryDto;
	}
	@Override
	public void saveBaseDataDictionaryAndItem(
			BaseDataDictionaryDto baseDataDictionaryDto) throws Exception {
		List<BaseDataDictionaryItemDto> list = baseDataDictionaryDto.getList();
		for (BaseDataDictionaryItemDto baseDataDictionaryItemDto : list) {
			BaseDataDictionaryItem baseDataDictionaryItem=new BaseDataDictionaryItem();
			BeanUtils.copyProperties(baseDataDictionaryItemDto, baseDataDictionaryItem);
			baseDataDictionaryItemService.save(baseDataDictionaryItem);
		}
		BaseDataDictionary baseDataDictionary =new BaseDataDictionary();
		BeanUtils.copyProperties(baseDataDictionaryDto, baseDataDictionary);
		baseDataDictionaryDao.save(baseDataDictionary);
	}
	@Override
	public int updateBaseDataDictionaryAndItem(
			BaseDataDictionaryDto baseDataDictionaryDto) throws Exception {
		List<BaseDataDictionaryItemDto> list = baseDataDictionaryDto.getList();
		for (BaseDataDictionaryItemDto baseDataDictionaryItemDto : list) {
			BaseDataDictionaryItem baseDataDictionaryItem=new BaseDataDictionaryItem();
			BeanUtils.copyProperties(baseDataDictionaryItemDto, baseDataDictionaryItem);
			baseDataDictionaryItemService.update(baseDataDictionaryItem);
		}
		BaseDataDictionary baseDataDictionary =new BaseDataDictionary();
		BeanUtils.copyProperties(baseDataDictionaryDto, baseDataDictionary);
		int update = baseDataDictionaryDao.update(baseDataDictionary);
		return update;
	}
	@Override
	public int deleteBaseDataDictionaryAndItem(String id) throws Exception {
		List<BaseDataDictionaryItemDto> list=baseDataDictionaryItemService.getItemListByDictionaryId(id);
		for (BaseDataDictionaryItemDto baseDataDictionaryItemDto : list) {
			BaseDataDictionaryItem baseDataDictionaryItem=new BaseDataDictionaryItem();
			BeanUtils.copyProperties(baseDataDictionaryItemDto, baseDataDictionaryItem);
			baseDataDictionaryItemService.deletePseudoObjectById(baseDataDictionaryItem.getId());
		}
		int i = baseDataDictionaryDao.deletePseudoObjectById(id);
		return i;
		
		
	}
	

}
