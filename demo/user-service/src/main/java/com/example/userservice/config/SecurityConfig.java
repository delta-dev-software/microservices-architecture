
package com.example.userservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;


import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register","/api/users/.well-known/jwks.json","/api/users/login")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt -> {
                                    try {
                                        jwt.decoder(jwtDecoder());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                })  // Configure the JWT decoder here
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        // Assuming you have configured your JWKSet as shown earlier
        JWKSet jwkSet = JWKSet.load(new ClassPathResource(".well-known/jwks.json").getInputStream());
        RSAKey rsaKey = (RSAKey) jwkSet.getKeys().stream()
                .filter(key -> key instanceof RSAKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No RSA key found in JWKS"));
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        PrivateKey privateKey = KeyUtils.loadPrivateKey("C:\\Users\\Pc-asus\\Downloads\\microservices-architecture\\demo\\user-service\\src\\main\\resources\\private_key.pem");

        PublicKey publicKey = KeyUtils.getRSAPublicKeyFromPEM("C:\\Users\\Pc-asus\\Downloads\\microservices-architecture\\demo\\user-service\\src\\main\\resources\\public_key.pem");

        // Create an RSAKey object for the public and private key
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) publicKey)
                .privateKey(privateKey)
                .keyID("your-key-id") // Use a unique key ID
                .build();

        // Create JWKSet with the RSA key
        JWKSet jwkSet = new JWKSet(Collections.singletonList(rsaKey));

        // Create JWKSource from the loaded JWKS
        JWKSource<SecurityContext> jwkSource = (jwkSelector, context) -> {
            // Select the RSA key from the JWKSet
            List<JWK> selectedJWKs = jwkSelector.select(jwkSet);
            return selectedJWKs.stream()
                    .filter(JWK::isPrivate)  // Ensure the key is private
                    .collect(Collectors.toList());
        };

        return new NimbusJwtEncoder(jwkSource);
    }




    @Bean
    @Scope("singleton")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

