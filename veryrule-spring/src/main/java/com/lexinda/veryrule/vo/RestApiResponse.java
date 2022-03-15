/* 
* Copyright  (C)  1997-2020  康成投资（中国）有限公司
* 
* http://www.rt-mart.com 
*  
* 版权归本公司所有，不得私自使用、拷贝、修改、删除，否则视为侵权 
*/
package com.lexinda.veryrule.vo;

public class RestApiResponse {
	
	/**
	 * 接口耗时 ms
	 */
	private long elapsedTime;
	/**
	 * 错误码
	 */
	private int errorCode;
	/**
	 * 错误描述
	 */
	private String errorDesc;
	/**
	 * 服务器时间
	 */
	private long serverTime;
	
	/**
	 * 业务出参项
	 */
	private Object body;

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}