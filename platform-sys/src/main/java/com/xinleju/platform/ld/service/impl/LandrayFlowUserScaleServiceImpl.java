package com.xinleju.platform.ld.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.ld.dao.LandrayFlowUserScaleDao;
import com.xinleju.platform.ld.entity.LandrayFlowUserScale;
import com.xinleju.platform.ld.service.LandrayFlowUserScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LandrayFlowUserScaleServiceImpl extends  BaseServiceImpl<String,LandrayFlowUserScale> implements LandrayFlowUserScaleService {
	

	@Autowired
	private LandrayFlowUserScaleDao landrayFlowUserScaleDao;
	

}
