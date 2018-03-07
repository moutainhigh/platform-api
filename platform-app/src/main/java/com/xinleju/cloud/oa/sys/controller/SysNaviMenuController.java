package com.xinleju.cloud.oa.sys.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xinleju.cloud.oa.portal.dto.PortalPageDto;
import com.xinleju.cloud.oa.portal.dto.service.PortalPageDtoServiceCustomer;
import com.xinleju.cloud.oa.util.CompressImgUtil;
import com.xinleju.platform.encrypt.EndecryptUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.sys.controller.util.LocationFlag;
import com.xinleju.cloud.oa.sys.dto.SysNaviMenuDto;
import com.xinleju.cloud.oa.sys.dto.ZTreeNode;
import com.xinleju.cloud.oa.sys.dto.service.SysNaviMenuDtoServiceCustomer;
import com.xinleju.cloud.oa.util.BCVFactory;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 导航菜单表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/sys/sysNaviMenu")
public class SysNaviMenuController {

    private static Logger log = Logger.getLogger(SysNaviMenuController.class);
    @Autowired
    private SysNaviMenuDtoServiceCustomer sysNaviMenuDtoServiceCustomer;

    @Autowired
    private PortalPageDtoServiceCustomer portalPageDtoServiceCustomer;
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
            if ("all".equals(id)) {
                id = "1";
            }
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }


    /**
     * 返回分页对象
     *
     * @param paramaterJson
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult page(@RequestBody String paramaterJson) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getPage(JacksonUtils.toJson(userInfo), paramaterJson);
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
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<SysNaviMenuDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, SysNaviMenuDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }


    /**
     * 保存实体对象
     *
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public void save(MultipartHttpServletRequest request, HttpServletResponse response) {
        PrintWriter pw = null;
        Boolean isReturn = false;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pw = response.getWriter();
            MultipartFile uploadfile =  request.getFile("image");
            MessageResult result = new MessageResult();
            SecurityUserBeanInfo userBeanInfo = LoginUtils
                    .getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            if(null!=uploadfile){
                long length = uploadfile.getSize();
                if(length>1*1024*1024){
                    result.setSuccess(false);
                    result.setMsg("图片尺寸不能大于1M");
                    pw.print(JacksonUtils.toJson(result));
                    pw.flush();
                    isReturn = true;
                }
            }
            if(!isReturn) {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String code = request.getParameter("code");
                String parentId = request.getParameter("parentId");
                String parentName = request.getParameter("parentName");
                String sort = request.getParameter("sort");
                String type = request.getParameter("type");
                String state = request.getParameter("state");
                String iconUrl = request.getParameter("iconUrl");//图标库地址2018-1-12

                String menuName = request.getParameter("menuName");
                String resourceId = request.getParameter("resourceId");
                String resourceName = request.getParameter("resourceName");
                String url = request.getParameter("url");
                String linkType = request.getParameter("linkType");
                String portalId = request.getParameter("portalId");
                String portalName = request.getParameter("portalName");
                byte[] headpic = null;
                if (null != uploadfile) {
                    InputStream is = uploadfile.getInputStream();
                    headpic = new byte[is.available()];
                    is.read(headpic);
                    is.close();
                }
                SysNaviMenuDto sysNaviMenuDto = new SysNaviMenuDto();
                sysNaviMenuDto.setId(id);
                sysNaviMenuDto.setType(type);
                sysNaviMenuDto.setCode(code);
                sysNaviMenuDto.setImage(headpic);
                sysNaviMenuDto.setName(name);
                sysNaviMenuDto.setMenuName(menuName);
                sysNaviMenuDto.setParentId(parentId);
                sysNaviMenuDto.setParentName(parentName);
                sysNaviMenuDto.setState(Boolean.valueOf(state));
                sysNaviMenuDto.setResourceId(resourceId);
                sysNaviMenuDto.setResourceName(resourceName);
                sysNaviMenuDto.setUrl(url);
                sysNaviMenuDto.setLinkType(linkType);
                sysNaviMenuDto.setDelflag(false);
                sysNaviMenuDto.setPortalId(portalId);
                sysNaviMenuDto.setPortalName(portalName);
                sysNaviMenuDto.setIconUrl(iconUrl);//图标库地址2018-1-12

                String saveJson = JacksonUtils.toJson(sysNaviMenuDto);
                String dubboResultInfo = sysNaviMenuDtoServiceCustomer.save(userJson, saveJson);
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    result.setResult(JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class));
                    result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                    result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
                } else {
                    result.setCode(dubboServiceResultInfo.getCode());
                    result.setSuccess(dubboServiceResultInfo.isSucess());
                    result.setMsg(dubboServiceResultInfo.getMsg());
                }
                pw.print(JacksonUtils.toJson(result));
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                pw.close();
            } catch (Exception e){}
        }
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
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
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
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }

        return result;
    }
    /**
     * 修改修改实体对象（启用禁用）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        SysNaviMenuDto sysNaviMenuDto = null;
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = sysNaviMenuDtoServiceCustomer.update(JacksonUtils.toJson(userInfo), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setCode(updateDubboServiceResultInfo.getCode());
                    result.setSuccess(updateDubboServiceResultInfo.isSucess());
                    result.setMsg(updateDubboServiceResultInfo.getMsg());
                }
            } else {
                result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(sysNaviMenuDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }
    /**
     * 修改修改实体对象
     *
     * @return
     */
    @RequestMapping(value = "/update")
    public void update(MultipartHttpServletRequest request, HttpServletResponse response) {
        PrintWriter pw = null;
        Boolean isReturn = false;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pw = response.getWriter();
            MultipartFile uploadfile = request.getFile("image");
            MessageResult result = new MessageResult();
            SecurityUserBeanInfo userBeanInfo = LoginUtils
                    .getSecurityUserBeanInfo();
            String userJson = JacksonUtils.toJson(userBeanInfo);
            if (null != uploadfile) {
                long length = uploadfile.getSize();
                if (length > 1 * 1024 * 1024) {
                    result.setCode(ErrorInfoCode.DATASIZEOUT_ERROR.getValue());
                    result.setSuccess(false);
                    result.setMsg(ErrorInfoCode.DATASIZEOUT_ERROR.getName());
                    pw.print(JacksonUtils.toJson(result));
                    pw.flush();
                    isReturn = true;
                }
            }
            if(!isReturn){
                SysNaviMenuDto sysNaviMenuDto = new SysNaviMenuDto();
                String id = request.getParameter("id");
                String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getObjectById(userJson, "{\"id\":\"" + id + "\"}");
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    String resultInfo = dubboServiceResultInfo.getResult();
                    sysNaviMenuDto =  JacksonUtils.fromJson(resultInfo,SysNaviMenuDto.class);
                    String name = request.getParameter("name");
                    String code = request.getParameter("code");
                    String parentId = request.getParameter("parentId");
                    String parentName = request.getParameter("parentName");
                    String sort = request.getParameter("sort");
                    String type = request.getParameter("type");
                    String state = request.getParameter("state");
                    String iconUrl = request.getParameter("iconUrl");//图标库地址2018-1-12

                    String menuName = request.getParameter("menuName");
                    String resourceId = request.getParameter("resourceId");
                    String resourceName = request.getParameter("resourceName");
                    String url = request.getParameter("url");
                    String linkType = request.getParameter("linkType");
                    String isDelPic = request.getParameter("isDelPic");
                    String portalId = request.getParameter("portalId");
                    String portalName = request.getParameter("portalName");
                    byte[] headpic = null;
                    if (null != uploadfile) {
                        if(uploadfile.getSize()>0) {
                            InputStream is = uploadfile.getInputStream();
                            headpic = new byte[is.available()];
                            is.read(headpic);
                            is.close();
                            sysNaviMenuDto.setImage(headpic);
                        }
                    }else if("0".equals(isDelPic)){
                        sysNaviMenuDto.setImage(null);
                    }
                    sysNaviMenuDto.setId(id);
                    sysNaviMenuDto.setType(type);
                    sysNaviMenuDto.setCode(code);
                    sysNaviMenuDto.setName(name);
                    sysNaviMenuDto.setMenuName(menuName);
                    sysNaviMenuDto.setParentId(parentId);
                    sysNaviMenuDto.setParentName(parentName);
                    if(state!=null&&!state.equals ("")){
                        sysNaviMenuDto.setState(Boolean.valueOf(state));
                    }
                    sysNaviMenuDto.setResourceId(resourceId);
                    sysNaviMenuDto.setResourceName(resourceName);
                    sysNaviMenuDto.setUrl(url);
                    sysNaviMenuDto.setLinkType(linkType);
                    sysNaviMenuDto.setPortalId(portalId);
                    sysNaviMenuDto.setPortalName(portalName);
                    sysNaviMenuDto.setIconUrl(iconUrl);//图标库地址2018-1-12

                    String updateJson = JacksonUtils.toJson(sysNaviMenuDto);
                    String updateDubboResultInfo = sysNaviMenuDtoServiceCustomer.update(userJson, updateJson);
                    DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                    if (updateDubboServiceResultInfo.isSucess()) {
                        Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                        result.setResult(i);
                        result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                        result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                    } else {
                        result.setCode(updateDubboServiceResultInfo.getCode());
                        result.setSuccess(updateDubboServiceResultInfo.isSucess());
                        result.setMsg(updateDubboServiceResultInfo.getMsg());
                    }
                } else {
                    result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg("不存在更新的对象");
                }
                pw.print(JacksonUtils.toJson(result));
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                pw.close();
            } catch (Exception e){}
        }
    }

    /**
     * 伪删除实体对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePseudo/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deletePseudo(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用deletePseudo方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }


    /**
     * 伪删除实体对象
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deletePseudoBatch/{ids}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deletePseudoBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用deletePseudoBatch方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
            result.setResult(e.getMessage());
        }

        return result;
    }

    /**
     * 返回菜单树 state 0 无效，1 有效，2，全部
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/getTree/{state}", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult getTree(@PathVariable("state") String state, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getTree(JacksonUtils.toJson(userInfo), state);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                if (Objects.equals("1", state)) {
                    List<ZTreeNode> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ZTreeNode.class);
                    result.setResult(list);
                } else {
                    List<SysNaviMenuDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, SysNaviMenuDto.class);
                    result.setResult(list);
                }
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getTree方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }
        return result;
    }

    /**
     * 移动实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/{location}/move", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult move(@PathVariable("location") String location, @RequestBody SysNaviMenuDto t) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = null;
            if (LocationFlag.getLocation(location) == LocationFlag.LAST) {
                dubboResultInfo = sysNaviMenuDtoServiceCustomer.toPre(JacksonUtils.toJson(userInfo), saveJson);
            } else if (LocationFlag.getLocation(location) == LocationFlag.NEXT) {
                dubboResultInfo = sysNaviMenuDtoServiceCustomer.toNext(JacksonUtils.toJson(userInfo), saveJson);
            }
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                SysNaviMenuDto sysNaviMenuDto = JacksonUtils.fromJson(resultInfo, SysNaviMenuDto.class);
                result.setResult(sysNaviMenuDto);
                result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用move方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 模糊查询 名称或菜单名称
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/fuzzySearch", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult fuzzySearch(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.fuzzySearch(JacksonUtils.toJson(userInfo), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<SysNaviMenuDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, SysNaviMenuDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.QUERYSUCCESS.isResult());
                result.setMsg(MessageInfo.QUERYSUCCESS.getMsg());
            } else {
                result.setCode(dubboServiceResultInfo.getCode());
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用fuzzySearch方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.QUERYERROR.isResult());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }

        return result;
    }


    @RequestMapping(value="/getUserInfo",method=RequestMethod.GET)
    public @ResponseBody MessageResult getUserInfo(){
        MessageResult result=new MessageResult();
        try {
        	Map<String,Object> map = new HashMap<String, Object>();
        	HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
     		HttpSession session = request.getSession();
     		String sessionId = session.getId();
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			//String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
            String loginName = securityUserBeanInfo.getSecurityUserDto().getLoginName();
			map.put("userLoginName", loginName);
			map.put("sessionId", sessionId);

            Map<String,Object> encryptParamMap = new HashMap<String,Object>();
            encryptParamMap.put("username",loginName);
            encryptParamMap.put("corpCode","xyre");

            Map<String,Object> encryptUserInfoMap = this.getEncryptUserInfo(encryptParamMap);
            map.putAll(encryptUserInfoMap);
            Map<String,String> keyMap = sysNaviMenuDtoServiceCustomer.getKey ();
            //按揭系统登陆名加密
            String mortgageLoginName = new EndecryptUtil ().get3DESEncrypt(loginName,keyMap.get ("privateKey"));
            map.put("mortgageLoginName",mortgageLoginName);
           //LLOA系统登陆名加密
            String LLOALoginName =  new EndecryptUtil ().get3DESEncrypt(loginName,keyMap.get ("LLOAPrivateKey"));
            map.put ("LLOALoginName",LLOALoginName);
            result.setResult(map);
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用getUserInfo方法:  【"+e.getMessage()+"】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
        }
        return result;
    }

    private Map<String,Object> getUserInformation(){
        Map<String,Object> map = new HashMap<String, Object>();
        HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        //String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
        String loginName = securityUserBeanInfo.getSecurityUserDto().getLoginName();
        map.put("userLoginName", loginName);
        map.put("sessionId", sessionId);

        Map<String,Object> encryptParamMap = new HashMap<String,Object>();
        encryptParamMap.put("username",loginName);
        encryptParamMap.put("corpCode","xyre");

        Map<String,Object> encryptUserInfoMap = this.getEncryptUserInfo(encryptParamMap);
        map.putAll(encryptUserInfoMap);
        Map<String,String> keyMap = sysNaviMenuDtoServiceCustomer.getKey ();
        String mortgageLoginName = new EndecryptUtil ().get3DESEncrypt(loginName,keyMap.get ("privateKey"));
        map.put("mortgageLoginName",mortgageLoginName);
        String LLOALoginName = new EndecryptUtil ().get3DESEncrypt(loginName,keyMap.get ("LLOAPrivateKey"));
        map.put("LLOALoginName",LLOALoginName);

        return map;
    }

    /**
     * 获取加密信息
     * @param map
     * @return
     */
    private Map<String,Object> getEncryptUserInfo(Map<String,Object> map){
        Map<String,Object> mapNew = new HashMap<String, Object>();
        try {
            String enUserName = null;
            String corpCode = null;

            if(map.size() >0 && map.get("username") != null && !"".equals(map.get("username"))){
                String username = map.get("username").toString();
                enUserName = BCVFactory.encrypt(username);
                enUserName=java.net.URLEncoder.encode(enUserName);
            }
            if(map.size() >0 && map.get("corpCode") != null && !"".equals(map.get("corpCode"))){
                corpCode = map.get("corpCode").toString();
                corpCode = BCVFactory.encrypt(corpCode);
                corpCode = java.net.URLEncoder.encode(corpCode);
            }

            SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal=Calendar.getInstance();
            int hour=cal.get(Calendar.HOUR_OF_DAY) ;   //差8个小时
            cal.set(Calendar.HOUR_OF_DAY,hour);
            Date date=cal.getTime();
            String dateString=df.format(date);
            dateString=BCVFactory.encrypt(dateString);
            dateString=java.net.URLEncoder.encode(dateString);

            mapNew.put("enUserName", enUserName);
            mapNew.put("corpCode", corpCode);
            mapNew.put("dateString", dateString);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用getUserInfo方法:  【"+e.getMessage()+"】");
        }

        return mapNew;
    }

    @RequestMapping(value="/getMenuInfo",method=RequestMethod.POST,consumes="application/json")
    public @ResponseBody MessageResult getMenuInfo(@RequestBody Map<String,Object> map){
        MessageResult result=new MessageResult();
       
        try {
        	String enUserName = null;
        	String corpCode = null;
			Map<String,Object> mapNew = new HashMap<String, Object>();
			if(map.size() >0 && map.get("username") != null && !"".equals(map.get("username"))){
				String username = map.get("username").toString();
			    enUserName = BCVFactory.encrypt(username); 
			    enUserName=java.net.URLEncoder.encode(enUserName);
			}
			if(map.size() >0 && map.get("corpCode") != null && !"".equals(map.get("corpCode"))){
				corpCode = map.get("corpCode").toString();
				corpCode = BCVFactory.encrypt(corpCode);
				corpCode = java.net.URLEncoder.encode(corpCode);
			}
			
		    SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss"); 
		    Calendar cal=Calendar.getInstance();    
		    int hour=cal.get(Calendar.HOUR_OF_DAY) ;   //差8个小时
		    cal.set(Calendar.HOUR_OF_DAY,hour); 
		    Date date=cal.getTime();   
		    String dateString=df.format(date); 
		    dateString=BCVFactory.encrypt(dateString);
		    dateString=java.net.URLEncoder.encode(dateString);
			
			mapNew.put("enUserName", enUserName);
			mapNew.put("corpCode", corpCode);
			mapNew.put("dateString", dateString);
			
            result.setResult(mapNew);
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用getUserInfo方法:  【"+e.getMessage()+"】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
        }
        return result;
    }


    @RequestMapping(value = "/getNaviMenuPortal", method = RequestMethod.GET,produces = "text/html; charset=utf-8")
    @ResponseBody
    public String getNaviMenuPortal(HttpServletRequest request) {
        long t1 = System.currentTimeMillis();
        MessageResult result = new MessageResult();
        String contentRowHtml = "";
        try {
            SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
            //获取用户参数
            Map<String,Object> userInfoMap = getUserInformation();
            //String dubboResultInfo = sysNaviMenuDtoServiceCustomer.getTree(JacksonUtils.toJson(userInfo), "ENABLED");
            Map<String,Object> paramMap = new HashMap<String,Object>();
            //paramMap.put("delflag",false);
            //paramMap.put("state",true);
            //paramMap.put("sidx","sort");
            //paramMap.put("sord","asc");
            String portalId = request.getParameter("portalId");
            if(portalId!=null){
                Map<String,Object> portalParamMap = new HashMap<String,Object>();
                portalParamMap.put("id",portalId);
                String portalDubboInfo = portalPageDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo),JacksonUtils.toJson(portalParamMap));
                DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(portalDubboInfo, DubboServiceResultInfo.class);
                if (dubboServiceResultInfo.isSucess()) {
                    PortalPageDto portalPageDto = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(),PortalPageDto.class);
                    String originPortalId = portalPageDto.getOriginPortalPageId();
                    if(originPortalId!=null){
                        portalId = originPortalId;
                    }
                }
                paramMap.put("portalId",portalId);
            }

            //String dubboResultInfo = sysNaviMenuDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo),JacksonUtils.toJson(paramMap));
            String dubboResultInfo = sysNaviMenuDtoServiceCustomer.queryListByPortalId(JacksonUtils.toJson(userInfo),JacksonUtils.toJson(paramMap));
            System.out.println("==============getNaviMenuPortal耗时01======================"+(System.currentTimeMillis()-t1));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<SysNaviMenuDto> list = JacksonUtils.fromJson(resultInfo, List.class, SysNaviMenuDto.class);
                String userName = (String) userInfoMap.get("userLoginName");//用户登录名
                String sessionId = (String) userInfoMap.get("sessionId");//sessionID
                String enUserName = (String) userInfoMap.get("enUserName");//蓝凌加密用户名
                String encorpCode = (String) userInfoMap.get("corpCode");//蓝凌公司代码
                String dateString = (String) userInfoMap.get("dateString");//
                String mortgageLoginName = (String) userInfoMap.get("mortgageLoginName");//按揭系统
                String LLOALoginName = (String) userInfoMap.get("LLOALoginName");//lanliOA系统
                int count = 0;

                List<SysNaviMenuDto> parentList = new ArrayList<SysNaviMenuDto>();
                Map<String,Object> childMaps = new HashMap<String,Object>();
                for (SysNaviMenuDto sysNaviMenuDto:list) {
                    String pid = sysNaviMenuDto.getParentId();
                    if(pid==null||"0".equals(pid)||"".equals (pid)){
                        parentList.add(sysNaviMenuDto);
                        List<SysNaviMenuDto> childList = (List<SysNaviMenuDto>) childMaps.get(sysNaviMenuDto.getId());
                        if (childList == null) {
                            childMaps.put(sysNaviMenuDto.getId(),new ArrayList<SysNaviMenuDto>());
                        }

                    }else{
                        List<SysNaviMenuDto> childList = (List<SysNaviMenuDto>) childMaps.get(sysNaviMenuDto.getParentId());
                        if (childList == null) {
                            childList = new ArrayList<SysNaviMenuDto>();
                            childList.add(sysNaviMenuDto);
                            childMaps.put(sysNaviMenuDto.getParentId(),childList);
                        }else{
                            childList.add(sysNaviMenuDto);
                        }
                    }
                    //divBuffer.append("<div class=\"wuye\"></div>");
                }
                System.out.println("==============getNaviMenuPortal耗时1======================"+(System.currentTimeMillis()-t1));
                StringBuffer contentBuf = new StringBuffer();
                for (SysNaviMenuDto sysNaviMenuDto:parentList) {
                    //String img = sysNaviMenuDto.getImage();
                    String name = sysNaviMenuDto.getName();
                    String id = sysNaviMenuDto.getId();
                    //String url = sysNaviMenuDto.getUrl();
                    //String pid = sysNaviMenuDto.getParentId();
                    //String linkType = sysNaviMenuDto.getLinkType();
                    StringBuffer divBuffer = new StringBuffer();
                    if (count == 0) {
                        divBuffer.append("<div class=\"scm-glyphicons  wuye\" style=\"border-top:none;margin-top:0px;\"> \n");
                    }else{
                        divBuffer.append("<div class=\"scm-glyphicons  wuye\" >");
                    }

                    divBuffer.append("<p class=\"p_l_title\" id=\"p_title_"+id+"\" data-toggle=\"collapse\" data-target=\"#ul_" + id + "\" aria-expanded=\"true\" aria-controls=\"ul_" + id + "\" style=\"cursor: pointer;text-align:center;\">"+name+"<span class=\"fa fa-angle-up\" id=\"collapseSearchSpan\"></span></p>\n");
                    divBuffer.append("<ul class=\"scm-glyphicons-list clearfix collapse in\" style=\"margin: auto;\" id=\"ul_"+id+"\">\n");
                    List<SysNaviMenuDto> childList = (List<SysNaviMenuDto>) childMaps.get(id);
                    for (SysNaviMenuDto child:childList) {
                       // String img1 = new String(child.getImage());
//                        String name1 = child.getName();
//                        String id1 = child.getId();
                        String url1 = child.getUrl();
//                        byte[] image1 = child.getImage ();
//                        String base64Img =image1!=null?"data:image/jpeg;base64,"+ Base64.getEncoder().encodeToString(image1):"";

                        if (url1.indexOf('?') != -1) {
                            String urlParamStr = url1.substring(url1.indexOf('?')+1);
                            urlParamStr = urlParamStr.replaceAll("=","\":\"").replaceAll("&","\",\"").replaceAll("amp;","");
                            urlParamStr = "{\""+ urlParamStr + "\"}";
                            Map<String,Object> urlParamMap = JacksonUtils.fromJson(urlParamStr,Map.class);
                            String home = urlParamMap==null?null:(String) urlParamMap.get("home");
                            String lanLin = urlParamMap==null?null:(String) urlParamMap.get("lanlin");
                            String mortgage = urlParamMap==null?null:(String) urlParamMap.get("mortgage");
                            String lloa = urlParamMap==null?null:(String) urlParamMap.get("LLOA");
                            if ("z".equals(home)) {
                                url1 = url1.replace("#[userName]",enUserName);
                                url1 = url1.replace("#[corpCode]",encorpCode);
                            } else if ("h".equals(home)) {
                                url1 = url1.replace("#[userName]",enUserName);
                            } else {
                                if (lanLin != null) {
                                    url1 = url1.replace("#[userName]",enUserName);
                                } else if (mortgage != null) {
                                    url1 = url1.replace("#[userName]",mortgageLoginName);
                                } else if(lloa != null){
                                    url1 = url1.replace("#[userName]",LLOALoginName);
                                }else{
                                    url1 = url1.replace("#[userName]",userName);
                                    url1 = url1.replace("#[sessionId]",sessionId);
                                }

                            }

                            if(lanLin!=null&&"true".equals(lanLin)){
                                url1 = url1.replace("","");
                            }
                        }

                  //      String pid1 = child.getParentId();
                        String linkType1 = child.getLinkType();
                 //       divBuffer.append("<li><a href=\"javascript:void(0)\" data-href=\""+url1+"\" data-linktype=\""+linkType1+"\"><span class=\"glyphicon-class\">"+name1+"</span></a></li>\n");
                       /*
                        * 图标库地址字段添加- - 注释- - - 2018-1-12
                        byte[] image = child.getImage();
                        byte[] image2 = image;//image!=null? CompressImgUtil.compressImg2(image,50,50):image;
                        if(image!=null&&image.length>32*1024){
                            image2 = image!=null? CompressImgUtil.compressImg2(image,50,50):image;
                        }

                        String base64Img = image2!=null?"data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(image2):"../../common/img/default2.png";
                        */
                       //图标库地址字段添加- - 修改- - - 2018-1-12
                        String base64Img = child.getIconUrl()!=null&&!child.getIconUrl().isEmpty()?child.getIconUrl():"../../common/img/default2.png";
                       if("INNER".equals (linkType1)){
                            divBuffer.append("<li>\n<a href=\"javascript:void(0);\" data-linkType=\""+linkType1+"\" data-href=\""+ url1+"\">\n" +
                                    "<img src=\""+ base64Img+"\"  title=\""
                                    +(child.getName())+"\">\n<span class=\"glyphicon-class\">"+child.getName()+"</span></a>\n</li>\n");
                        }else{
                            divBuffer.append("<li>\n<a  href=\"javascript:void(0);\" data-linkType=\"" + linkType1 + "\" data-href=\""+ url1+"\">\n" +
                                    "<img src=\""+ base64Img+"\"  title=\""
                                    +(child.getName())+"\">\n<span class=\"glyphicon-class\">"+child.getName()+"</span></a>\n</li>\n");
                        }

                    }

                    divBuffer.append("</ul>\n");
                    divBuffer.append("</div>\n");

                    contentBuf.append(divBuffer);
                    count++;
                }

                StringBuffer jsBuf = new StringBuffer();
                jsBuf.append("<script type=\"text/javascript\">\n");//fa fa-angle-up
                jsBuf.append("$(function(){\n" +
                        "       $('.wuye>ul>li>a').on('click',function(){\n" +
                        "           //debugger;\n " +
                        "           var href = $(this).attr('data-href');\n" +
                        "           var linkType = $(this).attr('data-linkType');\n" +
                        "           if(linkType=='OUTER'){\n" +
                        "            if(href.indexOf('LLOA=true')>-1){\n" +
                        "                     href = encodeURI(href); \n   " +
                        "            }   " +
                        "               window.open(href);\n" +
                        "           }else{\n" +
                        "               var aLink = href;\n" +
                        "               aLink = aLink.substring(aLink.indexOf('?'));\n" +
                        "               aLink = aLink.replace('?', '').replace(/&/g, '\",\"');\n" +
                        "               aLink = aLink.replace(/=/g, '\":\"');\n" +
                        "               var menuUrlObj ;\n" +
                        "               if (aLink != \"\") {\n" +
                        "                   menuUrlObj = JSON.parse('{\"' + aLink + '\"}');\n" +
                        "               }\n" +
                        "               if(menuUrlObj._proCode&&!menuUrlObj._menuCode){\n" +
                        "                   window.parent.switchPro(menuUrlObj._proCode);\n" +
                        "               }else if(menuUrlObj._proCode&&menuUrlObj._menuCode){\n" +
                        "                   window.parent.switchPro(menuUrlObj._proCode,menuUrlObj._menuCode);\n" +
                        "               }else{\n" +
                        "                   window.location.href = href;\n" +
                        "               }" +
                        "           }" +
                        "       });\n" +
                        "$('.scm-glyphicons-list').on('show.bs.collapse', function () {\n "+
                        "if($(this).prev().find(\"span\").hasClass(\"fa-angle-down\")){\n"+
                        "$(this).prev().find(\"span\").removeClass(\"fa-angle-down\");\n"+
                        "$(this).prev().find(\"span\").addClass(\"fa-angle-up\");}\n"+
                        " });\n"+
                        "$('.scm-glyphicons-list').on('hide.bs.collapse', function () {\n "+
                        "if($(this).prev().find(\"span\").hasClass(\"fa-angle-up\")){\n"+
                        "$(this).prev().find(\"span\").removeClass(\"fa-angle-up\");\n"+
                        "$(this).prev().find(\"span\").addClass(\"fa-angle-down\");}\n"+
                        " });\n"+
                        "\n");

