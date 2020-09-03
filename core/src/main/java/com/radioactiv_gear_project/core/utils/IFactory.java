package com.radioactiv_gear_project.core.utils;

public interface IFactory<TProduct> {
    TProduct instantiate();
}
