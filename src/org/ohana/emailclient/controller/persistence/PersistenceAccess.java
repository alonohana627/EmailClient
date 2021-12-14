package org.ohana.emailclient.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** PersistenceAccess is a class that lets us to encode and decode from the hard drive.
 */
public class PersistenceAccess {

    private static final String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "validAccounts.ser";
    private static Encoder encoder = new Encoder();

    /**
     * Loads a list of the accounts from the hard drive.
     * @return list of the accounts saved on the hard drive
     */
    public List<ValidAccount> loadFromPersistence(){
        List<ValidAccount> resultList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
            decodePasswords(persistedList);
            resultList.addAll(persistedList);
        } catch ( Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
    /**
     * Saves the list of logged-in accounts to the hard drive.
     */
    public void saveToPersistence(List<ValidAccount> validAccounts){
        try {
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            encodePasswords(validAccounts);
            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method to decode the passwords from BASE64 to normal
     * @param persistedList - decodes the passwords from the hard drive
     */
    private static void decodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount: persistedList){
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }

    /**
     * Utility method to decode the passwords from normal to BASE64
     * @param persistedList - decodes the passwords to the hard drive
     */
    private static void encodePasswords(List<ValidAccount> persistedList) {
        for (ValidAccount validAccount: persistedList){
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }
}
