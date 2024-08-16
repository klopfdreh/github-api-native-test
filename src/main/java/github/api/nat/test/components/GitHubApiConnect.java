package github.api.nat.test.components;

import github.api.nat.test.configs.GitHubApiNativeProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

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
        log.info(main.toString());
        GHContent origin = repository.getFileContent("README.md", "main");
        log.info(IOUtils.toString(origin.read(), Charset.defaultCharset()));
    }
}
