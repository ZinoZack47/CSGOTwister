package com.csgo.Utils;

public class Matrix3x4
{
    public Matrix3x4(float[] data)
    {
        for(int i = 0; i < Length; i++)
        {
            this.data[i] = data[i];
        }
    }

    public float At(int row, int column)
    {
        return data[row * 4 + column];
    }

    public Vector At(int column)
    {
        return new Vector(
            this.At(0, column),
            this.At(1, column),
            this.At(2, column)
        );
    }

    private float[] data = new float[Length];
    public final static int Length = 12;
    public final static int SIZE = Float.BYTES * Length;
}
