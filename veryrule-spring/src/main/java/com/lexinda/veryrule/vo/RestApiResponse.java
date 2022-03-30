package com.lexinda.veryrule.vo;
/**
 * 
 * @author lexinda
 *
 */
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