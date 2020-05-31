package me.mircea.cc.backend.config;

import com.nimbusds.oauth2.sdk.TokenIntrospectionResponse;
import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.AUDIENCE;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.CLIENT_ID;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.EXPIRES_AT;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.ISSUED_AT;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.ISSUER;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.NOT_BEFORE;
import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.SCOPE;

/**
 * Google outright contradicts the OAuth2 standard recommendations of keeping the ID token only for the client.
 * While validating the JWT signature would have been no doubt easier it is against the standard. Also Google
 * does not implement token introspection [RFC 7662]. But it does however, offer an endpoint to validate the access
 * token so that is what i've used and hacked around a bit to know that an access token is still valid.
 */
public class GoogleHackIntrospector implements ReactiveOpaqueTokenIntrospector {
    private URI introspectionUri;
    private WebClient webClient;

    private String authorityPrefix = "SCOPE_";

    /**
     * Creates a {@code OpaqueTokenReactiveAuthenticationManager} with the provided parameters
     *
     * @param introspectionUri The introspection endpoint uri
     * @param clientId         The client id authorized to introspect
     * @param clientSecret     The client secret for the authorized client
     */
    public GoogleHackIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be empty");
        Assert.hasText(clientId, "clientId cannot be empty");
        Assert.notNull(clientSecret, "clientSecret cannot be null");

        this.introspectionUri = URI.create(introspectionUri);
        this.webClient = WebClient.builder()
                .defaultHeaders(h -> h.setBasicAuth(clientId, clientSecret))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return Mono.just(token)
                .flatMap(this::makeRequest)
                .flatMap(this::adaptToNimbusResponse)
                .map(this::parseNimbusResponse)
                .map(this::castToNimbusSuccess)
                .doOnNext(response -> validate(token, response))
                .map(this::convertClaimsSet)
                .onErrorMap(e -> !(e instanceof OAuth2IntrospectionException), this::onError);
    }

    private Mono<ClientResponse> makeRequest(String token) {
        return this.webClient.post()
                .uri(this.introspectionUri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(BodyInserters.fromFormData("access_token", token))
                .exchange();
    }

    private Mono<HTTPResponse> adaptToNimbusResponse(ClientResponse responseEntity) {
        HTTPResponse response = new HTTPResponse(responseEntity.rawStatusCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, responseEntity.headers().contentType().get().toString());
        if (response.getStatusCode() != HTTPResponse.SC_OK) {
            return responseEntity.bodyToFlux(DataBuffer.class)
                    .map(DataBufferUtils::release)
                    .then(Mono.error(new OAuth2IntrospectionException(
                            "Introspection endpoint responded with " + response.getStatusCode())));
        } else {
            // Add active to fool next steps
            return responseEntity.bodyToMono(String.class)
                    .map(str -> {
                        JSONParser jsonParser = new JSONParser();
                        try {
                            JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
                            jsonObject.put("active", true);
                            return jsonObject.toJSONString();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .doOnNext(response::setContent)
                    .map(body -> response);
        }

    }

    private TokenIntrospectionResponse parseNimbusResponse(HTTPResponse response) {
        try {
            return TokenIntrospectionResponse.parse(response);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private TokenIntrospectionSuccessResponse castToNimbusSuccess(TokenIntrospectionResponse introspectionResponse) {
        if (!introspectionResponse.indicatesSuccess()) {
            throw new OAuth2IntrospectionException("Token introspection failed");
        }
        return (TokenIntrospectionSuccessResponse) introspectionResponse;
    }

    private void validate(String token, TokenIntrospectionSuccessResponse response) {
        // relying solely on the authorization server to validate this token (not checking 'exp', for example)
        if (!response.isActive()) {
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
    }

    private OAuth2AuthenticatedPrincipal convertClaimsSet(TokenIntrospectionSuccessResponse response) {
        Map<String, Object> claims = response.toJSONObject();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (response.getAudience() != null) {
            List<String> audiences = new ArrayList<>();
            for (Audience audience : response.getAudience()) {
                audiences.add(audience.getValue());
            }
            claims.put(AUDIENCE, Collections.unmodifiableList(audiences));
        }
        if (response.getClientID() != null) {
            claims.put(CLIENT_ID, response.getClientID().getValue());
        }
        if (response.getExpirationTime() != null) {
            Instant exp = response.getExpirationTime().toInstant();
            claims.put(EXPIRES_AT, exp);
        }
        if (response.getIssueTime() != null) {
            Instant iat = response.getIssueTime().toInstant();
            claims.put(ISSUED_AT, iat);
        }
        if (response.getIssuer() != null) {
            claims.put(ISSUER, issuer(response.getIssuer().getValue()));
        }
        if (response.getNotBeforeTime() != null) {
            claims.put(NOT_BEFORE, response.getNotBeforeTime().toInstant());
        }
        if (response.getScope() != null) {
            List<String> scopes = Collections.unmodifiableList(response.getScope().toStringList());
            claims.put(SCOPE, scopes);

            for (String scope : scopes) {
                authorities.add(new SimpleGrantedAuthority(this.authorityPrefix + scope));
            }
        }

        return new DefaultOAuth2AuthenticatedPrincipal(claims, authorities);
    }

    private URL issuer(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException("Invalid " + ISSUER + " value: " + uri);
        }
    }

    private OAuth2IntrospectionException onError(Throwable e) {
        return new OAuth2IntrospectionException(e.getMessage(), e);
    }
}
