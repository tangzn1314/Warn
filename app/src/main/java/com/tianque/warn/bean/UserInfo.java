package com.tianque.warn.bean;

import java.io.Serializable;

/**
 * @author ctrun
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = -3182497309274096914L;

	/**
	 * 登录token
	 */
	public String sid = "";
	public long id;
	public String name;

	/**
	 * 用户所属组织机构
	 */
	public Organization organization = new Organization();

}