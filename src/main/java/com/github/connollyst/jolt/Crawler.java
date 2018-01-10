package com.github.connollyst.jolt;

import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Sean Connolly
 */
class Crawler extends SimpleFileVisitor<Path> {

    private final Log log;
    private final Path inputDirectory;
    private final Path outputDirectory;
    private final JoltTransformer transformer;

    Crawler(Log log, Path inputDirectory, Path outputDirectory, JoltTransformer transformer) {
        this.log = log;
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.transformer = transformer;
    }


    @Override
    public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes sourceAttributes) throws IOException {
        log.info("Processing " + sourceFile);
        Path relativeSourcePath = inputDirectory.relativize(sourceFile);
        Path destinationPath = outputDirectory.resolve(relativeSourcePath);
        transformer.execute(sourceFile, destinationPath);
        return super.visitFile(sourceFile, sourceAttributes);
    }

}
