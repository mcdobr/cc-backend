package me.mircea.cc.backend.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebFluxConfiguration implements WebFluxConfigurer {

    @Bean
    public NimbusReactiveOpaqueTokenIntrospector nimbusReactiveOpaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
        OAuth2ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
        return new NimbusReactiveOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(),
                opaqueToken.getClientId(), opaqueToken.getClientSecret());
    }
}
