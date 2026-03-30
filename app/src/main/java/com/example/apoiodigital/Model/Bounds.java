package com.example.apoiodigital.Model;

public class Bounds {
    private Integer left;
    private Integer right;
    private Integer top;
    private Integer bottom;

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public Bounds(Integer left, Integer right, Integer top, Integer bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
}

