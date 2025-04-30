package com.codalab.platformer.entities;

public interface IActivable {

    void onActivated();

    boolean canActivate();

    void addGate(IGate gate);
}
