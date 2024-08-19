package github.api.nat.test.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitHubRuntimeHints implements RuntimeHintsRegistrar {

    private static final Logger LOGGER = Logger.getLogger(GitHubRuntimeHints.class.getName());

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator)).forEach(classpathEntry -> {
            // If the classpathEntry is no jar skip it
            if (!classpathEntry.endsWith(".jar")) {
                return;
            }

            try (JarInputStream is = new JarInputStream(new FileInputStream(classpathEntry))) {
                JarEntry entry;
                while ((entry = is.getNextJarEntry()) != null) {
                    if (entry.getName().endsWith(".class") && entry.getName().startsWith("org/kohsuke/github")) {
                        String githubApiClassName = entry.getName().replace("/", ".");
                        String githubApiClassNameWithoutClass = githubApiClassName.substring(0, githubApiClassName.length() - 6);
                        LOGGER.log(Level.INFO, "Registered class " + githubApiClassNameWithoutClass + " for reflections and serialization.");
                        hints.reflection().registerType(TypeReference.of(githubApiClassNameWithoutClass), MemberCategory.values());
                        hints.serialization().registerType(TypeReference.of(githubApiClassName));
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.INFO, "Error while reading jars", e);
            }
        });
    }
}
