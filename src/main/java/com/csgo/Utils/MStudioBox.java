package com.csgo.Utils;

public class MStudioBox
{
    public MStudioBox(int iBone, int iGroup, Vector vecBBMin, Vector vecBBMax, int nHitboxNameIndex, Vector angOffsetOrientation, float flRadius) {
        this.iBone = iBone;
        this.iGroup = iGroup;
        this.vecBBMin = vecBBMin;
        this.vecBBMax = vecBBMax;
        this.nHitboxNameIndex = nHitboxNameIndex;
        this.angOffsetOrientation = angOffsetOrientation;
        this.flRadius = flRadius;
    }

    int			iBone;
	int			iGroup;
	Vector		vecBBMin;
	Vector		vecBBMax;
	int			nHitboxNameIndex;
	Vector		angOffsetOrientation;
	float		flRadius;
}
