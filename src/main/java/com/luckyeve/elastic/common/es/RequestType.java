package com.luckyeve.elastic.common.es;

/**
 * 文档事件类型
 * @author lixy
 *
 */
public enum RequestType {
	
	ADD(1),
	UPDATE(1),
	DELETE(2);
	
	private int val;
	
	private RequestType(int val) {
		this.val = val;
	}
	
	public int value() {
		return val;
	}
	
}
