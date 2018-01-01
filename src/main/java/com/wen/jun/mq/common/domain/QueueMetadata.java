package com.wen.jun.mq.common.domain;

import io.netty.channel.Channel;

public class QueueMetadata {
	
	private String queue;
	
	private String clientId;
	
	private Channel channel;

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
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
