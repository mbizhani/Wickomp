package org.devocative.wickomp.vo;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public class PushMessage implements IWebSocketPushMessage {
	private int no;

	public PushMessage(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}

	@Override
	public String toString() {
		return "PushMessage: " + no;
	}
}
