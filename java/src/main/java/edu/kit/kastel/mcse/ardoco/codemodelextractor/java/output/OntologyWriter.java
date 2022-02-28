/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.output;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.kit.kastel.informalin.ontology.OntologyConnector;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaClassOrInterface;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaProject;

/**
 * @author Jan Keim
 *
 */
public class OntologyWriter {
    private static final Logger logger = LogManager.getLogger(OntologyWriter.class);
    private OntologyConnector ontologyConnector;
    private Path outputPath;

    public OntologyWriter(Path outputPath) {
        this.outputPath = outputPath;
        // this.ontologyConnector = new OntologyConnector("TODO");
    }

    public void write(JavaProject project) {
        for (var classOrInterface : project.getClassesAndInterfaces()) {
            writeClassOrInterface(classOrInterface);
        }
    }

    private void writeClassOrInterface(JavaClassOrInterface classOrInterface) {
        logger.info("Writing " + classOrInterface.toString());
        // TODO
    }

}
