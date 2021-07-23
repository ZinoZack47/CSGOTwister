package com.csgo.Features;

import com.csgo.Mem.Entities;
import com.csgo.Mem.MemManager;

import com.sun.jna.platform.win32.Win32VK;

/**
 * Radar
 */
public class Radar
{
    private Radar() {}

    public static boolean bToggle = true;

    public static void Execute()
    {
        if(MemManager.Get().isKeyPressed(Win32VK.VK_F5))
        {
            bToggle = !bToggle;
        }

        if(!bToggle)
            return;

        long pLocalPlayer = Entities.Get().LocalPlayer();

        int iTeam = Entities.Get().Team(pLocalPlayer);

        for(int id = 1; id <= 32; id++)
        {
            long pPlayer = Entities.Get().EntityFromId(id);

            boolean bDormant = Entities.Get().Dormant(pPlayer);

            if(bDormant)
                continue;

            int iPlayerTeam = Entities.Get().Team(pPlayer);

            int iPlayerHealth = Entities.Get().Health(pPlayer);

            boolean bPlayerSpotted = Entities.Get().Spotted(pPlayer);

            if(iPlayerTeam == 1 || iPlayerTeam == iTeam
            || iPlayerHealth <= 0 || bPlayerSpotted)
                continue;

            Entities.Get().Spotted(pPlayer, true);
        }
    }

}
