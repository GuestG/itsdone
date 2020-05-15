package edu.tacoma.uw.itsdone.model;

import java.io.Serializable;


/**
 * this is an Account class that is used when an account is being created
 *
 * @author Trevor Peters
 * @version 1.0
 * @since 2020-05-13
 */
public class Account implements Serializable {

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

}


