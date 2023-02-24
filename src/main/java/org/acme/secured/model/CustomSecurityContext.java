package org.acme.secured.model;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class CustomSecurityContext implements SecurityContext {
    private final CustomPrincipal customPrincipal;

    public CustomSecurityContext(CustomPrincipal customPrincipal) {
        super();
        this.customPrincipal = customPrincipal;
    }

    @Override
    public Principal getUserPrincipal() {
        return customPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "CustomJWT";
    }
}
