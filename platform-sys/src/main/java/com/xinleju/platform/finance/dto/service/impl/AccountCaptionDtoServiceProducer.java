package com.xinleju.platform.finance.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dto.AccountCaptionDto;
import com.xinleju.platform.finance.dto.service.AccountCaptionDtoServiceCustomer;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.service.AccountCaptionService;
import com.xinleju.platform.sys.base.dto.BaseProjectTypeDto;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

public class AccountCaptionDtoServiceProducer implements
		AccountCaptionDtoServiceCustomer {
	private static Logger log = Logger
			.getLogger(AccountCaptionDtoServiceProducer.class);
	@Autowired
	private AccountCaptionService accountCaptionService;

	public String save(String userInfo, String saveJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			AccountCaption accountCaption = JacksonUtils.fromJson(saveJson,
					AccountCaption.class);
			accountCaptionService.saveAccountCaption(accountCaption);
			info.setResult(JacksonUtils.toJson(accountCaption));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			AccountCaption accountCaption = JacksonUtils.fromJson(updateJson,
					AccountCaption.class);
			int result = accountCaptionService.updateAccountCaption(accountCaption);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			AccountCaption accountCaption = JacksonUtils.fromJson(deleteJson,
					AccountCaption.class);
			int result = accountCaptionService.deleteObjectById(accountCaption
					.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map = JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list = Arrays.asList(map.get("id").toString()
						.split(","));
				int result = accountCaptionService.deleteAllObjectByIds(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("删除对象成功!");
			}
		} catch (Exception e) {
			log.error("删除对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			AccountCaption accountCaption = JacksonUtils.fromJson(getJson,
					AccountCaption.class);
			AccountCaption result = accountCaptionService
					.getObjectById(accountCaption.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = accountCaptionService.getPage(map,
						(Integer) map.get("start"), (Integer) map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			} else {
				Page page = accountCaptionService.getPage(new HashMap(), null,
						null);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取分页对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取分页对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List list = accountCaptionService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = accountCaptionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			AccountCaption accountCaption = JacksonUtils.fromJson(deleteJson,
					AccountCaption.class);
			int result = accountCaptionService
					.deleteAccountCaptionById(accountCaption.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map = JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list = Arrays.asList(map.get("id").toString()
						.split(","));
				int result = accountCaptionService
						.deletePseudoAllObjectByIds(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("删除对象成功!");
			}
		} catch (Exception e) {
			log.error("删除对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xinleju.platform.finance.dto.service.AccountCaptionDtoServiceCustomer
	 * #getaccountCaptionTree(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAccountCaptionTree(String userJson, String paramaterJson) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Map<String, Object> map = JacksonUtils.fromJson(paramaterJson,
					HashMap.class);
			List<AccountCaptionDto> list = accountCaptionService
					.getAccountCaptionTree(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取树对象成功!");
			System.out.println(JacksonUtils.toJson(list));
		} catch (Exception e) {
			log.error("获取树对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取树对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xinleju.platform.finance.dto.service.AccountCaptionDtoServiceCustomer
	 * #queryCaptionList(java.lang.String, java.lang.String)
	 */
	@Override
	public String queryCaptionList(String userJson, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		List<ContentChildTreeData> resultList = new ArrayList<ContentChildTreeData>();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List<AccountCaption> list = accountCaptionService
						.queryCaptionList(map);
				String accoutCaptionId = (String) map.get("delTreeId");
				if (list.size() > 0) {
					for (AccountCaption treeNode : list) {
						if(treeNode.getPrefixId().indexOf(accoutCaptionId)>-1){
							continue;
						}else{
							ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
							contentChildTreeData.setpId(treeNode.getParentId());
							contentChildTreeData.setName(String.valueOf(treeNode
									.getName()));
							contentChildTreeData.setId(String.valueOf(treeNode
									.getId()));
							contentChildTreeData
							.setParentId(treeNode.getParentId());
							resultList.add(contentChildTreeData);
						}
					}
				}
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = accountCaptionService.queryCaptionList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}
}
