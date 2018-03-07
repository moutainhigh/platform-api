package com.xinleju.platform.out.app.old.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.data.cache.api.MDProductCacheService;
import com.xinleju.erp.data.cache.dto.ProductTypeDTO;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.service.api.extend.dto.RoleDTO;
import com.xinleju.erp.flow.service.api.extend.dto.UserDTO;

public class MDProductCacheServiceImpl implements MDProductCacheService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public FlowResult<List<ProductTypeDTO>> getProductTypeByParentId(
			String parentId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据Id查询产品类型
	 * @param id
	 * @return
	 */
	@Override
	public FlowResult<ProductTypeDTO> getProductTypeById(String productTypeId) {
		ProductTypeDTO  productTypeDTO = new ProductTypeDTO();
		List<Map<String,Object>> listResult = new ArrayList<Map<String,Object>>();
		FlowResult result=new FlowResult();
		try {
			String sql=" select id,`name`,`code`,`status`,remark statement,parent_id parentId ,delflag,prefix_id from pt_sys_base_project_type where id = '"+productTypeId+"'  ";
			listResult = jdbcTemplate.queryForList(sql);
			if(listResult.size()>0){
				productTypeDTO.setId((String)listResult.get(0).get("id"));
				productTypeDTO.setName((String)listResult.get(0).get("name"));
				productTypeDTO.setCode((String)listResult.get(0).get("code"));
				productTypeDTO.setParentId((String)listResult.get(0).get("parentId"));
				productTypeDTO.setStatement((String)listResult.get(0).get("statement"));
				
				if(listResult.get(0).get("delflag") != null){
					productTypeDTO.setIsDelete(Integer.parseInt((String)listResult.get(0).get("delflag")));
				}
				if(listResult.get(0).get("status")!= null){
					productTypeDTO.setStatus(Integer.parseInt((String)listResult.get(0).get("status")));
				}
				
				String prefixId = (String)listResult.get(0).get("prefix_id");
				productTypeDTO.setLevel(prefixId.split("-").length);
			}
			result.setSuccess(true);
			result.setResult(productTypeDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public FlowResult<List<ProductTypeDTO>> getProductTypeByIds(
			String[] productTypeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取所有子产品类型
	 * @param parentId
	 * @return
	 */
	@Override
	public FlowResult<List<ProductTypeDTO>> getAllSubProductType() {
		List<ProductTypeDTO> list = new ArrayList<ProductTypeDTO>();
		List<Map<String,Object>> listResult = new ArrayList<Map<String,Object>>();
		FlowResult result=new FlowResult();
		try {
			String sql="  select id,`name`,`code`,`status`,remark statement,parent_id parentId ,delflag,prefix_id from pt_sys_base_project_type where parent_id is not NULL AND delflag=0 ";
			listResult = jdbcTemplate.queryForList(sql);
			
			for(Map map:listResult){
				ProductTypeDTO  productTypeDTO = new ProductTypeDTO();
				productTypeDTO.setId((String)map.get("id"));
				productTypeDTO.setName((String)map.get("name"));
				productTypeDTO.setCode((String)map.get("code"));
				productTypeDTO.setParentId((String)map.get("parentId"));
				productTypeDTO.setStatement((String)map.get("statement"));
				if(map.get("delflag")!= null){
					productTypeDTO.setIsDelete(Integer.parseInt((String)map.get("delflag")));
				}
				if(map.get("status")!= null){
					productTypeDTO.setStatus(Integer.parseInt((String)map.get("status")));
				}
				
				String prefixId = (String)map.get("prefix_id");
				productTypeDTO.setLevel(prefixId.split("-").length);
				
				list.add(productTypeDTO);
			}
			
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public FlowResult<Map<String, ProductTypeDTO>> getProductTypeMapByIds(
			String[] productTypeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<ProductTypeDTO>> getAllProductType() {
		List<ProductTypeDTO> list = new ArrayList<ProductTypeDTO>();
		List<Map<String,Object>> listResult = new ArrayList<Map<String,Object>>();
		FlowResult result=new FlowResult();
		try {
			String sql="  select id,`name`,`code`,`status`,remark statement,parent_id parentId ,delflag,prefix_id from pt_sys_base_project_type where delflag=0";
			listResult = jdbcTemplate.queryForList(sql);
			
			for(Map map:listResult){
				ProductTypeDTO  productTypeDTO = new ProductTypeDTO();
				productTypeDTO.setId((String)map.get("id"));
				productTypeDTO.setName((String)map.get("name"));
				productTypeDTO.setCode((String)map.get("code"));
				productTypeDTO.setParentId((String)map.get("parentId"));
				productTypeDTO.setStatement((String)map.get("statement"));
				if(map.get("delflag")!= null){
					productTypeDTO.setIsDelete(Integer.parseInt((String)map.get("delflag")));
				}
				if(map.get("status")!= null){
					productTypeDTO.setStatus(Integer.parseInt((String)map.get("status")));
				}
				
				String prefixId = (String)map.get("prefix_id");
				productTypeDTO.setLevel(prefixId.split("-").length);
				
				list.add(productTypeDTO);
			}
			
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public FlowResult<List<ProductTypeDTO>> getProductTypeByProjectBranchId(
			String projectBranchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<ProductTypeDTO> getProductTypeByIdProductConsId(
			String productConsId) {// TODO Auto-generated method stub
		return null;}

	@Override
	public FlowResult<Map<String, List<ProductTypeDTO>>> getProductTypeInProjectBranchIdMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
