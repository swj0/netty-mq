package com.wen.jun.mq.core;

import com.wen.jun.mq.common.domain.Message;

public interface MessageListener {

	void onMessage(final Message msg);
}
