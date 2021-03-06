package com.xinleju.platform.sys.num.dto.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.num.dto.service.RulerSubDtoServiceCustomer;
import com.xinleju.platform.sys.num.entity.RulerSub;
import com.xinleju.platform.sys.num.service.RulerSubService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class RulerSubDtoServiceProducer implements RulerSubDtoServiceCustomer{
	private static Logger log = Logger.getLogger(RulerSubDtoServiceProducer.class);
	@Autowired
	private RulerSubService rulerSubService;

	public String save(String userInfo, String saveJson){
	   SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date=new Date();
	   Timestamp timestamp = Timestamp.valueOf(dateFormater.format(date));
		
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   //获取用户对象
		   SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
		   RulerSub rulerSub=JacksonUtils.fromJson(saveJson, RulerSub.class);
		   //用户信息赋值
		   rulerSub.setCreateDate(timestamp);
		   rulerSub.setTendId(user.getTendId());
		   rulerSub.setCreatePersonId(user.getSecurityUserDto().getId());
		   rulerSub.setCreatePersonName(user.getSecurityUserDto().getLoginName());
		   rulerSub.setCreateCompanyId(user.getSecurityUserDto().getBelongOrgId());
		   rulerSub.setCreateCompanyName(user.getSecurityUserDto().getBelongOrgName());
		   rulerSub.setCreateOrgId(user.getSecurityUserDto().getBelongOrgId());
		   rulerSub.setCreateOrgName(user.getSecurityUserDto().getBelongOrgName());
		   
		   rulerSubService.save(rulerSub);
		   info.setResult(JacksonUtils.toJson(rulerSub));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		Timestamp timestamp = Timestamp.valueOf(dateFormater.format(date));
		
	    DubboServiceResultInfo info=new DubboServiceResultInfo();
	    try {
	    	//获取用户对象
		   SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
		   RulerSub rulerSub=JacksonUtils.fromJson(updateJson, RulerSub.class);
		   
		   rulerSub.setUpdateDate(timestamp);
		   rulerSub.setUpdatePersonId(user.getSecurityUserDto().getId());
		   rulerSub.setUpdatePersonName(user.getSecurityUserDto().getLoginName());
		   int result=   rulerSubService.update(rulerSub);
		   info.setResult(JacksonUtils.toJson(result));
		   info.setSucess(true);
		   info.setMsg("更新对象成功!");
	    } catch (Exception e) {
		   log.error("更新对象失败!"+e.getMessage());
		   info.setSucess(false);
		   info.setMsg("更新对象失败!");
		   info.setExceptionMsg(e.getMessage());
		}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson){
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   RulerSub rulerSub=JacksonUtils.fromJson(deleteJson, RulerSub.class);
			   int result= rulerSubService.deleteObjectById(rulerSub.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList){
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   @SuppressWarnings("unchecked")
				   Map<String,Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= rulerSubService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			RulerSub rulerSub=JacksonUtils.fromJson(getJson, RulerSub.class);
			RulerSub	result = rulerSubService.getObjectById(rulerSub.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=rulerSubService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=rulerSubService.getPage(new HashMap<String,Object>(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<RulerSub> list=rulerSubService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<RulerSub> list=rulerSubService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   RulerSub rulerSub=JacksonUtils.fromJson(deleteJson, RulerSub.class);
			   int result= rulerSubService.deletePseudoObjectById(rulerSub.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList){
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   @SuppressWarnings("unchecked")
				   Map<String,Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= rulerSubService.deletePseudoAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getBeanIdByBillId(String userinfo, Map<String,Object> map) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			String sb = rulerSubService.getBeanIdByBillId(map);
			info.setResult(String.valueOf(sb));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getBillNumber(String userinfo, Map<String, Object> map) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			String sb = rulerSubService.saveBillNumberAndgetBillNumber(map);
			info.setResult(String.valueOf(sb));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

}
