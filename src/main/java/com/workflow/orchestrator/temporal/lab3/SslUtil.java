package com.workflow.orchestrator.temporal.lab3;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
// Package declaration: ye batata hai ki class kis folder/namespace mein hai.
// Isse project structure maintain hota hai.

import javax.net.ssl.SSLContext;
// SSLContext: secure TLS/SSL connections banane ke liye use hota hai.

import javax.net.ssl.KeyManagerFactory;
// KeyManagerFactory: client ke private key + certificate ko manage karta hai.

import javax.net.ssl.TrustManagerFactory;
// TrustManagerFactory: CA certificates ko manage karta hai jo server ko trust karne ke liye chahiye.

import java.io.FileInputStream;
// FileInputStream: keystore/truststore files ko disk se read karne ke liye.

import java.security.KeyStore;
// KeyStore: ek secure storage jisme certificates aur keys store hote hain.

public class SslUtil {
// Utility class jo SSLContext banane ke liye helper method provide karti hai.

    /*public static SSLContext createSslContext(String clientP12Path,
                                              String caJksPath,
                                              String password) {
        try {
            // Load client keystore (PKCS12 format)
            KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
            // PKCS12 ek format hai jisme client certificate + private key store hota hai.

            clientKeyStore.load(new FileInputStream(clientP12Path), password.toCharArray());
            // Client keystore file ko load kar rahe hain (path + password ke saath).

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            // KeyManagerFactory banaya jo client ke keys ko handle karega.

            kmf.init(clientKeyStore, password.toCharArray());
            // Client keystore ko initialize kiya with password.

            // Load CA truststore (JKS format)
            KeyStore trustStore = KeyStore.getInstance("JKS");
            // JKS ek format hai jisme CA certificates store hote hain.

            trustStore.load(new FileInputStream(caJksPath), password.toCharArray());
            // CA truststore file ko load kar rahe hain (path + password ke saath).

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            // TrustManagerFactory banaya jo CA truststore ko handle karega.

            tmf.init(trustStore);
            // Truststore ko initialize kiya.

            SSLContext sslContext = SSLContext.getInstance("TLS");
            // TLS protocol ke liye ek SSLContext banaya.

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            // SSLContext ko initialize kiya:
            // - KeyManagers (client cert + key)
            // - TrustManagers (CA certs)
            // - SecureRandom (null = default)

            return sslContext;
            // Final SSLContext return kar diya jo secure connection banane ke liye ready hai.
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSLContext", e);
            // Agar koi error aata hai to RuntimeException throw karenge.
        }
    }*/

    public static SslContext createSslContext(String clientCertPath, String caCertPath, String password) {
        try {
            // Netty SslContext build karo
            return SslContextBuilder.forClient()
                    .keyManager(new File(clientCertPath), new File(clientCertPath), password)
                    // Client certificate + private key
                    .trustManager(new File(caCertPath))
                    // CA truststore for server validation
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Netty SslContext for Temporal", e);
        }
    }
}
