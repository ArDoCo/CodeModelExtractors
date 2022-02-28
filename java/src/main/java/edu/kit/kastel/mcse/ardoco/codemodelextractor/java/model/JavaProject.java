package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class JavaProject {
    private MutableList<JavaClassOrInterface> classesAndInterfaces;

    public JavaProject() {
        classesAndInterfaces = Lists.mutable.empty();
    }

    /**
     * @param classOrInterface a java class or interface
     * @return add a {@link JavaClassOrInterface} to the project
     */
    public boolean addClassOrInterface(JavaClassOrInterface classOrInterface) {
        return this.classesAndInterfaces.add(classOrInterface);
    }

    /**
     * @return the classesAndInterfaces
     */
    public ImmutableList<JavaClassOrInterface> getClassesAndInterfaces() {
        return classesAndInterfaces.toImmutable();
    }

    /**
     * @return the classNames
     */
    public ImmutableList<String> getClassNames() {
        return getClasses().collect(JavaClassOrInterface::getFullyQualifiedName);
    }

    /**
     * @return the classes
     */
    public ImmutableList<JavaClassOrInterface> getClasses() {
        return getClassesAndInterfaces().reject(JavaClassOrInterface::isInterface);
    }

}
