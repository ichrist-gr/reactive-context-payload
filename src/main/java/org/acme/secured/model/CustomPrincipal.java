package org.acme.secured.model;

import java.security.Principal;

public class CustomPrincipal implements Principal {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
