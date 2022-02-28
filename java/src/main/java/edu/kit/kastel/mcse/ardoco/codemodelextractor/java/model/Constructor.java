/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import com.github.javaparser.ast.body.ConstructorDeclaration;

/**
 * @author Jan Keim
 *
 */
public class Constructor {
    private ConstructorDeclaration declaration;
    private JavaClassOrInterface container;

    public Constructor(ConstructorDeclaration declaration, JavaClassOrInterface container) {
        this.declaration = declaration;
        this.container = container;
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
