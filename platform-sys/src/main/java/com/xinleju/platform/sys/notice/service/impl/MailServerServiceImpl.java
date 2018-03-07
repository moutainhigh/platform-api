package com.xinleju.platform.sys.notice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.notice.dao.MailServerDao;
import com.xinleju.platform.sys.notice.entity.MailServer;
import com.xinleju.platform.sys.notice.service.MailServerService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MailServerServiceImpl extends  BaseServiceImpl<String,MailServer> implements MailServerService{


	@Autowired
	private MailServerDao mailServerDao;

	/**
	 * 校验code是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkCode(Map map)throws Exception{
		return mailServerDao.checkCode(map);
	}
	/**
	 * 保存服务器信息
	 * @param paramater
	 * @return
	 */
	public Integer saveServer(MailServer server)throws Exception{
		//校验编码重复
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("code", server.getCode());
		int c=checkCode(map);
		if(c>0){
			throw new InvalidCustomException("编码已存在，不可重复");
		}
		//如果是默认服务器，将其他的该为不默认
		if(server.getIsDefault()!=null && server.getIsDefault()){
			mailServerDao.unDefaultServer(map);
		}else{
			server.setIsDefault(false);
		}
		return save(server);
	}
	/**
	 *  修改服务器信息
	 * @param paramater
	 * @return
	 */
	public Integer updateServer(MailServer server)throws Exception{
		//校验编码重复
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("code", server.getCode());
		map.put("id", server.getId());
		int c=checkCode(map);
		if(c>0){
			throw new InvalidCustomException("编码已存在，不可重复");
		}
		//如果是默认服务器，将其他的该为不默认
		if(server.getIsDefault()!=null && server.getIsDefault()){
			mailServerDao.unDefaultServer(map);
		}else{
			server.setIsDefault(false);
		}
		return update(server);
	}
	/**
	 * 模糊查询列表
	 * @param paramater
	 * @return
	 */
	public List<MailServer> queryListLike(Map map)throws Exception{
		return mailServerDao.queryListLike(map);
	}
}
