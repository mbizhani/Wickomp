package org.devocative.wickomp.demo.vo;

import java.io.Serializable;

public class TerminalConnInfo implements Serializable {
	private static final long serialVersionUID = 8548580685461815658L;

	private String address = "192.168.40.131";
	private Integer port = 22;
	private String username = "root";
	private String password = "qazWSX@123";

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
