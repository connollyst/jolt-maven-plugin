package com.github.connollyst.jolt;

import org.apache.maven.plugin.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.verify;

/**
 * Test cases for the {@link JoltCrawler}.
 *
 * @author Sean Connolly
 */
@RunWith(MockitoJUnitRunner.class)
public class JoltCrawlerTest {

    private static final Log LOG = SLF4JMavenLogger.get(JoltCrawlerTest.class);

    @Mock
    private JoltTransformer transformer;

    @Rule
    public TemporaryFolder tempDirectory = new TemporaryFolder();

    @Test
    public void should() throws URISyntaxException, IOException {
        // Given
        Path inputDirectory = getTestResource("/json/input/source");
        Path outputDirectory = tempDirectory.newFolder().toPath();
        JoltCrawler crawler = new JoltCrawler(LOG, inputDirectory, outputDirectory, transformer);
        // When
        Files.walkFileTree(inputDirectory, crawler);
        // Then
        verify(transformer).execute(
                getTestResource("/json/input/source/a.json"),
                outputDirectory.resolve("a.json"));
        verify(transformer).execute(
                getTestResource("/json/input/source/b.json"),
                outputDirectory.resolve("b.json"));
        verify(transformer).execute(
                getTestResource("/json/input/source/subdirectory/c.json"),
                outputDirectory.resolve("subdirectory/c.json"));
        verify(transformer).execute(
                getTestResource("/json/input/source/subdirectory/deep-directory/d.json"),
                outputDirectory.resolve("subdirectory/deep-directory/d.json"));
    }

    /* UTILITIES */

    private Path getTestResource(String path) throws URISyntaxException {
        return Paths.get(getClass().getResource(path).toURI());
    }

}
