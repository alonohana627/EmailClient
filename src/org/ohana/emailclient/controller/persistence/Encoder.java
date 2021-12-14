package org.ohana.emailclient.controller.persistence;

import java.util.Base64;
/**Proof of concept of encoding the username and password to the hard drive.
 * Base64 is NOT an encryption, in fact, if we want to do it safely, we need to demand
 * the user for a "key" (or a password) and do it with AES or ACTUALLY encryption.
 */
public class Encoder {

    private static Base64.Encoder enc = Base64.getEncoder();
    private static Base64.Decoder dec = Base64.getDecoder();

    public String encode(String text){
        try {
            return enc.encodeToString(text.getBytes());
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decode(String text){
        try {
            return new String(dec.decode(text.getBytes()));
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
