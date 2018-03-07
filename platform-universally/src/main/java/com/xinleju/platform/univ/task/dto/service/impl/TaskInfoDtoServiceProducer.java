package com.xinleju.platform.univ.task.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.univ.task.dto.TaskInfoDto;
import com.xinleju.platform.univ.task.utils.QuartzManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.task.dto.service.TaskInfoDtoServiceCustomer;
import com.xinleju.platform.univ.task.entity.TaskInfo;
import com.xinleju.platform.univ.task.service.TaskInfoService;

/**
 * @author admin
 * 
 *
 */
 
public class TaskInfoDtoServiceProducer implements TaskInfoDtoServiceCustomer{
	private static Logger log = Logger.getLogger(TaskInfoDtoServiceProducer.class);
	@Autowired
	private TaskInfoService taskInfoService;

	public String save(String userInfo, String saveJson) {
 
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		String taskFor = null;
		try {
			TaskInfo taskInfo = JacksonUtils.fromJson(saveJson, TaskInfo.class);
			taskFor = taskInfo.getIsForPlatform()?"平台":"业务系统";
			
			
			
			// 编码唯一性验证
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("code", taskInfo.getCode());
			int count = taskInfoService.getCount(paramMap);
			if (count > 0) {
				info.setSucess(false);
				info.setMsg("自动任务的编码重复，请重新输入");
			} else {
				taskInfoService.save(taskInfo);
				info.setResult(JacksonUtils.toJson(taskInfo));
				info.setSucess(true);
				info.setMsg("保存任务成功!");
			}
			
		} catch (Exception e) {
			info.setSucess(false);
			if (e instanceof BeanCreationException) {
				if (e.getCause() instanceof CannotLoadBeanClassException) {
					info.setCode("0001");
					info.setMsg(taskFor + "任务类["+((CannotLoadBeanClassException)e.getCause()).getBeanClassName()+"]不存在");
				} else if (e.getCause() instanceof NoSuchMethodException) {
					info.setCode("0002");
					String method = e.getCause().getMessage();
					String fullMethodName = method.substring(0, method.indexOf('('));
					String methodName = fullMethodName.substring(fullMethodName.lastIndexOf('.') + 1);
					String rightName = methodName + method.substring(method.indexOf('('));
					info.setMsg(taskFor + "任务方法[" + rightName + "]不存在");
				} else {
					info.setMsg("保存任务失败!");
				}
			} else if (e instanceof CannotLoadBeanClassException) {
				info.setCode("0001");
				info.setMsg(taskFor + "任务类["+((CannotLoadBeanClassException)e).getBeanClassName()+"]不存在");
			} else if (e instanceof BeanDefinitionStoreException) {
				info.setCode("0003");
				info.setMsg(e.getMessage());
			} else {
				info.setMsg("保存任务失败!");
			}
			log.error("保存任务失败!" + e.getMessage(), e);
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
	public String update(String userInfo, String updateJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		String taskFor = null;
		try {
			TaskInfo taskInfo = JacksonUtils.fromJson(updateJson, TaskInfo.class);
			taskFor = taskInfo.getIsForPlatform()?"平台":"业务系统";
			
			
			// 编码唯一性验证
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("code", taskInfo.getCode());
			List<TaskInfo> taskList = taskInfoService.queryList(paramMap);
			int count = 0;
			for (TaskInfo ti : taskList) {
				if (!taskInfo.getId().equals(ti.getId())) {
					count = 1;
					break;
				}
			}
			if (count > 0) {
				info.setSucess(false);
				info.setMsg("自动任务的编码重复，请重新输入");
			} else {
				int result = taskInfoService.update(taskInfo);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("更新任务成功!");
			}
			
		} catch (Exception e) {
			log.error("更新任务失败!" + e.getMessage(), e);
			
			info.setSucess(false);
			if (e instanceof BeanCreationException) {
				if (e.getCause() instanceof CannotLoadBeanClassException) {
					info.setCode("0001");
					info.setMsg(taskFor + "任务类["+((CannotLoadBeanClassException)e.getCause()).getBeanClassName()+"]不存在");
				} else if (e.getCause() instanceof NoSuchMethodException) {
					info.setCode("0002");
					String method = e.getCause().getMessage();
					String fullMethodName = method.substring(0, method.indexOf('('));
					String methodName = fullMethodName.substring(fullMethodName.lastIndexOf('.') + 1);
					String rightName = methodName + method.substring(method.indexOf('('));
					info.setMsg(taskFor + "任务方法[" + rightName + "]不存在");
				} else {
					info.setMsg("保存任务失败!");
				}
			} else if (e instanceof CannotLoadBeanClassException) {
				info.setCode("0001");
				info.setMsg(taskFor + "任务类["+((CannotLoadBeanClassException)e).getBeanClassName()+"]不存在");
			} else if (e instanceof BeanDefinitionStoreException) {
				info.setCode("0003");
				info.setMsg(e.getMessage());
			} else {
				info.setMsg("保存任务失败!");
			}
			log.error("更新任务失败!" + e.getMessage(), e);
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
			   TaskInfo taskInfo=JacksonUtils.fromJson(deleteJson, TaskInfo.class);
			   int result= taskInfoService.deleteObjectById(taskInfo.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除任务成功!");
			} catch (Exception e) {
			 log.error("更新任务失败!"+e.getMessage(), e);
			 info.setSucess(false);
			 info.setMsg("删除更新任务失败!");
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
				   int result= taskInfoService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除任务成功!");
				}
			} catch (Exception e) {
			 log.error("删除任务失败!"+e.getMessage(), e);
			 info.setSucess(false);
			 info.setMsg("删除更新任务失败!");
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
			TaskInfo taskInfo=JacksonUtils.fromJson(getJson, TaskInfo.class);
			TaskInfo	result = taskInfoService.getObjectById(taskInfo.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取任务数据成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取任务数据失败!"+e.getMessage(), e);
			 info.setSucess(false);
			 info.setMsg("获取任务数据失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = taskInfoService.getPage(map, (Integer) map.get("start"), (Integer) map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			} else {
				Page page = taskInfoService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取分页对象失败!" + e.getMessage(), e);
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
				List list=taskInfoService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=taskInfoService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage(), e);
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

	@SuppressWarnings("unchecked")
	@Override
	public String updateTaskState(String userInfo, String ids, String updateMapJson) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(updateMapJson)) {
				Map<String, Object> map = JacksonUtils.fromJson(updateMapJson, HashMap.class);
				Map<String, String> idsMap = JacksonUtils.fromJson(ids, HashMap.class);
				List<String> list = Arrays.asList(idsMap.get("ids").split(","));
				int result = taskInfoService.updateTaskState(list, map);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("任务操作成功!");
			}
		} catch (Exception e) {
			log.error("任务操作失败!" + e.getMessage(), e);
			info.setSucess(false);
			info.setMsg("任务操作失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String executeNow(String userInfo, String ids) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(ids)) {
				@SuppressWarnings("unchecked")
				Map<String, String> idsMap = JacksonUtils.fromJson(ids, HashMap.class);
				List<String> list = Arrays.asList(idsMap.get("ids").split(","));
				int result = taskInfoService.executeNow(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("任务操作成功!");
			}
		} catch (Exception e) {
			log.error("任务操作失败!" + e.getMessage(), e);
			info.setSucess(false);
			info.setMsg("任务操作失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String taskTest(String userInfo, String parameter) {
		log.debug("---------------> 自动任务调用dubbo服务测试。。。。");
		log.debug("---------------> 参数值：userInfo:" + userInfo + ", parameter:" + parameter);
		return null;
	}
	/**
	 * 为租户系统提供初始化数据库是quartz初始定时任务
	 * @param userInfo
	 * @param paramJson
	 * @return
	 */
	@Override
	public String initQuartzJob(String userInfo, String paramJson) {
		DubboServiceResultInfo info = new DubboServiceResultInfo ();
		Map<String,Object> paramMap = JacksonUtils.fromJson (paramJson,Map.class);
		String tendCode = String.valueOf(paramMap.get ("tendCode"));
		try{
			List<TaskInfoDto> taskInfoList = JacksonUtils.fromJson (JacksonUtils.toJson (paramMap.get ("taskList")),List.class,TaskInfoDto.class);
			for(TaskInfoDto taskInfoDto:taskInfoList){
				TaskInfo taskInfo = new TaskInfo ();
				BeanUtils.copyProperties (taskInfoDto,taskInfo);
				QuartzManager.deleteTask(taskInfo, tendCode);
				QuartzManager.addTask(taskInfo, tendCode);
			};
			info.setSucess (true);
			info.setMsg (tendCode+"--初始化定时任务成功！");
			log.info (tendCode+"--初始化定时任务成功！");
		}catch (Exception e){
			e.printStackTrace ();
			log.error(tendCode+"--初始化定时任务失败！");
			info.setSucess (false);
			info.setMsg (tendCode+"--初始化定时任务失败！");
			info.setCode (ErrorInfoCode.SYSTEM_ERROR.getValue ());
		}
		return JacksonUtils.toJson (info);
	}
}
