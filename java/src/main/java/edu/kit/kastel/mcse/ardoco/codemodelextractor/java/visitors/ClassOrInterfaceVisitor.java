/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaClassOrInterface;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaProject;

public class ClassOrInterfaceVisitor extends VoidVisitorAdapter<JavaProject> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, JavaProject collector) {
        super.visit(n, collector);
        var visitedCoI = new JavaClassOrInterface(n);
        collector.addClassOrInterface(visitedCoI);
    }
}
