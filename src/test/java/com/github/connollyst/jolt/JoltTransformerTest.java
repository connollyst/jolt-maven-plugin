package com.github.connollyst.jolt;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JoltMojo}.
 *
 * @author Sean Connolly
 */
@RunWith(MockitoJUnitRunner.class)
public class JoltTransformerTest {

    private static final Log LOGGER = SLF4JMavenLogger.get(JoltTransformerTest.class);

    private static final boolean DO_MINIFY = true;
    private static final boolean DONT_MINIFY = false;

    @Test
    public void should() throws URISyntaxException, IOException {
        // Given
        Path input = getTestResource("/json/input/source/a.json");
        Path spec = getTestResource("/json/input/spec/a.json");
        Path output = Files.createTempFile("jolt-maven-plugin-junit-", ".json");
        JoltTransformer transformer = new JoltTransformer(LOGGER, DO_MINIFY, spec);
        // When
        transformer.execute(input, output);
        // Then
        assertThat(output).isEqualTo(output);
        assertThat(Files.exists(output)).isTrue();
    }

    /* UTILITIES */

    private Path getTestResource(String path) throws URISyntaxException {
        return Paths.get(getClass().getResource(path).toURI());
    }


}