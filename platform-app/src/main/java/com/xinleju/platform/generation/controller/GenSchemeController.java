package com.xinleju.platform.generation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.generation.dto.GenerationSchemeDto;
import com.xinleju.platform.generation.dto.service.GenerationSchemeDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author sy
 * 
 * 
 */
@Controller
@RequestMapping("/generation/generationScheme")
public class GenSchemeController {

	@Autowired
	private GenerationSchemeDtoServiceCustomer generationSchemeDtoServiceCustomer;
	
	private static Logger log = Logger.getLogger(GenSchemeController.class);

	
	/**
	 * 根据业务表的ID生成后台代码
	 * 
	 * @param id
	 *            业务系统表的id
	 * 
	 * @return 生成信息
	 */
	@RequestMapping(value = "/downloadgen/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadGen(@PathVariable("id") String id) {
		ResponseEntity<byte[]> responseEntity = null;
		// id = "000fbb2eef694532ab9df8c836311111";
		
		// 设置外键参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systableId", id);
		HttpHeaders headers = new HttpHeaders(); 
		try {
			String dubboResultInfo = generationSchemeDtoServiceCustomer.downloadFile(null,JacksonUtils.toJson(map));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Base64 b64 = new Base64();  
			    byte[] buffer = b64.decode(resultInfo);  
				//试着headers的属性
	            
	            String dfileName = new String("源码.zip".getBytes("gb2312"), "iso8859-1");
	            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	            headers.setContentDispositionFormData("attachment", dfileName);
	           //要用HttpStatus.OK，不要用HttpStatus.CREATED,后面的谷歌浏览器好使，IE会有错误
	            responseEntity = new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
	            
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
		}
		return responseEntity;
	}
	
	
	
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/createTable/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult createTable(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		// 设置外键参数
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("systableId", id);
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.createTable(null, JacksonUtils.toJson(map));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(true);
				result.setMsg(resultInfo);
			}else{
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg("【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用createTable方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg("【"+e.getMessage()+"】");
		}
		return result;
	}
	

	
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.getObjectById(null, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				GenerationSchemeDto generationSchemeDto=JacksonUtils.fromJson(resultInfo, GenerationSchemeDto.class);
				result.setResult(generationSchemeDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=generationSchemeDtoServiceCustomer.getPage(null, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.queryList(null, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<GenerationSchemeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,GenerationSchemeDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody GenerationSchemeDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=generationSchemeDtoServiceCustomer.save(null, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				GenerationSchemeDto generationSchemeDto=JacksonUtils.fromJson(resultInfo, GenerationSchemeDto.class);
				result.setResult(generationSchemeDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.deleteObjectById(null, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				GenerationSchemeDto generationSchemeDto=JacksonUtils.fromJson(resultInfo, GenerationSchemeDto.class);
				result.setResult(generationSchemeDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.deleteAllObjectByIds(null, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				GenerationSchemeDto generationSchemeDto=JacksonUtils.fromJson(resultInfo, GenerationSchemeDto.class);
				result.setResult(generationSchemeDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		GenerationSchemeDto generationSchemeDto=null;
		try {
			String dubboResultInfo=generationSchemeDtoServiceCustomer.getObjectById(null, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=generationSchemeDtoServiceCustomer.update(null, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					 result.setMsg(updateDubboServiceResultInfo.getMsg()+"【"+updateDubboServiceResultInfo.getExceptionMsg()+"】");
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(generationSchemeDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}


	


//	/**
//	 * 根据业务表的ID生成后台代码
//	 * 
//	 * @param id
//	 *            业务系统表的id
//	 * 
//	 * @return 生成信息
//	 */
//	@RequestMapping(value = "/gen/{id}", method = RequestMethod.GET)
//	public @ResponseBody
//	MessageResult gen(@PathVariable("id") String id) {
//		MessageResult result = new MessageResult();
//		try {
//			// id = "000fbb2eef694532ab9df8c836311111";
//			// ApplicationContext context =new
//			// ClassPathXmlApplicationContext(new String[]
//			// {"applicationContext.xml"});
//			// GenSystableService
//			// genSystableService=context.getBean(GenSystableService.class);
//			// GenSystableColumnService
//			// genSystableColumnService=context.getBean(GenSystableColumnService.class);
//			// GenSchemeService
//			// genSchemeService=context.getBean(GenSchemeService.class);
//
//			// 设置外键参数
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("g_systable_id", id);
//			// 查询业务表信息
//			GenerationSystable generationSystable = genSystableService
//					.getObjectById(id);
//			// 通过业务表外键查询业务表对应的字段信息
//			List<GenerationSystableColumn> list_column = genSystableColumnService
//					.queryList(map);
//			// 通过业务表外键查询业务表生成方案内容
//			List<GenerationScheme> list_scheme = genSchemeService
//					.queryList(map);
//
//			// 生成文件（带entity，service和dao）
//			this.printFile(generationSystable, list_column, list_scheme);
//			// //生成Entity
//			// t.printEntity(generationSystable,list_column,list_scheme);
//			// //生成Service
//			// t.printService(generationSystable,list_column,list_scheme);
//			// //生成ServiceImpl
//			// t.printServiceImpl(generationSystable,list_column,list_scheme);
//			// //生成Dao
//			// t.printDao(generationSystable,list_column,list_scheme);
//			// //生成DaoImpl
//			// t.printDaoImpl(generationSystable,list_column,list_scheme);
//			// //生成Controller
//			// t.printController(generationSystable,list_column,list_scheme);
//			result.setResult("生成代码成功！");
//			result.setSuccess(true);
//			result.setMsg("生成代码成功");
//		} catch (Exception e) {
//			////e.printStackTrace();
//			result.setSuccess(false);
//			result.setMsg("生成代码失败：" + "【" + e.getMessage() + "】");
//		}
//		return result;
//	}

	
	

//	/**
//	 * 生成File
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printFile(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		/*
//		 * //初始化参数 Properties properties=new Properties();
//		 * //设置velocity资源加载方式为file properties.setProperty("resource.loader",
//		 * "file"); //设置velocity资源加载方式为file时的处理类
//		 * properties.setProperty("file.resource.loader.class",
//		 * "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		 * //实例化一个VelocityEngine对象 VelocityEngine velocityEngine=new
//		 * VelocityEngine(properties); //实例化一个VelocityContext VelocityContext
//		 * context=new VelocityContext();
//		 */
//		Properties properties = new Properties();
//		properties
//				.put("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//		properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
//		properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
//		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
//		// 初始化
//		Velocity.init(properties);
//		// 取得VelocityContext对象
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化StringWriter
//		StringWriter writerController = new StringWriter();
//		StringWriter writerEntity = new StringWriter();
//		StringWriter writerService = new StringWriter();
//		StringWriter writerServiceImpl = new StringWriter();
//		StringWriter writerDao = new StringWriter();
//		StringWriter writerDaoImpl = new StringWriter();
//		StringWriter writerMapper = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//
//		Velocity.getTemplate("Controller_template.vm", "utf-8").merge(context,
//				writerController);
//		Velocity.getTemplate("Entity_template.vm", "utf-8").merge(context,
//				writerEntity);
//		Velocity.getTemplate("Service_template.vm", "utf-8").merge(context,
//				writerService);
//		Velocity.getTemplate("ServiceImpl_template.vm", "utf-8").merge(context,
//				writerServiceImpl);
//		Velocity.getTemplate("Dao_template.vm", "utf-8").merge(context,
//				writerDao);
//		Velocity.getTemplate("DaoImpl_template.vm", "utf-8").merge(context,
//				writerDaoImpl);
//		Velocity.getTemplate("MapperXml_template.vm", "utf-8").merge(context,
//				writerMapper);
//
//		/*
//		 * velocityEngine.mergeTemplate("vm/Controller_template.vm", "utf-8",
//		 * context, writerController); //
//		 * System.out.println(writerController.toString());
//		 * velocityEngine.mergeTemplate("vm/Entity_template.vm", "utf-8",
//		 * context, writerEntity); //
//		 * System.out.println(writerEntity.toString());
//		 * velocityEngine.mergeTemplate("vm/Service_template.vm", "utf-8",
//		 * context, writerService); //
//		 * System.out.println(writerService.toString());
//		 * velocityEngine.mergeTemplate("vm/ServiceImpl_template.vm", "utf-8",
//		 * context, writerServiceImpl); //
//		 * System.out.println(writerServiceImpl.toString());
//		 * velocityEngine.mergeTemplate("vm/Dao_template.vm", "utf-8", context,
//		 * writerDao); // System.out.println(writerDao.toString());
//		 * velocityEngine.mergeTemplate("vm/DaoImpl_template.vm", "utf-8",
//		 * context, writerDaoImpl); //
//		 * System.out.println(writerDaoImpl.toString());
//		 */
//		// 替换包名下的“。”换成文件路径的“/”
//		String packagePath = package_name.replaceAll("\\.", "/");
//
//		// 生成不同entity和service以及dao的地址路径
//		String path0 = g_scheme.getLocalUrl() + "/" + packagePath
//				+ "/controller/";
//		String path1 = g_scheme.getLocalUrl() + "/" + packagePath + "/entity/";
//		String path2 = g_scheme.getLocalUrl() + "/" + packagePath + "/service/";
//		String path3 = g_scheme.getLocalUrl() + "/" + packagePath
//				+ "/service/impl/";
//		String path4 = g_scheme.getLocalUrl() + "/" + packagePath + "/dao/";
//		String path5 = g_scheme.getLocalUrl() + "/" + packagePath
//				+ "/dao/impl/";
//
//		// String path6 = g_scheme.getLocalUrl() + "/" + packagePath +
//		// "/entity/";
//		// 创建文件夹
//		CreateFilePath(path0);
//		CreateFilePath(path1);
//		CreateFilePath(path2);
//		CreateFilePath(path3);
//		CreateFilePath(path4);
//		CreateFilePath(path5);
//		// 生成带路径以及文件名源文件
//		String fileName0 = path0 + class_name + "Controller.java";
//		String fileName1 = path1 + class_name + ".java";
//		String fileName2 = path2 + class_name + "Service.java";
//		String fileName3 = path3 + class_name + "ServiceImpl.java";
//		String fileName4 = path4 + class_name + "Dao.java";
//		String fileName5 = path5 + class_name + "DaoImpl.java";
//		String fileName6 = path1 + class_name + "Mapper.xml";
//		// 创建文件
//		CreateFile(fileName0, writerController.toString());
//		CreateFile(fileName1, writerEntity.toString());
//		CreateFile(fileName2, writerService.toString());
//		CreateFile(fileName3, writerServiceImpl.toString());
//		CreateFile(fileName4, writerDao.toString());
//		CreateFile(fileName5, writerDaoImpl.toString());
//		// 解决第一行是<?xml version="1.0" encoding="UTF-8"?>这种情况的乱码
//		CreateFile2(fileName6, writerMapper.toString());
//
//	}
//
//	/**
//	 * 创建文件目录
//	 * 
//	 * @param path
//	 */
//	public void CreateFilePath(String path) throws Exception {
//		File filePath = new File(path);
//		if (!filePath.exists()) {
//			System.out.println("创建[" + filePath.getAbsolutePath() + "]情况："
//					+ filePath.mkdirs());
//		} else {
//			System.out.println("存在目录：" + filePath.getAbsolutePath());
//		}
//	}
//
//	/**
//	 * 创建文件
//	 * 
//	 * @param filename
//	 * @param writerBody
//	 */
//	public void CreateFile(String fileName, String writerBody) throws Exception {
//		File file = new File(fileName);
//		FileWriter fw = null;
//		try {
//			fw = new FileWriter(file);
//			fw.write(writerBody);
//			System.out.println("创建[" + fileName + "]成功");
//		} catch (IOException e) {
//			////e.printStackTrace();
//		} finally {
//			if (null != fw) {
//				try {
//					fw.flush();
//					fw.close();
//				} catch (IOException ee) {
//					e////e.printStackTrace();
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * 创建文件2
//	 * 
//	 * @param filename
//	 * @param writerBody
//	 */
//	public void CreateFile2(String fileName, String writerBody)
//			throws Exception {
//		File file = new File(fileName);
//		FileOutputStream fos = null;
//		PrintStream ps = null;
//
//		try {
//			fos = new FileOutputStream(file);
//			ps = new PrintStream(fos, true, "UTF-8");// 这里我们就可以设置编码了
//			ps.print(writerBody.toString());
//			System.out.println("创建[" + fileName + "]成功");
//		} catch (IOException e) {
//			////e.printStackTrace();
//		} finally {
//			if (null != ps) {
//				try {
//					ps.flush();
//					ps.close();
//				} catch (Exception ee) {
//					e////e.printStackTrace();
//				}
//			}
//			if (null != fos) {
//				try {
//					fos.close();
//				} catch (IOException ee) {
//					e////e.printStackTrace();
//				}
//			}
//
//		}
//
//	}
//
//	/**
//	 * 生成entity
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printEntity(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/Entity_template.vm", "utf-8", context,
//				writer);
//		System.out.println(writer.toString());
//	}
//
//	/**
//	 * 生成Service
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printService(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/Service_template.vm", "utf-8",
//				context, writer);
//		System.out.println(writer.toString());
//	}
//
//	/**
//	 * 生成ServiceImpl
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printServiceImpl(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/ServiceImpl_template.vm", "utf-8",
//				context, writer);
//		System.out.println(writer.toString());
//	}
//
//	/**
//	 * 生成Dao
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printDao(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/Dao_template.vm", "utf-8", context,
//				writer);
//		System.out.println(writer.toString());
//	}
//
//	/**
//	 * 生成DaoImpl
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printDaoImpl(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/DaoImpl_template.vm", "utf-8",
//				context, writer);
//		System.out.println(writer.toString());
//	}
//
//	/**
//	 * 生成Controller
//	 * 
//	 * @param g_systable
//	 * @param l_column
//	 * @param l_scheme
//	 */
//	public void printController(GenerationSystable g_systable,
//			List<GenerationSystableColumn> l_column,
//			List<GenerationScheme> l_scheme) throws Exception {
//		// 初始化参数
//		Properties properties = new Properties();
//		// 设置velocity资源加载方式为file
//		properties.setProperty("resource.loader", "file");
//		// 设置velocity资源加载方式为file时的处理类
//		properties
//				.setProperty("file.resource.loader.class",
//						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//		// 实例化一个VelocityEngine对象
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		// 实例化一个VelocityContext
//		VelocityContext context = new VelocityContext();
//		// 向VelocityContext中放入键值
//
//		GenerationScheme g_scheme = l_scheme.get(0);
//		// 包路径
//		String package_name = g_scheme.getPackageName();
//		context.put("package_name", package_name);
//		// 模块名
//		String modulename = g_scheme.getModuleName();
//		context.put("modulename", modulename);
//		// 作者
//		String author = g_scheme.getAuthor();
//		context.put("author", author);
//		// 表名
//		String table_name = g_systable.getTableName();
//		context.put("table_name", table_name.toUpperCase());
//		// 表描述
//		String comments = g_systable.getComments();
//		context.put("comments", comments);
//		// 生成entity类名
//		String class_name = g_systable.getClassName();
//		context.put("class_name", class_name);
//
//		context.put("g_systable", g_systable);
//		context.put("g_scheme", g_scheme);
//		context.put("l_column", l_column);
//
//		// 实例化一个StringWriter
//		StringWriter writer = new StringWriter();
//		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
//		velocityEngine.mergeTemplate("vm/Controller_template.vm", "utf-8",
//				context, writer);
//		System.out.println(writer.toString());
//	}

}
