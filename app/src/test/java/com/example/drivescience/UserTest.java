package com.example.drivescience;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void hasRootDriverToken() {
        User user = new User();
        assertFalse(user.hasRootDriverToken());

        user.setRootDriverToken("a token");
        assertTrue(user.hasRootDriverToken());
    }
}