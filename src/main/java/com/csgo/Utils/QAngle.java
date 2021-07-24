package com.csgo.Utils;

public class QAngle
{
    public QAngle(float pitch, float yaw)
    {
        this(pitch, yaw, 0);
    }

    public QAngle(float pitch, float yaw, float roll)
    {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public QAngle(Vector vecAngle)
    {
        this.pitch = vecAngle.x;
        this.yaw = vecAngle.y;
        this.roll = vecAngle.x;
    }

    public float pitch;
    public float yaw;
    public float roll;
}
