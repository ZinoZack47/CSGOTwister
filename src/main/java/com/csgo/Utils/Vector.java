package com.csgo.Utils;

public class Vector extends AC3Cor
{
    public Vector(float flX, float flY, float flZ)
    {
        super(flX, flY, flZ);
    }

    public static Vector Add(Vector vec1, Vector vec2)
    {
        return new Vector(
            vec1.X() + vec2.X(),
            vec1.Y() + vec2.Y(),
            vec1.Z() + vec2.Z()
        );
    }

    public float Distance(Vector vecDestination)
    {
        return (float)Math.sqrt(
            Math.pow(this.X() - vecDestination.X(), 2) +
            Math.pow(this.Y() - vecDestination.Y(), 2) +
            Math.pow(this.Z() - vecDestination.Z(), 2)
        );
    }

    public float X()
    {
        return c1;
    }

    public float Y()
    {
        return c2;
    }

    public float Z()
    {
        return c3;
    }

    public void X(float flX)
    {
        c1 = flX;
    }

    public void Y(float flY)
    {
        c2 = flY;
    }

    public void Z(float flZ)
    {
        c3 = flZ;
    }
}
