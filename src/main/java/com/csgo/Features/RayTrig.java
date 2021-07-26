package com.csgo.Features;

import java.util.ArrayList;

import com.csgo.Mem.Entities;
import com.csgo.Mem.MemManager;
import com.csgo.Utils.*;

import com.sun.jna.platform.win32.Win32VK;

public class RayTrig
{
    public static void Execute()
    {
        if(!MemManager.Get().isKeyPressed(Win32VK.VK_MENU))
            return;

        long pLocalPlayer = Entities.Get().LocalPlayer();

        if(!Entities.Get().Alive(pLocalPlayer))
            return;

        Vector vecLocalPos = Entities.Get().EyePos(pLocalPlayer);
        QAngle qViewAngles = Entities.Get().ClientViewAngles();
        Vector vecDirection = qViewAngles.toDirection();

        Ray Beam = new Ray(vecLocalPos, vecDirection);
        for (int id = 1; id <= 32; id++)
        {
            long pEnt = Entities.Get().EntityFromId(id);

            if(!Entities.Get().isValidTarget(pEnt))
                continue;

            ArrayList<MStudioBox> arrHitBoxes = Entities.Get().Hitboxes(pEnt);

            if(arrHitBoxes == null)
                continue;

            Matrix3x4[] arrBoneMatrix = Entities.Get().BoneMatrix(pEnt);

            for (MStudioBox Hbx : arrHitBoxes)
            {
                if(!Beam.Trace(arrBoneMatrix[Hbx.iBone], Hbx.vecBBMin, Hbx.vecBBMax))
                    continue;

                Entities.Get().ForceAttack();
            }
        }
    }
}
