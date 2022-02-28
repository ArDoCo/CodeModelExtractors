/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import java.util.List;
import java.util.Objects;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class JavaClassOrInterface {
    private ClassOrInterfaceDeclaration declaration;

    public JavaClassOrInterface(ClassOrInterfaceDeclaration declaration) {
        this.declaration = declaration;
    }

    /**
     * @return the name
     */
    public String getName() {
        return declaration.getNameAsString();
    }

    /**
     * @return the fullyQualifiedName
     */
    public String getFullyQualifiedName() {
        return declaration.getFullyQualifiedName().orElse(getName());
    }

    /**
     * @return true, iff this is an interface
     */
    public boolean isInterface() {
        return declaration.isInterface();
    }

    public List<CodeComment> getAllComments() {
        var comments = declaration.getAllContainedComments();
        return comments.stream()
                .map(c -> new CodeComment(c.getClass().getSimpleName(), c.getContent(), c.getRange().map(r -> r.begin.line).orElse(-1),
                        !c.getCommentedNode().isPresent()))
                .toList();
    }

    public List<JavaMethod> getAllMethods() {
        return declaration.getMethods().stream().map(md -> new JavaMethod(md, this)).toList();
    }

    public List<Constructor> getConstructors() {
        return declaration.getConstructors().stream().map(cd -> new Constructor(cd, this)).toList();
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
        var other = (JavaClassOrInterface) obj;
        return Objects.equals(declaration, other.declaration);
    }

    @Override
    public String toString() {
        return "JavaClassOrInterface [" + (getFullyQualifiedName() != null ? "getFullyQualifiedName()=" + getFullyQualifiedName() + ", " : "")
                + "isInterface()=" + isInterface() + "]";
    }

}
