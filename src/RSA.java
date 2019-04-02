import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;



public class RSA {

        private static final String RSA_METHOD = "RSA/ECB/PKCS1Padding";
	private static final String RSA = "RSA";

	public static byte[] RSACipher(byte[] message, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_METHOD);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(message);

		} catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
		                  JOptionPane.showMessageDialog(null, e.getMessage());}
		return null;
	}

	public static byte[] RSADecipher(byte[] encryptedMessage, PrivateKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_METHOD);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(encryptedMessage);

		} catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
	}

	public static String encodePublicKey(PublicKey publicKey) {
                @SuppressWarnings("UnusedAssignment")
		KeyFactory fact = null;
		try {
			fact = KeyFactory.getInstance(RSA);
			X509EncodedKeySpec spec = fact.getKeySpec(publicKey, X509EncodedKeySpec.class);
			return Base64.encodeBase64String(spec.getEncoded());

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return "";
	}

	public static PublicKey decodePublicKey(String publicKeyAsString) {
		try {
			byte[] data = Base64.decodeBase64(publicKeyAsString);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
			KeyFactory fact = KeyFactory.getInstance(RSA);
			return fact.generatePublic(spec);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
	}

	public static String encodePrivateKey(PrivateKey privateKey) {
		try {
			KeyFactory fact = KeyFactory.getInstance(RSA);
			PKCS8EncodedKeySpec spec = fact.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
			byte[] packed = spec.getEncoded();
			String key64 = Base64.encodeBase64String(packed);

			Arrays.fill(packed, (byte) 0);
			return key64;

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return "";
	}

	public static PrivateKey decodePrivateKey(String privateKeyAsString) {
		try {
			byte[] clear = Base64.decodeBase64(privateKeyAsString);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
			KeyFactory fact = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = fact.generatePrivate(keySpec);
			Arrays.fill(clear, (byte) 0);

			return privateKey;

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
	}


    public static void main(String[] args)throws Exception {
        System.out.println("Enter plain text:");
        Scanner sc=new Scanner(System.in);
        String mess=sc.nextLine();
        System.out.println("Enter Private key");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
char[] password = sc.nextLine().toCharArray();

    try (FileInputStream fis = new FileInputStream("null")) {
        ks.load(fis, password);
    }
        KeyStore.ProtectionParameter protParam =
        new KeyStore.PasswordProtection(password);
// get my private key
    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
        ks.getEntry("privateKeyAlias", protParam);
    PrivateKey myPrivateKey = pkEntry.getPrivateKey();

    // save my secret key
    javax.crypto.SecretKey mySecretKey = null;
    KeyStore.SecretKeyEntry skEntry =
        new KeyStore.SecretKeyEntry(mySecretKey);
    ks.setEntry("secretKeyAlias", skEntry, protParam);

    // store away the keystore
    try (FileOutputStream fos = new FileOutputStream("newKeyStoreName")) {
        ks.store(fos, password);
    }
    }

}