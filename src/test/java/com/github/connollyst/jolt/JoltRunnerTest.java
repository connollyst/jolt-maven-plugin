package com.github.connollyst.jolt;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sean Connolly
 */
public class JoltRunnerTest {

    private static final Log LOG = SLF4JMavenLogger.get(JoltCrawlerTest.class);

    private static final boolean DO_MINIFY = true;
    private static final boolean DONT_MINIFY = false;

    @Rule
    public TemporaryFolder tempDirectory = new TemporaryFolder();

    @Test
    public void shouldTransformDirectoryTree() throws IOException, MojoExecutionException {
        // Given
        String specFile = getClass().getResource("/json/input/spec.json").getPath();
        String inputDirectory = getClass().getResource("/json/input/source").getPath();
        String outputDirectory = tempDirectory.newFolder().getAbsolutePath();
        JoltRunner runner = new JoltRunner(LOG, specFile, inputDirectory, outputDirectory, DO_MINIFY);
        // When
        runner.execute();
        // Then
        assertThat(Files.exists(Paths.get(outputDirectory).resolve("a.json"))).isTrue();
        assertThat(Files.exists(Paths.get(outputDirectory).resolve("b.json"))).isTrue();
        assertThat(Files.exists(Paths.get(outputDirectory).resolve("subdirectory/c.json"))).isTrue();
        assertThat(Files.exists(Paths.get(outputDirectory).resolve("subdirectory/deep-directory/d.json"))).isTrue();
    }

}