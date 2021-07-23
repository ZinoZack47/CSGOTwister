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

    public static Vector Add(Vector vec1, Vector vec2)
    {
        return new Vector(
            vec1.x + vec2.x,
            vec1.y + vec2.y,
            vec1.z + vec2.z
        );
    }

    public float x;
    public float y;
    public float z;
}
