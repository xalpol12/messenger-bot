package com.xalpol12.messengerbot.messengerplatform.config.secrets;

/**
 * Used for accessing secrets regardless of the
 * actual storing method.
 */
public interface SecretsConfig {
    String getVerificationToken();
    String getSecretKey();
}
