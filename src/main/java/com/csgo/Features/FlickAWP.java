package com.csgo.Features;

import java.util.ArrayList;

import com.csgo.Mem.Entities;
import com.csgo.Mem.MemManager;
import com.csgo.Utils.*;
import com.csgo.Utils.Enums.EItemDefinitionIndex;

import com.sun.jna.platform.win32.Win32VK;

/**
 * FlickAWP
 */
public class FlickAWP
{
    private static float GetFovToPlayer(QAngle viewAngle, QAngle aimAngle)
    {
        QAngle delta = QAngle.Sub(aimAngle, viewAngle);
        delta.Fix();
        return delta.Length2D();
    }

    private static QAngle GetClosestTargetAngles()
    {
        Vector vecLocalPos = Entities.Get().EyePos(Entities.Get().LocalPlayer());
        QAngle qViewAngles = Entities.Get().ClientViewAngles();
        QAngle qTargetAngles = null;
        Float flBestFov = 30.f;

        for(int id = 0; id <= 32; id++)
        {
            long pEnt = Entities.Get().EntityFromId(id);

            if(!Entities.Get().isValidTarget(pEnt))
                continue;

            ArrayList<MStudioBox> vecHitboxes = Entities.Get().Hitboxes(pEnt);

            if(vecHitboxes == null || vecHitboxes.isEmpty())
                continue;

            Matrix3x4[] arrBoneMatrix = Entities.Get().BoneMatrix(pEnt);

            if(arrBoneMatrix == null)
                continue;

            for(MStudioBox Hbx : vecHitboxes)
            {
                Vector vecMin = Vector.VectorTransfrom(Hbx.vecBBMin, arrBoneMatrix[Hbx.iBone]);
                Vector vecMax = Vector.VectorTransfrom(Hbx.vecBBMax, arrBoneMatrix[Hbx.iBone]);
                Vector vecHbxPos = Vector.Add(vecMax, vecMin);
                vecHbxPos.Div(2);
                QAngle qHbxAngles = QAngle.VectorAngles(Vector.Sub(vecHbxPos, vecLocalPos));
                qHbxAngles.Fix();
                float Fov = GetFovToPlayer(qViewAngles, qHbxAngles);
                if(flBestFov == null || Fov < flBestFov)
                {
                    qTargetAngles = qHbxAngles;
                    flBestFov = Fov;
                }
            }
        }
        return qTargetAngles;
    }

    private static QAngle Smooth(QAngle qTargetAngles, float flSmooth)
    {
        if(flSmooth <= 1)
            return qTargetAngles;

        QAngle qViewAngles = Entities.Get().ClientViewAngles();
        QAngle qDeltaAngles = QAngle.Sub(qTargetAngles, qViewAngles);
        qDeltaAngles.Fix();
        flSmooth = (Entities.Get().IntervalPerTick() * 64.0f) / flSmooth;
        qDeltaAngles.Mult(flSmooth);
        QAngle qAimAngles = QAngle.Add(qViewAngles, qDeltaAngles);
        qAimAngles.Fix();
        return qAimAngles;
    }

    public static void Execute()
    {
        if(!MemManager.Get().isKeyPressed(Win32VK.VK_LBUTTON))
            return;

        long pLocalPlayer = Entities.Get().LocalPlayer();

        if(!Entities.Get().Alive(pLocalPlayer))
            return;

        if(Entities.Get().CurrentWeaponId(pLocalPlayer) != EItemDefinitionIndex.WEAPON_AWP)
            return;

        //if(!Entities.Get().Scoped(pLocalPlayer))
        //    return;

        if(Entities.Get().CurrentWeaponClip(pLocalPlayer) <= 0
        || Entities.Get().Reloading(pLocalPlayer))
            return;

        QAngle qTargetAngles = GetClosestTargetAngles();

        if(qTargetAngles == null)
            return;

        QAngle qAngles = Smooth(qTargetAngles, 1);

        Entities.Get().ClientViewAngles(qAngles);
    }
}
