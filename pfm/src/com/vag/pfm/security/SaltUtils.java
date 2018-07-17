/** ********************************************************************************
 * @author  Gobinath A Created on Sep 24, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Sep 24, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * The Class SaltUtils.
 */
public final class SaltUtils {

    /**
     * Encrypt.
     * 
     * @param regularPwd the regular pwd
     * @return the string
     */
    public static String encrypt(String regularPwd) {
        return encrypt(regularPwd, getSalt(), false);
    }

    /**
     * Gets the salt.
     * 
     * @return the salt
     */
    private static byte[] getSalt() {
        SecureRandom saltGen;
        byte[] salt = new byte[12];
        saltGen = new SecureRandom();
        saltGen.nextBytes(salt);
        return salt;
    }

    /**
     * Authenticate.
     * 
     * @param newPwd the new pwd
     * @param saltPwd the salt pwd
     * @return true, if successful
     */
    public static boolean authenticate(String newPwd, String saltPwd) {
        return authenticate(newPwd, saltPwd, false);
    }

    /**
     * Authenticate.
     * 
     * @param newPwd the new pwd
     * @param saltPwd the salt pwd
     * @param install the install
     * @return true, if successful
     */
    public static boolean authenticate(String newPwd, String saltPwd, boolean install) {
        byte[] salt = new byte[12];
        byte[] checkPwd;
        System.arraycopy(hexToBytes(saltPwd), 0, salt, 0, 12);
        checkPwd = hexToBytes(encrypt(newPwd, salt, install));
        return Arrays.equals(hexToBytes(saltPwd), checkPwd);
    }

    /**
     * Authenticate installation.
     *
     * @param newPwd the new pwd
     * @param SaltPwd the salt pwd
     * @return true, if successful
     */
    public static boolean authenticateInstallation(String newPwd, String SaltPwd) {
        byte[] salt = new byte[12];
        String checkPwd;
        byte[] existPwd;

        existPwd = hexToBytes(SaltPwd);
        System.arraycopy(existPwd, existPwd.length - 12, salt, 0, 12);
        checkPwd = encrypt(newPwd, salt, true);

        return Arrays.equals(hexToBytes(SaltPwd), hexToBytes(checkPwd));
    }

    /**
     * Encrypt.
     * 
     * @param strPWD the str pwd
     * @param salt the salt
     * @param install the install
     * @return the string
     */
    private static String encrypt(String strPWD, byte[] salt, boolean install) {
        MessageDigest msgDigest;
        byte[] encPwd = new byte[0];
        byte[] saltPwd = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(salt);
            msgDigest.update(strPWD.getBytes());
            encPwd = msgDigest.digest();

            saltPwd = new byte[salt.length + encPwd.length];
            if (!install) {
                System.arraycopy(salt, 0, saltPwd, 0, salt.length);
                System.arraycopy(encPwd, 0, saltPwd, salt.length, encPwd.length);
            } else {
                System.arraycopy(encPwd, 0, saltPwd, 0, encPwd.length);
                System.arraycopy(salt, 0, saltPwd, encPwd.length, salt.length);
            }
            return bytesToHex(saltPwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Bytes to hex.
     * 
     * @param digest the digest
     * @return the string
     */
    private static String bytesToHex(byte[] digest) {
        String retVal = null;
        if (digest != null) {
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String temp = Integer.toHexString(0xFF & digest[i]);

                if (temp.length() < 2) {
                    hexString.append('0');
                    hexString.append(temp);
                } else {
                    hexString.append(temp);
                }
            }
            retVal = hexString.toString();
        }
        return retVal;
    }

    /**
     * Hex to bytes.
     * 
     * @param hexString the hex string
     * @return the byte[]
     */
    private static byte[] hexToBytes(String hexString) {
        byte[] retVal = null;
        if ((hexString != null) && ((hexString.length() % 2) == 0)) {
            retVal = new byte[hexString.length() / 2];
            int index = 0;
            for (int i = 0; i < hexString.length(); i += 2) {
                StringBuffer temp = new StringBuffer();
                temp.append(hexString.charAt(i));
                temp.append(hexString.charAt(i + 1));

                int intVal = Integer.parseInt(temp.toString(), 16);
                byte tempByte = (new Integer(intVal)).byteValue();
                retVal[index++] = tempByte;
            }
        }
        return retVal;
    }
}
