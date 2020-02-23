package de.dtonal.moneykeeper.dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.query.Query;

import de.dtonal.moneykeeper.model.AppUser;
import de.dtonal.moneykeeper.model.UserCredential;
import de.dtonal.moneykeeper.security.EncryptionService;

@Stateless
public class UserDao extends AbstractDao<AppUser> {
	public AppUser createUser(UserCredential credential)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String encryptedPassword = EncryptionService.generateStrongPasswordHash(credential.getPassword());
		System.out.println("encryptedPassword for user " + encryptedPassword);
		AppUser user = new AppUser(credential.getEmail(), encryptedPassword);
		return saveOrUpdate(user);
	}

	public AppUser findById(Long id) {
		return findById(AppUser.class, id);
	}

	public Optional<AppUser> findByEmail(String email) {
		AppUser appUserOpt = doInSession((user, session) -> {
			Query<AppUser> query = session.getNamedQuery("findByEmail");
			query.setParameter("email", email);
			List<AppUser> resultList = query.getResultList();
			AppUser result = null;
			if (!resultList.isEmpty()) {
				result = resultList.get(0);
			}
			return result;
		});
		return Optional.ofNullable(appUserOpt);

	}

	public AppUser validateCredentials(UserCredential credentials)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		Optional<AppUser> optionalUser = findByEmail(credentials.getEmail());

		if (optionalUser.isPresent()) {
			String encryptedPassword = EncryptionService.generateStrongPasswordHash(credentials.getPassword());
			if (optionalUser.get().getPassword().equals(encryptedPassword)) {
				return optionalUser.get();
			}
		}
		throw new RuntimeException("User not authorized");
	}

	public AppUser findById(Long id, Session session) {
		return findById(AppUser.class, id, session);
	}

}
