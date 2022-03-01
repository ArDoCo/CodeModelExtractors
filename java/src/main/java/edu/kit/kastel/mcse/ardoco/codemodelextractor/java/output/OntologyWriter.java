/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.output;

import java.io.File;
import java.nio.file.Path;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.kit.kastel.informalin.ontology.OntologyConnector;
import edu.kit.kastel.informalin.ontology.OntologyInterface;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.CodeComment;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaClassOrInterface;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaMethod;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaProject;

/**
 * @author Jan Keim
 *
 */
// TODO ontology and writer currently do not map methods and comments to class etc.
public class OntologyWriter {
    private static final Logger logger = LogManager.getLogger(OntologyWriter.class);

    private static final String JAVA_BASE_OWL = "https://informalin.github.io/knowledgebases/informalin_base_java.owl";
    private static final String DEFAULT_NAME_SPACE_URI = "https://informalin.github.io/knowledgebases/examples/java-example.owl#";

    private OntClass classOrInterfaceOntClass;
    private OntClass methodOntClass;
    private OntClass codeCommentOntClass;
    private DatatypeProperty javadocContentProperty;
    private DatatypeProperty typeProperty;
    private DatatypeProperty textProperty;
    private DatatypeProperty nameProperty;
    private DatatypeProperty fqnProperty;

    private OntologyInterface ontology = null;
    private Path outputPath;

    public OntologyWriter(Path outputPath) {
        this.outputPath = outputPath;
        this.ontology = OntologyConnector.createWithEmptyOntology(DEFAULT_NAME_SPACE_URI);
    }

    private void init() {
        if (ontology == null) {
            ontology = OntologyConnector.createWithEmptyOntology(DEFAULT_NAME_SPACE_URI);
        }

        ontology.addOntologyImport(JAVA_BASE_OWL);
        classOrInterfaceOntClass = ontology
                .getClassByIri("https://informalin.github.io/knowledgebases/informalin_base_java.owl#OWLClass_5c834f48_ae0d_40d8_8ea1_c193dc511593")
                .orElseThrow();
        methodOntClass = ontology
                .getClassByIri("https://informalin.github.io/knowledgebases/informalin_base_java.owl#OWLClass_baf2a951_c26b_479c_8995_f1439a95aa2f")
                .orElseThrow();
        codeCommentOntClass = ontology
                .getClassByIri("https://informalin.github.io/knowledgebases/informalin_base_java.owl#OWLClass_fcaa349b_0dde_4b4d_bab3_7971faad212a")
                .orElseThrow();
        javadocContentProperty = ontology.getDataProperty("javadocContent").orElseThrow();
        typeProperty = ontology.getDataProperty("type").orElseThrow();
        textProperty = ontology.getDataProperty("text").orElseThrow();
        nameProperty = ontology.getDataProperty("name").orElseThrow();
        fqnProperty = ontology.getDataProperty("fullyQualifiedName").orElseThrow();
    }

    public void write(JavaProject project) {
        init();

        for (var classOrInterface : project.getClassesAndInterfaces()) {
            writeClassOrInterface(classOrInterface);
        }

        var fileString = outputPath.toAbsolutePath() + File.separator + "output.owl";
        ontology.save(fileString);
    }

    private void writeClassOrInterface(JavaClassOrInterface classOrInterface) {
        // create Individual
        var classOrInterfaceName = classOrInterface.getName();
        var individual = ontology.addIndividualToClass(classOrInterfaceName, classOrInterfaceOntClass);

        // add data properties (name, fqn)
        ontology.addPropertyToIndividual(individual, nameProperty, classOrInterfaceName);
        ontology.addPropertyToIndividual(individual, fqnProperty, classOrInterface.getFullyQualifiedName());

        // go over methods
        for (var method : classOrInterface.getAllMethods()) {
            writeMethod(method);
        }

        // go over comments
        for (var comment : classOrInterface.getAllComments()) {
            writeComment(comment, classOrInterface);
        }
    }

    private void writeMethod(JavaMethod method) {
        // create Individual
        var methodName = method.getName();
        var individual = ontology.addIndividualToClass(methodName, methodOntClass);

        // add data properties (name, type, javadocContent)
        ontology.addPropertyToIndividual(individual, nameProperty, methodName);
        ontology.addPropertyToIndividual(individual, typeProperty, method.getType());
        var javadocContent = method.getJavadocContent();
        if (javadocContent != null) {
            ontology.addPropertyToIndividual(individual, javadocContentProperty, javadocContent);
        }

    }

    private void writeComment(CodeComment comment, JavaClassOrInterface parent) {
        // create Individual
        var name = "comment_" + parent.getName() + "_L" + comment.getLineNumber();
        var individual = ontology.addIndividualToClass(name, codeCommentOntClass);

        // add data property (text)
        ontology.addPropertyToIndividual(individual, textProperty, comment.getText());
    }

}
