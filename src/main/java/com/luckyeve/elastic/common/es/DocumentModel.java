package com.luckyeve.elastic.common.es;

import java.io.Serializable;
import java.util.Map;

/**
 * 字段
 * @author lixy
 *
 */
public class DocumentModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String index;
	
	private String type;
	
	private String id;
	
	private Map<String, ? super Object> source;
	
	private RequestType requestType;
	
	public DocumentModel() {
	}
	
	public DocumentModel(String index, String type, String id, Map<String, ? super Object> source) {
		this.index = index;
		this.type = type;
		this.id = id;
		this.setSource(source);
	}
	
	public DocumentModel(String index, String type, String id, Map<String, ? super Object> source, RequestType requestType) {
		this.index = index;
		this.type = type;
		this.id = id;
		this.setSource(source);
		this.requestType = requestType;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, ? super Object> getSource() {
		return source;
	}

	public void setSource(Map<String, ? super Object> source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RequestType getRequestType() {
		return requestType;
	}
	
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	
	

}
