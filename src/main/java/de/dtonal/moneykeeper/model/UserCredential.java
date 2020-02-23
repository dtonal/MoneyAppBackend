package de.dtonal.moneykeeper.model;

import java.io.Serializable;

public class UserCredential implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;

	public UserCredential() {
		super();
	}

	public UserCredential(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
