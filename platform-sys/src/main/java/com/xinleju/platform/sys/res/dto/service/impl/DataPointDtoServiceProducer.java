package com.xinleju.platform.sys.res.dto.service.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.res.dto.service.DataPointDtoServiceCustomer;
import com.xinleju.platform.sys.res.entity.DataPoint;
import com.xinleju.platform.sys.res.service.DataPointService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class DataPointDtoServiceProducer implements DataPointDtoServiceCustomer{
	private static Logger log = Logger.getLogger(DataPointDtoServiceProducer.class);
	@Autowired
	private DataPointService dataPointService;

	public String save(String userInfo, String saveJson){
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   DataPoint dataPoint=JacksonUtils.fromJson(saveJson, DataPoint.class);
		   //校验itemId+code是否重复 add gyh
		   Map<String,Object> param=new HashMap<String, Object>();
		   param.put("itemId",dataPoint.getItemId());
		   param.put("code",dataPoint.getCode());
		   param.put("id",dataPoint.getId());
		   Integer c=dataPointService.checkItemIdAndPointCode(param);
		   if(c!=null&&c>0){
			   throw new InvalidCustomException("该业务对象下此编码已存在，不可重复");
		   }
		   //校验itemId+sort是否重复 add gyh
		   if(dataPoint.getSort() != null){
			   param.put("code",null);
			   param.put("sort",dataPoint.getSort());
			   Integer sortC =dataPointService.checkItemIdAndPointCode(param);
			   if(sortC!=null && sortC>0){
				   throw new InvalidCustomException("该业务对象下此排序号已存在，不可重复");
			   }
		   }
		   if(dataPoint.getType().equals("2")) {//引用类型，校验
			   Integer d=dataPointService.checkYyTypeCount(param);
			   if (d != null && d > 0) {
				   throw new InvalidCustomException("业务对象下只能存在一个引用类型控制点");
			   }
		   }
		   dataPointService.save(dataPoint);
		   info.setResult(JacksonUtils.toJson(dataPoint));
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
			   DataPoint dataPoint=JacksonUtils.fromJson(updateJson, DataPoint.class);
			   //校验itemId+code是否重复 add gyh
			   Map<String,Object> param=new HashMap<String, Object>();
			   param.put("itemId",dataPoint.getItemId());
			   param.put("code",dataPoint.getCode());
			   param.put("id",dataPoint.getId());
			   Integer c=dataPointService.checkItemIdAndPointCode(param);
			   if(c!=null&&c>0){
				   throw new InvalidCustomException("该业务对象下此编码已存在，不可重复");
			   }
			   //校验itemId+sort是否重复 add gyh
			   if(dataPoint.getSort() != null){
				   param.put("code",null);
				   param.put("sort",dataPoint.getSort());
				   Integer sortC =dataPointService.checkItemIdAndPointCode(param);
				   if(sortC!=null && sortC>0){
					   throw new InvalidCustomException("该业务对象下此排序号已存在，不可重复");
				   }
			   }
			   if(dataPoint.getType().equals("2")){//引用类型，校验
				   Integer d=dataPointService.checkYyTypeCount(param);
				   if(d!=null&&d>0){
					   throw new InvalidCustomException("业务对象下只能存在一个引用类型控制点");
				   }
			   }
			   int result=   dataPointService.update(dataPoint);
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
			   DataPoint dataPoint=JacksonUtils.fromJson(deleteJson, DataPoint.class);
			   int result= dataPointService.deleteObjectById(dataPoint.getId());
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
				   int result= dataPointService.deleteAllObjectByIds(list);
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
			DataPoint dataPoint=JacksonUtils.fromJson(getJson, DataPoint.class);
			DataPoint	result = dataPointService.getObjectById(dataPoint.getId());
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
				Page page=dataPointService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=dataPointService.getPage(new HashMap(), null, null);
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
				List list=dataPointService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=dataPointService.queryList(null);
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

	//通过条件查询控制点列表
	@Override
	public String queryDataPointByPram(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<DataPoint> list=dataPointService.queryDataPointByPram(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	//根据IDs逻辑删除控制点
	@Override
	public String deleteByIds(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				String[] ids=null;
				if(map.containsKey("ids")&&StringUtils.isNotBlank(map.get("ids").toString())){
					ids=map.get("ids").toString().split(",");
				}else{
					throw new InvalidCustomException("控制点ID不可为空");
				}
				Integer c=dataPointService.deleteByIds(ids);
				info.setResult(JacksonUtils.toJson(c));
				info.setSucess(true);
				info.setMsg("删除成功!");
			}
		} catch (Exception e) {
			log.error("删除失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("删除失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryUserIdForDataPoint(String userInfo,String parameter){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(parameter)){
				Map<String,Object> map=JacksonUtils.fromJson(parameter, HashMap.class);
				Map<String,Map<String,Object>> userMap = dataPointService.queryUserIdForDataPoint(map);
				info.setResult(JacksonUtils.toJson(userMap));
				info.setSucess(true);
				info.setMsg("查询数据权限相关人员列表成功!");
			}
		} catch (Exception e) {
			log.error("查询数据权限相关人员列表失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("查询数据权限相关人员列表失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 批量保存排序号
	 * @return
	 */
	public String savePointSort(String userInfo, String saveJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(saveJson, HashMap.class);

			Integer res=dataPointService.savePointSort(map);

			info.setResult(JacksonUtils.toJson(res));
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
