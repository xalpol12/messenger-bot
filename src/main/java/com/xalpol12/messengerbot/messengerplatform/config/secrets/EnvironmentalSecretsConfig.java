package com.xalpol12.messengerbot.messengerplatform.config.secrets;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Retrieves secrets using environmental variables.
 * Configured using docker.compose file.
 */
@Component
@Profile("docker")
public class EnvironmentalSecretsConfig implements SecretsConfig {

    private final String verificationToken;
    private final String secretKey;

    public EnvironmentalSecretsConfig(Environment env) {
        verificationToken = env.getProperty("MESSENGER_APP_VERIFICATION_TOKEN");
        secretKey = env.getProperty("MESSENGER_APP_SECRET_KEY");
    }

    @Override
    public String getVerificationToken() {
        return verificationToken;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }
}
