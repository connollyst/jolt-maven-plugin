package com.github.connollyst.jolt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sean Connolly
 */
@Mojo(name = "transform")
public class JoltTransformMojo extends AbstractMojo {

    @Parameter(property = "source")
    private String source;
    @Parameter(property = "spec")
    private String spec;
    @Parameter(property = "result", defaultValue = "/target/jolt-result.json")
    private String result;
    @Parameter(property = "minify", defaultValue = "false")
    private boolean minify;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private final ObjectMapper mapper = new ObjectMapper();

    public void execute() throws MojoExecutionException, MojoFailureException {
        Path specFile = asInputFile(spec);
        Path sourceFile = asInputFile(source);
        Path resultFile = asOutputFile(result);
        new JoltTransformer(getLog(), mapper, getWriter(), specFile, sourceFile, resultFile).execute();
    }

    private ObjectWriter getWriter() {
        if (minify) {
            return mapper.writer();
        } else {
            return mapper.writerWithDefaultPrettyPrinter();
        }
    }

    private Path asInputFile(String filePath) throws MojoExecutionException {
        try {
            return Paths.get(getClass().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new MojoExecutionException("", e);
        }
    }

    private Path asOutputFile(String filePath) throws MojoExecutionException {
        try {
            return Paths.get(getClass().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new MojoExecutionException("", e);
        }
    }

    public String getSource() {
        return source;
    }

    public String getSpec() {
        return spec;
    }

    public String getResult() {
        return result;
    }

    public void setMinify(boolean minify) {
        this.minify = minify;
    }

}