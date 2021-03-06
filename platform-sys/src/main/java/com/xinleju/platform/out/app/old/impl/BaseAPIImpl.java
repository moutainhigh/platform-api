package com.xinleju.platform.out.app.old.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.utils.WhiteIpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.flow.flowutils.bean.AiBean;
import com.xinleju.erp.flow.flowutils.bean.CategoryDTO;
import com.xinleju.erp.flow.flowutils.bean.DebugInfo;
import com.xinleju.erp.flow.flowutils.bean.FileDto;
import com.xinleju.erp.flow.flowutils.bean.FlowDTO;
import com.xinleju.erp.flow.flowutils.bean.FlowInstanceDto;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.MsgDTO;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.flow.flowutils.bean.SimpleResult;
import com.xinleju.erp.flow.flowutils.bean.ToDoBean;
import com.xinleju.erp.flow.service.api.extend.BaseAPI;
import com.xinleju.erp.flow.service.api.extend.dto.CompanyDTO;
import com.xinleju.erp.flow.service.api.extend.dto.DataAuthDTO;
import com.xinleju.erp.flow.service.api.extend.dto.DeptDTO;
import com.xinleju.erp.flow.service.api.extend.dto.FlowInsDTO;
import com.xinleju.erp.flow.service.api.extend.dto.FuncDTO;
import com.xinleju.erp.flow.service.api.extend.dto.GroupDTO;
import com.xinleju.erp.flow.service.api.extend.dto.OpDTO;
import com.xinleju.erp.flow.service.api.extend.dto.OrgnDTO;
import com.xinleju.erp.flow.service.api.extend.dto.RoleDTO;
import com.xinleju.erp.flow.service.api.extend.dto.ScopeDTO;
import com.xinleju.erp.flow.service.api.extend.dto.UserDTO;
import com.xinleju.erp.sm.extend.dto.MailDTO;
import com.xinleju.erp.sm.extend.dto.SmDTO;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.tools.data.JacksonUtils;



public class BaseAPIImpl implements BaseAPI {
	
