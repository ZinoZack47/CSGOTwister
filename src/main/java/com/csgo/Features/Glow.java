package com.csgo.Features;

import com.csgo.Mem.Entities;
import com.csgo.Mem.MemManager;
import com.sun.jna.platform.win32.Win32VK;

public class Glow
{
    private static boolean bToggle = false;
    public static void Execute()
    {
        if(MemManager.Get().isKeyPressed(Win32VK.VK_NUMPAD1))
        {
            bToggle = !bToggle;
        }
        if(bToggle) Entities.Get().GlowEntities();
    }
}
