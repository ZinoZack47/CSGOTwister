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

        for(int id = 1; id <= 32; id++)
        {
            long pPlayer = Entities.Get().EntityFromId(id);

            if(!Entities.Get().isValidTarget(pPlayer, false))
                continue;

            Entities.Get().Spotted(pPlayer, true);
        }
    }

}
