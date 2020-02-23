package de.dtonal.moneykeeper.controller;

import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import de.dtonal.moneykeeper.dao.UserDao;
import de.dtonal.moneykeeper.model.AppUser;
import de.dtonal.moneykeeper.model.UserCredential;
import de.dtonal.moneykeeper.utils.SessionUtils;

@ManagedBean
@SessionScoped
@Named(value = "loginController")
public class LoginController implements Serializable {

	@Inject
	private UserDao userDao;

	private static final long serialVersionUID = 1094801825228386363L;

	private String pwd;
	private String msg;
	private String email;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// validate login
	public String validateUsernamePassword() {
		try {
			AppUser appUser = userDao.validateCredentials(new UserCredential(email, pwd));
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("userId", appUser.getId());
			return "home";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Incorrect Username and Passowrd", "Please enter correct username and Password"));
			return "login";
		}

	}

	// logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}
}
