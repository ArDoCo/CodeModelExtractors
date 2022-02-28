/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jan Keim
 *
 */
public class JavaCodeModelExtractor {
    private static final Logger logger = LogManager.getLogger(JavaCodeModelExtractor.class);

    private static final String CMD_HELP = "h";
    private static final String CMD_IN_DIR = "i";
    private static final String CMD_OUT_DIR = "o";

    private static Options options;

    private JavaCodeModelExtractor() {
        throw new IllegalAccessError();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CommandLine cmd = null;
        try {
            cmd = parseCommandLine(args);
        } catch (IllegalArgumentException | ParseException e) {
            logger.error(e.getMessage());
            printUsage();

            return;
        }

        if (cmd.hasOption(CMD_HELP)) {
            printUsage();
            return;
        }

        Path inputDir = null;
        Path outputDir = null;
        try {
            inputDir = ensureDir(cmd.getOptionValue(CMD_IN_DIR), true);
            outputDir = ensureDir(cmd.getOptionValue(CMD_OUT_DIR), true);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e.getCause());
            return;
        }

        runExtraction(inputDir, outputDir);
    }

    private static void printUsage() {
        var formatter = new HelpFormatter();
        formatter.printHelp("JavaCodeModelExtractor.jar", options);
    }

    private static void runExtraction(Path startingDir, Path outputDir) {
        var javaFileVisitor = new JavaFileVisitor();
        // walk all files and run the JavaFileVisitor
        try {
            Files.walkFileTree(startingDir, javaFileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // afterwards, process information and save them
        processAndSaveInformation(javaFileVisitor);
    }

    private static void processAndSaveInformation(JavaFileVisitor javaFileVisitor) {
        javaFileVisitor.getClassNames().forEach(System.out::println);
        System.out.println();
        var clazz = javaFileVisitor.getClasses().select(m -> m.getName().equals("LoopHelper")).getFirst();
        clazz.getConstructors().forEach(c -> {
            System.out.println(c.getContainer().getFullyQualifiedName());
            System.out.println(c.getJavadocContent());
        });
        clazz.getAllMethods().forEach(m -> {
            System.out.println(m.getContainer().getFullyQualifiedName());
            System.out.println(m.getName());
            System.out.println(m.getType());
            System.out.println(m.getJavadocContent());
            System.out.println();
        });
    }

    private static CommandLine parseCommandLine(String[] args) throws ParseException {
        options = new Options();
        Option opt;

        // Define Options ..
        opt = new Option(CMD_HELP, "help", false, "print this message");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option(CMD_IN_DIR, "in", true, "path to the input directory");
        opt.setRequired(true);
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(CMD_OUT_DIR, "out", true, "path to the output directory");
        opt.setRequired(true);
        opt.setType(String.class);
        options.addOption(opt);

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);

    }

    /**
     * Ensure that a directory exists (or create if allowed by parameter).
     *
     * @param path   the path to the file
     * @param create indicates whether creation is allowed
     * @return the file
     * @throws IOException if something went wrong
     */
    private static Path ensureDir(String path, boolean create) throws IOException {
        var file = new File(path);
        if (file.isDirectory() && file.exists()) {
            return Paths.get(file.toURI());
        }
        if (create) {
            file.mkdirs();
            return Paths.get(file.toURI());
        }

        // File not available
        throw new IOException("The specified directory does not exist: " + path);
    }

}
