package es.cc.esliceu.db.limbo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class GeneradorHash {
    private static MessageDigest md = null;

    public static String generaHash(String password){
        try {
            if (md==null){
                md = MessageDigest.getInstance("SHA-256");
            }

        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e.getMessage());
        }


        try {
            byte[] bytesOfMessage = password.getBytes("UTF-8");
            byte[] thedigest = md.digest(bytesOfMessage);
            return new String(thedigest);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static String generaRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
