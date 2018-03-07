package com.xinleju.platform.flow.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dao.FlowUserOpinionDao;
import com.xinleju.platform.flow.entity.FlowUserOpinion;
import com.xinleju.platform.flow.service.FlowUserOpinionService;

/**
 * @author 
 * 
 * 
 */

@Service
public class FlowUserOpinionServiceImpl extends  BaseServiceImpl<String,FlowUserOpinion> implements FlowUserOpinionService{
	

	@Autowired
	private FlowUserOpinionDao flowUserOpinionDao;

	@Override
	public int saveUserOpinIons(List<FlowUserOpinion> list) throws Exception {
		SecurityUserBeanInfo loginUser = LoginUtils.getSecurityUserBeanInfo();
		Map<String,Object> param = new HashMap<>();
		//获取当前用户当前的意见列表
		param.put("createPersonId", loginUser.getSecurityUserDto().getId());
		/*List<FlowUserOpinion> list_old = flowUserOpinionDao.queryList(param);
		//删除之前的
		for (int i = 0; i < list_old.size(); i++) {
			list_old.get(i).setDelflag(true);
		}
		int del = flowUserOpinionDao.updateBatch(list_old);*/
		int del = flowUserOpinionDao.delUserOpinion(param);
		if(del >= 0 && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setId(IDGenerator.getUUID());
			}
			//保存新增的
			return flowUserOpinionDao.saveBatch(list);
		}else{
			return del;
		}
	}

	@Override
	public List<FlowUserOpinion> queryUserOpinion(Map<String, Object> param) {
		return flowUserOpinionDao.queryUserOpinion(param);
	}

	@Override
	public int saveDefaultOpinion(String userOpinionId) {
		SecurityUserBeanInfo loginUser = LoginUtils.getSecurityUserBeanInfo();
		Map<String,Object> param = new HashMap<>();
		//获取当前用户当前的意见列表
		param.put("createPersonId", loginUser.getSecurityUserDto().getId());
		List<FlowUserOpinion> list_old = flowUserOpinionDao.queryList(param);
		//设置默认
		for (int i = 0; i < list_old.size(); i++) {
			FlowUserOpinion opinion = list_old.get(i);
			if(opinion.getId().equals(userOpinionId)){
				opinion.setIsDefault(true);
			}else {
				opinion.setIsDefault(false);
			}
		}
		int up = flowUserOpinionDao.updateBatch(list_old);
		return up;
	}
	
	
	

}
