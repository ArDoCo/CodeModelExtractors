/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import com.github.javaparser.StaticJavaParser;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaClassOrInterface;

/**
 * @author Jan Keim
 *
 */
public class JavaFileVisitor implements FileVisitor<Path> {
    private static final String JAVA_FILE_ENDING = ".java";
    private MutableList<JavaClassOrInterface> classes = Lists.mutable.empty();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        var fileName = file.getFileName().toString();
        if (fileName.endsWith(JAVA_FILE_ENDING)) {
            new ClassOrInterfaceVisitor().visit(StaticJavaParser.parse(file), classes);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * @return the classNames
     */
    public MutableList<String> getClassNames() {
        return classes.collect(JavaClassOrInterface::getFullyQualifiedName);
    }

    /**
     * @return the classes
     */
    public MutableList<JavaClassOrInterface> getClasses() {
        return classes;
    }

}
