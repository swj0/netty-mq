package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;

public class MessageBase implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageBase() {
	}

	public MessageBase(MessageSource source,MessageType type) {
		if(source == null || type == null	)
			throw new IllegalArgumentException("source ,type 不能为空!!");
		setSource(source);
		setMsgType(type);
	}
	
	
	
	private String clientId;
	
	
	private String msgId;
	
	private MessageSource source;
	
	
	private String msgContent;
	
	private MessageType msgType ;  
	
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public MessageSource getSource() {
		return source;
	}

	
	protected void setSource(MessageSource source) {
		this.source = source;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	protected void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}
	public MessageType getMsgType() {
		return msgType;
	}

	

}
