package com.issuetracker.mail;

/**
 *
 * @author Monika
 */
public class PasswordGenerator {

    public String generatePassword() {
        final int PASSWORD_LENGTH = 10;
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < PASSWORD_LENGTH; x++) {
            sb.append((char) ((int) (Math.random() * 26) + 97));
        }
        return sb.toString();
    }

}
