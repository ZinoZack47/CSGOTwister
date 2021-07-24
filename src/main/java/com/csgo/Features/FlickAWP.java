package com.csgo.Features;

import java.util.EnumSet;

import com.csgo.Mem.Entities;
import com.csgo.Mem.MemManager;
import com.csgo.Utils.*;
import com.csgo.Utils.Enums.EHitboxIndex;
import com.csgo.Utils.Enums.EItemDefinitionIndex;

import com.sun.jna.platform.win32.Win32VK;


/**
 * FlickAWP
 */
public class FlickAWP
{
    private Long GetClosestPlayer()
    {
        Long pTarget = null;
        Vector vecLocalPos = Entities.Get().Origin(Entities.Get().LocalPlayer());
        EHitboxIndex minBone = EHitboxIndex.HITBOX_HEAD;
        EHitboxIndex maxBone = EHitboxIndex.HITBOX_MAX;
        for(int id = 0; id <= 32; id++)
        {
            long pEnt = Entities.Get().EntityFromId(id);

            if(Entities.Get().isValidTarget(pEnt))
                continue;

            for(EHitboxIndex eBone : EnumSet.range(minBone, maxBone))
            {
                if(eBone == EHitboxIndex.HITBOX_MAX)
                    break;

                Vector vecTarget = Entities.Get().GetHitboxPos(pEnt, eBone);
            }
        }
        return pTarget;
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

        if(Entities.Get().CurrentWeaponClip(pLocalPlayer) <= 0
        || Entities.Get().Reloading(pLocalPlayer))
            return;


    }
}
