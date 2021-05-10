package me.mircea.cc.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
public class SecurityConfiguration {
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable()
                .cors().and()
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt()
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.POST);
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.PATCH);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);

        corsConfig.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        corsConfig.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
        corsConfig.addAllowedHeader(HttpHeaders.CACHE_CONTROL);

        corsConfig.setMaxAge(Duration.ofMinutes(5));
        corsConfig.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthoritiesConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    /**
     * @implNote I am using the global roles for the entire realm, you may choose to use roles for a specific client.
     * I am also adding scopes because you might want logic that the request comes from a client which was allowed a
     * certain scope AND from a user which has a certain role.
     */
    private static class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt source) {
            Map<String, Object> realmAccess = Objects.requireNonNullElseGet(source.getClaimAsMap("realm_access"),
                    Collections::emptyMap);
            List<String> roles = (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());
            List<String> scopes = Objects.requireNonNullElseGet(source.getClaimAsStringList("scope"),
                    Collections::emptyList);

            return Stream.concat(
                    roles.stream()
                        .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName)),
                    scopes.stream()
                        .map(scopeName -> new SimpleGrantedAuthority("SCOPE_" + scopeName))
            ).collect(Collectors.toList());
        }
    }
}
