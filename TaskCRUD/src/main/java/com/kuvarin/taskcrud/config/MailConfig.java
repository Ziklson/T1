package com.kuvarin.taskcrud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;
    private Properties properties = new Properties();

    public String getNoReply() {
        return properties.getProperty("no-replay");
    }

    public String getDefaultRecipient() {
        return properties.getProperty("to");
    }

}
