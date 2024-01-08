package com.xalpol12.messengerbot.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookVariables {

    @Value("${facebook.api.version}")
    private String API_VERSION;

    @Value("${facebook.page.id}")
    private String PAGE_ID; //TODO: add support for multiple facebook pages

    public String getApiVersion() {
        return API_VERSION;
    }

    public String getPageId() {
        return PAGE_ID;
    }
}
