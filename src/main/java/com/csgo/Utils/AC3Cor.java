package com.csgo.Utils;

public class AC3Cor //This stands for Abstract Class w/ 3 Coordinates
{
    protected AC3Cor()
    {
        this(0, 0, 0);
    }

    protected  AC3Cor(float c1, float c2)
    {
        this(c1, c2, 0);
    }

    protected AC3Cor(float c1, float c2, float c3)
    {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    protected AC3Cor(AC3Cor ac3Cor)
    {
        this.c1 = ac3Cor.c1;
        this.c2 = ac3Cor.c2;
        this.c3 = ac3Cor.c3;
    }

    public AC3Cor(float[] Array3C)
    {
        this.Override(Array3C);
    }

    public void Add(float flFactor)
    {
        this.c1 += flFactor;
        this.c2 += flFactor;
        this.c3 += flFactor;
    }

    public void Sub(float flFactor)
    {
        this.c1 -= flFactor;
        this.c2 -= flFactor;
        this.c3 -= flFactor;
    }

    public void Mult(float flFactor)
    {
        this.c1 *= flFactor;
        this.c2 *= flFactor;
        this.c3 *= flFactor;
    }

    public void Div(float flFactor)
    {
        this.c1 /= flFactor;
        this.c2 /= flFactor;
        this.c3 /= flFactor;
    }

    public float Length2D()
    {
        return (float)Math.sqrt(
            Math.pow(this.c1, 2) +
            Math.pow(this.c2, 2)
        );
    }

    public void Override(float Array3C[]) // 3C Stands for 3 Coordinates
    {
        if(Array3C.length != 3)
            return;

        this.c1 = Array3C[0];
        this.c2 = Array3C[1];
        this.c3 = Array3C[2];
    }

    public float[] toArray()
    {
        float[] AC3Array = new float[3];
        AC3Array[0] = this.c1;
        AC3Array[1] = this.c2;
        AC3Array[2] = this.c3;
        return AC3Array;
    }

    protected Float c1, c2, c3;
}
