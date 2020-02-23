package de.dtonal.moneykeeper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import de.dtonal.moneykeeper.dao.UserDao;
import de.dtonal.moneykeeper.db.HibernateUtil;
import de.dtonal.moneykeeper.model.AppUser;
import de.dtonal.moneykeeper.model.UserCredential;
import junit.framework.TestCase;

public class UserDaoTest extends TestCase {

	public void testCreateAndFind() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		HibernateUtil.configName = "hibernatetest.cfg.xml";
		UserDao userDao = new UserDao();
		AppUser appUser = userDao.createUser(new UserCredential("name", "password"));
		AppUser foundById = userDao.findById(appUser.getId());
		assertNotNull(foundById);
	}

	public void testCreateAndFindByMail() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		HibernateUtil.configName = "hibernatetest.cfg.xml";
		UserDao userDao = new UserDao();
		AppUser appUser = userDao.createUser(new UserCredential("name@game.zz", "password"));
		appUser.setUsername("MyUsername");
		userDao.saveOrUpdate(appUser);
		Optional<AppUser> foundByMail = userDao.findByEmail("name@game.zz");
		assertNotNull(foundByMail);
		assertEquals(appUser.getUsername(), foundByMail.get().getUsername());
	}

	public void testEncryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		HibernateUtil.configName = "hibernatetest.cfg.xml";
		UserDao userDao = new UserDao();
		UserCredential credential = new UserCredential("name@game.zz", "password");
		AppUser appUser = userDao.createUser(credential);
		AppUser userFromCredentials = userDao.validateCredentials(credential);
	}

	public void testDoubleCreation() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		HibernateUtil.configName = "hibernatetest.cfg.xml";
		UserDao userDao = new UserDao();
		UserCredential credential = new UserCredential("name@game.zz", "password");
		AppUser appUser = userDao.createUser(credential);
		credential.setEmail("anotherone");
		AppUser appUser2 = userDao.createUser(credential);
		assertEquals(appUser.getPassword(), appUser2.getPassword());
	}
}
