package com.codalab.platformer.utils;

public interface IPoolable {
    /// method used to reset object when pooled
    void onPool();
}
