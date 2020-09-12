package com.radioactive.gear.project.core.utils;

public interface IFactory<TProduct> {
    TProduct instantiate();
}
