package com.csgo.Features;

import com.csgo.Mem.*;

/**
 * Radar
 */
public class Radar extends Offsets
{
    private Radar() {}


    public static void Execute()
    {
        long pLocalPlayer = MemManager.ReadDWORD(MemManager.Get().Proc(),
        MemManager.Get().Client() + dwLocalPlayer);

        if(MemManager.bDebugMode)
        {
            System.out.printf("LocalPlayer at: 0x%06x\n", pLocalPlayer);
        }

        int iTeam = MemManager.ReadInt(MemManager.Get().Proc(),
        pLocalPlayer + m_iTeamNum);

        if(MemManager.bDebugMode)
        {
            System.out.println("LocalPlayer Team: " + iTeam);
        }

        for(int i = 1; i <= 32; i++)
        {
            long pPlayer = MemManager.ReadDWORD(MemManager.Get().Proc(),
            MemManager.Get().Client() + dwEntityList + (i - 1) * 0x10);

            boolean bDormant = MemManager.ReadBool(MemManager.Get().Proc(),
            pPlayer + m_bDormant);

            if(bDormant)
                continue;

            int iPlayerTeam = MemManager.ReadInt(MemManager.Get().Proc(),
            pPlayer + m_iTeamNum);

            int iPlayerHealth = MemManager.ReadInt(MemManager.Get().Proc(),
            pPlayer + m_iHealth);

            boolean bPlayerSpotted = MemManager.ReadBool(MemManager.Get().Proc(),
            pPlayer + m_bSpotted);

            if(MemManager.bDebugMode)
            {
                System.out.println("Player Team: " + iPlayerTeam);
                System.out.println("Player Health: " + iPlayerHealth);
                System.out.println("Player Spotted: " + bPlayerSpotted);
                System.out.println();
            }

            if(iPlayerTeam == 1 || iPlayerTeam == iTeam
            || iPlayerHealth <= 0 || bPlayerSpotted)
                continue;

            MemManager.WriteBool(MemManager.Get().Proc(),
            pPlayer + m_bSpotted,
            true);
        }
    }

}
