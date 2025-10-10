package com.rashmita.common.config;

import com.rashmita.common.model.SuperAdminDto;
import com.rashmita.common.util.YamlPropertySourceFactory;
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
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:superAdmin.yml")
public class SuperAdminConfiguration {
    private List<SuperAdminDto> superAdmins;

    public void setSuperAdmins(List<SuperAdminDto> superAdmins) {
        this.superAdmins = superAdmins;
    }

}
