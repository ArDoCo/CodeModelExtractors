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

import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaProject;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.output.OntologyWriter;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.visitors.JavaFileVisitor;

/**
 * @author Jan Keim
 *
 */
public class JavaCodeModelExtractor {
    private static final Logger logger = LogManager.getLogger(JavaCodeModelExtractor.class);

    private static final String CMD_HELP = "h";
    private static final String CMD_IN_DIR = "i";
    private static final String CMD_OUT_DIR = "o";
    private static final String CMD_IN_OWL = "e";

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
        File outputFile = null;
        File extendFile = null;
        try {
            inputDir = ensureDir(cmd.getOptionValue(CMD_IN_DIR), true);
            outputFile = new File(cmd.getOptionValue(CMD_OUT_DIR));
            extendFile = new File(cmd.getOptionValue(CMD_IN_OWL));
        } catch (IOException e) {
            logger.warn(e.getMessage(), e.getCause());
            return;
        }

        runExtraction(inputDir, outputFile, extendFile);
    }

    private static void runExtraction(Path startingDir, File outputFile, File extendFile) {
        logger.info("Start extracting \"{}\".", startingDir);
        var javaFileVisitor = new JavaFileVisitor();
        // walk all files and run the JavaFileVisitor
        try {
            Files.walkFileTree(startingDir, javaFileVisitor);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e.getCause());
        }
        // afterwards, process information and save them
        processAndSaveInformation(javaFileVisitor.getProject(), outputFile, extendFile);
    }

    private static void processAndSaveInformation(JavaProject javaFileVisitor, File outputFile, File extendFile) {
        // process
        // no process for now
        if (logger.isInfoEnabled()) {
            var numClasses = javaFileVisitor.getClassesAndInterfaces().size();
            logger.info("Extraction finished with {} extracted classes and interfaces.", numClasses);
        }

        // finally, save the information
        save(javaFileVisitor, outputFile, extendFile);
    }

    private static void save(JavaProject project, File outputFile, File extendFile) {
        logger.info("Writing to ontology");
        OntologyWriter writer;
        if (extendFile == null) {
            writer = OntologyWriter.withEmptyOntology(outputFile);
        } else {
            logger.info("Extending existing ontology at \"{}\"", extendFile);
            writer = OntologyWriter.extendExistingOntology(extendFile, outputFile);
        }

        writer.write(project);
        logger.info("Finished saving. Exiting now...");
    }

    private static void printUsage() {
        var formatter = new HelpFormatter();
        formatter.printHelp("JavaCodeModelExtractor.jar", options);
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

        opt = new Option(CMD_IN_OWL, "extend", true, "path to the owl file that should be extended (instead of creating from empty)");
        opt.setRequired(false);
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(CMD_OUT_DIR, "out", true, "path to the output file that should be used for saving");
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
