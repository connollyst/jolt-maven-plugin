package com.github.connollyst.jolt;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

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
    public void should() throws IOException, MojoExecutionException {
        // Given
        String specFile = "/json/input/spec.json";
        String inputDirectory = "/json/input/source";
        String outputDirectory = tempDirectory.newFolder().getAbsolutePath();
        JoltRunner runner = new JoltRunner(LOG, specFile, inputDirectory, outputDirectory, DO_MINIFY);
        // When
        runner.execute();
        // Then
    }

}