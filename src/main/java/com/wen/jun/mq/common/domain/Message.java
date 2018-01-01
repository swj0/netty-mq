package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;

public class Message extends MessageBase implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	
	public Message() {
	}
	
	
	public Message(MessageSource source,MessageType type) {
		super(source, type);
	}
	
	
	@Override
	public String toString() {
		return "Message [getMsgId()=" + getMsgId() + ", getSource()=" + getSource() + ", getMsgContent()="
				+ getMsgContent() + ", getClientId()=" + getClientId() + ", getMsgType()=" + getMsgType()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	
	
}
