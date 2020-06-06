package edu.tacoma.uw.itsdone;

import org.junit.Test;

import edu.tacoma.uw.itsdone.model.Account;
import static org.junit.Assert.*;

public class AccountTest {
    @Test
    public void testAccountConstructor() {
        Account account = new Account("trevor", "peters", "Fadepaw", "Trevor07@uw.edu", "test1@!3");
        assertNotNull(account);
    }
    @Test
    public void testAccountConstructorBadEmail() {
        try {
            new Account("trevor", "peters", "Fadepaw", "null", "test1@!3");
            new Account("trevor", "peters", "Fadepaw", null, "test1@!3");
            fail("Account created with invalid email");
        } catch(IllegalArgumentException e) {

        }
    }

    @Test
    public void testAccountConstructorBadPassword() {
        try {
            new Account("trevor", "peters", "Fadepaw", "Trevor07@uw.edu", "null");
            new Account("trevor", "peters", "Fadepaw", "Trevor07@uw.edu", null);
            fail("Account created with invalid password");
        } catch(IllegalArgumentException e) {

        }
    }

    @Test
    public void testAccountGetters() {
        Account account = new Account("trevor", "peters", "Fadepaw", "Trevor07@uw.edu", "test1@!3");
        assert(account.getFirstName() == "trevor");
        assert(account.getLastName() == "peters");
        assert(account.getUsername() == "Fadepaw");
        assert(account.getEmail() == "Trevor07@uw.edu");
        assert(account.getPassword() == "test1@!3");
    }



}
