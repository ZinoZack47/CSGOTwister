package com.csgo.Utils;

public class Vector extends AC3Cor
{
    public Vector(float flX, float flY, float flZ)
    {
        super(flX, flY, flZ);
    }

    public Vector()
    {
        super();
    }

    public Vector(float[] arrVector)
    {
        super(arrVector);
    }

    public static Vector Add(Vector vec1, Vector vec2)
    {
        return new Vector(
            vec1.X() + vec2.X(),
            vec1.Y() + vec2.Y(),
            vec1.Z() + vec2.Z()
        );
    }

    public static Vector Sub(Vector vec1, Vector vec2)
    {
        return new Vector(
            vec1.X() - vec2.X(),
            vec1.Y() - vec2.Y(),
            vec1.Z() - vec2.Z()
        );
    }

    public float Dot(Vector vecOther)
    {
        return this.X() * vecOther.X() + this.Y() * vecOther.Y() + this.Z() * vecOther.Z();
    }

    public float Distance(Vector vecDestination)
    {
        return (float)Math.sqrt(
            Math.pow(this.X() - vecDestination.X(), 2) +
            Math.pow(this.Y() - vecDestination.Y(), 2) +
            Math.pow(this.Z() - vecDestination.Z(), 2)
        );
    }

    public static Vector VectorTransfrom(Vector vecIn, Matrix3x4 matIn)
    {
        float[] arrOut = new float[3];

        for(int i = 0; i < 3; i++)
            arrOut[i] = vecIn.Dot(matIn.At(i)) + matIn.At(i, 3);

        return new Vector(arrOut);
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
