package com.csgo.Utils;

public class QAngle extends AC3Cor
{
    public QAngle(float flPitch, float flYaw, float flRoll)
    {
        super(flPitch, flYaw, flRoll);
    }

    public QAngle(Vector vecAngles)
    {
        super(vecAngles);
    }

    public static QAngle Add(QAngle ang1, QAngle ang2)
    {
        return new QAngle(
            ang1.Pitch() + ang2.Pitch(),
            ang1.Yaw() + ang2.Yaw(),
            ang1.Roll() + ang2.Roll()
        );
    }

    public Vector toDirection()
    {
        float radPitch = (float)Math.toRadians(this.Pitch());
        float radYaw = (float)Math.toRadians(this.Yaw());

        float cosPitch = (float)Math.cos(radPitch);
        float sinPitch = (float)Math.sin(radPitch);

        float cosYaw = (float)Math.cos(radYaw);
        float sinYaw = (float)Math.sin(radYaw);

        return new Vector(
            cosPitch * cosYaw,
            cosPitch * sinYaw,
            -sinPitch
        );
    }

    public void Fix()
    {
        this.Normalize();
        this.Clamp();
    }

    private void Normalize()
    {
        float[] arrAngle = this.toArray();

        for(int i =0; i < 3; i++)
        {
            while (arrAngle[i] < -180.f)
            {
                arrAngle[i] += 360.f;
            }
            while (arrAngle[i] > 180.f)
            {
                arrAngle[i] -= 360.f;
            }
        }

        this.Override(arrAngle);
    }

    void Clamp()
    {
		if (this.Pitch() > 89.0f) this.Pitch(89.0f);
		else if (this.Pitch() < -89.0f) this.Pitch(-89.0f);

		if (this.Yaw() > 180.0f) this.Yaw(180.0f);
		else if (this.Yaw() < -180.0f) this.Yaw(-180.0f);

		this.Roll(0);
	}

    public static QAngle VectorAngles(Vector vecForward)
    {
		float tmp, yaw, pitch;

		if(vecForward.X() == 0 && vecForward.X() == 0)
        {
			yaw = 0;
			pitch = vecForward.Z() > 0 ? 270 : 90;
		}
        else
        {
			yaw = (float)(Math.atan2(vecForward.Y(), vecForward.X()) * 180 / Math.PI);
            if(yaw < 0) yaw += 360;

			tmp = (float)Math.sqrt(vecForward.X() * vecForward.X() + vecForward.Y() * vecForward.Y());

            pitch = (float)(Math.atan2(-vecForward.Z(), tmp) * 180 / Math.PI);
			if(pitch < 0) pitch += 360;
		}

		return new QAngle(pitch, yaw, 0);
	}

    public float Pitch()
    {
        return c1;
    }

    public float Yaw()
    {
        return c2;
    }

    public float Roll()
    {
        return c3;
    }

    public void Pitch(float flPitch)
    {
        c1 = flPitch;
    }

    public void Yaw(float flYaw)
    {
        c2 = flYaw;
    }

    public void Roll(float flRoll)
    {
        c3 = flRoll;
    }
}
