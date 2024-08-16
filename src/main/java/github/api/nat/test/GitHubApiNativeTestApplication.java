package github.api.nat.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GitHubApiNativeTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GitHubApiNativeTestApplication.class).run(args);
    }
}
