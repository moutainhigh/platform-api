package com.xinleju.platform.ld.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.ld.dao.LandrayNewsInfoDao;
import com.xinleju.platform.ld.entity.LandrayNewsInfo;
import com.xinleju.platform.ld.service.LandrayNewsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LandrayNewsInfoServiceImpl extends  BaseServiceImpl<String,LandrayNewsInfo> implements LandrayNewsInfoService {
	

	@Autowired
	private LandrayNewsInfoDao landrayNewsInfoDao;


	@Override
	public Page getNewsDataByPage(Map map) throws Exception {
		Page page=new Page();
		List<Map<String,Object>> list = landrayNewsInfoDao.getBillData(map);
		Integer count = landrayNewsInfoDao.getBillDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

	@Override
	public List<LandrayNewsInfo> portalList(Map map) throws Exception {
		return landrayNewsInfoDao.portalList(map);
	}
}
