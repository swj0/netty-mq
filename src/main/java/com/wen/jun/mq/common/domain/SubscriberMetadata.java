package com.wen.jun.mq.common.domain;

import io.netty.channel.Channel;

public class SubscriberMetadata {
	
	private String topic;
	
	private String clientId;
	
	private Channel channel;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
	
	

}
