package com.csgo.Mem;

public class Entities extends Offsets
{
    private Entities() { }

    private static Entities INSTANCE = new Entities();

    public static Entities Get()
    {
        return INSTANCE;
    }

    public long LocalPlayer()
    {
        long pLocalPlayer = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwLocalPlayer);
        return pLocalPlayer;
    }

    public long EntityFromId(int id)
    {
        long pEnt = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (id - 0x01) * 0x10);
        return pEnt;
    }

    public boolean Dormant(long pEnt)
    {
        boolean bDormant = MemManager.Get().ReadBool(pEnt + m_bDormant);
        return bDormant;
    }

    public int Team(long pEnt)
    {
        int iTeam = MemManager.Get().ReadInt(pEnt + m_iTeamNum);
        return iTeam;
    }

    public int Health(long pEnt)
    {
        int iHealth = MemManager.Get().ReadInt(pEnt + m_iHealth);
        return iHealth;
    }

    public boolean GunGameImmune(long pEnt)
    {
        boolean bGunGameImmune = MemManager.Get().ReadBool(pEnt + m_bGunGameImmunity);
        return bGunGameImmune;
    }

    public boolean Flashed(long pEnt)
    {
        float flFlashDuration = MemManager.Get().ReadFloat(pEnt + m_flFlashDuration);
        return (flFlashDuration > 0.f);
    }

    public void FlashIntensity(long pEnt, float flAlpha)
    {
        MemManager.Get().WriteFloat(pEnt + m_flFlashMaxAlpha, flAlpha);
    }

    public short CurrentWeaponId(Long pEnt)
    {
        long dwWeapAddress = MemManager.Get().ReadDWORD(pEnt + m_hActiveWeapon) & 0xFFF;
        long dwWeapEnt = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (dwWeapAddress - 0x01) * 0x10);
        short iWeapId = MemManager.Get().ReadShort(dwWeapEnt + m_iItemDefinitionIndex);
        return iWeapId;
    }

    public boolean Spotted(long pEnt)
    {
        boolean bSpotted = MemManager.Get().ReadBool(pEnt + m_bSpotted);
        return bSpotted;
    }

    public void Spotted(long pEnt, boolean bNewSpotted)
    {
        MemManager.Get().WriteBool(pEnt + m_bSpotted, bNewSpotted);
    }
}
