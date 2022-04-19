/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JavaProject implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private List<JavaClassOrInterface> classesAndInterfaces;

    public JavaProject() {
        classesAndInterfaces = new ArrayList<>();
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
        return Lists.immutable.withAll(classesAndInterfaces);
    }

    /**
     * @return the classNames
     */
    public ImmutableList<String> getClassNames() {
        return getClasses().collect(JavaClassOrInterface::getFullyQualifiedName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * @return the classes
     */
    public ImmutableList<JavaClassOrInterface> getClasses() {
        return getClassesAndInterfaces().reject(JavaClassOrInterface::isInterface);
    }

}
