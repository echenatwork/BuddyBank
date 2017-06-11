package web.auth;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Eric on 3/12/2017.
 */
public class PasswordTool {
    // TODO key length should be configurable
    // TODO iterations can be stored in the db with the salt
    private int iterations = 100;
    private int keyLength = 256;



    private SecureRandom secureRandom = new SecureRandom();
    private BASE64Encoder base64Encoder = new BASE64Encoder();
    private BASE64Decoder base64Decoder = new BASE64Decoder();

    public String hashPassword(String password, String salt) {
        byte[] saltBytes = null;
        try {
            saltBytes = base64Decoder.decodeBuffer(salt);
        } catch (Exception e ) {
            throw new RuntimeException(e);
        }
        return base64Encoder.encode(hashPassword(password.toCharArray(), saltBytes, iterations, keyLength));
    }

    private byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    public String generateSalt(int saltLength) {
        byte[] saltBytes = new byte[saltLength];
        secureRandom.nextBytes(saltBytes);
        return base64Encoder.encode(saltBytes);
    }
}
