package com.xinleju.cloud.oa.portal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.portal.dto.PortalPageDto;
import com.xinleju.cloud.oa.portal.dto.service.PortalPageDtoServiceCustomer;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.tools.data.JacksonUtils;

import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.uitls.OpeLogInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 站点表控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/portal/portalPage")
public class PortalPageController {

    private static Logger log = Logger.getLogger(PortalPageController.class);

    @Autowired
    private PortalPageDtoServiceCustomer portalPageDtoServiceCustomer;

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
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
     *
     * @return
     */
    @RequestMapping(value = "/getPortalWithPermision", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getPortalWithPermision() {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.getPortalWithPermision(userInfo);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getPortalWithPermision方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/getPortalListWithPermision", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getPortalListWithPermision() {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.getPortalListWithPermision(userInfo);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<PortalPageDto> portalPageDto = JacksonUtils.fromJson(resultInfo, ArrayList.class,PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 返回分页对象
     *
     * @param map
     * 分页条件参数解释：start--从第几条开始
     * limit--每页显示数量
     * {
     * "limit":1,
     * "start":0
     * }
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult page(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.getPage(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param map 条件查询参数
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    @ResponseBody
    public MessageResult queryList(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.queryList(userInfo, paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<PortalPageDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, PortalPageDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }
        return result;
    }


    /**
     * 保存实体对象
     *
     * @param t 站点实体对象
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @OpeLogInfo(sysCode="OA",node = "门户新建")
    public MessageResult save(@RequestBody PortalPageDto t) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            //SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = portalPageDtoServiceCustomer.save(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
            	   result.setSuccess(dubboServiceResultInfo.isSucess());
                   result.setMsg(dubboServiceResultInfo.getMsg());
                   result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
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
     * @param id 站点id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @OpeLogInfo(sysCode="OA",node = "门户删除")
    public MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.deleteObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }

        return result;
    }


    /**
     * 删除实体对象
     *
     * @param ids 要删除的站点id字符串，以逗号分隔
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    @OpeLogInfo(node = "门户删除",sysCode = "OA")
    public MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.deleteAllObjectByIds(userInfo, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
        }

        return result;
    }

    /**
     * 修改修改实体对象
     *
     * @param id 要更新的站点id
     * @param map 要修改的实体对象属性-值map集合
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    @OpeLogInfo(node = "门户更新",sysCode = "OA")
    public MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        PortalPageDto portalPageDto = null;
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = portalPageDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = portalPageDtoServiceCustomer.update(userInfo, updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    PortalPageDto i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), PortalPageDto.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(updateDubboServiceResultInfo.isSucess());
                    result.setMsg(updateDubboServiceResultInfo.getMsg());
                    result.setCode(updateDubboServiceResultInfo.getResult());
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(portalPageDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 复制站点实体对象
     *
     * @param t 站点实体对象复制
     * @return
     */
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    @OpeLogInfo(node = "门户复制",sysCode = "OA")
    public MessageResult SaveAndCopy(@PathVariable("id") String id, @RequestBody PortalPageDto t) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("originId",id);
            paramMap.put("portalPage",t);
            String saveJson = JacksonUtils.toJson(paramMap);
            String dubboResultInfo = portalPageDtoServiceCustomer.saveAndCopy(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PortalPageDto portalPageDto = JacksonUtils.fromJson(resultInfo, PortalPageDto.class);
                result.setResult(portalPageDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 更新门户启用禁用状态
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/updatePortalStatus", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @OpeLogInfo(sysCode="OA",node = "门户状态更新")
    public MessageResult updatePortalStatus(@RequestBody Map<String,Object> map) {
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            //SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
            Boolean status = (Boolean) map.get("status");

            String saveJson = JacksonUtils.toJson(map);
            String dubboResultInfo = portalPageDtoServiceCustomer.updatePortalStatus(userInfo, saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<PortalPageDto> portalPageDtos = JacksonUtils.fromJson(resultInfo, List.class,PortalPageDto.class);
                result.setResult(portalPageDtos);
                result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                result.setMsg((status!=null&&status)?"门户启用成功！":"门户禁用成功");
            } else {
                result.setSuccess(dubboServiceResultInfo.isSucess());
                result.setMsg(dubboServiceResultInfo.getMsg());
                result.setCode(dubboServiceResultInfo.getResult());
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(map);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
                result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }
}
