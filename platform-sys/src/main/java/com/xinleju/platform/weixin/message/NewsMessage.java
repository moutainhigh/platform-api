package com.xinleju.platform.weixin.message; 

/** 
 * 文本消息 
 *  
 * @author maguanglei
 * @date   20141013
 */
public class NewsMessage extends BaseMessage { 
	
	private Articles news;

	public Articles getNews() {
		return news;
	}

	public void setNews(Articles news) {
		this.news = news;
	}
	
} 
