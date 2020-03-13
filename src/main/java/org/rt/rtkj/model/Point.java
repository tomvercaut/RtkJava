package org.rt.rtkj.model;

public interface Point<T> {
    T getX();

    T getY();

    T getZ();

    T getW();

    void setX(T value);

    void setY(T value);

    void setZ(T value);

    void setW(T value);

}
