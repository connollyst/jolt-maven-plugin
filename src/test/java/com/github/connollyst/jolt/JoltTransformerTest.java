package com.github.connollyst.jolt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JoltTransformMojo}.
 *
 * @author Sean Connolly
 */
@RunWith(MockitoJUnitRunner.class)
public class JoltTransformerTest {

    @Mock
    private Log log;
    private ObjectMapper mapper = new ObjectMapper();
    private ObjectMapper reader = mapper;
    private ObjectWriter writer = mapper.writer();

    @Test
    public void should() throws URISyntaxException, IOException, MojoExecutionException {
        // Given
        Path input = getTestResource("/input/source/a.json");
        Path spec = getTestResource("/input/spec/a.json");
        Path output = Files.createTempFile("jolt-maven-plugin-junit-", ".json");
        JoltTransformer transformer = new JoltTransformer(log, reader, writer, spec, input, output);
        // When
        Path actual = transformer.execute();
        // Then
        assertThat(actual).isEqualTo(actual);
        assertThat(Files.exists(actual)).isTrue();
    }

    /* UTILITIES */

    private Path getTestResource(String path) throws URISyntaxException {
        return Paths.get(getClass().getResource(path).toURI());
    }


}