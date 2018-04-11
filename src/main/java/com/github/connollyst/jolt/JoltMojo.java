package com.github.connollyst.jolt;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Sean Connolly
 */
@Mojo(name = "transform")
public class JoltMojo extends AbstractMojo {

    @Parameter(property = "jolt.transform.specFile", defaultValue = "${project.basedir}/src/main/resources/jolt.json/")
    private String specFile;
    @Parameter(property = "jolt.transform.inputDirectory", defaultValue = "${project.basedir}/src/main/resources/json/")
    private String inputDirectory;
    @Parameter(property = "jolt.transform.outputDirectory", defaultValue = "${project.build.directory}/json/")
    private String outputDirectory;
    @Parameter(property = "jolt.transform.minify", defaultValue = "false")
    private boolean minify;

    public void execute() throws MojoExecutionException, MojoFailureException {
        JoltRunner jolt = new JoltRunner(getLog(), specFile, inputDirectory, outputDirectory, minify);
        jolt.verify();
        jolt.execute();
    }

}