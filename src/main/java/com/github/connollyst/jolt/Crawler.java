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
    private final JoltTransformer transformer;

    Crawler(Log log, JoltTransformer transformer) {
        this.log = log;
        this.transformer = transformer;
    }

    @Override
    public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes sourceAttributes) throws IOException {
        log.info("Processing " + sourceFile);
        transformer.execute(sourceFile);
        return super.visitFile(sourceFile, sourceAttributes);
    }

}
