package com.csgo.Utils;

public class MStudioBox
{
    public MStudioBox(int iBone, int iGroup, Vector vecBBMin, Vector vecBBMax,
    int nHitboxNameIndex, QAngle angOffsetOrientation, float flRadius)
    {
        this.iBone = iBone;
        this.iGroup = iGroup;
        this.vecBBMin = vecBBMin;
        this.vecBBMax = vecBBMax;
        this.nHitboxNameIndex = nHitboxNameIndex;
        this.angOffsetOrientation = angOffsetOrientation;
        this.flRadius = flRadius;
    }

    public int		iBone;
	public int		iGroup;
	public Vector	vecBBMin;
	public Vector	vecBBMax;
	public int		nHitboxNameIndex;
	public QAngle	angOffsetOrientation;
	public float	flRadius;
}
