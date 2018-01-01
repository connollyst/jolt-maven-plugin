package com.github.connollyst.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Sean Connolly
 */
@Mojo(name = "transform")
public class JoltTransformMojo extends AbstractMojo {

    @Parameter(property = "input")
    private String inputFile;
    @Parameter(property = "spec")
    private String specFile;
    @Parameter(property = "output", defaultValue = "target/jolt-output.json")
    private String outputFile;

    private final ObjectMapper mapper = new ObjectMapper();

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Transforming " + inputFile + " using " + specFile + "..");
        Map<String, Object> input = readInput(inputFile);
        Chainr spec = readSpec(specFile);
        Object output = spec.transform(input);
        writeOutput(output);
    }

    private Chainr readSpec(String path) throws MojoExecutionException {
        return Chainr.fromSpec(readInput(path));
    }

    private Map<String, Object> readInput(String path) throws MojoExecutionException {
        try {
            return mapper.readValue(new File(
                    path), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException ioe) {
            throw new MojoExecutionException("...", ioe);
        }
    }

    private void writeOutput(Object output) throws MojoExecutionException {
        try {
            mapper.writeValue(new File("result.json"), output);
        } catch (IOException ioe) {
            throw new MojoExecutionException("...", ioe);
        }
    }

}