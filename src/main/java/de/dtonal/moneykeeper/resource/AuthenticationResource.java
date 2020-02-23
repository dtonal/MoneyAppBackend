package de.dtonal.moneykeeper.resource;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.dtonal.moneykeeper.dao.UserDao;
import de.dtonal.moneykeeper.model.AppUser;
import de.dtonal.moneykeeper.model.UserCredential;
import de.dtonal.moneykeeper.security.EncryptionService;
import de.dtonal.moneykeeper.security.JwtService;

@Path("/authentication")
public class AuthenticationResource {

	@Inject
	private JwtService jwtService;

	@Inject
	private UserDao userDao;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/signup")
	public Response signup(UserCredential credentials) {
		try {
			AppUser appUser = userDao.createUser(credentials);
			// Issue a token for the user
			String token = issueToken(appUser.getId());

			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(UserCredential credentials) {

		try {

			// Authenticate the user using the credentials provided
			AppUser appUser = authenticate(credentials.getEmail(), credentials.getPassword());

			// Issue a token for the user
			String token = issueToken(appUser.getId());

			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	private AppUser authenticate(String email, String password) throws Exception {
		// Authenticate against a database, LDAP, file or whatever
		// Throw an Exception if the credentials are invalid
		Optional<AppUser> optionalUser = userDao.findByEmail(email);

		if (optionalUser.isPresent()) {
			String encryptedPassword = EncryptionService.generateStrongPasswordHash(password);
			if (optionalUser.get().getPassword().equals(encryptedPassword)) {
				return optionalUser.get();
			}
		}

		throw new RuntimeException("User not authorized");
	}

	private String issueToken(Long userId) throws IOException {
		return jwtService.createJws(userId.toString());
	}

}
