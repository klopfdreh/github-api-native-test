package github.api.nat.test;

import jakarta.annotation.PostConstruct;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubApiConnect {


    GitHubApiNativeProperties gitHubApiNativeProperties;

    @PostConstruct
    public void connectToGitHub() throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(gitHubApiNativeProperties.getToken()).build();
        GHRepository repository = github.getRepository("");
        GHBranch main = repository.getBranch("main");
    }
}
