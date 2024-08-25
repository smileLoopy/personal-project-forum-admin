package com.personal.projectforumadmin.dto.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Admin Project Properties
 *
 * @param forum forum related property
 */
@ConfigurationProperties("project")
public record ProjectProperties(Forum forum) {

    /**
     * Forum related properties
     *
     * @param url forum service host name
     */
    public record Forum(String url) {}
}
