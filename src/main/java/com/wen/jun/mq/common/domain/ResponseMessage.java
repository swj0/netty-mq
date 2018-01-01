package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.constant.StatusCode;

public class ResponseMessage extends MessageBase implements java.io.Serializable{

	/**
	 * 4
	 */
	private static final long serialVersionUID = 1L;

	private String reason ;
	
	private Throwable cause ;
	
	private StatusCode statusCode ;
	
	private boolean success;
	
	public ResponseMessage() {
	}
	
	public ResponseMessage(MessageSource source,MessageType type) {
		super(source, type);
	}
	
	
	
	public ResponseMessage(boolean success,Throwable cause ) {
		this.success = success;
	}
	

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public StatusCode getStatus() {
		return statusCode;
	}

	public void setStatus(StatusCode status) {
		this.statusCode = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	@Override
	public String toString() {
		return "ResponseMessage [reason=" + reason + ", cause=" + cause + ", statusCode=" + statusCode + ", success="
				+ success + ", getMsgId()=" + getMsgId() + ", getSource()=" + getSource() + ", getMsgContent()="
				+ getMsgContent() + ", getClientId()=" + getClientId() + ", getMsgType()=" + getMsgType()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
}
