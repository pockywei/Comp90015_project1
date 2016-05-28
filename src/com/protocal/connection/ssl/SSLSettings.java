package com.protocal.connection.ssl;

import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLSettings {

    private static final String key = "server_key.jks";
    private static final String protocol = "SSL";
    private static final String password = "123456";

    public static void initClientSSL() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(SSLSettings.class.getResourceAsStream(key),
                null);

        TrustManagerFactory trustFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustStore);
        
        TrustManager[] trustManagers = trustFactory.getTrustManagers();
        defaultSSL(null, trustManagers);
    }

    public static void initServerSSL() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] keyPassword = password.toCharArray();
        keyStore.load(SSLSettings.class.getResourceAsStream(key),
                keyPassword);

        KeyManagerFactory keyFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(keyStore, keyPassword);

        KeyManager[] keyManagers = keyFactory.getKeyManagers();
        defaultSSL(keyManagers, null);
    }

    private static void defaultSSL(KeyManager[] km, TrustManager[] tm)
            throws Exception {
        SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(km, tm, null);
        SSLContext.setDefault(sslContext);
    }
}
