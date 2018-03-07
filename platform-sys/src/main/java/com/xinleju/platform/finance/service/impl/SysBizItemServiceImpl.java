package com.xinleju.platform.finance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dao.SysBizItemDao;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.entity.SysBizItem;
import com.xinleju.platform.finance.entity.SysRegister;
import com.xinleju.platform.finance.service.SysBizItemService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class SysBizItemServiceImpl extends  BaseServiceImpl<String,SysBizItem> implements SysBizItemService{
	

	@Autowired
	private SysBizItemDao sysBizItemDao;

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.SysBizItemService#getSysBizItempage(java.util.Map)
	 */
	@Override
	public Page getSysBizItempage(Map map) {
		   Page page =new Page();
		   List<SysBizItem> list=sysBizItemDao.getSysBizItempageList(map);
		   Integer total=sysBizItemDao.getSysBizItempageListCount(map);
		   page.setLimit((Integer) map.get("limit"));
		   page.setList(list);
		   page.setStart((Integer) map.get("start"));
		   page.setTotal(total);
		   return page;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.SysBizItemService#getSysBizItemList(java.util.Map)
	 */
	@Override
	public List<SysBizItem> getSysBizItemList(Map map) throws Exception {
	   return sysBizItemDao.getSysBizItempageList(map);
	}
	

}
