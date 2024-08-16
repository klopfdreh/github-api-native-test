package github.api.nat.test.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "github.api.nat")
@Data
public class GitHubApiNativeProperties {

    private String username;

    private String token;

    private String repository;
}
