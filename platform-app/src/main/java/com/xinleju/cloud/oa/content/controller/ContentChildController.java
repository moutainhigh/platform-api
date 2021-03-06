package com.xinleju.cloud.oa.content.controller;

import java.util.*;

import com.xinleju.platform.out.app.org.service.OrgnazationOutServiceCustomer;
import com.xinleju.platform.sys.res.dto.UserAuthDataOrgList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.content.dto.ContentChildDto;
import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.cloud.oa.content.dto.service.ContentChildDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 分类表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/content/contentChild")
public class ContentChildController {

    private static Logger log = Logger.getLogger(ContentChildController.class);

    @Autowired
    private ContentChildDtoServiceCustomer contentChildDtoServiceCustomer;

    @Autowired
    private OrgnazationOutServiceCustomer orgnazationOutServiceCustomer;
    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentChildDto contentChildDto = JacksonUtils.fromJson(resultInfo, ContentChildDto.class);
                result.setResult(contentChildDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 返回分页对象
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getPage(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.queryList(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 保存实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult save(@RequestBody ContentChildDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.save(userJson, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                //ContentChildDto contentChildDto = JacksonUtils.fromJson(resultInfo, ContentChildDto.class);
                Map<String,Object> contentChildDto = JacksonUtils.fromJson(resultInfo, HashMap.class);
                contentChildDto.put("pid",contentChildDto.get("parentId"));
                contentChildDto.put("pName",contentChildDto.get("parentName"));
                result.setResult(contentChildDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                String exceptionMsg = dubboServiceResultInfo.getMsg()!=null?dubboServiceResultInfo.getMsg():"【" + dubboServiceResultInfo.getExceptionMsg() + "】";
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + exceptionMsg);
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentChildDto contentChildDto = JacksonUtils.fromJson(resultInfo, ContentChildDto.class);
                result.setResult(contentChildDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    /**
     * 删除实体对象
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentChildDto contentChildDto = JacksonUtils.fromJson(resultInfo, ContentChildDto.class);
                result.setResult(contentChildDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    /**
     * 修改修改实体对象
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentChildDto contentChildDto = null;
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = contentChildDtoServiceCustomer.update(userJson, updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    String exceptionMsg = dubboServiceResultInfo.getMsg()!=null?dubboServiceResultInfo.getMsg():"【" + dubboServiceResultInfo.getExceptionMsg() + "】";
                    result.setMsg(updateDubboServiceResultInfo.getMsg() + exceptionMsg );
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentChildDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 根据父id获取目录结构树，如果ID为空，则查询所有目录结构
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentChildTree/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentChildTree(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getContentList(userJson, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildTreeData> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildTreeData.class);
                result.setResult(contentList);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    @RequestMapping(value = "/getGuuid", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getGuuid() {
        MessageResult result = new MessageResult();
        result.setResult(IDGenerator.getUUID());
        result.setSuccess(MessageInfo.GETSUCCESS.isResult());
        result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        return result;
    }

    /**
     * 根据父id获取目录结构树，如果ID为空，则查询所有目录结构
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentChildTreeById/{id}", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult getContentChildTreeById(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getContentChildTreeById(userJson, "{\"contentType\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildTreeData> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildTreeData.class);
                result.setResult(contentList);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 根据父id获取目录结构树，只查询一层父类结构
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentParentTreeById/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentParentTreeById(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getContentParentTreeById(userJson, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildTreeData> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildTreeData.class);
                result.setResult(contentList);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 根据父id获取获取该父类下面的目录对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentChildByContentTypeId/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentChildByContentTypeId(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.getContentChildByContentTypeId(userJson, "{\"parentId\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildDto> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildDto.class);
                result.setResult(contentList);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getContentList方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    @RequestMapping(value = "/queryTreeList", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryTreeList(@RequestBody Map<String, Object> paramMap) {
        MessageResult result = new MessageResult();
        try {
          SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String paramaterJson = JacksonUtils.toJson(paramMap);
            String dubboResultInfo = contentChildDtoServiceCustomer.queryTreeList(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<Map<String,Object>> list = JacksonUtils.fromJson(resultInfo, List.class, Map.class);

                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;

    }
    
    /**
     * 返回符合条件的列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryContentChildList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryContentChildList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            String dubboResultInfo = contentChildDtoServiceCustomer.queryContentChildList(userJson, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentChildDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentChildDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

}
