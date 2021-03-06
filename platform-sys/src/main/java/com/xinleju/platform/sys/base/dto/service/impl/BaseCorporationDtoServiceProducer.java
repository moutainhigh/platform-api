package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.BaseCorporationDto;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.dto.service.BaseCorporationDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.BaseCorporation;
import com.xinleju.platform.sys.base.service.BaseCorporationService;
import com.xinleju.platform.sys.base.utils.StatusType;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class BaseCorporationDtoServiceProducer implements BaseCorporationDtoServiceCustomer{
	private static Logger log = Logger.getLogger(BaseCorporationDtoServiceProducer.class);
	@Autowired
	private BaseCorporationService baseCorporationService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BaseCorporation baseCorporation=JacksonUtils.fromJson(saveJson, BaseCorporation.class);
		   baseCorporationService.save(baseCorporation);
		   info.setResult(JacksonUtils.toJson(baseCorporation));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch(DataIntegrityViolationException e){
			log.error("保存对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("字段长度过长！");
			 info.setExceptionMsg(e.getMessage());
			 
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
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseCorporation baseCorporation=JacksonUtils.fromJson(updateJson, BaseCorporation.class);
			   int result=   baseCorporationService.update(baseCorporation);
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
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseCorporation baseCorporation=JacksonUtils.fromJson(deleteJson, BaseCorporation.class);
			   int result= baseCorporationService.deleteObjectById(baseCorporation.getId());
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
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= baseCorporationService.deleteAllObjectByIds(list);
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
			BaseCorporation baseCorporation=JacksonUtils.fromJson(getJson, BaseCorporation.class);
			BaseCorporation	result = baseCorporationService.getObjectById(baseCorporation.getId());
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
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			//	Page page=baseCorporationService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				Page page=baseCorporationService.getAllListByPage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=baseCorporationService.getPage(new HashMap(), null, null);
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
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=baseCorporationService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=baseCorporationService.queryList(null);
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
	public String getCorporationAndAccontById(String userinfo, String getJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			BaseCorporationDto baseCorporation=JacksonUtils.fromJson(getJson, BaseCorporationDto.class);
			BaseCorporationDto	result = baseCorporationService.getCorporationAndAccontById(baseCorporation.getId());
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
	public String saveCorporationAndAccont(String userinfo, String saveJson) {
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseCorporationDto baseCorporationDto=JacksonUtils.fromJson(saveJson, BaseCorporationDto.class);
			   baseCorporationService.saveCorporationAndAccont(baseCorporationDto);
			   info.setResult(JacksonUtils.toJson(baseCorporationDto));
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
	public String deleteCorporationAndAccontByCorporationId(String userinfo,
			String deleteJson) {
		  DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseCorporationDto baseCorporationDto=JacksonUtils.fromJson(deleteJson, BaseCorporationDto.class);
			   int result= baseCorporationService.deleteCorporationAndAccontByCorporationId(baseCorporationDto.getId());
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
	public String updateCorporationAndAccont(String userinfo, String updateJson) {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseCorporationDto baseCorporationDto=JacksonUtils.fromJson(updateJson, BaseCorporationDto.class);
			   int result=   baseCorporationService.updateCorporationAndAccont(baseCorporationDto);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新对象成功!");
			}  catch(DataIntegrityViolationException e){
				log.error("保存对象失败!"+e.getMessage());
				 info.setSucess(false);
				 info.setMsg("字段长度过长！");
				 info.setExceptionMsg(e.getMessage());
				 
			}catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String updateStatus(String userinfo, String updateStatus) {
		  DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
		
			   BaseCorporationDto baseCorporationDto=JacksonUtils.fromJson(updateStatus, BaseCorporationDto.class);
			   BaseCorporationDto	baseCorporationDtoBean= baseCorporationService.getCorporationAndAccontById(baseCorporationDto.getId());
			   int result=   baseCorporationService.updateStatus(baseCorporationDtoBean);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新状态成功!");
			} catch (Exception e) {
			 log.error("更新状态失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新状态失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteAllByIds(String userinfo, String ids) {
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Map map=JacksonUtils.fromJson(ids, HashMap.class);
			 String id = (String) map.get("id");
			   int result= baseCorporationService.deleteAllByIds(id);
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

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dto.service.BaseCorporationDtoServiceCustomer#getPaymentOrganTree(java.lang.String, java.lang.String)
	 */
	@Override
	public String getPaymentOrganTree(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		List<ContentChildTreeData> resultList=new ArrayList<ContentChildTreeData>();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=baseCorporationService.getBaseCorporationList(map);
				
				  if(list.size() > 0){
					  for (Map<String, Object> treeNode : list) {
						  String status =(String) treeNode.get("status");
						  if(status.equals(StatusType.StatusOpen.getCode())){
							  ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
							  contentChildTreeData.setpId("1");
							  contentChildTreeData.setName(String.valueOf(treeNode.get("name")));
							  contentChildTreeData.setId(String.valueOf(treeNode.get("id")));
							  contentChildTreeData.setParentId("1");
							  resultList.add(contentChildTreeData);
						  }
					  }
				   }
				info.setResult(JacksonUtils.toJson(resultList));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=baseCorporationService.queryList(null);
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
}
