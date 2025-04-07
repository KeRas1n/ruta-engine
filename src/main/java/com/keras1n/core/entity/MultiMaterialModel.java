package com.keras1n.core.entity;

import java.util.ArrayList;
import java.util.List;

public class MultiMaterialModel {
    private final List<Model> submodels = new ArrayList<>();

    public void add(Model model) {
        submodels.add(model);
    }

    public List<Model> getSubmodels() {
        return submodels;
    }
}
