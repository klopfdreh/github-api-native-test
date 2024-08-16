package github.api.nat.test;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GitHubApiConnect {
    private final GitHubApiNativeProperties gitHubApiNativeProperties;

    public GitHubApiConnect(GitHubApiNativeProperties gitHubApiNativeProperties) {
        this.gitHubApiNativeProperties = gitHubApiNativeProperties;
    }

    @PostConstruct
    public void connectToGitHub() throws IOException {
        GitHub github = new GitHubBuilder().withPassword(gitHubApiNativeProperties.getUsername(), gitHubApiNativeProperties.getToken()).build();
        GHRepository repository = github.getRepository(gitHubApiNativeProperties.getRepository());
        GHBranch main = repository.getBranch("main");
        log.debug(main.toString());
    }
}