	private static Logger log = Logger.getLogger(BaseAPIImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public FlowResult<Boolean> updateFlowData(String bizId, String soCode, Map<String, Object> data) {
		
		return null;
	}

	@Override
	public FlowResult<List<CategoryDTO>> getRootCategories() {
		
		log.info("getRootCategories start");

		FlowResult<List<CategoryDTO>> result = new FlowResult<List<CategoryDTO>>();
		String sql = "select id,name from pt_sys_res_app where delflag=0 AND status=1";
		RowMapper<CategoryDTO> rowMapper = new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class);

		try {
			List<CategoryDTO> queryList = jdbcTemplate.query(sql, rowMapper);
			if (CollectionUtils.isEmpty(queryList)) {
				result.setResult(null);
			} else {
				result.setResult(queryList);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("getRootCategories error", e);
		}

		log.info("getRootCategories end");

		return result;
	}

	@Override
	public FlowResult<OrgnDTO> getParentOrgnByChildId(Long childId) {
		
		return null;
	}

	@Override
	public FlowResult<List<CategoryDTO>> getSubCategories(String parentId) {
		log.info("getSubCategories start");

		FlowResult<List<CategoryDTO>> result = new FlowResult<List<CategoryDTO>>();
		String sql = "select id,name from pt_flow_business_object "
				+ "where (data_type='1' or (data_type='2' and for_flow='1'))"
				+ " and delflag=0 and (parent_id=? or app_id = ?) order by sort";
		//zhengjiajie 增加了 or app_id = ? order by sort
		RowMapper<CategoryDTO> rowMapper = new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class);

		try {
			List<CategoryDTO> queryList = jdbcTemplate.query(sql, rowMapper, parentId, parentId);
			if (CollectionUtils.isEmpty(queryList)) {
				result.setResult(null);
			} else {
				result.setResult(queryList);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("getSubCategories error", e);
		}

		log.info("getSubCategories end");
		
		return result;
	}

	@Override
	public FlowResult<PageBean<FlowDTO>> getFlows(String categoryId, Integer start, Integer limit,
			Map<String, Object> extParm) {
		
		log.info("getFlows start");

		FlowResult<PageBean<FlowDTO>> result = new FlowResult<PageBean<FlowDTO>>();

		LinkedList<String> lstErrorInfoList = new LinkedList<String>();
		/*if (StringUtils.isBlank(categoryId)) {
			lstErrorInfoList.add("分类ID为空");
		}
		if (extParm == null || extParm.isEmpty()) {
			lstErrorInfoList.add("参数为空为空");
		}*/
		if (lstErrorInfoList.size() > 0) {
			DebugInfo debugInfo = new DebugInfo();
			debugInfo.setErrDesc(lstErrorInfoList);
			result.faliure();
			result.setDebugInfo(debugInfo);
			return result;
		}
		
		try {
			PageBean<FlowDTO> flowDTOPage = getFlowDTOPage(categoryId, start, limit, extParm);
			if (flowDTOPage == null) {
				result.setResult(null);
			} else {
				result.setResult(flowDTOPage);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("getFlows error", e);
		}

		log.info("getFlows end");

		return result;
	}

	@Override
	public FlowResult<Map<String, String>> getFlowNamesMapByFlowCodes(String... flowCodes) {
		FlowResult<Map<String, String>> result = new FlowResult<Map<String, String>>();
		if (flowCodes != null && flowCodes.length > 0 ) {
			String joinFlowCodes = StringUtils.join(flowCodes, "','");
			String sql = "select code,name from pt_flow_fl fl where ifnull(fl.delflag,0)=0 and fl.use_status=1 and fl.status='1' and code in ('" + joinFlowCodes + "')";
			List<FlDto> list = jdbcTemplate.query(sql,new RowMapper<FlDto>(){
				@Override
				public FlDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					FlDto flDTO = new FlDto();
					flDTO.setCode(rs.getString("code"));
					flDTO.setName(rs.getString("name"));
					return flDTO;
				}
				
			});
			
			if(CollectionUtils.isNotEmpty(list)){
				Map<String, String> map = new HashMap<String, String>();
				for (FlDto flDto : list) {
					map.put(flDto.getCode(), flDto.getName());
				}
				result.setResult(map);
			}			
		}
		return result;
	}

	@Override
	public FlowResult<Boolean> undo(String bizId, Long sodId, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> undo(String bizId, String flowCode, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> finishFi(String bizId, Long sodId, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Map<String, String>> getCurrentFlowUsers(String[] flowCodes, String[] bizIds) {
		
		log.info("getCurrentFlowUsers start");
		
		FlowResult<Map<String, String>> result = new FlowResult<Map<String, String>>();

		LinkedList<String> lstErrorInfoList = new LinkedList<String>();
		if(flowCodes == null || flowCodes.length == 0){
			lstErrorInfoList.add("流程编码请求参数为空");
		}
		if(bizIds == null || bizIds.length == 0){
			lstErrorInfoList.add("业务编号请求参数为空");
		}
		if (lstErrorInfoList.size() > 0) {
			DebugInfo debugInfo = new DebugInfo();
			debugInfo.setErrDesc(lstErrorInfoList);
			result.faliure();
			result.setDebugInfo(debugInfo);
			return result;
		}
		
		log.info("flowCode=" + JacksonUtils.toJson(flowCodes) +",bizId=" + JacksonUtils.toJson(bizIds));
		
		String joinBizId = StringUtils.join(bizIds, "','");
		String joinFlowCode = StringUtils.join(flowCodes,"','");
		String sql = "select i.business_id,g.participant_name from pt_flow_instance_task t,pt_flow_instance_group g,pt_flow_instance_post p, pt_flow_instance_ac ac,pt_flow_instance i,pt_flow_fl fl "
				+ "where t.group_id=g.id and g.ac_post_id=p.id and p.ac_id=ac.id and i.fl_id=fl.id and ac.fi_id=i.id "
				+ "and ifnull(t.delflag,0)=0 and ifnull(g.delflag,0)=0 and ifnull(p.delflag,0)=0 and ifnull(ac.delflag,0)=0 and ifnull(i.delflag,0)=0 "
				+ " and t.status='2' and p.status='2' and i.status='1' "
				+ " and i.business_id in('"+joinBizId+"') and fl.code in('"+joinFlowCode+"') ";
		log.info(" --->> sql ="+sql);
		
		final HashMap<String, String> mapResult = new HashMap<String, String>();
		try {
			
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					if(mapResult.containsKey(rs.getString("business_id"))){
						mapResult.put(rs.getString("business_id"), mapResult.get(rs.getObject("business_id")) + "," + rs.getString("participant_name"));
					}else {
						mapResult.put(rs.getString("business_id"),rs.getString("participant_name"));
					}
				}
			});
			result.setResult(mapResult);
		} catch (Exception e) {
			result.faliure();
			log.error("getCurrentFlowUsers error", e);
		}
		
		log.info("getCurrentFlowUsers end");
		
		return result;
	}

	@Override
	public FlowResult<List<FlowInstanceDto>> getEndFlowUsers(String flowCodes, String bizIds) {
		
		return null;
	}

	@Override
	public FlowResult<List<FileDto>> getFileList(String category, String bizIds) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> finishFi(String bizId, String flowCode, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> giveBackFi(String bizId, Long sodId, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> giveBackFi(String bizId, String flowCode, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> finishFiBranch(String bizId, String flowCode, String endTag, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> autoCommitFlow(String bizId, String flowCode, Long userId, Long spGwId,
			String userNote) {
		
		return null;
	}

	@Override
	public FlowResult<FlowInsDTO> getFlowInsByFlowCodeAndBizId(String flowCode, String bizId) {
		
		return null;
	}

	@Override
	public FlowResult<List<FlowInsDTO>> getFlowInsByFlowCodeAndBizIds(String flowCode, List<String> bizIds) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> canUseFlow(String flowCode, Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<List<AiBean>> getAiList(Long fiId) {
		
		return null;
	}

	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	@Override
	public FlowResult<Boolean> sendMsg(String module, String msgId, String title, String url, 
			String typedDate, String opType, String msgType, String loginName, Map<String, Object> extParm) {
		//sendMsg("PU", "supplier_eval_2063750", "请对放心贷科技创新股份有限公司-建筑施工总承包进行现场考察打分", 
		//"eval_score!evalStrong.do?businessId=2217391&participationId=2063750&evalOrgnInsId=2061793", 
		// "2017-07-10 18:37:21", "DB", "0", "jiangkuo", null)
		FlowResult<Boolean> result=new FlowResult<>();
		
		String business_id = "N/A";
		String mobibleParam = "";
		String mobibleUrl = "mobile/common/common.html";
		String firstType = "common";
		if(extParm==null){
			extParm = new HashMap<String, Object>();
		}
		String extParmText = "";
		if(extParm!=null && extParm.get("fi_id")!=null){
			extParmText = extParm.get("fi_id").toString();
		}
		if(msgId == null){
			msgId = "N/A";
		}
		if(title == null){
			title = "N/A";
		}
		if(loginName == null){
			loginName = "N/A";
		}
		
		if(msgType == null){
			msgType = "N/A";
		}
		
		if(opType == null){
			opType = "N/A";
		}
		
		if(module == null){
			module = "N/A";
		}
		
		if(business_id == null) {
			business_id = "N/A";
		}
		
		typedDate = getDate("yyyy-MM-dd HH:mm:ss");
		log.info("sendMsg() 001 typedDate="+typedDate);
		
		if(url == null){
			url = "N/A";
		}
		int resultSum = 0;
		//001-处理发起  和 待办待阅未读的情况
		if("LC".equals(opType) || "FQ".equals(opType) 
			|| "DB".equals(opType) || "DY".equals(opType)  || "WD".equals(opType)){
			
			if("LC".equals(opType)){//为了统一起见, 把LC'发起'转为FQ
				opType = "FQ";
			}
			
			if("FQ".equals(opType)){
				String querySql = " select count(id) from pt_flow_sys_notice_msg "
				+ " where app_code='"+module+"' and id='"+msgId+"' and op_type='"+opType+"' ";
				log.info("000 sendMsg() 查询是否存在相同的发起消息  querySql = "+querySql);
				int sum = jdbcTemplate.queryForObject(querySql, Integer.class);
				if(sum>0){//如果已经存在相同的发起的消息了
					result.setResult(true);
					result.setSuccess(true);
					return result;
				}
			}
			
			if("WD".equals(opType)){//为了统一起见, 把WD'未读'转为DY
				opType = "DY";
			}
			
			log.info("sendMsg() 001 loginName="+loginName);
			String sql="select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position"
					+" from pt_sys_org_user u "
					+" LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+" LEFT JOIN pt_sys_org_post p on pu.post_id = p.id"
					+" LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id"
					+" where u.login_name = '"+loginName+"'";
			log.info("001 sendMsg() 查询用户信息 sql = "+sql);
			
			RowMapper<UserDTO> rowMapper=new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			UserDTO userDto = jdbcTemplate.queryForObject(sql,rowMapper);
			String userId = userDto.getId();
			String realName = userDto.getRealName();
			//String userId = "user_2041865";
			//String realName = "司卫军";
			
			log.info("sendMsg() 002 userId="+userId+"; realName="+realName);
			
			StringBuilder builder  = new StringBuilder("INSERT INTO pt_flow_sys_notice_msg(");
			builder.append(" id,  title, login_name, user_id, user_name,") //5
			.append(" msg_type, op_type, source, app_code, business_id, ") //5
			.append(" send_date, extend_info, url, mobible_param, is_open, ")//5
			.append(" mobible_url, if_bussniss_object_lock, first_type, delflag) values(");//3
			builder.append(" '"+msgId+"','"+title+"','"+loginName+"','"+userId+"','"+realName+"', ")
			.append(" '"+msgType+"','"+opType+"','"+module+"', '"+module+"','"+business_id+"', ")
			.append(" '"+typedDate+"','"+extParmText+"','"+url+"', '"+mobibleParam+"', '1', ");
//			.append(""+mobibleUrl+", 0, '"+firstType+"', '0' )")
			String mUrl=extParm.get("mobileUrl")!=null?extParm.get("mobileUrl").toString():"";
			String ifBussnissObjectLock=extParm.get("ifBussnissObjectLock")!=null?extParm.get("ifBussnissObjectLock").toString():"";
			if(StringUtils.isNotBlank(mUrl)){
				builder.append("'"+mUrl+"',");
			}else {
				builder.append("'"+mobibleUrl+"',");
			}
			if(StringUtils.isNotBlank(ifBussnissObjectLock)){
				builder.append("'"+ifBussnissObjectLock+"',");
			}else {
				builder.append(" 0 ,");
			}
			builder.append("'"+firstType+"', '0')");
			log.info("sendMsg() 003 sql="+builder.toString());
			resultSum = jdbcTemplate.update(builder.toString());
			
		}else if("YB".equals(opType) || "YD".equals(opType)  //已办 已读 已阅  删除
				|| "YY".equals(opType) || "RM".equals(opType) ){
			if("YD".equals(opType) ){
				opType = "YY";
			}
			
			String updateSql = "update pt_flow_sys_notice_msg "
			+ " set op_type='"+opType+"', deal_date=now(), extend_info='"+extParmText+"' "
			+ " where app_code='"+module+"' and id='"+msgId+"' ";
			log.info("000 sendMsg() 执行修改消息类型的状态  updateSql = "+updateSql);
			if("RM".equals(opType)){
				updateSql = " update pt_flow_sys_notice_msg "
					+ " set op_type='"+opType+"', deal_date=now(), extend_info='"+extParmText+"', delflag=1 "
					+ " where app_code='"+module+"' and id='"+msgId+"' ";
				log.info("000 sendMsg() 执行修改消息类型的状态  updateSql = "+updateSql);
			}
			resultSum = jdbcTemplate.update(updateSql);
		}
		
		Boolean resultBool = true;
		if(resultSum==0){
			resultBool = false;
		}
		result.setResult(resultBool);
		result.setSuccess(true);
		return result;
	}

	@Override
	public FlowResult<Boolean> store(String module, String storeType, String contentType, String contentId,
			String title, String url) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> unstore(String module, String storeType, String contentType, String contentId) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> isStored(String module, String storeType, String contentType, String contentId) {
		
		return null;
	}

	@Override
	public FlowResult<DataAuthDTO> getDataAuth(String loginName) {
		
		return null;
	}

	@Override
	public FlowResult<List<FuncDTO>> getFuncAuth(String loginName, String moduleCode) {
		List<FuncDTO> listFuncDTO = new ArrayList<FuncDTO>();
		List<Map<String,Object>> roleList = null;
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			String queryRoleSql = "SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE  "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  "
					+" where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+" and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '"+loginName+"'  "
					+" and pu.delflag = 0 and p.delflag = 0 and p.status = 1 "
					+" UNION "
					+" SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  "
					+" where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '"+loginName+"' and r.delflag = 0 and r.status = 1 ";
			roleList = jdbcTemplate.queryForList(queryRoleSql);
			List<String> roleListString = null;
			String roleString = "";
			if(null != roleList && roleList.size()>0){
				for(Map map:roleList){
					roleString += "'"+(String)map.get("id")+"',";
				}
			}
			
			if(roleString.length()>0){
				roleString = roleString.substring(0,roleString.length()-1);
			}else{
				result.setSuccess(false);
				result.setResult(listFuncDTO);
				return result;
			}
			//'02171fcbc4264f17953af8f09aa57ecf','80ffdb603c7741db86bede84522a91b6','890aadd5a36d465b9d545fccc934b5ca','fa5e056a9b86457ba3430fde679921c2'
//			String queryScopeSql = " select t.id,t.item_id,t.CODE,t.NAME,tt.role_id ,GROUP_CONCAT(ttt.val) val from pt_sys_res_data_point t "
//					+" LEFT JOIN pt_sys_res_data_permission tt on t.id = tt.data_point_id  "
//					+" LEFT JOIN pt_sys_res_data_point_permission_val ttt on ttt.data_permission_id = tt.id  "
//					+" where tt.role_id in ("+roleString+") "
//					+" GROUP BY t.id,tt.role_id  ";
			
			
			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url,r.id resId,r.name reaName,r.`code` resCode,r.url resUrl,r.parent_id resParentId,r.app_id appId,app.`code` appCode from pt_sys_res_operation t  "
					+" LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id "
					+" LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id "
					+" left JOIN pt_sys_res_app app on t.app_id = app.id "
					+" where tt.role_id in ("+roleString+") ";
			if(null != moduleCode && !"".equals(moduleCode)){
				queryResultSql = queryResultSql + " AND app.`code` = '"+moduleCode+"' ";
			}
			
			queryResultSql = queryResultSql + " GROUP BY r.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for(Map map:resultList){
				FuncDTO  funcDTO = new FuncDTO();
				funcDTO.setId((String)map.get("resId"));
				funcDTO.setCode((String)map.get("resCode"));
				funcDTO.setName((String)map.get("reaName"));
				funcDTO.setUrl((String)map.get("resUrl"));
				funcDTO.setParentId((String)map.get("resParentId"));
				funcDTO.setSystemCode((String)map.get("appCode"));
				listFuncDTO.add(funcDTO);
			}
			result.setSuccess(true);
			result.setResult(listFuncDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	@Override
	public FlowResult<List<OpDTO>> getOpAuth(String loginName, String moduleCode, String funcCode) { 
		List<OpDTO> listOpDTO = new ArrayList<OpDTO>();
		List<Map<String,Object>> roleList = null;
		List<Map<String,Object>> postList = null;
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			String queryRoleSql = "SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE,pu.post_id as postId,pu.user_id as userId "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  "
					+" where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+" and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '"+loginName+"'  "
					+" and pu.delflag = 0 and p.delflag = 0 and p.status = 1 "
					+" UNION "
					+" SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE,ru.post_id as postId,ru.user_id as userId "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  "
					+" where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '"+loginName+"' and r.delflag = 0 and r.status = 1 ";
			roleList = jdbcTemplate.queryForList(queryRoleSql);
			List<String> roleListString = null;
			String roleString = "";
			String postIds="";
			if(null != roleList && roleList.size()>0){
				for(Map map:roleList){
					if(roleString.contains(map.get("id").toString())){
						roleString +="'"+(String)map.get("postId")+"',";
						postIds +="'"+(String)map.get("postId")+"',";
					}else {
						roleString += "'"+(String)map.get("id")+"',"+"'"+(String)map.get("postId")+"',";
						postIds +="'"+(String)map.get("postId")+"',";
					}
				}
				roleString+="'"+(String)roleList.get(0).get("userId")+"',";
			}
			//根据postid查询 pt_sys_org_role_user的roleId,解决角色引人授权问题
			String postSql="SELECT s.role_id as postId FROM pt_sys_org_role_user s "
					+ "WHERE s.post_id IN ( "+postIds.substring(0,postIds.length()-1)+" ) "
					+ "AND s.post_id IS NOT NULL AND s.user_id = '';";
			postList = jdbcTemplate.queryForList(postSql);
			if(!postList.isEmpty()&&postList.size()>0){
				for(Map map:postList){
					roleString +="'"+(String)map.get("postId")+"',";
				}
			}
			if(roleString.length()>0){
				roleString = roleString.substring(0,roleString.length()-1);
			}else{
				result.setSuccess(false);
				result.setResult(listOpDTO);
				return result;
			}
			
			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url from pt_sys_res_operation t  " 
					+" LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id " 
					+" LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id " 
					+" left JOIN pt_sys_res_app app on t.app_id = app.id " 
					+" where tt.role_id in ("+roleString+") " ;
			
			if(null != moduleCode && !"".equals(moduleCode)){
				queryResultSql = queryResultSql + " AND app.code = '"+moduleCode+"' ";
			}
			if(null != funcCode && !"".equals(funcCode)){
				queryResultSql = queryResultSql + " and r.`code` = '"+funcCode+"' ";
			}
			queryResultSql = queryResultSql + " GROUP BY t.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for(Map map:resultList){
				String id = (String)map.get("id");
				String name = (String)map.get("name");
				String code = (String)map.get("code");
				String resource_id = (String)map.get("resource_id");
				String url = (String)map.get("url");
				OpDTO opDTO = new OpDTO();
				opDTO.setName(name);
				opDTO.setCode(code);
				opDTO.setFuncModuleId(resource_id);
				opDTO.setButtonUrl(url);
				listOpDTO.add(opDTO);
			}
			result.setSuccess(true);
			result.setResult(listOpDTO);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
		
	}

	@Override
	public FlowResult<Boolean> getNotSubmitFiByFlowCodeAndBizId(String flowCode, String bizId) {
		
		return null;
	}

	@Override
	public FlowResult<String> getMaxBizCode(String defineCode) {
		
		return null;
	}

	@Override
	public FlowResult<String> getNextBizCode(String defineCode) {
		
		return null;
	}

	@Override
	public FlowResult<List<OrgnDTO>> findRootOrgns(String[] includeTypes) {
		
		return null;
	}

	@Override
	public FlowResult<List<OrgnDTO>> findSubOrgns(String parentId, String[] includeTypes) {
		FlowResult<List<OrgnDTO>> result=new FlowResult<List<OrgnDTO>>();
		try {
			String sql="SELECT o.id,o.id refId,o.`name`,o.parent_id parentId,o.type,o.`code`,o.`status` from pt_sys_org_orgnazation o"
					+" WHERE o.delflag = 0 and o.`status`=1 and o.type in ('%s') and o.parent_id=?"
					+" UNION"
					+" SELECT o.id, o.id refId, o.`name`, o.parent_id parentId, o.type, o.`code`, o.`status`"
					+" FROM pt_sys_org_orgnazation o WHERE o.delflag = 0 AND o.`status` = 1 and o.type in ('%s')  AND o.root_id = ? and IFNULL(o.parent_id,'')=''";
			if (includeTypes!=null&&includeTypes.length>0) {
				String type=StringUtils.join(includeTypes,"','");
				type=type.replaceAll("department", "dept");
				type=type.replaceAll("company", "company','zb");
				sql=String.format(sql, type,type);
				RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
				List<OrgnDTO> list= jdbcTemplate.query(sql,rowMapper,parentId,parentId);
				result.setResult(list);
			}else {
				result.setResult(null);
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public FlowResult<PageBean<UserDTO>> getUserList(String peId, String name, Integer start, Integer limit,
			Boolean includeAllSubOrgns, Map<String, Object> extParm) {
		List<UserDTO> list = null;
		List<Map<String,Object>> listResult = new ArrayList<Map<String,Object>>();
		FlowResult result=new FlowResult();
		String sql = "";
		String sqlCount="";
		//判断是否包含子集
		try {
			if(includeAllSubOrgns){
				String orgSql = "select id from pt_sys_org_orgnazation t where t.prefix_id like '%"+peId+"%'";
				RowMapper<String> rowMapperOrg=new BeanPropertyRowMapper<String>(String.class);
				//查询包括自己的所有下级组织机构Id
				listResult = jdbcTemplate.queryForList(orgSql);
//				List<String> listOrgs = jdbcTemplate.query(orgSql,rowMapperOrg);
				String orgids = "";
				if(listResult.size()>0){
					for(int i =0;i<listResult.size();i++){
						listResult.get(i).get("id");
						orgids += (String)listResult.get(i).get("id")+"','";
					}
				}
				if(orgids.length()>0){
					orgids = orgids.substring(0,orgids.length()-3);
				}
				
//				String orgids = StringUtils.join(listOrgs, "','");
						
				sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email "
						+" FROM "
						+" pt_sys_org_user u "
						+" WHERE "
						+" u.id IN ( "
						+" SELECT "
						+" pu.user_id "
						+" FROM "
						+" pt_sys_org_post_user pu "
						+" WHERE "
						+" pu.post_id IN ( "
						+" SELECT "
						+" p.id "
						+" FROM "
						+" pt_sys_org_post p "
						+" WHERE "
						+" p.ref_id in ('"+orgids+"') "
						+" ) "
						+" ) ";
				
				sqlCount = "select count(*) "
						+" FROM "
						+" pt_sys_org_user u "
						+" WHERE "
						+" u.id IN ( "
						+" SELECT "
						+" pu.user_id "
						+" FROM "
						+" pt_sys_org_post_user pu "
						+" WHERE "
						+" pu.post_id IN ( "
						+" SELECT "
						+" p.id "
						+" FROM "
						+" pt_sys_org_post p "
						+" WHERE "
						+" p.ref_id in ('"+orgids+"') "
						+" ) "
						+" ) ";
				
				if(null != name && !"".equals(name)){
					String sqlName = " and (u.login_name like '%"+name+"%' or u.real_name like '%"+name+"%')  ";
					sql = sql + sqlName;
					sqlCount = sqlCount+sqlName;
				}
				
				if(null != start){
					String sqlLimit = " LIMIT "+start+","+limit+"  ";
					sql = sql + sqlLimit;
				}
				//查询总数
				int total= jdbcTemplate.queryForObject(sqlCount, Integer.class);
				
				RowMapper<UserDTO> rowMapper=new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
				//查询结果集
				list = jdbcTemplate.query(sql,rowMapper);
				
				PageBean pb = new PageBean(start,limit,total,list);
				pb.setList(list);
				result.setSuccess(true);
				result.setResult(pb);
			}else{
				sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email "
						+" FROM "
						+" pt_sys_org_user u "
						+" WHERE "
						+" u.id IN ( "
						+" SELECT "
						+" pu.user_id "
						+" FROM "
						+" pt_sys_org_post_user pu "
						+" WHERE "
						+" pu.post_id IN ( "
						+" SELECT "
						+" p.id "
						+" FROM "
						+" pt_sys_org_post p "
						+" WHERE "
						+" p.ref_id = '"+peId+"' "
						+" ) "
						+" ) ";
				
				sqlCount = "select count(*) "
						+" FROM "
						+" pt_sys_org_user u "
						+" WHERE "
						+" u.id IN ( "
						+" SELECT "
						+" pu.user_id "
						+" FROM "
						+" pt_sys_org_post_user pu "
						+" WHERE "
						+" pu.post_id IN ( "
						+" SELECT "
						+" p.id "
						+" FROM "
						+" pt_sys_org_post p "
						+" WHERE "
						+" p.ref_id = '"+peId+"' "
						+" ) "
						+" ) ";
				
				if(null != name && !"".equals(name)){
					String sqlName = " and (u.login_name like '%"+name+"%' or u.real_name like '%"+name+"%')  ";
					sql = sql + sqlName;
					sqlCount = sqlCount+sqlName;
				}
				
				if(null != start){
					String sqlLimit = " LIMIT "+start+","+limit+"  ";
					sql = sql + sqlLimit;
				}
				//查询总数
				int total= jdbcTemplate.queryForObject(sqlCount, Integer.class);
				
				RowMapper<UserDTO> rowMapper=new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
				//查询结果集
				list = jdbcTemplate.query(sql,rowMapper);
				
				PageBean pb = new PageBean(start,limit,total,list);
				pb.setList(list);
				result.setSuccess(true);
				result.setResult(pb);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByCompanyIdAndRoleId1(String compayId, String roleId) {
		
		return null;
	}

	@Override
	public FlowResult<UserDTO> getUserById(String userId) {
		FlowResult result=new FlowResult();
		try {
			String sql="select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position"
					+" from pt_sys_org_user u "
					+" LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+" LEFT JOIN pt_sys_org_post p on pu.post_id = p.id"
					+" LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id"
					+" where u.id = '"+userId+"' ";
			RowMapper<UserDTO> rowMapper=new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			UserDTO userDto = jdbcTemplate.queryForObject(sql,rowMapper);
			result.setSuccess(true);
			result.setResult(userDto);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public FlowResult<UserDTO> getUserByLoginName(String loginName) {
		FlowResult result=new FlowResult();
		try {
			String sql="select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position"
					+" from pt_sys_org_user u "
					+" LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+" LEFT JOIN pt_sys_org_post p on pu.post_id = p.id"
					+" LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id"
					+" where u.login_name = '"+loginName+"' and u.delflag = 0";
			RowMapper<UserDTO> rowMapper=new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			UserDTO userDto = jdbcTemplate.queryForObject(sql,rowMapper);
			result.setSuccess(true);
			result.setResult(userDto);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserByRealName(String realName) {
		
		return null;
	}

	@Override
	public FlowResult<DeptDTO> getDeptById(String deptId) {
		
		return null;
	}

	@Override
	public FlowResult<List<DeptDTO>> getUserDepts(String loginName) {
		String userIdSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=?";
		String userId=jdbcTemplate.queryForObject(userIdSql,String.class,loginName);
		FlowResult<List<DeptDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_user u on u.belong_org_id =o.id"
				+" where u.id=? and o.delflag=0 and o.`status`=1 and o.type in ('dept')"
				+" UNION"
				+" SELECT o.id,o.`code`,o.`name`,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation o"
				+" LEFT JOIN pt_sys_org_post p on p.ref_id =o.id "
				+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
				+" where o.delflag=0 and p.delflag=0 and o.`status`=1 and pu.user_id=? and o.type in ('dept')";
		RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
		List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,userId,userId);
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<List<RoleDTO>> getRootStandardRoles() {
		
		return null;
	}

	/**
     * 
     * 获取标准角色根
     * 
     * @return
     */
	@Override
	public FlowResult<List<RoleDTO>> getRootStandardRoles(Boolean includeDisabled) {
		String queryResultSql = "";
		if(includeDisabled){
			queryResultSql = " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog  where type = 1 ";
		}else{
			queryResultSql = " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog  where type = 1 and ( parent_id is null or parent_id ='' ) ";
		}
		
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for(Map map:resultList){
				RoleDTO  roleDTO = new RoleDTO();
				roleDTO.setId((String)map.get("id"));
				roleDTO.setName((String)map.get("name"));
				roleDTO.setNamefix((String)map.get("prefix_name"));
				roleDTO.setType((String)map.get("type"));
				roleDTO.setParentId((String)map.get("pId"));
				roleDTO.setCode((String)map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
		
	}

	@Override
	public FlowResult<List<RoleDTO>> getSubStandardRoles(String parentId) {
		
		return null;
	}
	/**
     * 根据ID获取子标准角色
     * 
     * @param parentId
     *            父角色ID
     * @return
     */
	@Override
	public FlowResult<List<RoleDTO>> getSubStandardRoles(String parentId, Boolean includeDisabled) {
		String queryResultSql = "";
		if(includeDisabled){
			queryResultSql = " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_id from pt_sys_org_role_catalog where prefix_id like '%"+parentId+"%' and id != '%"+parentId+"%' "
							+" UNION "
							+" select id,name,prefix_name,'1' type,catalog_id pId,`code`,prefix_id  from pt_sys_org_standard_role where prefix_id like '%"+parentId+"%' ";
		}else{
			queryResultSql = " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_id from pt_sys_org_role_catalog where parent_id = '"+parentId+"' "
							+" UNION "
							+" select id,name,prefix_name,'1' type,catalog_id pId,`code`,prefix_id  from pt_sys_org_standard_role where catalog_id = '"+parentId+"' ";
		}
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for(Map map:resultList){
				RoleDTO  roleDTO = new RoleDTO();
				roleDTO.setId((String)map.get("id"));
				roleDTO.setName((String)map.get("name"));
				roleDTO.setNamefix((String)map.get("prefix_name"));
				roleDTO.setType((String)map.get("type"));
				roleDTO.setParentId((String)map.get("pId"));
				roleDTO.setCode((String)map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
		
	}

	/**
     * 根据用户ID获取主角色
     * 
     * @param userId
     *            用户ID
     * @return
     */
	@Override
	public FlowResult<RoleDTO> getMainRole(String longinName) {
		String queryResultSql = "";
		queryResultSql = "  select s.id,s.name,s.prefix_name,'1' type,s.catalog_id pId,s.`code`,s.prefix_id "+
				" from pt_sys_org_standard_role  s"+
				" LEFT JOIN pt_sys_org_post p on s.id = p.role_id "+
				" LEFT JOIN pt_sys_org_post_user t on p.id = t.post_id "+
				" where t.user_id = '"+longinName+"' "+
				" and t.is_default = 1 ";
		RoleDTO  roleDTO = new RoleDTO();
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			resultList = jdbcTemplate.queryForList(queryResultSql);
			if(null!=resultList &&resultList.size()>0){
				roleDTO.setId((String)resultList.get(0).get("id"));
				roleDTO.setName((String)resultList.get(0).get("name"));
				roleDTO.setNamefix((String)resultList.get(0).get("prefix_name"));
				roleDTO.setType((String)resultList.get(0).get("type"));
				roleDTO.setParentId((String)resultList.get(0).get("pId"));
				roleDTO.setCode((String)resultList.get(0).get("code"));
				result.setSuccess(true);
				result.setResult(roleDTO);
			}
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
		
	}
	
	@Override
	public FlowResult<RoleDTO> getMainRole(Long userId) {
		
		return null;
	}

	@Override
	public FlowResult<RoleDTO> getRoleByCompanyId(String standardRoleId, String companyId) {
		
		return null;
	}

	@Override
	public FlowResult<RoleDTO> getRoleByTeamId(String standardRoleId, String teamId) {
		
		return null;
	}

	@Override
	public FlowResult<RoleDTO> getRoleByRoleId(String standardRoleId, String roleId) {
		
		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getAllCompany(Boolean includeDisabled) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o  "
				+" where o.delflag=0 and o.type in ('zb','company') ";
		FlowResult<List<CompanyDTO>> result=new FlowResult<>();
		if(includeDisabled == null || !includeDisabled){
			sql=sql+" and o.`status`=1";
		}
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getAllCompanyLevel1(Boolean includeDisabled) {
		
		return null;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyById(String id) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o  "
				+" where o.delflag=0  and o.id =? and o.type in ('zb','company')";
		FlowResult<CompanyDTO> result=new FlowResult<>();
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper,id);
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<SimpleResult> initGroupPartyStruct(GroupDTO groupDTO) {
		
		return null;
	}

	@Override
	public FlowResult<PageBean<MsgDTO>> getMsgList(MsgDTO msgDTO, Integer start, Integer limit) {
		
		return null;
	}

	@Override
	public FlowResult<Integer> getMsgCount(MsgDTO msgDTO) {
		
		return null;
	}

	@Override
	public FlowResult<ToDoBean> getToDoBeanByWiId(Long wiId, String category) {
		
		return null;
	}

	@Override
	public FlowResult<ToDoBean> getToDoBeanByTaskId(String taskId, String category) {
		
		return null;
	}

	@Override
	public FlowResult<ToDoBean> getToDoBeanByflowCodeAndbizId(String flowCode, String bizId) {
		
		return null;
	}

	@Override
	public FlowResult<SimpleResult> completeTask(Long opUserId, String taskId, String userNote, String opCode,
			String opName, String dealUsersIds, String backStepId, String backTaskId, boolean backSkip,
			String flowInsName) {
		
		return null;
	}

	@Override
	public FlowResult<SimpleResult> startFlowTask(String spGwId, String startUserId, String userNote, String flowCode,
			String bizId) {
		
		return null;
	}

	@Override
	public FlowResult<ToDoBean> getTodoBeanByWiId4Mobile(Long fiId, String category) {
		
		return null;
	}

	@Override
	public FlowResult<PageBean<Object[]>> getGroupLoginNameMsgDTO(MsgDTO msgDTO, Integer start, Integer limit) {
		
		return null;
	}

	@Override
	public FlowResult<SimpleResult> completeWork(Long opUserId, Long wiId, String userNote, String opCode,
			String opName, Long opRoleId, String dealUsersIds, Long backToWpId, String backSkip, String flowInsName) {
		
		return null;
	}

	@Override
	public FlowResult<Long> getDeptOrgnIdByLoginNameAndWiId(String loginName, Long wiId) {
		
		return null;
	}

	@Override
	public FlowResult<List<String>> getProjectBranchByScopeId(String authUserLoginName, String scopeId, String type) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> saveMail(MailDTO mc) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> saveSm(SmDTO sc) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> saveLog(Long userId, String moduleCode, String funcName, String url, String loginfo,
			String errorMsg, String returnMsg, String sign, Long type) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> saveLog(String ipAddress, Long userId, String loginName, String moduleCode,
			String funcName, String url, String loginfo, String errorMsg, String returnMsg, String sign, Long type) {
		
		return null;
	}

	@Override
	public FlowResult<PageBean<MsgDTO>> getMsgCreationList(String loginName, Integer start, Integer limit) {
		
		return null;
	}

	@Override
	public FlowResult<PageBean<MsgDTO>> getMsgNewCreationList(String loginName, Integer start, Integer limit) {
		
		return null;
	}

	@Override
	public FlowResult<List<FuncDTO>> getFuncAuthByModuleCode(String moduleCode) {
		
		return null;
	}

	@Override
	public FlowResult<List<OpDTO>> getOpAuthByModuleCode(String moduleCode) {
		
		return null;
	}

	@Override
	public FlowResult<List<String>> getFiCurrentAi(String bizId, String flowCode) {
		
		return null;
	}

	@Override
	public FlowResult<Boolean> rejectWi(Long wiId, String opCode, String opName, String userNote, Long backToWpId,
			String backSkip, String flowInsName, UserDTO userDto) {
		
		return null;
	}

	@Override
	public FlowResult<String> getOpTypeByWiId(String wiid) {
		
		return null;
	}

	@Override
	public FlowResult<Integer> getFileCount(String moduleCode, String ownid) {
		
		return null;
	}

	/*
	 * String[] arg0={"Category1","Ownerid1"};
	 * String[] arg1={"Category2","Ownerid2"};
	 * @see com.xinleju.erp.flow.service.api.extend.BaseAPI#getChkDraftFileCount(java.lang.String[], java.lang.String[])
	 */
	@Override
	public FlowResult<Integer> getChkDraftFileCount(String[] chkDraftBefore, String[] chkDraftAfter) {
		FlowResult<Integer> result = new FlowResult<>();
		StringBuffer sql = new StringBuffer();
		if (null == chkDraftBefore) {
			result.setSuccess(true);
			result.setResult(0);
		}
		sql.append("  SELECT COUNT(id) from pt_univ_file_attachment");
		sql.append("  WHERE business_id = '").append(chkDraftBefore[1]).append("' and category_id = '").append(chkDraftBefore[0]).append("'");
		if (null != chkDraftAfter) {
			sql.append("  UNION ALL");
			sql.append("  SELECT COUNT(id) from pt_univ_file_attachment");
			sql.append("  WHERE business_id = '").append(chkDraftAfter[1]).append("' and category_id = '").append(chkDraftAfter[0]).append("'");
		}
		
		List<Integer> listCount = jdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		});
		result.setSuccess(true);
		result.setResult(Collections.max(listCount));
		return result;
	}

	@Override
	/**
	 * 根据flowCode检查是否存在校稿环节
	 */
	public FlowResult<Boolean> isExistsChkDraftNode(String flowCode) {
		log.info("isExistsChkDraftNode start");

		FlowResult<Boolean> result = new FlowResult<Boolean>();
		String sql = "select count(fl_id) from pt_flow_ac ac ,  ( select id, max(version) from pt_flow_fl "+
					"    where status=1 and delflag=0 and use_status=1 and code= ? ) aa "+
					" where ac.approve_type_id in ('JG','jg')  and ac.fl_id = aa.id";
		try {
			log.info("isExistsChkDraftNode sql="+sql);
			int total = jdbcTemplate.queryForObject(sql, Integer.class, flowCode);
			if (total>=1) {
				result.setResult(true);
			} else {
				result.setResult(false);
			}
		} catch (Exception e) {
			result.faliure();
			log.error("isExistsChkDraftNode error", e);
		}

		log.info("isExistsChkDraftNode end");
		
		return result;
	}

	@Override
	public FlowResult<Boolean> isExistsRunningProofreadStep(String bizId, String flowCode, Long currLoginName) {
		
		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getAllCompanyLevel(Boolean includeDisabled) {
		
		return null;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByCompanyIdAndRoleId(Long compayId, Long roleId) {
		
		return null;
	}

	@Override
	public FlowResult<List<ScopeDTO>> getAuthScope(String moduleCode, String authUserLoginName, Long ctrId,
			Long fieldId) {
		
		return null;
	}

	@Override
	public List<RoleDTO> getPostsByCurrentUser(String loginName) {
		
		return null;
	}
	
	private PageBean<FlowDTO> getFlowDTOPage(String categoryId, Integer start, Integer limit, Map<String, Object> extParm) throws Exception{
		PageBean<FlowDTO> flowDTOPage = new PageBean<FlowDTO>();
    	if (null == start){
    		start = 0;
    	}
    	if (null == limit){
    		limit = 15;
    	}
    	
		
		StringBuffer sql = new StringBuffer("SELECT A.CODE AS flowCode,A.NAME AS flowName FROM PT_FLOW_FL A WHERE A.delflag = 0 AND A.status = 1 AND A.use_status = 1");
		StringBuffer sqlCount = new StringBuffer("SELECT COUNT(A.CODE) FROM PT_FLOW_FL A WHERE A.delflag = 0 AND A.status = 1 AND A.use_status = 1");
		StringBuffer conndition = new StringBuffer();
		/*if (categoryId!=null && StringUtils.isNotBlank(categoryId)) {
			conndition.append(" AND A.business_object_id IN ('");
			conndition.append("select id from pt_flow_business_object where parent_id='");
			conndition.append(categoryId);
			conndition.append("'");
			conndition.append("')");
		}*/
		
		if (categoryId!=null && StringUtils.isNotBlank(categoryId)) {
			conndition.append(" AND A.business_object_id IN ( ");
			conndition.append("select id from pt_flow_business_object where delflag=0 and ( parent_id='").append(categoryId)
			                   .append("' or app_id='").append(categoryId).append("' or id='").append(categoryId).append("' )");
			conndition.append(" )");
		}
		
		
		if (null != extParm) {
			if (extParm.containsKey("serviceObjectDefineId")) {
				String businessObjectId = String.valueOf(null != extParm.get("serviceObjectDefineId") ? extParm.get("serviceObjectDefineId") : "").trim();
				conndition.append(" AND (A.business_object_id = '");
				conndition.append(businessObjectId);
				conndition.append("' OR A.app_id = '");
				conndition.append(businessObjectId);
				conndition.append("')");
			}			
			if (extParm.containsKey("flName")) {
				String name = String.valueOf(null != extParm.get("flName") ? extParm.get("flName") : "").trim();
				conndition.append(" AND A.name LIKE '%");
				conndition.append(name);
				conndition.append("%'");
			}
			if (extParm.containsKey("flCode")) {
				String code = String.valueOf(null != extParm.get("flCode") ? extParm.get("flCode") : "").trim();
				conndition.append(" AND A.code LIKE '%");
				conndition.append(code);
				conndition.append("%'");
			}
		}
		
		sql.append(conndition).append(" ORDER BY A.version DESC").append(" LIMIT " + start + "," + limit);
		sqlCount.append(conndition);
		
		log.info(" 查询总数  jt.queryForObject()--->> sqlCount ="+sqlCount.toString());
		log.info(" 查询结果集  jt.queryForObject()--->> sql ="+sql.toString());
		
		
		//查询总数
		int total = jdbcTemplate.queryForObject(sqlCount.toString(), Integer.class);
		//查询结果集
		RowMapper<FlowDTO> rowMapper = new BeanPropertyRowMapper<FlowDTO>(FlowDTO.class);
		List<FlowDTO> list = jdbcTemplate.query(sql.toString(), rowMapper);
		flowDTOPage = new PageBean<FlowDTO>(start, limit, total, list);
		
		return flowDTOPage;
	}
	
	public static void main(String args[]){
		BaseAPIImpl test = new BaseAPIImpl();
		test.getFlows("60002", 0, 100, null);
	}

	/**
     * 插入或者更新菜单
     * @author sy
     * @param id 菜单Id，为空插入菜单，不为空，修改菜单
     * @param code  菜单code
     * @param name 菜单名称
     * @param url 菜单url
     * @param mobileUrl 菜单手机url
     * @param appCode 菜单对应的系统code
     * @param parentCode 菜单对应的上级菜单code 为空添加一级菜单
     * @param openmode 菜单打开方式，1是内部打开，0是外部打开
     * @param remark 备注
     * @return 菜单ID
     */
	@Override
	public FlowResult<String> saveOrUpdateMenu(String id, String code,
			String name, String url, String mobileUrl, String appCode,
			String parentCode, String openmode, String remark) {
		FlowResult<String> result=new FlowResult<>();
		//白名单拦截ip , add by gyh 20180130
		boolean checkIp = WhiteIpUtils.checkDubboMethod();
		if(!checkIp){
			result.setSuccess(false);
			DebugInfo info = new DebugInfo();
			info.addErrDesc(String.format(ErrorInfoCode.WRONG_WHITE_IP.getName(),WhiteIpUtils.getIpAddress()));
			result.setDebugInfo(info);
			return result;
		}
		if(null == id || "".equals(id)){
			String uuid = IDGenerator.getUUID();
			//查询系统Id，
			String app_id = "";
			String tend_id ="";
			String prefixIdApp ="";
			String prefixNameApp ="";
			String prefixSortApp ="";
			String sqlQueryApp ="select id,name,tend_id ,code,prefix_id,prefix_name,prefix_sort from pt_sys_res_app t where t.code = '"+appCode+"'";
			List<Map<String,Object>> listApp = new ArrayList<Map<String,Object>>();
			listApp = jdbcTemplate.queryForList(sqlQueryApp);
			if(listApp.size()>0){
					app_id = (String)listApp.get(0).get("id");
					tend_id = (String)listApp.get(0).get("tend_id");
					prefixIdApp = (String)listApp.get(0).get("prefix_id");
					prefixNameApp = (String)listApp.get(0).get("prefix_name");
					prefixSortApp = (String)listApp.get(0).get("prefix_sort");
			}else{
				result.setResult("传递的系统没有");
				return result;
			}
			
			//获取最大排序号
			String sqlGetMaxSort = "SELECT IFNULL(t.sort,0) maxSort from pt_sys_res_resource t order by t.sort  desc limit 1";
			int total= jdbcTemplate.queryForObject(sqlGetMaxSort, Integer.class);
			
			String sort = total+1+"";
			String parent_id ="";
			String prefix_id = "";
			String prefix_name = "";
			String prefix_sort = "";
			//获取上级菜单
			if(null==parentCode || "".equals(parentCode)){
				prefix_id = prefixIdApp+"/"+uuid;
				prefix_name = prefixNameApp+"/"+name;
				prefix_sort = prefixSortApp+"-"+String.format("B%05d", Long.parseLong(sort));
			}else{
				String sqlQueryPMenu ="select id,delflag,concurrency_version,code,name,tend_id,mobile_url,url,app_id,status,sort,parent_id,prefix_id,prefix_name,prefix_sort,isinventedmenu,isoutmenu,openmode,remark "+
						"from pt_sys_res_resource t where t.app_id = '"+app_id+"' and t.`code` = '"+parentCode+"'";
				List<Map<String,Object>> listMenu = new ArrayList<Map<String,Object>>();
				listMenu = jdbcTemplate.queryForList(sqlQueryPMenu);
				if(listMenu.size()>0){
					String prefixIdMenu = (String)listMenu.get(0).get("prefix_id");
					String prefixNameMenu = (String)listMenu.get(0).get("prefix_name");
					String prefixSortMenu = (String)listMenu.get(0).get("prefix_sort");
					parent_id = (String)listMenu.get(0).get("id");
					prefix_id = prefixIdMenu+"/"+uuid;
					prefix_name = prefixNameMenu+"/"+name;
					prefix_sort = prefixSortMenu+"-"+String.format("B%05d", Long.parseLong(sort));
				}else{
					result.setResult("传递的上级菜单没有");
					return result;
				}
			}
			String sql =" insert into pt_sys_res_resource(id,delflag,concurrency_version,code,name,tend_id," +
					"mobile_url,url,app_id,status,sort,parent_id,prefix_id,prefix_name,prefix_sort,isinventedmenu,isoutmenu,openmode,remark)"+
					"VALUES"+
					"('"+uuid+"','0','0','"+code+"','"+name+"','"+tend_id+"','"+mobileUrl+"','"+url+"','"+app_id+"'," +
					"'1','"+sort+"','"+parent_id+"','"+prefix_id+"','"+prefix_name+"','"+prefix_sort+"','0','0','"+openmode+"','"+remark+"')";
			int i = jdbcTemplate.update(sql);
			result.setResult(uuid);
		}else{
			//查询原始数据
			String sortOld = "";
			String sqlQueryMenu ="select id,delflag,concurrency_version,code,name,tend_id,mobile_url,url,app_id,status,sort,parent_id,prefix_id,prefix_name,prefix_sort,isinventedmenu,isoutmenu,openmode,remark "+
					"from pt_sys_res_resource t where id = '"+id+"'";
			List<Map<String,Object>> listMenuOld = new ArrayList<Map<String,Object>>();
			listMenuOld = jdbcTemplate.queryForList(sqlQueryMenu);
			if(listMenuOld.size()>0){
				 sortOld = (String)listMenuOld.get(0).get("sort");
			}else{
				result.setResult("传递的Id没有对应的数据");
				return result;
			}
			
			//查询系统Id，
			String app_id = "";
			String prefixIdApp ="";
			String prefixNameApp ="";
			String prefixSortApp ="";
			String sqlQueryApp ="select id,name,tend_id ,code,prefix_id,prefix_name,prefix_sort from pt_sys_res_app t where t.code = '"+appCode+"'";
			List<Map<String,Object>> listApp = new ArrayList<Map<String,Object>>();
			listApp = jdbcTemplate.queryForList(sqlQueryApp);
			if(listApp.size()>0){
					app_id = (String)listApp.get(0).get("id");
					prefixIdApp = (String)listApp.get(0).get("prefix_id");
					prefixNameApp = (String)listApp.get(0).get("prefix_name");
					prefixSortApp = (String)listApp.get(0).get("prefix_sort");
			}else{
				result.setResult("传递的系统没有");
				return result;
			}
			
			
			
			String parent_id ="";
			String prefix_id = "";
			String prefix_name = "";
			String prefix_sort = "";
			if(null==parentCode || "".equals(parentCode)){
				prefix_id = prefixIdApp+"/"+id;
				prefix_name = prefixNameApp+"/"+name;
				prefix_sort = prefixSortApp+"-"+String.format("B%05d", Long.parseLong(sortOld));
			}else{
				String sqlQueryPMenu ="select id,delflag,concurrency_version,code,name,tend_id,mobile_url,url,app_id,status,sort,parent_id,prefix_id,prefix_name,prefix_sort,isinventedmenu,isoutmenu,openmode,remark "+
						"from pt_sys_res_resource t where t.app_id = '"+app_id+"' and t.`code` = '"+parentCode+"'";
				List<Map<String,Object>> listMenu = new ArrayList<Map<String,Object>>();
				listMenu = jdbcTemplate.queryForList(sqlQueryPMenu);
				if(listMenu.size()>0){
					String prefixIdMenu = (String)listMenu.get(0).get("prefix_id");
					String prefixNameMenu = (String)listMenu.get(0).get("prefix_name");
					String prefixSortMenu = (String)listMenu.get(0).get("prefix_sort");
					parent_id = (String)listMenu.get(0).get("id");
					prefix_id = prefixIdMenu+"/"+id;
					prefix_name = prefixNameMenu+"/"+name;
					prefix_sort = prefixSortMenu+"-"+String.format("B%05d", Long.parseLong(sortOld));
				}else{
					result.setResult("传递的上级菜单没有");
					return result;
				}
			}
			
			String sql ="update pt_sys_res_resource set code='"+code+"' ,name='"+name+"',url='"+url+"',mobile_url='"+mobileUrl+"' where id = '"+id+"'";
			int i = jdbcTemplate.update(sql);
			result.setResult(id);
		}
		return result;
	}
	/**
     * 插入按钮
     * @author sy
     * @param name,code,funcModuleId,buttonurl,
     * @return 按钮Id
     */
	@Override
	public FlowResult<List<String>> saveOpDTO(List<OpDTO> opDtoList) {
		FlowResult<List<String>> result=new FlowResult<>();
		//白名单拦截ip , add by gyh 20180130
		boolean checkIp = WhiteIpUtils.checkDubboMethod();
		if(!checkIp){
			result.setSuccess(false);
			DebugInfo info = new DebugInfo();
			info.addErrDesc(String.format(ErrorInfoCode.WRONG_WHITE_IP.getName(),WhiteIpUtils.getIpAddress()));
			result.setDebugInfo(info);
			return result;
		}
		List<String> idList = new ArrayList<String>();
		for(OpDTO opDto:opDtoList){
			String id = IDGenerator.getUUID();
			String name = opDto.getName();
			String code = opDto.getCode();
			String delflag = "0";
			String appId = "";
			String resourceId = "";
			String prefixId = "";
			String prefixName = "";
			String prefixSort = "";
			String url = opDto.getButtonUrl();
			String type = "1";
			String tendId = "";
			//获取最大排序号
			String sqlGetMaxSort = "SELECT IFNULL(t.sort,0) maxSort from pt_sys_res_operation t order by t.sort  desc limit 1";
			int total= jdbcTemplate.queryForObject(sqlGetMaxSort, Integer.class);
			
			String sort = total+1+"";
			
			String sqlQueryPMenu ="select id,delflag,concurrency_version,code,name,tend_id,mobile_url,url,app_id,status,sort,parent_id,prefix_id,prefix_name,prefix_sort,isinventedmenu,isoutmenu,openmode,tend_id,remark "+
					"from pt_sys_res_resource t where t.id = '"+opDto.getFuncModuleId()+"'";
			List<Map<String,Object>> listMenu = new ArrayList<Map<String,Object>>();
			listMenu = jdbcTemplate.queryForList(sqlQueryPMenu);
			if(listMenu.size()>0){
				String prefixIdMenu = (String)listMenu.get(0).get("prefix_id");
				String prefixNameMenu = (String)listMenu.get(0).get("prefix_name");
				String prefixSortMenu = (String)listMenu.get(0).get("prefix_sort");
				appId = (String)listMenu.get(0).get("app_id");
				resourceId = (String)listMenu.get(0).get("id");
				tendId = (String)listMenu.get(0).get("tend_id");
				prefixId = prefixIdMenu+"/"+id;
				prefixName = prefixNameMenu+"/"+name;
				prefixSort = prefixSortMenu+"-"+String.format("C%05d", Long.parseLong(sort));
			}else{
				idList.add("传递的按钮上级菜单不存在");
				result.setResult(idList);
				return result;
			}
			String sql =" insert into pt_sys_res_operation(id,name,code,delflag,app_id,resource_id,prefix_id,prefix_name,prefix_sort,url,type,tend_id,sort)"+
					"VALUES"+
					"('"+id+"','"+name+"','"+code+"','"+delflag+"','"+appId+"','"+resourceId+"'," +
							"'"+prefixId+"','"+prefixName+"','"+prefixSort+"','"+url+"','"+type+"','"+tendId+"','"+sort+"')";
				int i = jdbcTemplate.update(sql);
				idList.add(id);
				
		}
		result.setResult(idList);
		return result;
	}
}
