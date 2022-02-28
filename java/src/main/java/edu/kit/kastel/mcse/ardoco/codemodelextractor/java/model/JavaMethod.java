/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(declaration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var other = (JavaMethod) obj;
        return Objects.equals(declaration, other.declaration);
    }

    @Override
    public String toString() {
        return declaration.getDeclarationAsString();
    }

}
