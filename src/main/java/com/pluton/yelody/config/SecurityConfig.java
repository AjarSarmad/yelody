//package com.pluton.yelody.config;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @SuppressWarnings("deprecation")
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            // your security config here
//            .authorizeRequests()
//            .requestMatchers("/yelody/**").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            ;
//        return http.build();
//    }
//    @Bean
//    @ConditionalOnProperty(prefix = "authentication", name = "enabled", havingValue = "false")
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration dummyRegistration = ClientRegistration.withRegistrationId("dummy")
//                .clientId("google-client-id")
//                .clientSecret("google-client-secret")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("openid")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .build();
//        return new InMemoryClientRegistrationRepository(dummyRegistration);
//    }
//}
