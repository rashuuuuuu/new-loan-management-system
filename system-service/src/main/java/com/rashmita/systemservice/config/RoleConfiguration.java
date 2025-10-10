package com.rashmita.systemservice.config;

import com.rashmita.systemservice.model.RoleDto;
import com.rashmita.systemservice.util.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "data-init")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:roles.yml")
public class RoleConfiguration {
    private List<RoleDto> rolesList;

}
