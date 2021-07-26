package com.csgo.Utils;

public class Ray
{
	public Ray(Vector vecOrigin, Vector vecDirection)
	{
		r_vecOrigin = vecOrigin;
		r_vecDirection = vecDirection;
	}

	public boolean Trace(Matrix3x4 Mx, Vector vecButtomLeft, Vector vecTopRight)
	{
		Vector transRay = VectorITransform(r_vecOrigin, Mx);
		Vector dirRay = VectorIRotate(r_vecDirection, Mx);

		return CollAABB(transRay, dirRay, vecButtomLeft, vecTopRight);
	}

    private Vector VectorITransform(Vector in1, Matrix3x4 in2)
	{
		float[] init = new float[3];

		init[0] = in1.X() - in2.At(0, 3);
		init[1] = in1.Y() - in2.At(1, 3);
		init[2] = in1.Z() - in2.At(2, 3);

		return new Vector(
            init[0] * in2.At(0, 0) + init[1] * in2.At(1, 0) + init[2] * in2.At(2, 0),
		    init[0] * in2.At(0, 1) + init[1] * in2.At(1, 1) + init[2] * in2.At(2, 1),
	        init[0] * in2.At(0, 2) + init[1] * in2.At(1, 2) + init[2] * in2.At(2, 2)
        );
    }

	private Vector VectorIRotate(Vector in1, Matrix3x4 in2)
	{
        return new Vector(
		    in1.X() * in2.At(0, 0) + in1.Y() * in2.At(1, 0) + in1.Z() * in2.At(2, 0),
		    in1.X() * in2.At(0, 1) + in1.Y() * in2.At(1, 1) + in1.Z() * in2.At(2, 1),
		    in1.X() * in2.At(0, 2) + in1.Y() * in2.At(1, 2) + in1.Z() * in2.At(2, 2)
        );
	}

	private boolean CollAABB(Vector vecOrigin, Vector vecDirection, Vector vecButtomLeft, Vector vecTopRight)
	{
		float txMin, txMax;
		float tyMin, tyMax;
		float tzMin, tzMax;

		if (vecDirection.X() >= 0.0f)
		{
			txMin = (vecButtomLeft.X() - vecOrigin.X()) / vecDirection.X();
			txMax = (vecTopRight.X() - vecOrigin.X()) / vecDirection.X();
		}
		else
		{
			txMin = (vecTopRight.X() - vecOrigin.X()) / vecDirection.X();
			txMax = (vecButtomLeft.X() - vecOrigin.X()) / vecDirection.X();
		}

		if (vecDirection.Y() >= 0.0f)
		{
			tyMin = (vecButtomLeft.Y() - vecOrigin.Y()) / vecDirection.Y();
			tyMax = (vecTopRight.Y() - vecOrigin.Y()) / vecDirection.Y();
		}
		else
		{
			tyMin = (vecTopRight.Y() - vecOrigin.Y()) / vecDirection.Y();
			tyMax = (vecButtomLeft.Y() - vecOrigin.Y()) / vecDirection.Y();
		}

		if (txMin > tyMax || tyMin > txMax)
			return false;

		if (tyMin > txMin)
			txMin = tyMin;

		if (tyMax < txMax)
			txMax = tyMax;

		if (vecDirection.Z() >= 0.0f)
		{
			tzMin = (vecButtomLeft.Z() - vecOrigin.Z()) / vecDirection.Z();
			tzMax = (vecTopRight.Z() - vecOrigin.Z()) / vecDirection.Z();
		}
		else
		{
			tzMin = (vecTopRight.Z() - vecOrigin.Z()) / vecDirection.Z();
			tzMax = (vecButtomLeft.Z() - vecOrigin.Z()) / vecDirection.Z();
		}

		if (txMin > tzMax || tzMin > txMax)
			return false;

		if (txMin < 0 || txMax < 0)
			return false;

		return true;
	}

	Vector r_vecOrigin, r_vecDirection;
}
