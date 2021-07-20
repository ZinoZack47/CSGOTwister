package com.csgo.Features;

import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.WinDef.DWORD;

import com.csgo.Mem.*;

/**
 * Radar
 */
public class Radar extends Offsets
{
    private Radar() {}

    public static void Execute()
    {
        Pointer pLocalPlayer = MemManager.RPM(MemManager.Get().Proc(),
        new Pointer(Pointer.nativeValue(MemManager.Get().Client()) + dwLocalPlayer),
        DWORD.SIZE);

        if(MemManager.bDebugMode)
        {
            System.out.println("LocalPlayer at: " + pLocalPlayer.toString());
        }

        int iTeam = MemManager.RPM(MemManager.Get().Proc(),
        new Pointer(Pointer.nativeValue(pLocalPlayer) + m_iTeamNum),
        Integer.BYTES).getInt(0);

        if(MemManager.bDebugMode)
        {
            System.out.println("LocalPlayer Team: " + iTeam);
        }

        for(int i = 1; i <= 32; i++)
        {
            Pointer pPlayer = MemManager.RPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(MemManager.Get().Client()) + dwEntityList + (i - 1) * 0x10),
            DWORD.SIZE);

            int bDormant = MemManager.RPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(pPlayer) + m_bDormant),
            1).getInt(0);

            if(bDormant == 1)
                continue;

            int iPlayerTeam = MemManager.RPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(pPlayer) + m_iTeamNum),
            Integer.BYTES).getInt(0);

            int iPlayerHealth = MemManager.RPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(pPlayer) + m_iHealth),
            Integer.BYTES).getInt(0);

            int bPlayerSpotted = MemManager.RPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(pPlayer) + m_bSpotted),
            1).getInt(0);

            if(MemManager.bDebugMode)
            {
                System.out.println("Player Team: " + iPlayerTeam);
                System.out.println("Player Health: " + iPlayerHealth);
                System.out.println("Player Spotted: " + bPlayerSpotted);
                System.out.println();
            }

            if(iPlayerTeam == 1 || iPlayerTeam == iTeam
            || iPlayerHealth <= 0 || bPlayerSpotted == 1)
                continue;

            Memory pNewSpotted = new Memory(Pointer.nativeValue(pPlayer) + m_bSpotted);
            pNewSpotted.setInt(0, 1);

            MemManager.WPM(MemManager.Get().Proc(),
            new Pointer(Pointer.nativeValue(pPlayer) + m_bSpotted),
            pNewSpotted,
            1);
        }
    }

}
