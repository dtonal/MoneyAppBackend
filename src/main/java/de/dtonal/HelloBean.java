package de.dtonal;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "welcome")
@SessionScoped
public class HelloBean implements Serializable {
	public HelloBean() {
		System.out.println("HelloBean instantiated");
	}

	public String getMessage() {
		return "I'm alive!";
	}
}
