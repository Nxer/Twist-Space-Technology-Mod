package com.Nxer.TwistSpaceTechnology.common.TechTree;

import java.io.Serializable;
import java.util.HashSet;

public class Technology implements Serializable {

    public String name;

    public int id;

    public String description;

    public HashSet<Integer> dependency;

    public boolean isResearched;

    public Technology(String name, String description) {
        this.name = name;
        this.id = -1;
        this.description = description;
        dependency = new HashSet<>();
        isResearched = false;
    }

    public void setDependency(Integer dependency) {
        this.dependency.add(dependency);
    }

}
