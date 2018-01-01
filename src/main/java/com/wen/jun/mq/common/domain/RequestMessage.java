package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;

public class RequestMessage  extends MessageBase implements java.io.Serializable{
	
	private String topic;
	
	//这个代表了队列名称或者主题的名称
	private String destination;

	private String groupName;
	

	
	public RequestMessage(MessageType type){
		
	}
	public RequestMessage(MessageSource source,MessageType type) {
		super(source, type);
	}
	
	/**
	 * 进行组
	 * @param groupName
	 */
	public  void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	@Override
	public String toString() {
		return "RequestMessage [topic=" + topic + ", destination=" + destination + ", groupName=" + groupName
				+ ", getDestination()=" + getDestination() + ", getMsgId()=" + getMsgId() + "]";
	}
	
	

}
