package de.dtonal.moneykeeper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import de.dtonal.moneykeeper.security.EncryptionService;
import junit.framework.TestCase;

public class EncryptionTest extends TestCase {
	public void testJust() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String pass1 = EncryptionService.generateStrongPasswordHash("123456");
		String pass2 = EncryptionService.generateStrongPasswordHash("123456");
		assertEquals(pass1, pass2);
	}
}
