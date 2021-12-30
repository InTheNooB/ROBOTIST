package wrk.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Encrypt {

    /**
     * Encrypts text with SHA256.
     *
     * @param input, the text to encrypt
     * @return hash, byte[]
     */
    public static byte[] getSHA(String input) {
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encrypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts an hash into a String.
     *
     * @param hash, byte[] of the hash
     * @return String
     */
    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros 
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
