package com.xinleju.platform.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.flow.dao.InstanceAccessibleDao;
import com.xinleju.platform.flow.dto.AddReaderDto;
import com.xinleju.platform.flow.dto.BatchModifyReaderDto;
import com.xinleju.platform.flow.dto.InstanceAccessibleDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.InstanceAccessible;
import com.xinleju.platform.flow.service.InstanceAccessibleService;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;

/**
 * 修改可阅人服务
 * 
 */
@Service
public class InstanceAccessibleServiceImpl extends  BaseServiceImpl<String,InstanceAccessible> implements InstanceAccessibleService{
	

	@Autowired
	private InstanceAccessibleDao instanceAccessibleDao;

	/**
	 * 修改指定流程实例的可阅人集合
	 * 
	 * @param type:add/delete/reset
	 * @param instanceId
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean doUpdateReader(String type, String instanceId, List<UserDto> userList) throws Exception {
		
		//查询现在可阅人
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fiId", instanceId);
		List<InstanceAccessible> oldList = this.queryList(params);
		Map<String, InstanceAccessible> filterMap = new HashMap<String, InstanceAccessible>();
		for(InstanceAccessible instanceAccessible : oldList) {
			filterMap.put(instanceAccessible.getAccessibleId(), instanceAccessible);
		}
		
		//重置
		if("reset".equals(type)) {
			this.deleteByInstanceId(instanceId);
			List<InstanceAccessible> newUserList = new ArrayList<InstanceAccessible>();
			for(UserDto user : userList) {
				InstanceAccessible instanceAccessible = new InstanceAccessible();
				instanceAccessible.setId(IDGenerator.getUUID());
				instanceAccessible.setFiId(instanceId);
				instanceAccessible.setAccessibleId(user.getId());
				instanceAccessible.setAccessibleName(user.getName());
				newUserList.add(instanceAccessible);
				
			}
			saveBatch(newUserList);
			
			//增加
		} else if("add".equals(type)) {
			List<InstanceAccessible> newUserList = new ArrayList<InstanceAccessible>();
			for(UserDto user : userList) {
				if(filterMap.get(user.getId()) == null) {
					InstanceAccessible instanceAccessible = new InstanceAccessible();
					instanceAccessible.setId(IDGenerator.getUUID());
					instanceAccessible.setFiId(instanceId);
					instanceAccessible.setAccessibleId(user.getId());
					instanceAccessible.setAccessibleName(user.getName());
					newUserList.add(instanceAccessible);
				}
			}
			saveBatch(newUserList);
			
			//删除
		} else {
			List<String> ids = new ArrayList<String>();
			for(UserDto user : userList) {
				InstanceAccessible instanceAccessible = filterMap.get(user.getId());
				if(instanceAccessible != null) {
					ids.add(instanceAccessible.getId());
				}
			}
			this.deletePseudoAllObjectByIds(ids);
		}
		
		return true;
	}
	
	@Override
	public boolean deleteByInstanceId(String instanceId) {
		instanceAccessibleDao.deleteByInstanceId(instanceId);
		return true;
	}

	@Override
	public void addResetReaderFormData(BatchModifyReaderDto readerDto) throws Exception {
		String operateType = readerDto.getOperateType();
		String instanceIdText = readerDto.getInstanceIdText();
		String instanceIds[] = instanceIdText.split(",");
		//System.out.println("\n\n addResetReaderFormData operateType="+operateType+" instanceIdText = "+instanceIdText);
		
		if("reset".equals(operateType)){//重新设置需要删除对应的流程实例的可阅读人数据
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("instanceIds", instanceIds);
			instanceAccessibleDao.deleteReaderDataByParamMap(paramMap);
		}
		
		List<AddReaderDto> readerList = readerDto.getReaderList();
		for(int idx1 = 0; idx1<instanceIds.length; idx1++){
			String instanceId = instanceIds[idx1];
			for(AddReaderDto readerData : readerList){
				InstanceAccessible item = new InstanceAccessible();
				item.setFiId(instanceId);
				String idValues[] =  readerData.getIdValue().split(",");
				String nameValues[] =  readerData.getNameValue().split(",");
				if(StringUtils.isNotBlank(readerData.getIdValue())){
					for(int idx2=0; idx2<idValues.length; idx2++){
						item.setAccessibleId(idValues[idx2]);
						item.setAccessibleName(nameValues[idx2]);
						item.setId(IDGenerator.getUUID());
						item.setDelflag(false);
						instanceAccessibleDao.save(item);
					}
				}
			}
		}
		//System.out.println("\n\n addResetReaderFormData() is end.....");
	}

	@Override
	public void deleteReaderByFormData(BatchModifyReaderDto readerDto) {
		String instanceIdText = readerDto.getInstanceIdText().replaceAll("&&", ";;");
		String composeIdArray[] = instanceIdText.split("__");
		//实际上是var composedId = dataItem.instanceId+"&&"+dataItem.accessibleId+"__";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("composeIds", composeIdArray);
		instanceAccessibleDao.deleteReaderDataByParamMap(params);
	}

	@Override
	public List<InstanceAccessibleDto> queryInstanceReaderList(Map<String, Object> map) {
		String instanceIds = (String)map.get("instanceIds");
		if(instanceIds!=null && !"".equals(instanceIds) && !"null".equals(instanceIds)){
			String instanceIdArray[] =  instanceIds.split(",");
			map.remove("instanceIds");
			map.put("instanceIds", instanceIdArray);
			return instanceAccessibleDao.queryInstanceReaderList(map);
		}else{
			return new ArrayList<InstanceAccessibleDto>();
		}
		
	}
}
