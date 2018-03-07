package com.xinleju.platform.sys.base.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.dto.BusinessObjectDto;
import com.xinleju.platform.flow.dto.BusinessObjectVariableDto;
import com.xinleju.platform.flow.dto.service.BusinessObjectDtoServiceCustomer;
import com.xinleju.platform.flow.dto.service.BusinessObjectVariableDtoServiceCustomer;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.sys.base.dao.CustomFormDao;
import com.xinleju.platform.sys.base.dao.CustomFormGroupDao;
import com.xinleju.platform.sys.base.dao.CustomFormInstanceDao;
import com.xinleju.platform.sys.base.dao.CustomFormVersionHistoryDao;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.CustomFormMobileConvert;
import com.xinleju.platform.sys.base.dto.GeneralPaymentDTO;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormGroup;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;
import com.xinleju.platform.sys.base.entity.CustomFormVersionHistory;
import com.xinleju.platform.sys.base.service.CustomFormService;
import com.xinleju.platform.sys.num.dto.service.RulerSubDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.service.OperationDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.service.ResourceDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CustomFormServiceImpl extends  BaseServiceImpl<String,CustomForm> implements CustomFormService{
	

	@Autowired
	private CustomFormDao customFormDao;

	@Autowired
	private CustomFormVersionHistoryDao customFormVersionHistoryDao;
	
	@Autowired
	private CustomFormGroupDao customFormGroupDao;

	@Autowired
	private CustomFormInstanceDao customFormInstanceDao;
	
	@Autowired
	private BusinessObjectDtoServiceCustomer businessObjectDtoServiceCustomer;

	@Autowired
	private BusinessObjectVariableDtoServiceCustomer businessObjectVariableDtoServiceCustomer;
	
	@Autowired
	private RulerSubDtoServiceCustomer rulerSubDtoServiceCustomer;
	
	@Autowired
	private OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer;
	
	//菜单注册服务
    @Autowired
    private ResourceDtoServiceCustomer resourceDtoServiceCustomer;
	
    //功能点注册
    @Autowired
    private OperationDtoServiceCustomer operationDtoServiceCustomer;
	//EntryDtoServiceCustomer entryDtoServiceCustomer=(EntryDtoServiceCustomer) SpringContextUtils.getBeanByClass(EntryDtoServiceCustomer.class);
	
    private static final String UNIT = "万仟佰拾亿仟佰拾万仟佰拾元角分";  
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
    private static final double MAX_VALUE = 9999999999999.99D;  
    
    private static Logger log = Logger.getLogger(CustomFormServiceImpl.class);
    
	@Override
	public Integer queryMaxSort(String parentId) {
		return customFormDao.queryMaxSort(parentId);
	}

	@Override
	public Integer validateIsExist(CustomForm customForm,String type) {
		return customFormDao.validateIsExist(customForm,type);
	}

	@Override
	public String validateBeforeSave(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomFormDto customFormDtoVal=this.validateIsExist(saveJson);
		try {
			if(customFormDtoVal.isCodeExist() || customFormDtoVal.isNameExist()){
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("编码或名称重复！");
			}else{
				CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
				if(customForm!=null && customForm.getDataItemControl()==2){
					customForm.setDataItemId("CustomFormSearchAuthority");
				}
				String entryUrl="/platform-app/sysManager/customFormInstance/customFormInstance_list.html?id="+customForm.getId();
				Integer maxSort=queryMaxSort(customForm.getParentId());
				customForm.setSort(maxSort==null?1L:Long.valueOf(maxSort+1));
//				customForm.setFlowPathName(entryUrl);
				if(customForm.getIsInner()==2){
					customForm.setUrl(entryUrl);
				}
				if(1!=customForm.getIsInner()){
					customForm.setResourceId(IDGenerator.getUUID());
				}
				customForm.setDelflag(false);
				customFormDao.save(customForm);
				
				//自定义表单，外部连接推送菜单
				if(1!=customForm.getIsInner()){
	        		this.registerMenu(userInfo, customForm);
				}
//				String entryJson;
//				try {
//					EntryDto entryDto=new EntryDto();
//					entryDto.setId(customForm.getId());
//					entryDto.setCode(customForm.getCode());
//					entryDto.setName(customForm.getName());
//					entryDto.setStatus(1);
//					entryDto.setDelflag(false);
//					entryDto.setIsInner(0);
//					entryDto.setParentId(customForm.getParentId());
//					entryDto.setUrl(entryUrl);
//					entryJson = JacksonUtils.toJson(entryDto);
//					
//					entryDtoServiceCustomer.saveEntryAndAuthor(userInfo,entryJson);
//				} catch (Exception e) {
//				}
				
				BeanUtils.copyProperties(customForm, customFormDtoVal);
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("保存成功！");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	  * @Description:推送菜单
	  * @author:zhangfangzhi
	  * @date 2017年7月12日 下午3:05:51
	  * @version V1.0
	 */
	private String registerMenu(String userInfo, CustomForm customForm) {
    	String menuResult = resourceDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + customForm.getResourceId() + "\"}");
        Map<String, Object> menuResultMap = JacksonUtils.fromJson(menuResult, HashMap.class);
        String menuStr = (String) menuResultMap.get("result");
        Map<String, Object> oldMenuMap = JacksonUtils.fromJson(menuStr, HashMap.class);
        String registerMenuResult = null;
        Map<String, Object> resourceMap = new HashMap<String, Object>();
    	resourceMap.put("id", customForm.getResourceId());//编码
    	resourceMap.put("code", customForm.getCode()+"_MENU");//菜单编码
    	resourceMap.put("name", customForm.getName());//菜单名称
    	resourceMap.put("url", customForm.getUrl());//菜单url
    	resourceMap.put("appId", "9d6cba61c4b24a5699c339a49471a0e7");//应用Id
    	CustomFormGroup customFormGroupParent=customFormGroupDao.getObjectById(customForm.getParentId());
    	resourceMap.put("parentId", customFormGroupParent.getResourceId());//上级菜单ID
    	resourceMap.put("status", 1);//状态
    	resourceMap.put("sort", customForm.getSort());//排序
    	resourceMap.put("openmode", 0);//打开方式
    	resourceMap.put("remark", "");//说明
    	resourceMap.put("isoutmenu", 0);//是否外部链接
        if (oldMenuMap != null) {
            oldMenuMap.putAll(resourceMap);
            registerMenuResult = resourceDtoServiceCustomer.update(userInfo, JacksonUtils.toJson(oldMenuMap));
        } else {
            registerMenuResult = resourceDtoServiceCustomer.save(userInfo, JacksonUtils.toJson(resourceMap));
        }
        
        //为新注册的菜单注册一个查询按钮
        Map<String, Object> operationMap = new HashMap<String, Object>();
        operationMap.put("name", "查询");
        operationMap.put("code", customForm.getCode() + "_QUERY");
        operationMap.put("appId", "9d6cba61c4b24a5699c339a49471a0e7");
        operationMap.put("resourceId", customForm.getResourceId());
        operationMap.put("type", "1");
        registOperation(userInfo, operationMap);

        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(registerMenuResult, DubboServiceResultInfo.class);
        if (!dubboServiceResultInfo.isSucess()) {
            throw new RuntimeException("菜单注册失败！");
        }

        return registerMenuResult;
    }
	
	/**
     * 向系统中动态注册操作按钮
     * @param userInfo
     * @param saveJson
     * @return
     * @throws Exception
     */
    private String registOperation(String userInfo, Map<String, Object> operationMap) {

        String operationResult = operationDtoServiceCustomer.queryList(userInfo, JacksonUtils.toJson(operationMap));
        Map<String, Object> operationResultMap = JacksonUtils.fromJson(operationResult, HashMap.class);

        String oldoperationStr = (String) operationResultMap.get("result");
        List<Map<String, Object>> list = JacksonUtils.fromJson(oldoperationStr, ArrayList.class, HashMap.class);
        Map<String, Object> oldoperationResultMap = null;//
        if (list != null && list.size() > 0) {
            oldoperationResultMap = list.get(0);
        }

        String registerOperationResult = null;
        if (oldoperationResultMap != null) {
            oldoperationResultMap.putAll(operationMap);
            registerOperationResult = operationDtoServiceCustomer.update(userInfo, JacksonUtils.toJson(oldoperationResultMap));
        } else {
            operationMap.put("id", IDGenerator.getUUID());
            registerOperationResult = operationDtoServiceCustomer.save(userInfo, JacksonUtils.toJson(operationMap));
        }

        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(registerOperationResult, DubboServiceResultInfo.class);
        if (!dubboServiceResultInfo.isSucess()) {
            throw new RuntimeException("按钮注册失败！");
        }
        return registerOperationResult;
    }

	private CustomFormDto validateIsExist(String saveJson) {
		CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
		Integer isExistCode=customFormDao.validateIsExist(customForm,"code");
		Integer isExistName=customFormDao.validateIsExist(customForm,"name");
		CustomFormDto customFormDto = new CustomFormDto();
		if(isExistCode !=null && isExistCode>0){
			customFormDto.setCodeExist(true);
		}
		if(isExistName !=null && isExistName>0){
			customFormDto.setNameExist(true);
		}
		return customFormDto;
	}

	@Override
	public String validateBeforeUpdate(String userInfo, String updateJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomFormDto customFormDtoVal=this.validateIsExist(updateJson);
		try {
			if(customFormDtoVal.isCodeExist() || customFormDtoVal.isNameExist()){
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("编码或名称重复！");
			}else{
				CustomForm customForm=JacksonUtils.fromJson(updateJson, CustomForm.class);
				String entryUrl="/platform-app/sysManager/customFormInstance/customFormInstance_list.html?id="+customForm.getId();
				if(customForm.getIsInner()==2){
					customForm.setUrl(entryUrl);
				}
				int result= customFormDao.update(customForm);
				
//				Map<String,Object> map=new HashMap<String,Object>();
//				map.put("name", customForm.getName());
//				map.put("code", customForm.getCode());
//				map.put("parentId", customForm.getParentId());
//				String dubboResultInfo=entryDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\""+customForm.getId()+"\"}");
//				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
//				if(dubboServiceResultInfo.isSucess()){
//					String resultInfo= dubboServiceResultInfo.getResult();
//					Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
//					oldMap.putAll(map);
//					String updateJsonEntry= JacksonUtils.toJson(oldMap);
//					entryDtoServiceCustomer.saveEntryAndAuthor(userInfo,updateJsonEntry);
//				}
				
				//自定义表单，外部连接推送菜单
				if(1!=customForm.getIsInner()){
	        		this.registerMenu(userInfo, customForm);
				}
				
				BeanUtils.copyProperties(customForm, customFormDtoVal);
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("更新对象成功!");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getTemplateTree(String userInfo, String paramaterJson) {
//		String result=flDtoServiceCustomer.queryList(userInfo, paramaterJson);
//		DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(result, DubboServiceResultInfo.class);
//	    if(dubboServiceResultInfo.isSucess()){
//			String resultInfo= dubboServiceResultInfo.getResult();
//			List<FlDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,FlDto.class);
//			
//			
//	    }
		return "";
	}

	@Override
	public Page getPageSort(Map map) {
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<Map<String,Object>> list = customFormDao.getPageSort(map);
		// 获取条件的总数据量
		Integer count = customFormDao.getPageSortCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}

	@Override
	public int deleteCustomFormById(String userInfo, String id) {
		int result=0;
//		result=customFormDao.deletePseudoObjectById(id);
		try {
			result=this.deleteButtonAndResourceById(userInfo, id);
			businessObjectDtoServiceCustomer.deletePseudoObjectById(userInfo, "{\"id\":\""+id+"\"}");
		} catch (Exception e) {
			log.error("删除对象失败!"+e.getMessage());
		}
		return result;
	}

	/**
	  * @Description:删除按钮、菜单、表单
	  * @author:zhangfangzhi
	  * @date 2017年7月12日 下午4:55:10
	  * @version V1.0
	 * @throws Exception 
	 */
	private int deleteButtonAndResourceById(String userInfo, String id) throws Exception {
		CustomForm customForm = customFormDao.getObjectById(id);
    	if(1!=customForm.getIsInner()){//为外部链接
    		//先删除注册的按钮，如果按钮删除不成功则此数据无法删除
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("resourceId", customForm.getResourceId());
    		String operationResult = operationDtoServiceCustomer.queryList(userInfo, JacksonUtils.toJson(pMap));
            Map<String, Object> operationResultMap = JacksonUtils.fromJson(operationResult, HashMap.class);

            String oldoperationStr = (String) operationResultMap.get("result");
            List<Map<String, Object>> list = JacksonUtils.fromJson(oldoperationStr, ArrayList.class, HashMap.class);
            Map<String, Object> oldoperationResultMap = null;//
            if (list != null && list.size() > 0) {
                oldoperationResultMap = list.get(0);
            }

            String delOperationResult = null;
            if (oldoperationResultMap != null) {
                delOperationResult = operationDtoServiceCustomer.deletePseudoObjectById(userInfo, "{\"id\":\"" + oldoperationResultMap.get("id") + "\"}");
            }
            if (delOperationResult != null) {
                DubboServiceResultInfo delOperationResultInfo = JacksonUtils.fromJson(delOperationResult, DubboServiceResultInfo.class);
                if (!delOperationResultInfo.isSucess()) {
                    throw new Exception("按钮删除失败！");
                }
            }

            //再删除注册的菜单，如果菜单删除不成功则此数据无法删除
            String menuResult = resourceDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + customForm.getResourceId() + "\"}");
            DubboServiceResultInfo menuResultInfo = JacksonUtils.fromJson(menuResult, DubboServiceResultInfo.class);
            if (!menuResultInfo.isSucess()) {
                throw new Exception("数据删除失败！");
            }

            String menuObj = menuResultInfo.getResult();
            Map<String, Object> menuMap = JacksonUtils.fromJson(menuObj, HashMap.class);
            String delMenuResult = null;
            if (menuMap != null) {
                delMenuResult = resourceDtoServiceCustomer.deletePseudoObjectById(userInfo, "{\"id\":\"" + customForm.getResourceId() + "\"}");
            }
            if (delMenuResult != null) {
                DubboServiceResultInfo delMenuResultInfo = JacksonUtils.fromJson(delMenuResult, DubboServiceResultInfo.class);
                if (!delMenuResultInfo.isSucess()) {
                    throw new Exception("菜单删除失败！");
                }
            }
    	}
    	return customFormDao.deletePseudoObjectById(id);
    }

	@Override
	public String validateBeforeCopy(String userInfo, String copyJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomFormDto customFormDtoVal=this.validateIsExist(copyJson);
		try {
			if(customFormDtoVal.isCodeExist() || customFormDtoVal.isNameExist()){
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("编码或名称重复！");
			}else{
				//复制模板
				CustomForm customFormSource=JacksonUtils.fromJson(copyJson, CustomForm.class);
				CustomForm customFormTarget=new CustomForm();
				BeanUtils.copyProperties(customFormSource, customFormTarget);
				customFormTarget.setId(IDGenerator.getUUID());
				customFormTarget.setCopySourceId(customFormSource.getCopySourceId());
				customFormTarget.setCode(customFormSource.getCode());
				customFormTarget.setName(customFormSource.getName());
				customFormTarget.setStatus(customFormSource.getStatus());
				customFormTarget.setIsInner(customFormSource.getIsInner());
				String entryUrl="/platform-app/sysManager/customFormInstance/customFormInstance_list.html?id="+customFormTarget.getId();
				Integer maxSort=queryMaxSort(customFormSource.getParentId());
				customFormTarget.setSort(maxSort==null?1L:Long.valueOf(maxSort+1));
//				customFormTarget.setFlowPathName(entryUrl);
				customFormTarget.setUrl(entryUrl);
				customFormTarget.setDelflag(false);
				customFormTarget.setParentId(customFormSource.getParentId());
				customFormTarget.setParentName(customFormSource.getParentName());
				customFormSource=customFormDao.getObjectById(customFormSource.getCopySourceId());
				customFormTarget.setFormFormatJson(customFormSource.getFormFormatJson());
				customFormTarget.setFormFormatHtml(customFormSource.getFormFormatHtml());
				customFormTarget.setFormShowColumn(customFormSource.getFormShowColumn());
				customFormTarget.setFormSearchKey(customFormSource.getFormSearchKey());
				customFormTarget.setFormSearchSeniorKey(customFormSource.getFormSearchSeniorKey());
				customFormTarget.setFlowVariable(customFormSource.getFlowVariable());
				customFormTarget.setIsComplete("0");
				customFormTarget.setResourceId(IDGenerator.getUUID());
				customFormTarget.setDataItemId(customFormSource.getDataItemId());
				customFormTarget.setDataItemControl(customFormSource.getDataItemControl());
				customFormDao.save(customFormTarget);
				
				//自定义表单推送菜单
				this.registerMenu(userInfo, customFormTarget);
				
				//快速入口推送
//				String entryJson;
//				try {
//					EntryDto entryDto=new EntryDto();
//					entryDto.setId(customFormTarget.getId());
//					entryDto.setCode(customFormTarget.getCode());
//					entryDto.setName(customFormTarget.getName());
//					entryDto.setStatus(1);
//					entryDto.setDelflag(false);
//					entryDto.setIsInner(0);
//					entryDto.setParentId(customFormTarget.getParentId());
//					entryDto.setUrl(entryUrl);
//					entryJson = JacksonUtils.toJson(entryDto);
//					
//					entryDtoServiceCustomer.saveEntryAndAuthor(userInfo, entryJson);
//				} catch (Exception e) {
//				}
				
				BeanUtils.copyProperties(customFormSource, customFormDtoVal);
				info.setResult(JacksonUtils.toJson(customFormDtoVal));
				info.setSucess(true);
				info.setMsg("保存成功！");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getTemplateById(String userInfo, String getJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomFormDto customFormDto = new CustomFormDto();
		try {
			Map map=JacksonUtils.fromJson(getJson, HashMap.class);
			String customFormId=(String) map.get("id");
			String customFormVersionId=(String) map.get("cfVid");
			if(map!=null && customFormId!=null){
				
				CustomForm	result = customFormDao.getObjectById(customFormId);
				if(customFormVersionId!=null && !"customFormRelease".equals(customFormVersionId)){
					CustomFormVersionHistory customFormVersionHistory=customFormVersionHistoryDao.getObjectById(customFormVersionId);
					BeanUtils.copyProperties(customFormVersionHistory, result);
				}
				
				CustomFormGroup customFormGroup=customFormGroupDao.getObjectById(result.getParentId());
				
				String pCode=this.getCustomFormGroup(customFormGroup,"code");
				String pName=this.getCustomFormGroup(customFormGroup,"name");
				BeanUtils.copyProperties(result, customFormDto);
				Integer flag=customFormDao.isHasInstance(result.getId());
				if(flag !=null && flag>0){
					customFormDto.setHasInstance(false);
				}else{
					customFormDto.setHasInstance(false);
				}
				customFormDto.setParentCode(pCode);
				customFormDto.setParentName(pName);
				info.setResult(JacksonUtils.toJson(customFormDto));
				info.setSucess(true);
				info.setMsg("获取对象成功!");
			}else{
				info.setResult(JacksonUtils.toJson(customFormDto));
				info.setSucess(false);
				info.setMsg("缺少必要参数!");
			}
		} catch (DataAccessException e) {
			info.setSucess(false);
			info.setMsg("获取对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	private String getCustomFormGroup(CustomFormGroup customFormGroup, String type) {
		String result="@@";
		if(customFormGroup!=null){
			CustomFormGroup customFormGroupParent=customFormGroupDao.getObjectById(customFormGroup.getParentId());
			if("code".equals(type)){
				if(customFormGroupParent!=null){
					return customFormGroupParent.getCode()+"@@"+customFormGroup.getCode();
				}else{
					return "@@"+customFormGroup.getCode();
				}
			}else if("name".equals(type)){
				if(customFormGroupParent!=null){
					return customFormGroupParent.getName()+"@@"+customFormGroup.getName();
				}else{
					return "@@"+customFormGroup.getName();
				}
			}
		}else{
			return result;
		}
		return result;
	}

	@Override
	public Integer isHasInstanceByFormId(String customFormId) {
		return customFormDao.isHasInstance(customFormId);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public String saveGenerateData(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> idMaps=this.getIdMaps();
			CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
			CustomForm customFormPre=customFormDao.getObjectById(customForm.getId());
			List<BusinessObjectVariableDto> varList=this.getVariableList(userInfo, customFormPre.getId());
			if(customForm.getName()!=null && !"".equals(customForm.getName())){
				String[] rowStrs=customForm.getName().split(";");
				for(int i=0;i<rowStrs.length;i++){
					
					String[] customFormStrs=rowStrs[i].split(",");
					if(idMaps==null || idMaps.get(customFormStrs[0])==null){
						continue;
					}
					CustomFormGroup customFormGroup=(CustomFormGroup) idMaps.get(customFormStrs[0]);
					CustomForm customFormNew = new CustomForm();
					
					BeanUtils.copyProperties(customFormPre, customFormNew);
					
					customFormNew.setId(IDGenerator.getUUID());
					customFormNew.setCode(customFormStrs[2]);
					customFormNew.setName(customFormStrs[1]);
					customFormNew.setParentId(customFormGroup.getId());
					customFormNew.setParentName(customFormGroup.getName());
					String entryUrl="/platform-app/sysManager/customFormInstance/customFormInstance_list.html?id="+customFormNew.getId();
//					customFormNew.setFlowPathName(entryUrl);
					customFormNew.setUrl(entryUrl);
					customFormNew.setDelflag(false);
					customFormNew.setCreateCompanyName("FundCustomFormGenerateData");
					if(customFormStrs.length>3){
						customFormNew.setCreateCompanyId(customFormStrs[3]);
					}
					customFormNew.setCopySourceId(customFormPre.getId());
					customFormNew.setSort(10000L+i);
//					customFormNew.setIsComplete("0");
					customFormNew.setResourceId(IDGenerator.getUUID());
					customFormDao.save(customFormNew);
					
					//生成业务对象
					BusinessObjectDto businessObjectDto=this.generateBusinessObject(customFormNew,varList);
					businessObjectDtoServiceCustomer.saveObjectAndVariableData(userInfo, JacksonUtils.toJson(businessObjectDto));
					//注册菜单
					this.registerMenu(userInfo, customFormNew);
					
					//资金数据迁移处理
					if(customFormStrs.length>3){
						toDealFund(customFormStrs[3],customFormNew,userInfo,customForm.getCode());
					}
					
				}
				info.setSucess(true);
				info.setMsg("保存成功！");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
//	@Override
//	public String saveGenerateData(String userInfo, String saveJson) {
//		DubboServiceResultInfo info=new DubboServiceResultInfo();
//		try {
//			CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
//			CustomForm customFormPre=customFormDao.getObjectById(customForm.getId());
//			List<BusinessObjectVariableDto> varList=this.getVariableList(userInfo, customFormPre.getId());
//			
//			Map<String,Object> paramMap = new HashMap<String,Object>();
//			paramMap.put("delflag", false);
//			List<CustomForm> list=customFormDao.queryList(paramMap);
//			
//			if(list!=null && list.size()>0){
//				for(int i=0;i<list.size();i++){
//					CustomForm customFormDb=list.get(i);
//					//生成业务对象
//					BusinessObjectDto businessObjectDto=this.generateBusinessObject(customFormDb,varList);
//					businessObjectDtoServiceCustomer.saveObjectAndVariableData(userInfo, JacksonUtils.toJson(businessObjectDto));
//				}
//			}
//		} catch (DataAccessException e) {
//			//e.printStackTrace();
//			info.setSucess(false);
//			info.setMsg("保存失败!");
//			info.setExceptionMsg(e.getMessage());
//		}
//		return JacksonUtils.toJson(info);
//	}


	private void toDealFund(String fkbdId, CustomForm customFormNew, String userInfo, String instanceId) {
		CustomFormInstance customFormInstanceTemplate=customFormInstanceDao.getObjectById(instanceId);
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("fkbdId", fkbdId);
		List<GeneralPaymentDTO> generalPaymentDTOList=customFormDao.queryGeneralPaymentByFkbdId(paramMap);
		if(generalPaymentDTOList!=null && generalPaymentDTOList.size()>0){
			List<CustomFormInstance> customFormInstanceList=new ArrayList<CustomFormInstance>();
			for(int s=0;s<generalPaymentDTOList.size();s++){
				GeneralPaymentDTO generalPaymentDTO=generalPaymentDTOList.get(s);
				CustomFormInstance customFormInstance=new CustomFormInstance();
				customFormInstance.setId(generalPaymentDTO.getId());
				customFormInstance.setConcurrencyVersion(0);
				customFormInstance.setCreateDate(new Timestamp(DateUtils.parseDate(generalPaymentDTO.getDappludate(), "yyyy-MM-dd").getTime()));
				customFormInstance.setCreatePersonName(generalPaymentDTO.getVapplicant());
				customFormInstance.setCustomFormId(customFormNew.getId());
				customFormInstance.setDelflag(false);
				customFormInstance.setFormFlowVariableValue(dealFlowVariable(generalPaymentDTO));
				customFormInstance.setFormMobileValueJson(dealMobileData(generalPaymentDTO,customFormNew));
				customFormInstance.setFormSearchKey(dealSearchData(generalPaymentDTO));
				customFormInstance.setFormSearchSeniorValue(dealSearchData(generalPaymentDTO));
				customFormInstance.setFormValueJson(dealFormValueJson(generalPaymentDTO,customFormInstanceTemplate,customFormNew));
				customFormInstance.setInstanceId(generalPaymentDTO.getInstanceId());
				customFormInstance.setOperateCompanyId(generalPaymentDTO.getCorpid());
				customFormInstance.setOperateCompanyName(generalPaymentDTO.getCorpname());
				customFormInstance.setOperateDepartmentId(generalPaymentDTO.getVapplydepartid().toString());
				customFormInstance.setOperateDepartmentName(generalPaymentDTO.getVapplydepart().toString());
				customFormInstance.setOperateQiId(generalPaymentDTO.getProjectid());
				customFormInstance.setOperateQiName(generalPaymentDTO.getVprojectname());
				String orgObjDubboServiceResultInfo=orgnazationDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + generalPaymentDTO.getProjectid() + "\"}");
			    DubboServiceResultInfo orgDubboServiceResultInfo= JacksonUtils.fromJson(orgObjDubboServiceResultInfo, DubboServiceResultInfo.class);
			    OrgnazationDto orgnazationDto=null;
				if(orgDubboServiceResultInfo.isSucess()){
					String resultInfo= orgDubboServiceResultInfo.getResult();
					orgnazationDto=JacksonUtils.fromJson(resultInfo, OrgnazationDto.class);
				}
				if(orgnazationDto!=null){
					customFormInstance.setOperateProjectId(orgnazationDto.getId());
					customFormInstance.setOperateProjectName(orgnazationDto.getName());
				}
				customFormInstance.setPayformid(generalPaymentDTO.getPayformid());
				customFormInstance.setStatus(generalPaymentDTO.getVapprovestatus());
//				customFormInstance.setTendId(tendId);
				customFormInstanceList.add(customFormInstance);
			}
			customFormInstanceDao.saveBatch(customFormInstanceList);
		}
	}

	/**
	  * @Description:查询数据处理
	  * @author:zhangfangzhi
	  * @date 2017年12月4日 上午10:25:51
	  * @version V1.0
	 * @param generalPaymentDTO 
	 */
	private String dealSearchData(GeneralPaymentDTO generalPaymentDTO) {
		String vtheme="cmp_fundPayment_theme_v1:"+generalPaymentDTO.getVtheme()+":cmp_fundPayment_theme_v1";
		String operateDate="cmp_fundPayment_operateDate_v1:"+generalPaymentDTO.getDappludate()+":cmp_fundPayment_operateDate_v1";
		String importDate="cmp_fundPayment_importDate_v1:"+generalPaymentDTO.getDimportdate()+":cmp_fundPayment_importDate_v1";
		String payDate="cmp_fundPayment_payDate_v1:"+generalPaymentDTO.getDpaydate()+":cmp_fundPayment_payDate_v1";
		return vtheme+","+operateDate+","+importDate+","+payDate;
	}

	/**
	  * @Description:手机端数据处理
	  * @author:zhangfangzhi
	  * @date 2017年12月4日 上午10:23:35
	  * @version V1.0
	 * @param generalPaymentDTO 
	 * @param customFormNew 
	 */
	private String dealMobileData(GeneralPaymentDTO generalPaymentDTO, CustomForm customFormNew) {
		List<CustomFormMobileConvert> customFormMobileConvertList=new ArrayList<CustomFormMobileConvert>();
		CustomFormMobileConvert customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("主题");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVtheme()==null?"":generalPaymentDTO.getVtheme());
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("单据编号");
		customFormMobileConvert.setType("FormNumber");
		customFormMobileConvert.setValue(generalPaymentDTO.getVapplynum()==null?"":generalPaymentDTO.getVapplynum());
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("业务类型");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(getBusinessTypeName(customFormNew.getBusinessType()));
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办人");
		customFormMobileConvert.setType("Operator");
		customFormMobileConvert.setValue(generalPaymentDTO.getVapplicant()==null?"":generalPaymentDTO.getVapplicant());
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办日期");
		customFormMobileConvert.setType("DateInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getDappludate()==null?"":generalPaymentDTO.getDappludate());
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办部门");
		customFormMobileConvert.setType("OperateDepartment");
		customFormMobileConvert.setValue(generalPaymentDTO.getVapplydepart()==null?"":generalPaymentDTO.getVapplydepart());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办公司");
		customFormMobileConvert.setType("OperateCompany");
		customFormMobileConvert.setValue(generalPaymentDTO.getCorpname()==null?"":generalPaymentDTO.getCorpname());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办分期");
		customFormMobileConvert.setType("OperateQi");
		customFormMobileConvert.setValue(generalPaymentDTO.getVprojectname()==null?"":generalPaymentDTO.getVprojectname());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("经办项目");
		customFormMobileConvert.setType("OperateProject");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("付款单位");
		customFormMobileConvert.setType("LegalPerson");
		customFormMobileConvert.setValue(generalPaymentDTO.getVpayunit()==null?"":generalPaymentDTO.getVpayunit());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("付款对象类型");
		customFormMobileConvert.setType("SingleSelect");
		customFormMobileConvert.setValue("1".equals(generalPaymentDTO.getVfkdxtype())?"公对公":"公对私");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款银行");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVskbank()==null?"":generalPaymentDTO.getVskbank());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款单位");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款账户名称");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款银行账号");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVskbanknum()==null?"":generalPaymentDTO.getVskbanknum());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款行所在省");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款行所在市");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款行机构号");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款行联行号");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("收款行CNAPS号");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue("");
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("支付方式");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getVpaymethod()==null?"":generalPaymentDTO.getVpaymethod());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("申请金额");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getNpaymentmny()==null?"":generalPaymentDTO.getNpaymentmny().toString());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("大写金额");
		customFormMobileConvert.setType("TextInput");
		customFormMobileConvert.setValue(generalPaymentDTO.getNpaymentmny()==null?"":change(generalPaymentDTO.getNpaymentmny()));
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("调拨类型");
		customFormMobileConvert.setType("SingleSelect");
		customFormMobileConvert.setValue(generalPaymentDTO.getVdbtype()==null?"":generalPaymentDTO.getVdbtype());
		customFormMobileConvertList.add(customFormMobileConvert);
		
		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("业务部门");
		customFormMobileConvert.setType("SingleSelect");
		customFormMobileConvert.setValue(generalPaymentDTO.getVcwdept()==null?"":generalPaymentDTO.getVcwdept());
		customFormMobileConvertList.add(customFormMobileConvert);

		customFormMobileConvert=new CustomFormMobileConvert();
		customFormMobileConvert.setName("说明");
		customFormMobileConvert.setType("TextAreaForm");
		if(generalPaymentDTO.getVpaymentmemo()!=null){
			if(generalPaymentDTO.getVpaymentmemo().contains("\n")){
				customFormMobileConvert.setValue(generalPaymentDTO.getVpaymentmemo().replace("\n", "<br/>"));
			}else{
				customFormMobileConvert.setValue(generalPaymentDTO.getVpaymentmemo());
			}
		}else{
			customFormMobileConvert.setValue("");
		}
		customFormMobileConvertList.add(customFormMobileConvert);
		
		return JacksonUtils.toJson(customFormMobileConvertList);
	}

	/**
	  * @Description:流程变量处理
	  * @author:zhangfangzhi
	  * @date 2017年12月4日 上午10:22:05
	  * @version V1.0
	 * @param userInfo 
	 * @param generalPaymentDTO 
	 */
	private String dealFlowVariable(GeneralPaymentDTO gpDTO) {
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("付款单位", gpDTO.getVpayunit());
//		map.put("申请金额",nf.format(gpDTO.getNpaymentmny()==null?0.00d:gpDTO.getNpaymentmny()));
//		map.put("主题", gpDTO.getVtheme());
//		map.put("调拨类型", gpDTO.getVdbtype());  
		map.put("cmp_fundPayment_theme_v1", gpDTO.getVtheme());  
		map.put("cmp_fundPayment_napplymny_v1", nf.format(gpDTO.getNpaymentmny()==null?0.00d:gpDTO.getNpaymentmny()));  
		map.put("cmp_fundPayment_allocationtype_v1", gpDTO.getVdbtype());  
		map.put("cmp_fundPayment_payorgname_v1", gpDTO.getVpayunit());  
		map.put("cmp_fundPayment_vcwdept_v1", gpDTO.getVcwdept());  
		return JacksonUtils.toJson(map);
	}

	public static String change(double v) {  
        if (v < 0 || v > MAX_VALUE){  
            return "参数非法!";  
        }  
        long l = Math.round(v * 100);  
        if (l == 0){  
            return "零元整";  
        }  
        String strValue = l + "";  
        // i用来控制数  
        int i = 0;  
        // j用来控制单位  
        int j = UNIT.length() - strValue.length();  
        String rs = "";  
        boolean isZero = false;  
        for (; i < strValue.length(); i++, j++) {  
         char ch = strValue.charAt(i);  
         if (ch == '0') {  
          isZero = true;  
          if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {  
           rs = rs + UNIT.charAt(j);  
           isZero = false;  
          }  
         } else {  
          if (isZero) {  
           rs = rs + "零";  
           isZero = false;  
          }  
          rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);  
         }  
        }  
        if (!rs.endsWith("分")) {  
         rs = rs + "整";  
        }  
        rs = rs.replaceAll("亿万", "亿");  
        return rs;  
      }
	/**
	  * @Description:将字段值转换为JSON值
	  * @author:zhangfangzhi
	  * @date 2017年12月4日 上午10:01:53
	  * @version V1.0
	 */
	private String dealFormValueJson(GeneralPaymentDTO generalPaymentDTO, CustomFormInstance customFormInstanceTemplate, CustomForm customFormNew) {
		Map<String,Map<String,Object>> resultFormJsonMap = JacksonUtils.fromJson(customFormInstanceTemplate.getFormValueJson(), HashMap.class);
		if(resultFormJsonMap!=null && resultFormJsonMap.size()>0){
			for (Map<String,Object> reMap : resultFormJsonMap.values()) {
				String cmpName=(String) reMap.get("rewriteFlag");	
				String cmpNameOper=(String) reMap.get("cmpName");
				reMap.put("cmpValue","");
				reMap.put("cmpValueShowName","");
				if("vtheme".equals(cmpName)){//主题
					reMap.put("cmpValue", generalPaymentDTO.getVtheme()==null?"":generalPaymentDTO.getVtheme());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVtheme()==null?"":generalPaymentDTO.getVtheme());
				}else if("vbusinesscode".equals(cmpName)){//单据编号
					reMap.put("cmpValue", generalPaymentDTO.getVapplynum()==null?"":generalPaymentDTO.getVapplynum());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVapplynum()==null?"":generalPaymentDTO.getVapplynum());
				}else if("vbusinesstype".equals(cmpName)){//业务类型
					reMap.put("cmpValue", getBusinessTypeName(customFormNew.getBusinessType()));
					reMap.put("cmpValueShowName", getBusinessTypeName(customFormNew.getBusinessType()));
				}else if("dapplydate".equals(cmpName)){//经办日期
					reMap.put("cmpValue", generalPaymentDTO.getDappludate()==null?"":generalPaymentDTO.getDappludate());
					reMap.put("cmpValueShowName", generalPaymentDTO.getDappludate()==null?"":generalPaymentDTO.getDappludate());
				}else if("payorgname".equals(cmpName)){//付款单位
					reMap.put("cmpValue", generalPaymentDTO.getVpayunitcode()==null?"":generalPaymentDTO.getVpayunitcode());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVpayunit()==null?"":generalPaymentDTO.getVpayunit());
				}else if("bankpaytype".equals(cmpName)){//付款对象类型
					reMap.put("cmpValue", generalPaymentDTO.getVfkdxtype()==null?"":generalPaymentDTO.getVfkdxtype());
					reMap.put("cmpValueShowName", "1".equals(generalPaymentDTO.getVfkdxtype())?"公对公":"公对私");
				}else if("recbankname".equals(cmpName)){//收款银行
					reMap.put("cmpValue", generalPaymentDTO.getVskbank()==null?"":generalPaymentDTO.getVskbank());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVskbank()==null?"":generalPaymentDTO.getVskbank());
				}else if("recorgname".equals(cmpName)){//收款单位名称
					reMap.put("cmpValue", generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
				}else if("recaccountname".equals(cmpName)){//收款账户名称
					reMap.put("cmpValue", generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVskunit()==null?"":generalPaymentDTO.getVskunit());
				}else if("recaccountno".equals(cmpName)){//收款银行账号
					reMap.put("cmpValue", generalPaymentDTO.getVskbanknum()==null?"":generalPaymentDTO.getVskbanknum());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVskbanknum()==null?"":generalPaymentDTO.getVskbanknum());
				}else if("paymenttype".equals(cmpName)){//支付方式
					reMap.put("cmpValue", jiesuancovert(generalPaymentDTO.getVpaymethod()));
					reMap.put("cmpValueShowName", generalPaymentDTO.getVpaymethod()==null?"":generalPaymentDTO.getVpaymethod());
				}else if("napplymny".equals(cmpName)){//申请金额
					reMap.put("cmpValue", generalPaymentDTO.getNpaymentmny()==null?"":generalPaymentDTO.getNpaymentmny());
					reMap.put("cmpValueShowName", generalPaymentDTO.getNpaymentmny()==null?"":generalPaymentDTO.getNpaymentmny());
				}else if("napplymny_cny".equals(cmpName)){//大写金额
					reMap.put("cmpValue", generalPaymentDTO.getNpaymentmny()==null?"":change(generalPaymentDTO.getNpaymentmny()));
					reMap.put("cmpValueShowName", generalPaymentDTO.getNpaymentmny()==null?"":change(generalPaymentDTO.getNpaymentmny()));
				}else if("allocationtype".equals(cmpName)){//调拨类型
					reMap.put("cmpValue", generalPaymentDTO.getVdbtype()==null?"":generalPaymentDTO.getVdbtype());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVdbtype()==null?"":generalPaymentDTO.getVdbtype());
				}else if("vcwdept".equals(cmpName)){//业务部门
					reMap.put("cmpValue", generalPaymentDTO.getVcwdept()==null?"":generalPaymentDTO.getVcwdept());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVcwdept()==null?"":generalPaymentDTO.getVcwdept());
				}else if("paymentmemo".equals(cmpName)){//说明
					if(generalPaymentDTO.getVpaymentmemo()!=null){
						if(generalPaymentDTO.getVpaymentmemo().contains("\n")){
							reMap.put("cmpValue", generalPaymentDTO.getVpaymentmemo().replace("\n", "<br/>"));
							reMap.put("cmpValueShowName", generalPaymentDTO.getVpaymentmemo().replace("\n", "<br/>"));
						}else{
							reMap.put("cmpValue", generalPaymentDTO.getVpaymentmemo());
							reMap.put("cmpValueShowName", generalPaymentDTO.getVpaymentmemo());
						}
					}
				}else if("importstatus".equals(cmpName)){//导入状态 
					reMap.put("cmpValue", generalPaymentDTO.getImportstatus()==null?"":generalPaymentDTO.getImportstatus());
					reMap.put("cmpValueShowName", generalPaymentDTO.getImportstatus()==null?"":generalPaymentDTO.getImportstatus());
				}else if("dimportdate".equals(cmpName)){//导入日期
					reMap.put("cmpValue", generalPaymentDTO.getDimportdate()==null?"":generalPaymentDTO.getDimportdate());
					reMap.put("cmpValueShowName", generalPaymentDTO.getDimportdate()==null?"":generalPaymentDTO.getDimportdate());
				}else if("paystatus".equals(cmpName)){//支付状态 
					reMap.put("cmpValue", generalPaymentDTO.getPaystatus()==null?"":generalPaymentDTO.getPaystatus());
					reMap.put("cmpValueShowName", generalPaymentDTO.getPaystatus()==null?"":generalPaymentDTO.getPaystatus());
				}else if("paybankname".equals(cmpName)){//付款银行（资金回写属性）
					reMap.put("cmpValue", generalPaymentDTO.getPaybankname()==null?"":generalPaymentDTO.getPaybankname());
					reMap.put("cmpValueShowName", generalPaymentDTO.getPaybankname()==null?"":generalPaymentDTO.getPaybankname());
				}else if("payaccountcode".equals(cmpName)){//付款银行账号（资金回写属性）
					reMap.put("cmpValue", generalPaymentDTO.getPayaccountcode()==null?"":generalPaymentDTO.getPayaccountcode());
					reMap.put("cmpValueShowName", generalPaymentDTO.getPayaccountcode()==null?"":generalPaymentDTO.getPayaccountcode());
				}else if("dpaydate".equals(cmpName)){//付款日期
					reMap.put("cmpValue", generalPaymentDTO.getDpaydate()==null?"":generalPaymentDTO.getDpaydate());
					reMap.put("cmpValueShowName", generalPaymentDTO.getDpaydate()==null?"":generalPaymentDTO.getDpaydate());
				}else if("npaymny".equals(cmpName)){//付款金额（资金回写属性）
					reMap.put("cmpValue", generalPaymentDTO.getNpaymny()==null?"":generalPaymentDTO.getNpaymny());
					reMap.put("cmpValueShowName", generalPaymentDTO.getNpaymny()==null?"":generalPaymentDTO.getNpaymny());
				}
				
				if("Operator".equals(cmpNameOper)){//经办人
					reMap.put("cmpValue", generalPaymentDTO.getVapplicant()==null?"":generalPaymentDTO.getVapplicant());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVapplicant()==null?"":generalPaymentDTO.getVapplicant());
				}else if("OperateDepartment".equals(cmpNameOper)){//经办部门
					reMap.put("cmpValueIds","");
					reMap.put("cmpValue", generalPaymentDTO.getVapplydepart()==null?"":generalPaymentDTO.getVapplydepart());
					reMap.put("cmpValueIds", generalPaymentDTO.getVapplydepartid()==null?"":generalPaymentDTO.getVapplydepartid());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVapplydepart()==null?"":generalPaymentDTO.getVapplydepart());
				}else if("OperateCompany".equals(cmpNameOper)){//经办公司
					reMap.put("cmpValue", generalPaymentDTO.getCorpname()==null?"":generalPaymentDTO.getCorpname());
					reMap.put("cmpValueShowName", generalPaymentDTO.getCorpname()==null?"":generalPaymentDTO.getCorpname());
				}else if("OperateQi".equals(cmpNameOper)){//经办分期
					reMap.put("cmpValue", generalPaymentDTO.getVprojectname()==null?"":generalPaymentDTO.getVprojectname());
					reMap.put("cmpValueShowName", generalPaymentDTO.getVprojectname()==null?"":generalPaymentDTO.getVprojectname());
				}
				
			}
		}
		return JacksonUtils.toJson(resultFormJsonMap);
	}
	
	/**
	 * 结算方式转换
	 * @param type
	 * @return
	 */
	private String jiesuancovert(String type){
		if(type==null || "".equals(type)){
			return "";
		}
		if("电汇".equals(type)){
			return "1";
		}else if("现金".equals(type)){
			return "2";
		}else if("支票".equals(type)){
			return "3";
		}else if("代扣代缴".equals(type)){
			return "4";
		}else{
			return "1";
		}
	}
	
	/**
	  * @Description:业务种类
	  * @author:zhangfangzhi
	  * @date 2017年11月21日 下午2:22:46
	  * @version V1.0
	 */
	private String getBusinessTypeName(String cellvalue) {
		if ("1".equals(cellvalue)) {
			return "外部结算-有无合同付款";
		} else if("2".equals(cellvalue)){
			return "外部结算-退工程保证金";
		} else if("3".equals(cellvalue)){
			return "外部结算-销售类退款";
		} else if("4".equals(cellvalue)){
			return "外部结算-报销(含工资)";
		} else if("5".equals(cellvalue)){
			return "外部借款";
		} else if("6".equals(cellvalue)){
			return "员工结算-报销";
		} else if("7".equals(cellvalue)){
			return "员工结算-借款";
		} else if("8".equals(cellvalue)){
			return "资金调拨";
		} else if("9".equals(cellvalue)){
			return "退质保金";
		} else{
			return "";
		}
	}

	private BusinessObjectDto generateBusinessObject(CustomForm customFormNew, List<BusinessObjectVariableDto> varList) {
		String hostAddress = "192.168.3.134";
//		String onlineAddress = "oa.xyre.com";
		
		try {
			InetAddress address = InetAddress.getLocalHost();
			hostAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		BusinessObjectDto businessObjectDto=new BusinessObjectDto();
		businessObjectDto.setId(customFormNew.getId());
		businessObjectDto.setAppCode("OA");
		businessObjectDto.setApproveClass("http://"+hostAddress+":8080/platform-app/sys/base/customFormInstance/getVariable");
		businessObjectDto.setBusidataClass("http://"+hostAddress+":8080/platform-app/sys/base/customFormInstance/updateStatus");
		businessObjectDto.setCallbackClass("http://"+hostAddress+":8080/platform-app/sys/base/customFormInstance/updateStatus");
		businessObjectDto.setCallbackMethod("getVariable");
		businessObjectDto.setCode(customFormNew.getCode());
		businessObjectDto.setDelflag(false);
		businessObjectDto.setName(customFormNew.getName());
		
		CustomFormGroup customFormGroup=customFormGroupDao.getObjectById(customFormNew.getParentId());
		businessObjectDto.setParentName(this.getCustomFormGroup(customFormGroup,"name"));
		businessObjectDto.setParentCode(this.getCustomFormGroup(customFormGroup,"code"));
		
		businessObjectDto.setPcUrl("http://"+hostAddress+":8080/platform-app/sysManager/customFormInstance/customFormInstance_flow.html");
		
		businessObjectDto.setVariableList(varList);
		return businessObjectDto;
	}

	private List<BusinessObjectVariableDto> getVariableList(String userInfo,String preId) {
		Map map=new HashMap();
		map.put("businessObjectId", preId);
		map.put("delflag", false);
		String paramaterJson = JacksonUtils.toJson(map);
		String dubboResultInfo=businessObjectVariableDtoServiceCustomer.queryList(userInfo, paramaterJson);
		DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		if(dubboServiceResultInfo.isSucess()){
			String resultInfo= dubboServiceResultInfo.getResult();
			List<BusinessObjectVariableDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BusinessObjectVariableDto.class);
			List<BusinessObjectVariableDto> cmpList=new ArrayList<BusinessObjectVariableDto>();
			String codeArray[] = {"flow_business_company_id", "flow_business_dept_id", "flow_business_project_id", 
		              "flow_business_project_branch_id", "business_object_id", "start_user_id",
		               "flow_business_company_name", "flow_business_dept_name", "flow_business_project_name", 
		               "flow_business_project_branch_name", "business_object_name", "start_user_name",
		               };
			List<String> defaultVariable=Arrays.asList(codeArray);
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					BusinessObjectVariableDto businessObjectVariableDto=list.get(i);
					if(businessObjectVariableDto!=null && !defaultVariable.contains(businessObjectVariableDto.getCode())){
						BusinessObjectVariableDto businessObjectVariableDtoNew=new BusinessObjectVariableDto();
						businessObjectVariableDtoNew.setName(businessObjectVariableDto.getName());
						businessObjectVariableDtoNew.setCode(businessObjectVariableDto.getCode());
						businessObjectVariableDtoNew.setDelflag(false);
						businessObjectVariableDtoNew.setType(businessObjectVariableDto.getType());
						businessObjectVariableDtoNew.setForFinance(false);
						businessObjectVariableDtoNew.setForFlowBranch(true);
						cmpList.add(businessObjectVariableDtoNew);
					}
				}
			}
			return cmpList;
		}
		return null;
	}

	private Map<String, Object> getIdMaps() {
		Map<String, Object> resultMap=new HashMap<String, Object>();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("delflag", false);
		List<CustomFormGroup> list=customFormGroupDao.queryList(paramMap);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				CustomFormGroup customFormGroup=list.get(i);
				resultMap.put(customFormGroup.getCode(), customFormGroup);
			}
		}
		return resultMap;
	}

	@Override
	public String getFormNumber(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
		customForm=customFormDao.getObjectById(customForm.getId());
		try {
			String finalStr="";
			if(customForm!=null && customForm.getParentId()!=null){
				CustomFormGroup customFormGroup=customFormGroupDao.getObjectById(customForm.getParentId());
				if(customFormGroup!=null && customFormGroup.getCode()!=null){
					finalStr+=customFormGroup.getCode();
					finalStr+="-";
					finalStr+=customForm.getCode();
					finalStr+="-";
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("code", "customFormRuler");
					map.put("status", "1");
					map.put("delflag", 0);
					String dubboResultInfo=rulerSubDtoServiceCustomer.getBillNumber(userInfo,map);
					DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
					if(dubboServiceResultInfo.isSucess()){
						String resultInfo= dubboServiceResultInfo.getResult();
						finalStr+=resultInfo;
						info.setResult(finalStr);
						info.setSucess(true);
						info.setMsg("获取编号成功！");
					}else{
						info.setResult(null);
						info.setSucess(false);
						info.setMsg("获取编号失败！");
					}
//					Integer currentNumber=customFormInstanceDao.queryCurrentNumber();
//					String numberStr="";
//					if(currentNumber!=null){
//						currentNumber++;
//						numberStr=String.valueOf(currentNumber);
//						if(numberStr.length()<=4){
//							numberStr=numberToStr(currentNumber);
//						}
//						String dateStr=getDayStr();
//						finalStr+=dateStr;
//						finalStr+="-";
//						finalStr+=numberStr;
//					}
				}
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("查询失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	private String getDayStr() {
		 Format format = new SimpleDateFormat("yyyyMMdd");
	     return format.format(new Date());
	}

	private String numberToStr(int currentNumber) {
		String number = String.valueOf(currentNumber);
		String formatStr ="0000000000";
		return formatStr.substring(0, 4-number.length())+number;
	}
	
	@Override
	public List queryListForQuickEntry(Map map) {
		return customFormDao.queryListForQuickEntry(map);
	}

	@Override
	public int updateSort(CustomForm object, Map<String, Object> map) {
		String  sortType= String.valueOf(map.get("sortType"));
		Long sort1 = object.getSort();
		String parentId = object.getParentId();
		map.clear();
		map.put("parentId", parentId);
		List<CustomForm> rulerList = customFormDao.queryListForUpdateSort(map);
		if("1".equals(sortType)){
			for (int i = 0; i < rulerList.size(); i++) {
				Long sort2 = rulerList.get(i).getSort();
				if(sort2.longValue()==sort1.longValue()&&i!=0){
					Long sort3 = rulerList.get(i-1).getSort();
					rulerList.get(i-1).setSort(sort2);
					rulerList.get(i).setSort(sort3);
					customFormDao.updateSort(rulerList.get(i-1));
					customFormDao.updateSort(rulerList.get(i));
					break;
				}
			}
		}else if("2".equals(sortType)){
			for (int i = 0; i < rulerList.size(); i++) {
				Long sort2 = rulerList.get(i).getSort();
				if(sort2.longValue()==sort1.longValue()&&i!=rulerList.size()-1){
					Long sort3 = rulerList.get(i+1).getSort();
					rulerList.get(i+1).setSort(sort2);
					rulerList.get(i).setSort(sort3);
					customFormDao.updateSort(rulerList.get(i+1));
					customFormDao.updateSort(rulerList.get(i));	
					break;
				}
			}
		}
		return 1;
	}

	/**
	 * 1
	 */
	@Override
	public int updateForPublish(CustomForm customForm, String isTemplateChange) {
		SecurityUserDto securityUserDto=LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		int result = 0;
		try {
			//当isTemplateChange==1时，说明模板只是修改属性没有修改布局，这个时候不生成新的版本
			if(isTemplateChange!=null && "1".equals(isTemplateChange)){
				//1.复制当前未保存的模板存入历史模板
				String curTemplateUuid=IDGenerator.getUUID();
				CustomForm customFormHis=customFormDao.getObjectById(customForm.getId());
				CustomFormVersionHistory customFormVersionHistory=new CustomFormVersionHistory();
				BeanUtils.copyProperties(customFormHis, customFormVersionHistory);
				customFormVersionHistory.setId(curTemplateUuid);
				customFormVersionHistory.setCustomFormId(customForm.getId());//当前历史版本从属于哪个表单
				CustomFormVersionHistory customFormVersionHistoryMax=customFormDao.queryCustomFormVersionHistoryMax(customForm.getId());
				if(customFormVersionHistoryMax!=null && customFormVersionHistoryMax.getVersionName()!=null){
					String currentNum=customFormVersionHistoryMax.getVersionName().substring(customFormVersionHistoryMax.getVersionName().lastIndexOf("-")+1);
					currentNum = numberToStr(Integer.valueOf(currentNum)+1);
					customFormVersionHistory.setVersionName(customForm.getCode()+DateUtils.getDate()+"-"+currentNum);
				}else{
					customFormVersionHistory.setVersionName(customForm.getCode()+DateUtils.getDate()+"-"+"0001");
				}
				customFormVersionHistory.setVersionCreateDate(new Timestamp(System.currentTimeMillis()));
				customFormVersionHistory.setVersionCreatePersonId(securityUserDto.getId());
				customFormVersionHistory.setVersionCreatePersonName(securityUserDto.getRealName());
				result=customFormVersionHistoryDao.save(customFormVersionHistory);
				//2.修改历史模板与实例的关系
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("templateId",customForm.getId());
				paramMap.put("templateHisId", curTemplateUuid);
				customFormInstanceDao.updateInstanceToHisTemplate(paramMap);
			}
			//3.保存最新的模板
			result= customFormDao.update(customForm);
		} catch (DataAccessException e) {
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return result;
	}
	public static void main(String[] args) {
		Double d=null;
		System.out.println(change(d));
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public String saveGenerateDataEx(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> idMaps=this.getIdMaps();
			CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
			CustomForm customFormPre=customFormDao.getObjectById(customForm.getId());
			List<BusinessObjectVariableDto> varList=this.getVariableList(userInfo, customFormPre.getId());
			if(customForm.getName()!=null && !"".equals(customForm.getName())){
				String[] rowStrs=customForm.getName().split(";");
				for(int i=0;i<rowStrs.length;i++){
					
					String[] customFormStrs=rowStrs[i].split(",");
					if(idMaps==null || idMaps.get(customFormStrs[0])==null){
						continue;
					}
					CustomFormGroup customFormGroup=(CustomFormGroup) idMaps.get(customFormStrs[0]);
					CustomForm customFormNew = new CustomForm();
					
					BeanUtils.copyProperties(customFormPre, customFormNew);
					
					customFormNew.setId(IDGenerator.getUUID());
					customFormNew.setCode(customFormStrs[2]);
					customFormNew.setName(customFormStrs[1]);
					customFormNew.setParentId(customFormGroup.getId());
					customFormNew.setParentName(customFormGroup.getName());
					String entryUrl="/platform-app/sysManager/customFormInstance/customFormInstance_list.html?id="+customFormNew.getId();
					customFormNew.setUrl(entryUrl);
					customFormNew.setDelflag(false);
					customFormNew.setCreateCompanyName("FundCustomFormGenerateData");
					if(customFormStrs.length>3){
						customFormNew.setCreateCompanyId(customFormStrs[3]);
					}
					customFormNew.setCopySourceId(customFormPre.getId());
					customFormNew.setSort(20000L+i);
					customFormNew.setResourceId(IDGenerator.getUUID());
					customFormDao.save(customFormNew);
					
					//生成业务对象
					BusinessObjectDto businessObjectDto=this.generateBusinessObject(customFormNew,varList);
					businessObjectDtoServiceCustomer.saveObjectAndVariableData(userInfo, JacksonUtils.toJson(businessObjectDto));
					//注册菜单
					this.registerMenu(userInfo, customFormNew);
					
					//资金数据迁移处理
//					if(customFormStrs.length>3){
//						toDealFund(customFormStrs[3],customFormNew,userInfo,customForm.getCode());
//					}
					
				}
				info.setSucess(true);
				info.setMsg("保存成功！");
			}
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public String saveGenerateDataExInstance(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
			List<CustomForm> customFormList=customFormDao.queryListByEx();
			if(customFormList!=null && customFormList.size()>0){
				for(int i=0;i<customFormList.size();i++){
					CustomForm customFormEx=customFormList.get(i);
					//资金数据迁移处理
					toDealFund(customFormEx.getCreateCompanyId(),customFormEx,userInfo,customForm.getCode());
				}
			}
			info.setSucess(true);
			info.setMsg("保存成功！");
		} catch (DataAccessException e) {
			//e.printStackTrace();
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
