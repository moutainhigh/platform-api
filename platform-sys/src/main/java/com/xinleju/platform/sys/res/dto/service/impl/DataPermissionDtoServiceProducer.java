package com.xinleju.platform.sys.res.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.res.dto.service.DataPermissionDtoServiceCustomer;
import com.xinleju.platform.sys.res.entity.DataPermission;
import com.xinleju.platform.sys.res.entity.DataPoint;
import com.xinleju.platform.sys.res.entity.DataPointPermissionVal;
import com.xinleju.platform.sys.res.entity.FuncPermission;
import com.xinleju.platform.sys.res.service.DataPermissionService;
import com.xinleju.platform.sys.res.service.DataPointPermissionValService;
import com.xinleju.platform.sys.res.service.DataPointService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class DataPermissionDtoServiceProducer implements DataPermissionDtoServiceCustomer{
	private static Logger log = Logger.getLogger(DataPermissionDtoServiceProducer.class);
	@Autowired
	private DataPermissionService dataPermissionService;
	
	@Autowired
	private DataPointPermissionValService dataPointPermissionValService;
	
	@Autowired
	private DataPointService dataPointService;


	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   DataPermission dataPermission=JacksonUtils.fromJson(saveJson, DataPermission.class);
		   dataPermissionService.save(dataPermission);
		   info.setResult(JacksonUtils.toJson(dataPermission));
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
			   DataPermission dataPermission=JacksonUtils.fromJson(updateJson, DataPermission.class);
			   int result=   dataPermissionService.update(dataPermission);
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
			   DataPermission dataPermission=JacksonUtils.fromJson(deleteJson, DataPermission.class);
			   int result= dataPermissionService.deleteObjectById(dataPermission.getId());
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
				   int result= dataPermissionService.deleteAllObjectByIds(list);
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
			DataPermission dataPermission=JacksonUtils.fromJson(getJson, DataPermission.class);
			DataPermission	result = dataPermissionService.getObjectById(dataPermission.getId());
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
				Page page=dataPermissionService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=dataPermissionService.getPage(new HashMap(), null, null);
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
				List list=dataPermissionService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=dataPermissionService.queryList(null);
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

	//保存数据授权
	@Override
	public String saveDataAuth(String userInfo, String paramater) throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Map<String,Object> param =JacksonUtils.fromJson(paramater, HashMap.class);
			   dataPermissionService.saveDataAuthAndVal(param);
			   info.setResult(paramater);
			   info.setSucess(true);
			   info.setMsg("保存成功!");
			} catch (Exception e) {
			 log.error("保存失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("保存失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}
	
	//保存数据授权(角色到数据)(控制点和指定数据都保存)
	@Override
	public String saveDataAuthRoleToData(String userInfo, String paramater) throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Map<String,Object> param =JacksonUtils.fromJson(paramater, HashMap.class);
			   dataPermissionService.saveDataAuthRoleToData(param);
			   info.setResult(paramater);
			   info.setSucess(true);
			   info.setMsg("保存成功!");
			} catch (Exception e) {
			 log.error("保存失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("保存失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}
	//查询数据授权（根据控制项Id和角色Ids）
	@Override
	public String queryAuthDataByitemIdAndroleIds(String userInfo, String paramer){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramer)) {
				Map map = JacksonUtils.fromJson(paramer, HashMap.class);
				Map<String,Object> resultMap = dataPermissionService.queryAuthDataByitemIdAndroleIds(map);
				info.setResult(JacksonUtils.toJson(resultMap));
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("查询对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("查询对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据（控制项Id和控制点Id或者指定数据ID（类型判断如果类型是dataPoint是控制点ID））查询已授权数据
	 * @param map
	 * @return
	 */
	@Override
	public String queryAuthDataByitemIdAndPointId(String userInfo, String paramer){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramer)) {
				Map map = JacksonUtils.fromJson(paramer, HashMap.class);
				Map<String,Object> resultMap = dataPermissionService.queryAuthDataByitemIdAndPointId(map);
				info.setResult(JacksonUtils.toJson(resultMap));
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("查询对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("查询对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	//保存数据授权(数据到角色)
	@Override
	public String saveDataAuthDataToRole(String userInfo, String paramater) throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Map<String,Object> param =JacksonUtils.fromJson(paramater, HashMap.class);
			   dataPermissionService.saveDataAuthDataToRole(param);
			   info.setResult(paramater);
			   info.setSucess(true);
			   info.setMsg("保存成功!");
			} catch (Exception e) {
			 log.error("保存失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("保存失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}
	//保存引入数据授权
	@Override
	public String saveBatchDataImport(String userInfo, String paramater) throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map =JacksonUtils.fromJson(paramater, HashMap.class);
			if(null == map.get("importObjectId") || "".equals((String)map.get("importObjectId"))){
				throw new InvalidCustomException("需要更改的对象为空");
			}
			
			if(null == map.get("objectids") || "".equals((String)map.get("objectids"))){
				throw new InvalidCustomException("需要引入的对象为空");
			}
			String objectids = (String)map.get("objectids");
			String importObjectId = (String)map.get("importObjectId");
			
			
			String[] importObjectIdList = importObjectId.split(",");
			
			for(String imObjectId :importObjectIdList){
				
				Map<String,Object> mapImObjectId = new HashMap<String,Object>();
				mapImObjectId.put("imObjectId", imObjectId);
				//删除当前对象现有的所有控制点授权以及对应的val值
				List<String> listDataPermissionIds = dataPermissionService.queryAuthDataIdByobjectIds(mapImObjectId);
				List<String> listDataPointPermissionIds = dataPermissionService.queryAuthDataValIdByobjectIds(mapImObjectId);
				if (listDataPermissionIds.size() > 0) {
					dataPermissionService.deleteAllObjectByIds(listDataPermissionIds);
				}
				if (listDataPointPermissionIds.size() > 0) {
					dataPointPermissionValService.deleteAllObjectByIds(listDataPointPermissionIds);
				}
				mapImObjectId.put("imObjectId", objectids);
				//保存引入对象的控制点授权和对应的val
				List<Map<String,Object>> savelistData = dataPermissionService.queryAuthDataByobjectIds(mapImObjectId);

				for(Map<String,Object> m :savelistData){
					DataPermission dataPermission = new DataPermission();
					String dataPermissionId = IDGenerator.getUUID();
					dataPermission.setId(dataPermissionId);
					dataPermission.setRoleId(imObjectId);
					dataPermission.setDataPointId((String)m.get("data_point_id"));
					Map<String,Object> mapImdataPermissionId = new HashMap<String,Object>();
					mapImdataPermissionId.put("imdataPermissionId", (String)m.get("id"));
					List<Map<String,Object>> savelistValData = dataPermissionService.queryAuthValDataBydataPermissionId(mapImdataPermissionId);
					for(Map<String,Object> mVal :savelistValData){
						DataPointPermissionVal dataPointPermissionVal = new DataPointPermissionVal();
						dataPointPermissionVal.setId(IDGenerator.getUUID());
						dataPointPermissionVal.setDataPermissionId(dataPermissionId);
						dataPointPermissionVal.setVal((String)mVal.get("val"));
						dataPointPermissionValService.save(dataPointPermissionVal);
					}
					dataPermissionService.save(dataPermission);
				}
			}
			info.setResult(paramater);
			info.setSucess(true);
			info.setMsg("保存成功!");
		} catch (Exception e) {
			log.error("保存失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	

	@Override
	public String saveBatchDataToObjectBytypeAndItemId(String userInfo, String saveJsonList){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				Map mapAuthData  = (HashMap) map.get("saveData");
				String type  = (String) map.get("type");
				String dataItemId  = (String) map.get("dataItemId");
				String pointIdOrVal  =  map.get("id").toString();
				Map<String,Object> mapCon = new HashMap<String,Object>();
				mapCon.put("itemId", dataItemId);
				mapCon.put("ids", "");
				mapCon.put("type", type);
				//根据控制项ID查询出已经配置的对象(控制点或者指定val传递为空查询出来当前控制项所有的配置，有新的添加，要删除原先配置的控制点)
				Map<String,Object> resultMap = dataPermissionService.queryAuthDataByitemIdAndPointId(mapCon);
				List<Map<String,Object>> listDataPermission =  (List<Map<String,Object>>)resultMap.get("listDataPermission");
				List<Map<String,Object>> listDataPointPermission =  (List<Map<String,Object>>)resultMap.get("listDataPointPermission");
				
				
				//保存的数据为对象Id绑定控制点，
				if(type.equals("dataPoint")){
					if (mapAuthData != null) {
						//删除的Id数据
						List<String> listDataPermissionIds = new ArrayList<String>();
						Set<String> set = mapAuthData.keySet();
						for (String key : set) {
							String[] keyIds = key.split("#");
							String[] roleIds = keyIds[0].split("/");
							String roleId = roleIds[roleIds.length-1];
							String operationId = keyIds[1];
							if(!operationId.equals("operationId")){
								if("1".equals(mapAuthData.get(key))){
									DataPermission dataPermission = new DataPermission();
									dataPermission.setId(IDGenerator.getUUID());
									dataPermission.setRoleId(roleId);
									dataPermission.setDataPointId(operationId);
									//如果原有数据有已保存的控制点，要进行删除
									//例：roleid:本部门，新增数据为roleid:本公司，要删除掉本部门
									for(int i=0;i<listDataPermission.size();i++){
										Map<String,Object> dataMap = listDataPermission.get(i);
										if(((String)dataMap.get("role_id")).equals(roleId) ){
											listDataPermissionIds.add(((String)dataMap.get("id")));
										}
									}
									dataPermissionService.save(dataPermission);
								}else{
									for(int i=0;i<listDataPermission.size();i++){
										Map<String,Object> dataMap = listDataPermission.get(i);
										if(((String)dataMap.get("role_id")).equals(roleId) && ((String)dataMap.get("data_point_id")).equals(operationId)){
											listDataPermissionIds.add(((String)dataMap.get("id")));
										}
									}
								}
							}
							
						}
						if (listDataPermissionIds.size() > 0) {
							dataPermissionService.deleteAllObjectByIds(listDataPermissionIds);
						}
					}
				}else{
					//根据控制项ID查询出来控制点Id
					//要类型是指定数据的那个控制点Id
					Map<String,Object> mapgetDataItemId = new HashMap<String,Object>();
					mapgetDataItemId.put("itemId", dataItemId);
					List<DataPoint> list=dataPointService.queryDataPointByPram(mapgetDataItemId);
					String dataPointId = "";
					for(DataPoint dp:list){
						if(dp.getType().equals("2")){
							dataPointId = dp.getId();
						}
					}
					//保存的数据为对象Id绑定指定val数据
					if (mapAuthData != null) {
						//删除的控制点Id数据
						List<String> listDataPermissionIds = new ArrayList<String>();
						//删除的指定数据val数据
						List<String> listDataPointPermissionIds = new ArrayList<String>();
						Set<String> set = mapAuthData.keySet();
						for (String key : set) {
							String[] keyIds = key.split("#");
							String[] roleIds = keyIds[0].split("/");
							String roleId = roleIds[roleIds.length-1];
							String operationId = keyIds[1];
							if(!operationId.equals("operationId")){
								if("1".equals(mapAuthData.get(key))){
									String dataPermissionId = "";
									boolean isexistDataPoint = false;
									for(int i=0;i<listDataPermission.size();i++){
										Map<String,Object> dataMap = listDataPermission.get(i);
										if(((String)dataMap.get("role_id")).equals(roleId) ){
											String dataPointType = (String)dataMap.get("type");
											//如果控制点不是指定类型，要进行删除，如果是，要保留控制点ID
											if(dataPointType.equals("2")){
												isexistDataPoint = true;
												dataPermissionId = (String)dataMap.get("id");
											}else{
												listDataPermissionIds.add(((String)dataMap.get("id")));
											}
										}
									}
									//isexistDataPoint为true，说明有原先的指定控制点Id，直接进行保存即可，如果没有，要先保存指定类型控制点和角色的绑定
									if(isexistDataPoint){
										DataPointPermissionVal dataPointPermissionVal = new DataPointPermissionVal();
										dataPointPermissionVal.setId(IDGenerator.getUUID());
										dataPointPermissionVal.setDataPermissionId(dataPermissionId);
										dataPointPermissionVal.setVal(operationId);
										dataPointPermissionValService.save(dataPointPermissionVal);
									}else{
										DataPermission dataPermission = new DataPermission();
										dataPermissionId = IDGenerator.getUUID();
										dataPermission.setId(dataPermissionId);
										dataPermission.setRoleId(roleId);
										dataPermission.setDataPointId(dataPointId);
										dataPermissionService.save(dataPermission);
										DataPointPermissionVal dataPointPermissionVal = new DataPointPermissionVal();
										dataPointPermissionVal.setId(IDGenerator.getUUID());
										dataPointPermissionVal.setDataPermissionId(dataPermissionId);
										dataPointPermissionVal.setVal(operationId);
										dataPointPermissionValService.save(dataPointPermissionVal);
									}
//									dataPermissionService.save(dataPermission);
								}else{
									for(int i=0;i<listDataPointPermission.size();i++){
										Map<String,Object> dataMap = listDataPointPermission.get(i);
										if(((String)dataMap.get("role_id")).equals(roleId) && ((String)dataMap.get("val")).equals(operationId)){
											listDataPointPermissionIds.add(((String)dataMap.get("id")));
										}
									}
								}
							}
							
						}
						if (listDataPermissionIds.size() > 0) {
							dataPermissionService.deleteAllObjectByIds(listDataPermissionIds);
						}
						
						if (listDataPointPermissionIds.size() > 0) {
							dataPointPermissionValService.deleteAllObjectByIds(listDataPointPermissionIds);
						}
					}
				
				}
				
				
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
