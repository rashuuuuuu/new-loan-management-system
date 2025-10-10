package com.rashmita.systemservice.config;

import com.rashmita.systemservice.model.AccessGroupDto;
import com.rashmita.systemservice.util.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Configuration class to map accessGroups.yml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "data-init")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:accessGroup.yml")
public class AccessGroupsConfiguration {
    private List<AccessGroupDto> accessGroupsList;

}