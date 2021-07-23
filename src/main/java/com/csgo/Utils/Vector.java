package com.csgo.Utils;

public class Vector
{
    public Vector() {
        this(0, 0, 0);
    }

    public Vector(float x, float y) {
        this(x, y, 0);
    }

    public Vector(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x;
    public float y;
    public float z;
}
