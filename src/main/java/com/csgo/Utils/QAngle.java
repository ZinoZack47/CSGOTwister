package com.csgo.Utils;

public class QAngle extends AC3Cor
{
    public QAngle(float flPitch, float flYaw, float flRoll)
    {
        super(flPitch, flYaw, flRoll);
    }

    public static QAngle Add(QAngle ang1, QAngle ang2)
    {
        return new QAngle(
            ang1.Pitch() + ang2.Pitch(),
            ang1.Yaw() + ang2.Yaw(),
            ang1.Roll() + ang2.Roll()
        );
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
