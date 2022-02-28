/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * @author Jan Keim
 *
 */
public class JavaMethod {

    private MethodDeclaration declaration;
    private JavaClassOrInterface container;

    public JavaMethod(MethodDeclaration declaration, JavaClassOrInterface container) {
        this.declaration = declaration;
        this.container = container;
    }

    /**
     * @return the name
     */
    public String getName() {
        return declaration.getNameAsString();
    }

    /**
     * @return the type
     */
    public String getType() {
        return declaration.getType().asString();
    }

    public String getJavadocContent() {
        var javadocComment = declaration.getJavadocComment();
        if (javadocComment.isEmpty()) {
            return null;
        }
        return javadocComment.get().getContent();
    }

    /**
     * @return the container class or interface
     */
    public JavaClassOrInterface getContainer() {
        return container;
    }

}
