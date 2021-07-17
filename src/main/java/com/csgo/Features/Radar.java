package com.csgo.Features;

import com.sun.jna.Pointer;

import com.csgo.Mem.*;

/**
 * Radar
 */
public class Radar extends Offsets
{
    private Radar() {}

    public static void Execute()
    {
        Pointer LocalPlayer = MemManager.Get().Client().getPointer(dwLocalPlayer);
        int iTeam = LocalPlayer.getInt(m_iTeamNum);

        for(int i = 1; i <= 32; i++)
        {
            Pointer Player = MemManager.Get().Client().getPointer(dwEntityList + (i - 1) * 0x10);

            int bDormant = Player.getInt(m_bDormant);

            if(bDormant == 1)
                continue;

            int iPlayerTeam = Player.getInt(m_iTeamNum);

            if(iPlayerTeam == 1 || iPlayerTeam == iTeam)
                continue;

            int iPlayerHealth = Player.getInt(m_iHealth);

            if(iPlayerHealth <= 0)
                continue;

            int bPlayerSpotted = Player.getInt(m_bSpotted);

            if(bPlayerSpotted == 1)
                continue;

            Player.setInt(m_bSpotted, 1);
        }
    }

}
