/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model.JavaClassOrInterface;

public class ClassOrInterfaceVisitor extends VoidVisitorAdapter<List<JavaClassOrInterface>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<JavaClassOrInterface> collector) {
        super.visit(n, collector);
        var visitedCoI = new JavaClassOrInterface(n);
        collector.add(visitedCoI);
    }
}
