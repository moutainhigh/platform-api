package com.xinleju.platform.weixin.message; 

import java.util.List;

/** 
 * 文本消息 
 *  
 * @author maguanglei
 * @date   20141013
 */
public class Articles { 
	
	// 多条图文消息信息，默认第一个item为大图 
    private List<Article> articles; 
 
    public List<Article> getArticles() { 
        return articles; 
    } 
 
    public void setArticles(List<Article> articles) { 
        this.articles = articles; 
    } 
} 
