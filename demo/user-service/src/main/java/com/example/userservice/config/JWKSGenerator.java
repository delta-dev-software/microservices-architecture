package com.example.userservice.config;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

public class JWKSGenerator {

    public static void main(String[] args) throws Exception {
        // Generate RSA Key Pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Create RSAKey object for public key
        RSAKey rsaPublicKey = new RSAKey.Builder((RSAPublicKey) publicKey)
                .keyID("your-public-key-id") // Use a unique key ID
                .build();

        // Create JWKSet with the public RSA key
        JWKSet jwkSet = new JWKSet(Collections.singletonList((JWK) rsaPublicKey));

        // Convert JWKSet to JSON string
        String jwksJson = jwkSet.toJSONObject().toString();

        // Save JWKS to .well-known/jwks.json
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\Pc-asus\\Downloads\\microservices-architecture\\demo\\user-service\\src\\main\\resources\\.well-known/jwks.json")) {
            fileWriter.write(jwksJson);
        }

        // Save private key to PEM file
        try (FileWriter privateKeyFileWriter = new FileWriter("C:\\Users\\Pc-asus\\Downloads\\microservices-architecture\\demo\\user-service\\src\\main\\resources\\private_key.pem")) {
            privateKeyFileWriter.write("-----BEGIN PRIVATE KEY-----\n");
            privateKeyFileWriter.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded()));
            privateKeyFileWriter.write("\n-----END PRIVATE KEY-----");
        }

        // Save public key to PEM file
        try (FileWriter publicKeyFileWriter = new FileWriter("C:\\Users\\Pc-asus\\Downloads\\microservices-architecture\\demo\\user-service\\src\\main\\resources\\public_key.pem")) {
            publicKeyFileWriter.write("-----BEGIN PUBLIC KEY-----\n");
            publicKeyFileWriter.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()));
            publicKeyFileWriter.write("\n-----END PUBLIC KEY-----");
        }
    }
}
