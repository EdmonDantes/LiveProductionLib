/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression;

public final class Trio<K, T, M> {
    private K first;
    private T second;
    private M third;


    public Trio(K first, T second, M third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public K getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public M getThird() { return third; }

    public void setFirst(K first) { this.first = first; }

    public void setSecond(T second) { this.second = second; }

    public void setThird(M third) { this.third = third; }
}
