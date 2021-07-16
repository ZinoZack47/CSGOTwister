package com.csgo.Features;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORD;

import com.csgo.Mem.*;

/**
 * Radar
 */
public class Radar extends Offsets {
    private Radar() {}

    public static void Execute()
    {
        for(int i = 1; i <= 64; i++)
        {
            Pointer LocalPlayer = MemManager.Get().Client().getPointer(dwLocalPlayer);

            int iTeam = LocalPlayer.getInt(m_iTeamNum);
            int bDormant = LocalPlayer.getInt(m_bDormant);

            if(bDormant == 1)
                continue;

            System.out.println("Teamid : " + iTeam);
        }
    }

}