//                        "});\n");
             /*   jsBuf.append("</script>\n");
                jsBuf.append ("<script type=\"text/javascript\">\n");*/
//                jsBuf.append ("$(\".p_l_title\").on(\"click\",function (e) {\n");
//                jsBuf.append ("if($(this).find(\"span\").hasClass(\"fa-angle-down\")){\n");
//                jsBuf.append (" $(this).find(\"span\").removeClass(\"fa-angle-down\");\n");
//                jsBuf.append ("$(this).find(\"span\").addClass(\"fa-angle-up\");\n");
//                jsBuf.append ("}else{\n");
//                jsBuf.append ("$(this).find(\"span\").removeClass(\"fa-angle-up\");\n");
//                jsBuf.append ("$(this).find(\"span\").addClass(\"fa-angle-down\");\n");
//                jsBuf.append (" }\n");
//                jsBuf.append ("e.stopPropagation();\n");
//                jsBuf.append ("});\n");
                jsBuf.append ("});\n");
                jsBuf.append ("</script>\n");
                contentBuf.append(jsBuf);
                contentRowHtml = contentBuf.toString();

            } else {
                contentRowHtml = "暂无数据！";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getNaviMenuPortal方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        long t2 = System.currentTimeMillis();

        System.out.println("==============getNaviMenuPortal耗时======================"+(t2-t1));
        return contentRowHtml;
    }
}