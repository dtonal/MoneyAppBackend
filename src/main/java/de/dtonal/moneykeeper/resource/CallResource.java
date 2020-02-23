package de.dtonal.moneykeeper.resource;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.dtonal.moneykeeper.dao.UserDao;
import de.dtonal.moneykeeper.db.HibernateUtil;
import de.dtonal.moneykeeper.model.AppUser;
import de.dtonal.moneykeeper.security.Secured;

@Path("/callservice")
public class CallResource {
	@Context
	SecurityContext securityContext;

	@Inject
	private UserDao userDao;

	@GET
	@Path("/calls")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Call> getCalls() {
		List<Call> calls = new ArrayList<>();
		var call = new Call();
		call.setCallId("callid");
		call.setCallName("name");
		call.setTimestamp("now");
		calls.add(call);
		return calls;
	}

	@Secured
	@GET
	@Path("/callssecured")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Call> getCallsSecured(@Context SecurityContext securityContext) {
		Principal principal = securityContext.getUserPrincipal();
		String userId = principal.getName();
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			AppUser foundById = userDao.findById(Long.valueOf(userId), session);
			System.out.println("User " + foundById.getEmail() + " is calling");
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		List<Call> calls = new ArrayList<>();
		var call = new Call();
		call.setCallId("callid");
		call.setCallName("name");
		call.setTimestamp("now");
		calls.add(call);

		return calls;
	}
}
