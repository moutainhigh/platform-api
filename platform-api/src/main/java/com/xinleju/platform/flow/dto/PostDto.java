package com.xinleju.platform.flow.dto;

import java.util.List;

public class PostDto {
	private String id; 
	private String name;
	private String parseType;
	private List<UserDto> users;	//人可以全部为空
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParseType() {
		return parseType;
	}
	public void setParseType(String parseType) {
		this.parseType = parseType;
	}
	public List<UserDto> getUsers() {
		return users;
	}
	public void setUsers(List<UserDto> users) {
		this.users = users;
	}

}
