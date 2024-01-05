package com.xalpol12.messengerbot.messengerplatform.config.secrets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Retrieves secrets using .properties file, used
 * in local development mode.
 */
@Component
@Profile("!docker")
public class LocalSecretsConfig implements SecretsConfig {

    @Value("${messenger.app.verification.token:}")
    private String verificationToken;

    @Value("${messenger.app.secret.key:}")
    private String secretKey;

    @Override
    public String getVerificationToken() {
        return verificationToken;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }
}
