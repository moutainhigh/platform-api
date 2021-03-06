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
import com.xinleju.platform.sys.org.dto.RoleAndCataDto;
import com.xinleju.platform.sys.org.dto.RoleCatalogDto;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.dto.service.RoleCatalogDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.RoleCatalog;
import com.xinleju.platform.sys.org.entity.StandardRole;
import com.xinleju.platform.sys.org.service.RoleCatalogService;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;


/**
 * @author admin
 * 
 *
 */
 
public class RoleCatalogDtoServiceProducer implements RoleCatalogDtoServiceCustomer{
	private static Logger log = Logger.getLogger(RoleCatalogDtoServiceProducer.class);
	@Autowired
	private RoleCatalogService roleCatalogService;
	
	@Autowired
	private StandardRoleService standardRoleService;
	
	@Autowired
	private AppSystemService appSystemService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   RoleCatalog roleCatalog=JacksonUtils.fromJson(saveJson, RoleCatalog.class);
		   roleCatalogService.save(roleCatalog);
		   info.setResult(JacksonUtils.toJson(roleCatalog));
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
			   RoleCatalog roleCatalog=JacksonUtils.fromJson(updateJson, RoleCatalog.class);
			   int result=roleCatalogService.updateNew(roleCatalog);
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
			   RoleCatalog roleCatalog=JacksonUtils.fromJson(deleteJson, RoleCatalog.class);
			   int result= roleCatalogService.deleteObjectById(roleCatalog.getId());
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
				   int result= roleCatalogService.deleteAllObjectByIds(list);
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
			RoleCatalog roleCatalog=JacksonUtils.fromJson(getJson, RoleCatalog.class);
			RoleCatalog	result = roleCatalogService.getObjectById(roleCatalog.getId());
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
				Page page=roleCatalogService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=roleCatalogService.getPage(new HashMap(), null, null);
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
				List list=roleCatalogService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=roleCatalogService.queryList(null);
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
	public String getRoleTree(String userInfo, String paramater){
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try{
			Map<String,Object> param=JacksonUtils.fromJson(paramater, HashMap.class);
			//查询出来第一级根目录
			List<RoleNodeDto> list  = roleCatalogService.queryRoleCatalogRoot(param);
			param.put("sidx", "sort");
			param.put("sord", "asc");
			if(null!= param.get("roleCataStatus") ){
				param.put("status", param.get("roleCataStatus"));
			};
			//查询出来所有的目录
			List list_catalogAll = roleCatalogService.queryList(param);
			
			String isRole=(String)param.get("isRole");//是否查询角色：Y是，N否
			isRole=isRole==null?"Y":isRole;
			List list_roleAll=null;
			if(isRole.equals("Y")){
				//查询出来所有未删除的标准角色
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("delflag", "0");
				map.put("sidx", "sort");
				map.put("sord", "asc");
				if(null!= param.get("roleCataStatus") ){
					map.put("status", param.get("roleCataStatus"));
				};
				list_roleAll= standardRoleService.queryList(map);
			}
			
			//查询出来所有的角色目录以及角色
			for(RoleNodeDto roleNodeDto:list){
				getNodeList(roleNodeDto,list_catalogAll,list_roleAll);
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
	 * 递归
	 * @param paramater
	 * @return
	 */
	public RoleNodeDto getNodeList(RoleNodeDto roleNodeDto,List list_catalogAll,List list_roleAll) throws Exception{
		//查询子目录
//		List<RoleNodeDto> list_cata  = roleCatalogService.queryRoleCatalogList(roleNodeDto.getId());
		List<RoleNodeDto> list_cata  = getChildCatas(roleNodeDto,list_catalogAll);
		//查询子角色
//		List<RoleNodeDto> list_role  = standardRoleService.queryRoleListByCataId(roleNodeDto.getId());
		List<RoleNodeDto> list_role  = getChildRoles(roleNodeDto,list_roleAll);
		List<RoleNodeDto> list_c = new ArrayList<RoleNodeDto>();
		list_c.addAll(list_cata);
		list_c.addAll(list_role);
//		list_role.addAll(list_cata);
		roleNodeDto.setChildren(list_c);
		if(list_cata!=null && list_cata.size()>0){
			for(RoleNodeDto roleNodeDto1:list_cata){
				getNodeList(roleNodeDto1,list_catalogAll,list_roleAll);
			}
		}else{
			return roleNodeDto;
		}
		return roleNodeDto;
	}
	
	//查询子目录
	public List<RoleNodeDto> getChildCatas(RoleNodeDto roleNodeDto,List list_catalogAll){
		List<RoleNodeDto> list = new ArrayList<RoleNodeDto>();
		if(null!= list_catalogAll && list_catalogAll.size()>0 ){
			for(int i=0;i<list_catalogAll.size();i++){
				RoleCatalog roleCatalog = (RoleCatalog)list_catalogAll.get(i);
				if(roleNodeDto.getId().equals(roleCatalog.getParentId())){
					RoleNodeDto  roleDto = new RoleNodeDto();
					roleDto.setId(roleCatalog.getId());
					roleDto.setCode(roleCatalog.getCode());
					roleDto.setName(roleCatalog.getName());
					roleDto.setType((roleCatalog.getType()?"1":"0"));
					roleDto.setSort(roleCatalog.getSort());
					roleDto.setStatus(roleCatalog.getStatus());
					roleDto.setMold("cata");
					roleDto.setParentId(roleCatalog.getParentId());
					roleDto.setPrefixId(roleCatalog.getPrefixId());
					roleDto.setPrefixName(roleCatalog.getPrefixName());
					roleDto.setPrefixSort(roleCatalog.getPrefixSort());
					list.add(roleDto);
				}
			}
		}
		
		return list;
	}
	
	//查询子角色
	public List<RoleNodeDto> getChildRoles(RoleNodeDto roleNodeDto,List list_roleAll){
		List<RoleNodeDto> list = new ArrayList<RoleNodeDto>();
		if(null!= list_roleAll && list_roleAll.size()>0 ){
			for(int i=0;i<list_roleAll.size();i++){
				StandardRole standardRole = (StandardRole)list_roleAll.get(i);
				if(roleNodeDto.getId().equals(standardRole.getCatalogId())){
					RoleNodeDto  roleDto = new RoleNodeDto();
					roleDto.setId(standardRole.getId());
					roleDto.setCode(standardRole.getCode());
					roleDto.setName(standardRole.getName());
					roleDto.setType((standardRole.getType()?"1":"0"));
					roleDto.setSort(standardRole.getSort());
					roleDto.setStatus(standardRole.getStatus());
					roleDto.setMold("role");
					roleDto.setParentId(standardRole.getCatalogId());
					roleDto.setPrefixId(standardRole.getPrefixId());
					roleDto.setPrefixName(standardRole.getPrefixName());
					roleDto.setPrefixSort(standardRole.getPrefixSort());
					list.add(roleDto);
				}
			}
		}
		
		return list;
	}
	
	@Override
	public String saveRoleOrCata(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   RoleAndCataDto roleAndCataDto=JacksonUtils.fromJson(saveJson, RoleAndCataDto.class);
		   
		   //保存为目录
		   if("cata".equals(roleAndCataDto.getMold())){
			   RoleCatalog roleCatalog= new RoleCatalog();
			   //固定属性
			   roleCatalog.setId(roleAndCataDto.getId());
			   roleCatalog.setCreateDate(roleAndCataDto.getCreateDate()==null? null:roleAndCataDto.getCreateDate()); 
			   roleCatalog.setUpdateDate(roleAndCataDto.getUpdateDate()==null? null:roleAndCataDto.getUpdateDate()); 
			   roleCatalog.setCreatePersonId(roleAndCataDto.getCreatePersonId());
			   roleCatalog.setCreatePersonName(roleAndCataDto.getCreatePersonName());
			   roleCatalog.setUpdatePersonId(roleAndCataDto.getUpdatePersonId());
			   roleCatalog.setUpdatePersonName(roleAndCataDto.getUpdatePersonName());
			   roleCatalog.setCreateOrgId(roleAndCataDto.getCreateOrgId());
			   roleCatalog.setCreateOrgName(roleAndCataDto.getCreateOrgName());
			   roleCatalog.setCreateCompanyId(roleAndCataDto.getCreateCompanyId()); 
			   roleCatalog.setCreateCompanyName(roleAndCataDto.getCreateCompanyName()); 
			   roleCatalog.setConcurrencyVersion(roleAndCataDto.getConcurrencyVersion()); 
			   roleCatalog.setDelflag(roleAndCataDto.getDelflag());
			   
//			   //检查是否重名
			   Map mapcon = new HashMap<>();
			   mapcon.put("pId", roleAndCataDto.getCatalogId());
			   mapcon.put("name", roleAndCataDto.getName());
			   mapcon.put("type", "cata");
					   
			   Integer c=roleCatalogService.checkName(mapcon);
				if(c>0){
					throw new InvalidCustomException("名称已存在，不可重复");
				}
				//目录属性
			   roleCatalog.setCode(roleAndCataDto.getCode());
			   roleCatalog.setName(roleAndCataDto.getName());
			   roleCatalog.setType(roleAndCataDto.getType());
			   roleCatalog.setParentId(roleAndCataDto.getCatalogId());
			   roleCatalog.setIcon(roleAndCataDto.getIcon());
			   roleCatalog.setStatus(roleAndCataDto.getStatus());
			   roleCatalog.setRemark(roleAndCataDto.getRemark());
			   
			   Map<String,Object> map=new HashMap<String,Object>();
			   map.put("tableName", "pt_sys_org_role_catalog");
			   Long maxSort=appSystemService.getMaxSort(map)+1L;//排序号自动加1
			   //默认设置最大排序
			   roleCatalog.setSort(maxSort);
			   RoleCatalog parentRoleCatalog =  roleCatalogService.getObjectById(roleAndCataDto.getCatalogId());
			   
			   String prefixName=parentRoleCatalog.getPrefixName();
			   prefixName=prefixName.replaceAll("\\\\", "\\\\\\\\");
			   prefixName=prefixName.replaceAll("'", "\\\\\'");
			   roleCatalog.setPrefixId(parentRoleCatalog.getPrefixId()+"/"+roleAndCataDto.getId());
			   roleCatalog.setPrefixName(prefixName+"/"+roleAndCataDto.getName());
			   roleCatalog.setPrefixSort(parentRoleCatalog.getPrefixSort()+"-"+String.format("A%05d", maxSort));
			   
			   
			   roleCatalogService.save(roleCatalog);
			   info.setResult(JacksonUtils.toJson(roleCatalog));
			   info.setSucess(true);
			   info.setMsg("保存角色目录成功!");
		   }else if("role".equals(roleAndCataDto.getMold())){
			   //保存为角色
			   StandardRole standardRole = new StandardRole();
			   //固定属性
			   standardRole.setId(roleAndCataDto.getId());
			   standardRole.setCreateDate(roleAndCataDto.getCreateDate()==null? null:roleAndCataDto.getCreateDate()); 
			   standardRole.setUpdateDate(roleAndCataDto.getUpdateDate()==null? null:roleAndCataDto.getUpdateDate()); 
			   standardRole.setCreatePersonId(roleAndCataDto.getCreatePersonId());
			   standardRole.setCreatePersonName(roleAndCataDto.getCreatePersonName());
			   standardRole.setUpdatePersonId(roleAndCataDto.getUpdatePersonId());
			   standardRole.setUpdatePersonName(roleAndCataDto.getUpdatePersonName());
			   standardRole.setCreateOrgId(roleAndCataDto.getCreateOrgId());
			   standardRole.setCreateOrgName(roleAndCataDto.getCreateOrgName());
			   standardRole.setCreateCompanyId(roleAndCataDto.getCreateCompanyId()); 
			   standardRole.setCreateCompanyName(roleAndCataDto.getCreateCompanyName()); 
			   standardRole.setConcurrencyVersion(roleAndCataDto.getConcurrencyVersion()); 
			   standardRole.setDelflag(roleAndCataDto.getDelflag());
			 //检查是否重名
			   Map mapcon = new HashMap<>();
			   mapcon.put("pId", roleAndCataDto.getCatalogId());
			   mapcon.put("name", roleAndCataDto.getName());
			   mapcon.put("type", "role");
					   
			   Integer c=roleCatalogService.checkName(mapcon);
				if(c>0){
					throw new InvalidCustomException("名称已存在，不可重复");
				}
			   //角色属性
			   standardRole.setCode(roleAndCataDto.getCode());
			   standardRole.setName(roleAndCataDto.getName());
			   standardRole.setType(roleAndCataDto.getType());
			   standardRole.setCatalogId(roleAndCataDto.getCatalogId());
			   standardRole.setIcon(roleAndCataDto.getIcon());
			   standardRole.setStatus(roleAndCataDto.getStatus());
			   standardRole.setRemark(roleAndCataDto.getRemark());
			   standardRole.setDisabledId(roleAndCataDto.getDisabledId());
			   standardRole.setDisabledDate(roleAndCataDto.getDisabledDate());
			   standardRole.setTendId(roleAndCataDto.getTendId());
			   
			   Map<String,Object> map=new HashMap<String,Object>();
			   map.put("tableName", "pt_sys_org_standard_role");
			   Long maxSort=appSystemService.getMaxSort(map)+1L;//排序号自动加1
			   //默认设置最大排序
			   standardRole.setSort(maxSort);
			   RoleCatalog parentRoleCatalog =  roleCatalogService.getObjectById(roleAndCataDto.getCatalogId());
			   String prefixName=parentRoleCatalog.getPrefixName();
			   prefixName=prefixName.replaceAll("\\\\", "\\\\\\\\");
			   prefixName=prefixName.replaceAll("'", "\\\\\'");
			   standardRole.setPrefixId(parentRoleCatalog.getPrefixId()+"/"+roleAndCataDto.getId());
			   standardRole.setPrefixName(prefixName+"/"+roleAndCataDto.getName());
			   standardRole.setPrefixSort(parentRoleCatalog.getPrefixSort()+"-"+String.format("B%05d", maxSort));
			 
			   
			   standardRoleService.save(standardRole);
			   info.setResult(JacksonUtils.toJson(standardRole));
			   info.setSucess(true);
			   info.setMsg("保存角色成功!");
		   }
		   
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!"+e.getMessage());
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			RoleCatalogDto roleCatalogDto=JacksonUtils.fromJson(deleteJson, RoleCatalogDto.class);
			int result= roleCatalogService.deleteOrgAllSon(roleCatalogDto);
//			int result= orgnazationService.deleteObjectById(orgnazation.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("删除更新对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("删除对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}
}
