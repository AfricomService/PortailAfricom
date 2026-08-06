package com.societe.portail.web.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

/**
 * REST controller for managing global OIDC logout.
 */
@RestController
public class LogoutResource {

    private final ClientRegistration registration;

    @Value("${application.public-url}")
    private String publicUrl;

    public LogoutResource(ClientRegistrationRepository registrations) {
        this.registration = registrations.findByRegistrationId("oidc");
    }

    /**
     * {@code POST  /api/logout} : logout the current user.
     *
     * @param request the {@link HttpServletRequest}.
     * @param idToken the ID token.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a body with a global logout URL.
     */
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, 
            @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {
        
        StringBuilder logoutUrl = new StringBuilder();
        String issuerUri = this.registration.getProviderDetails().getIssuerUri();
        
        if (issuerUri.contains("auth0.com")) {
            logoutUrl.append(issuerUri.endsWith("/") ? issuerUri + "v2/logout" : issuerUri + "/v2/logout");
        } else {
            logoutUrl.append(this.registration.getProviderDetails()
                .getConfigurationMetadata().get("end_session_endpoint").toString());
        }

        // URL publique fixée en config, indépendante du proxy/port interne
        String postLogoutRedirectUri = publicUrl.endsWith("/") ? publicUrl : publicUrl + "/";

        if (logoutUrl.indexOf("auth0.com") > -1) {
            logoutUrl.append("?client_id=").append(this.registration.getClientId())
                     .append("&returnTo=").append(postLogoutRedirectUri);
        } else {
            logoutUrl.append("?id_token_hint=").append(idToken.getTokenValue())
                     .append("&post_logout_redirect_uri=")
                     .append(URLEncoder.encode(postLogoutRedirectUri, StandardCharsets.UTF_8));
        }

        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl.toString()));
    }
}
