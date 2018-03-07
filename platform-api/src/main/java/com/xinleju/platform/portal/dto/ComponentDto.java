package com.xinleju.platform.portal.dto;

import com.xinleju.platform.base.dto.BaseDto;


/**
 * @author admin
 */
public class ComponentDto extends BaseDto {


    //组件标题
    private String title;


    //组件编码
    private String code;


    //组件图标
    private byte[] titleIcon;


    //请求类型
    private String reqType;


    //组件内容url
    private String contentUrl;


    //组件内容类型
    private String contentType;


    //组件描述
    private String description;


    //组件类别ID
    private String categoryId;

    //显示关闭
    private Boolean displayClose;

    //显示最大化
    private Boolean displayMax;

    //显示最小化
    private Boolean displayMin;

    //显示删除
    private Boolean displayDelete;

    //显示刷新
    private Boolean displayRefresh;

    //显示移动
    private Boolean displayMove;

    //更多URL
    private String moreUrl;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }





    public byte[] getTitleIcon() {
		return titleIcon;
	}

	public void setTitleIcon(byte[] titleIcon) {
		this.titleIcon = titleIcon;
	}

	public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }


    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getDisplayClose() {
        return displayClose;
    }

    public void setDisplayClose(Boolean displayClose) {
        this.displayClose = displayClose;
    }

    public Boolean getDisplayMax() {
        return displayMax;
    }

    public void setDisplayMax(Boolean displayMax) {
        this.displayMax = displayMax;
    }

    public Boolean getDisplayMin() {
        return displayMin;
    }

    public void setDisplayMin(Boolean displayMin) {
        this.displayMin = displayMin;
    }

    public Boolean getDisplayDelete() {
        return displayDelete;
    }

    public void setDisplayDelete(Boolean displayDelete) {
        this.displayDelete = displayDelete;
    }

    public Boolean getDisplayRefresh() {
        return displayRefresh;
    }

    public void setDisplayRefresh(Boolean displayRefresh) {
        this.displayRefresh = displayRefresh;
    }

    public Boolean getDisplayMove() {
        return displayMove;
    }

    public void setDisplayMove(Boolean displayMove) {
        this.displayMove = displayMove;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }
}
