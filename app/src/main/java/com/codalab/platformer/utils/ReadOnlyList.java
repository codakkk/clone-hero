package com.codalab.platformer.utils;

import java.util.Iterator;
import java.util.List;

public class ReadOnlyList<T> implements Iterable<T> {
    private final List<T> list;

    public ReadOnlyList(List<T> list) {
        this.list = list;
    }

    public int size () {
        return list.size();
    }

    public T get (int index) {
        return list.get(index);
    }

    public boolean contains (T value) {
        return list.contains(value);
    }

    public int indexOf (T value) {
        return list.indexOf(value);
    }

    public int lastIndexOf (T value) {
        return list.lastIndexOf(value);
    }

    public T[] toArray () {
        return (T[]) list.toArray();
    }

    public int hashCode() {
        return list.hashCode();
    }

    public boolean equals (Object object) {
        return list.equals(object);
    }

    public String toString () {
        return list.toString();
    }

    @Override
    public Iterator<T> iterator () {
        return list.iterator();
    }
}