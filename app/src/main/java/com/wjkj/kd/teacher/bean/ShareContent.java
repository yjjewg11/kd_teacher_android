package com.wjkj.kd.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/11.
 */
public class ShareContent implements Serializable{
	private String title;
	private String content;
	private String pathurl;
	private String httpurl;

	public ShareContent(String title, String content, String pathurl, String httpurl) {
		this.title = title;
		this.content = content;
		this.pathurl = pathurl;
		this.httpurl = httpurl;
	}

	public ShareContent() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPathurl() {
		return pathurl;
	}

	public void setPathurl(String pathurl) {
		this.pathurl = pathurl;
	}

	public String getHttpurl() {
		return httpurl;
	}

	public void setHttpurl(String httpurl) {
		this.httpurl = httpurl;
	}
}
