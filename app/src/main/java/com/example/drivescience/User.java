package com.example.drivescience;

public class User {
    private String rootDriverToken;

    public User() {}

    public String getRootDriverToken() {
        return rootDriverToken;
    }

    public boolean hasRootDriverToken() {
        return rootDriverToken != null && !rootDriverToken.isEmpty();
    }

    public void setRootDriverToken(String token) {
        rootDriverToken = token;
    }
}
