package com.Nxer.TwistSpaceTechnology.common.TechTree;

public enum TechPool {

    TestTech1("TEST1", "TEST_DESCRIPTION1"),
    TestTech2("TEST2", "TEST_DESCRIPTION2");

    public final Technology tech;

    TechPool(String name, String description) {
        tech = new Technology(name, description);
    }

}
