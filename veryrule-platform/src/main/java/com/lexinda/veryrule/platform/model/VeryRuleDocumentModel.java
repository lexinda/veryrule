package com.lexinda.veryrule.platform.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lexinda.veryrule.bo.RuleFlowBo;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleDocumentModel{
	private Integer id;
	private String name;
	private String author;
	private String key;
	private String desc;
	//[{"key":"beta","value":"2"}]
	private List<Domain> domains;
	//{"type":"post","contentType":"application/json"}
	private Request request;
	private List<Param> paramIn;
	private String paramInExample;
	private List<Param> paramOut;
	private String paramOutExample;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public List<Param> getParamIn() {
		return paramIn;
	}

	public void setParamIn(List<Param> paramIn) {
		this.paramIn = paramIn;
	}

	public String getParamInExample() {
		return paramInExample;
	}

	public void setParamInExample(String paramInExample) {
		this.paramInExample = paramInExample;
	}

	public List<Param> getParamOut() {
		return paramOut;
	}

	public void setParamOut(List<Param> paramOut) {
		this.paramOut = paramOut;
	}

	public String getParamOutExample() {
		return paramOutExample;
	}

	public void setParamOutExample(String paramOutExample) {
		this.paramOutExample = paramOutExample;
	}
	
	public static class Domain{
		private String key;
		private String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public static class Request{
		private String type;
		private String contentType;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
	}

	public static class Param{
		private Integer id;
		private String field;
		private String name;
		private String require;
		private String dtype;
		private String desc;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getRequire() {
			return require;
		}
		public void setRequire(String require) {
			this.require = require;
		}
		public String getDtype() {
			return dtype;
		}
		public void setDtype(String dtype) {
			this.dtype = dtype;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
}
