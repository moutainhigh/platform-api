package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.service.RootDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.Root;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.org.service.RootService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class RootDtoServiceProducer implements RootDtoServiceCustomer{
	private static Logger log = Logger.getLogger(RootDtoServiceProducer.class);
	@Autowired
	private RootService rootService;
	
	@Autowired
	private OrgnazationService orgnazationService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   Root root=JacksonUtils.fromJson(saveJson, Root.class);
		   rootService.save(root);
		   info.setResult(JacksonUtils.toJson(root));
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
			   Root root=JacksonUtils.fromJson(updateJson, Root.class);
			   int result=   rootService.update(root);
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
			   Root root=JacksonUtils.fromJson(deleteJson, Root.class);
			   int result= rootService.deleteObjectById(root.getId());
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
				   int result= rootService.deleteAllObjectByIds(list);
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
			Root root=JacksonUtils.fromJson(getJson, Root.class);
			Root	result = rootService.getObjectById(root.getId());
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
				Page page=rootService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=rootService.getPage(new HashMap(), null, null);
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
				List list=rootService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=rootService.queryList(null);
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
	
	/**
	 * 获取符合条件的根目录下树列表
	 * @param paramater
	 * @return
	 */
	@Override
	public String getTree(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try{
			Map<String,Object> map =JacksonUtils.fromJson(paramater, HashMap.class);
			//查询出来第一级根目录
			List<OrgnazationNodeDto> list  = rootService.queryListRoot(map);
			//查询出来所有目录
			List<OrgnazationNodeDto> list_cata  = rootService.queryAllRoot(map);
			//查询出来所有组织机构
			List<OrgnazationNodeDto> list_org  = orgnazationService.queryAllOrgList(map);
			
			for(OrgnazationNodeDto orgnazationNodeDto:list){
				getRootList(orgnazationNodeDto,list_cata,list_org);
			}
			
			info.setResult(JacksonUtils.toJson(list));
		    info.setSucess(true);
		    info.setMsg("获取树对象成功!");
			System.out.println(JacksonUtils.toJson(list));
		}catch(Exception e){
			log.error("获取树对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取树对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	
	/**
	 * 递归获取目录子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public OrgnazationNodeDto getRootList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_cata,List<OrgnazationNodeDto> list_org) throws Exception{
//		List<OrgnazationNodeDto> list1  = rootService.queryListRoot(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryCataChildNode(orgnazationNodeDto.getId(),list_cata);
		orgnazationNodeDto.setChildren(list1);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getRootList(orgnazationNodeDto1,list_cata,list_org);
			}
		}else{
			List<OrgnazationNodeDto> ll= getOrgRootList(orgnazationNodeDto,list_org);
			for(OrgnazationNodeDto orgnazationNodeDto2:ll){
				getOrgList(orgnazationNodeDto2,list_org);
			}
			return orgnazationNodeDto;
		}
		getOrgList(orgnazationNodeDto,list_org);
		return orgnazationNodeDto;
	}
	
	//获取目录子节点目录（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryCataChildNode(String parentId,List<OrgnazationNodeDto> list_cata){
		List<OrgnazationNodeDto> list_childNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_cata){
			if(parentId.equals(orgNodeDto.getParentId())){
				list_childNode.add(orgNodeDto);
			}
		}
		
		return list_childNode;
		
	}
	
	
	/**
	 * 获取目录下的一级集团和公司
	 * @param orgnazationNodeDto
	 * @return
	 */
	public List<OrgnazationNodeDto> getOrgRootList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_org ) throws Exception{
//		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgListRoot(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryOrgRootNode(orgnazationNodeDto.getId(),list_org);
		orgnazationNodeDto.setChildren(list1);
		return list1;
	}
	
	/**
	 * 递归查询组织结构子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public OrgnazationNodeDto getOrgList(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_org) throws Exception{
//		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgList(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryOrgChildNode(orgnazationNodeDto.getId(),list_org);
		orgnazationNodeDto.setChildren(list1);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getOrgList(orgnazationNodeDto1,list_org);
			}
		}else{
			return orgnazationNodeDto;
		}
		return orgnazationNodeDto;
	}
	
	//查询组织结构子节点（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryOrgChildNode(String parentId,List<OrgnazationNodeDto> list_org){
		List<OrgnazationNodeDto> listOrgChildNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_org){
			if(parentId.equals(orgNodeDto.getParentId())){
				listOrgChildNode.add(orgNodeDto);
			}
		}
		return listOrgChildNode;
	}
	
	//查询目录下的一级集团或者公司（代替从数据库中进行查询）
	public List<OrgnazationNodeDto> queryOrgRootNode(String rootId,List<OrgnazationNodeDto> list_org){
		List<OrgnazationNodeDto> listOrgChildNode = new ArrayList<OrgnazationNodeDto>();
		for(OrgnazationNodeDto orgNodeDto:list_org){
			if(rootId.equals(orgNodeDto.getRootId())){
				if(orgNodeDto.getParentId() == null || "".equals(orgNodeDto.getParentId())){
					if("company".equals(orgNodeDto.getType()) || "zb".equals(orgNodeDto.getType())){
						listOrgChildNode.add(orgNodeDto);
					}
				}
			}
		}
		return listOrgChildNode;
	}
	
	
	
	/**
	 * 获取符合条件的根目录下树列表
	 * @param paramater
	 * @return
	 */
	@Override
	public String getOrgTreeByType(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try{
			Map<String,Object> map =JacksonUtils.fromJson(paramater, HashMap.class);
			//查询出来第一级根目录
			List<OrgnazationNodeDto> list  = rootService.queryListRoot(map);
			//查询出来所有目录
			List<OrgnazationNodeDto> list_cata  = rootService.queryAllRoot(map);
			//查询出来所有组织机构
			List<OrgnazationNodeDto> list_org  = orgnazationService.queryAllOrgList(map);
			String type = "all";
			if(map.get("type")!=null ){
				type = (String)map.get("type");
			}
			
			
			for(OrgnazationNodeDto orgnazationNodeDto:list){
				getRootListBytype(orgnazationNodeDto,list_cata,list_org,type);
			}
			
			info.setResult(JacksonUtils.toJson(list));
		    info.setSucess(true);
		    info.setMsg("获取树对象成功!");
			System.out.println(JacksonUtils.toJson(list));
		}catch(Exception e){
			log.error("获取树对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取树对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 递归获取目录子节点
	 * @param orgnazationNodeDto
	 * @return
	 */
	public OrgnazationNodeDto getRootListBytype(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_cata,List<OrgnazationNodeDto> list_org,String type) throws Exception{
//		List<OrgnazationNodeDto> list1  = rootService.queryListRoot(orgnazationNodeDto.getId());
		List<OrgnazationNodeDto> list1  = queryCataChildNodeBytype(orgnazationNodeDto.getId(),list_cata);
		orgnazationNodeDto.setChildren(list1);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getRootListBytype(orgnazationNodeDto1,list_cata,list_org,type);
			}
		}else{
			List<OrgnazationNodeDto> ll= getOrgRootList(orgnazationNodeDto,list_org);
			for(OrgnazationNodeDto orgnazationNodeDto2:ll){
				getOrgListBytype(orgnazationNodeDto2,list_org,type);
			}
			return orgnazationNodeDto;
		}
		getOrgListBytype(orgnazationNodeDto,list_org,type);
		return orgnazationNodeDto;
	}
	
	//获取目录子节点目录（代替从数据库中进行查询）
		public List<OrgnazationNodeDto> queryCataChildNodeBytype(String parentId,List<OrgnazationNodeDto> list_cata){
			List<OrgnazationNodeDto> list_childNode = new ArrayList<OrgnazationNodeDto>();
			for(OrgnazationNodeDto orgNodeDto:list_cata){
				if(parentId.equals(orgNodeDto.getParentId())){
					list_childNode.add(orgNodeDto);
				}
			}
			
			return list_childNode;
			
		}
		
		/**
		 * 递归查询组织结构子节点
		 * @param orgnazationNodeDto
		 * @return
		 */
		public OrgnazationNodeDto getOrgListBytype(OrgnazationNodeDto orgnazationNodeDto,List<OrgnazationNodeDto> list_org,String type) throws Exception{
//			List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgList(orgnazationNodeDto.getId());
			List<OrgnazationNodeDto> list1  = queryOrgChildNodeBytype(orgnazationNodeDto.getId(),list_org,type);
			orgnazationNodeDto.setChildren(list1);
			if(list1!=null && list1.size()>0){
				for(OrgnazationNodeDto orgnazationNodeDto1:list1){
					getOrgListBytype(orgnazationNodeDto1,list_org,type);
				}
			}else{
				return orgnazationNodeDto;
			}
			return orgnazationNodeDto;
		}
		
		//查询组织结构子节点（代替从数据库中进行查询）
		public List<OrgnazationNodeDto> queryOrgChildNodeBytype(String parentId,List<OrgnazationNodeDto> list_org,String type){
			List<OrgnazationNodeDto> listOrgChildNode = new ArrayList<OrgnazationNodeDto>();
			for(OrgnazationNodeDto orgNodeDto:list_org){
				if(parentId.equals(orgNodeDto.getParentId())){
					if(type.equals("all")){
						listOrgChildNode.add(orgNodeDto);
					}else if(type.equals("company")){
						if(orgNodeDto.getType().equals("company") || orgNodeDto.getType().equals("zb") ){
							listOrgChildNode.add(orgNodeDto);
						}
					}else if(type.equals("dept")){
						if(orgNodeDto.getType().equals("company") || orgNodeDto.getType().equals("zb") || orgNodeDto.getType().equals("dept")){
							listOrgChildNode.add(orgNodeDto);
						}
					}else if(type.equals("group")){
						if(orgNodeDto.getType().equals("company") || orgNodeDto.getType().equals("zb") || orgNodeDto.getType().equals("group")){
							listOrgChildNode.add(orgNodeDto);
						}
					}else if(type.equals("branch")){
						if(orgNodeDto.getType().equals("company") || orgNodeDto.getType().equals("zb") || orgNodeDto.getType().equals("group") || orgNodeDto.getType().equals("branch")){
							listOrgChildNode.add(orgNodeDto);
						}
					}
				}
			}
			return listOrgChildNode;
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
}
