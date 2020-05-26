package edu.tacoma.uw.itsdone.model;

import java.io.Serializable;
import java.util.regex.Pattern;


/**
 * this is an Account class that is used when an account is being created
 *
 * @author Trevor Peters
 * @version 1.0
 * @since 2020-05-13
 */
public class Account implements Serializable {
    public static int MIN_PASSWORD_LENGTH = 6;
    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private String mFirstName;
    private String mLastName;
    private String mUsername;
    private String mEmail;
    private String mPassword;



    /**
     * Constructor for the account class
     *
     * @param first first name
     * @param last last name
     * @param user username
     * @param email email address
     * @param pwd password
     */
    public Account (String first, String last, String user, String email, String pwd){
        mFirstName = first;
        mLastName = last;
        mUsername = user;
        mEmail = email;
        mPassword = pwd;

    }

    /** returns first name */
    public String getFirstName() { return mFirstName; }
    /** returns last name */
    public String getLastName() { return mLastName; }
    /** returns username */
    public String getUsername() { return mUsername; }
    /** returns email */
    public String getEmail() { return mEmail; }
    /** returns password */
    public String getPassword() { return mPassword; }

    /**
     * throw an exception for passwords that don't meet requirement
     *
     * @param pswd the password given
     * @throws IllegalArgumentException
     */
    public static void passwordValidation(String pswd) throws IllegalArgumentException {
        boolean foundDigit = false, foundSymbol = false;
        if  (pswd == null || pswd.length() < MIN_PASSWORD_LENGTH)
            throw new IllegalArgumentException("Password be " + MIN_PASSWORD_LENGTH + " or more characters long");
        for (int i=0; i<pswd.length(); i++) {
            if (Character.isDigit(pswd.charAt(i)))
                foundDigit = true;
            if (!Character.isLetterOrDigit(pswd.charAt(i)))
                foundSymbol = true;
        }
        if (!foundDigit || !foundSymbol)
            throw new IllegalArgumentException("Password must contain at least one number and special");
    }

    public static void emailValidation (String email) throws IllegalArgumentException {
        if (email == null ||  !EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("please enter a valid email");
    }

}


