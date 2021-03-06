package com.xinleju.platform.flow.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.AddReaderDto;
import com.xinleju.platform.flow.dto.BatchModifyReaderDto;
import com.xinleju.platform.flow.dto.ParticipantDto;
import com.xinleju.platform.flow.dto.service.ParticipantDtoServiceCustomer;
import com.xinleju.platform.flow.entity.Participant;
import com.xinleju.platform.flow.service.ParticipantService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class ParticipantDtoServiceProducer implements ParticipantDtoServiceCustomer{
	private static Logger log = Logger.getLogger(ParticipantDtoServiceProducer.class);
	@Autowired
	private ParticipantService participantService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   Participant participant=JacksonUtils.fromJson(saveJson, Participant.class);
		   participantService.save(participant);
		   info.setResult(JacksonUtils.toJson(participant));
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
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Participant participant=JacksonUtils.fromJson(updateJson, Participant.class);
			   int result=   participantService.update(participant);
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
			   Participant participant=JacksonUtils.fromJson(deleteJson, Participant.class);
			   int result= participantService.deleteObjectById(participant.getId());
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
				   int result= participantService.deleteAllObjectByIds(list);
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
			Participant participant=JacksonUtils.fromJson(getJson, Participant.class);
			Participant	result = participantService.getObjectById(participant.getId());
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
				Page page=participantService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=participantService.getPage(new HashMap(), null, null);
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
				//支持排序的设置参数
				map.put("sidx", "sort");
				map.put("sord", "asc");
				List list=participantService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=participantService.queryList(null);
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
			   Participant participant=JacksonUtils.fromJson(deleteJson, Participant.class);
			   int result= participantService.deletePseudoObjectById(participant.getId());
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
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= participantService.deletePseudoAllObjectByIds(list);
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
	public String batchModifyReader(String userInfo, String modifyJson) {
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   List<Participant> readerList = JacksonUtils.fromJson(modifyJson, ArrayList.class, Participant.class);
		   participantService.batchModifyReader(readerList);
		   info.setResult("批量修改完成");
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
	public String replaceFlowParticipant(String userJson, String saveJson) {
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   List<ParticipantDto> approverList = JacksonUtils.fromJson(saveJson, ArrayList.class, ParticipantDto.class);
			   participantService.replaceFlowParticipant(approverList);
			   info.setResult("批量修改完成");
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
	public String queryFlowReaderList(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String, Object> paramMap=JacksonUtils.fromJson(paramater, HashMap.class);
			//支持排序的设置参数
			List<ParticipantDto> list = participantService.queryFlowReaderList(paramMap);
			info.setResult(JacksonUtils.toJson(list));
		    info.setSucess(true);
		    info.setMsg("获取列表对象成功!");
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
	public String deleteReaderByFormData(String userInfo, String saveJson) {
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BatchModifyReaderDto readerDto=JacksonUtils.fromJson(saveJson, BatchModifyReaderDto.class);
		   participantService.deleteReaderByFormData(readerDto);
		   info.setResult(JacksonUtils.toJson(readerDto));
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
	public String addResetReaderFormData(String userInfo, String saveJson) {
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BatchModifyReaderDto readerDto=JacksonUtils.fromJson(saveJson, BatchModifyReaderDto.class);
		   participantService.addResetReaderFormData(readerDto);
		   info.setResult(JacksonUtils.toJson(readerDto));
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
	public String queryReplaceParticipantList(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				//支持排序的设置参数
				/*map.put("sidx", "sort");
				map.put("sord", "asc");*/
				List list=participantService.queryReplaceParticipantList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=participantService.queryList(null);
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
	public String saveReplaceApprover(String userInfo, String saveReplaceJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> saveReplaceMap=JacksonUtils.fromJson(saveReplaceJson, Map.class);
			SecurityUserBeanInfo userBeanInfo = JacksonUtils.fromJson(userInfo,SecurityUserBeanInfo.class);
			SecurityUserDto securityUserDto = userBeanInfo.getSecurityUserDto();
			Map<String,Object> userInfoMap = new HashMap<String,Object>();
			userInfoMap.put("updatePersonId",securityUserDto.getId());
			userInfoMap.put("updatePersonName",securityUserDto.getRealName());
			participantService.saveReplaceApprover(userInfoMap,saveReplaceMap);
			info.setResult(JacksonUtils.toJson(saveReplaceMap));
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
}
