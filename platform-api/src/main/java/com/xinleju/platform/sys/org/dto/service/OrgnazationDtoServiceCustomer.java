package com.xinleju.platform.sys.org.dto.service;
import java.util.List;
import java.util.Map;
import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;

public interface OrgnazationDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	/**
	 * 修改组织结构顺序
	 * 
	 * @param guuid-map
	 */
	public String updateOrgSort(String userInfo, String paramater);

	/**
	 * 获取所有的公司 (只查公司)
	 * @param paramater
	 * @return
	 */
	public String queryListCompany(String userInfo, String paramaterJson);
	
	/**
	 * 更改组织机构状态
	 * @param paramater
	 * @return
	 */
	public String updateStatus(String userInfo, String updateJson) ;
	/**
	 * 修改组织状态
	 * @param paramater
	 * @return
	 */
	public String updateOrgStatus(String userInfo, String updateJson) ;
	
	/**
	 * 根据IDs获取结果集
	 * @param paramater
	 * @return
	 */
	public String queryResListByIds(String userInfo, String paramater);


	/**
	 * 根据用户id，获取其角色/岗位/组织/菜单  等信息
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getUserRPOMInfoByUserId(String userInfo, String paramater);
	/**
	 *  查询用户所有组织信息：所属组织U岗位组织
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getUserAllOrgs(String userInfo, String paramater);
	/**
	 *  查询部门 或项目分期 （包含集团和公司）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getDeptOrBranch(String userInfo, String paramater);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String queryListCompanyTree(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String queryListCompanyAndZb(String userJson, String paramaterJson);
	/**
	 * 根据岗位id获取其上级组织id
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getOrgsByPostId(String userJson, String paramaterJson);
	/**
	 * 复制粘贴组织结构
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String copyAndPasteOrg(String userJson, String paramaterJson);

	/**
	 * 读excel并插入db
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String readExcelAndInsert(String userJson, List<OrgnazationExcelDto> orgList, String parentId);


}
