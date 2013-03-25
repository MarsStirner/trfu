package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.text.ParseException;

public class PasswordPropertyConverter extends PropertyConverter {
    private static String secret = "599F3AECA3451CBE";
    private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
    private static SecretKey key;
    private static Cipher cipher;
    private static Base64 coder;

    static {
        try {
            key = new SecretKeySpec(secret.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            coder = new Base64(32,linebreak,true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public Object getAsObject(String s) throws ParseException {
        byte[] encypted = coder.decode(s);
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(encypted);
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new String(decrypted);

    }

    @Override
    public String getAsString(Object o) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(o.toString().getBytes());
            return  new String(coder.encode(cipherText));
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;

    }
}
