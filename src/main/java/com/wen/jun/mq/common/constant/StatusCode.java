package com.wen.jun.mq.common.constant;

public enum StatusCode {
	
	LinkUsabled(1),
	LinkEstablished(2),//链路已经建立
	LinkNotEstablished(3),//链路还没有建立
	LinkNotActived(4),//链路没有激活
	LinkNotOpened(5),//链路没有打开
	LinkNotWritabled(6);//链路不可写
	
	
	
	private int type;
	
	
	StatusCode(int type){
		this.type = type;
	}

}
