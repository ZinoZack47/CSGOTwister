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
        long pLocalPlayer = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwLocalPlayer);

        int iTeam = MemManager.Get().ReadInt(pLocalPlayer + m_iTeamNum);

        for(int i = 1; i <= 32; i++)
        {
            long pPlayer = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (i - 1) * 0x10);

            boolean bDormant = MemManager.Get().ReadBool(pPlayer + m_bDormant);

            if(bDormant)
                continue;

            int iPlayerTeam = MemManager.Get().ReadInt(pPlayer + m_iTeamNum);

            int iPlayerHealth = MemManager.Get().ReadInt(pPlayer + m_iHealth);

            boolean bPlayerSpotted = MemManager.Get().ReadBool(pPlayer + m_bSpotted);

            if(iPlayerTeam == 1 || iPlayerTeam == iTeam
            || iPlayerHealth <= 0 || bPlayerSpotted)
                continue;

            MemManager.Get().WriteBool(pPlayer + m_bSpotted, true);
        }
    }

}
