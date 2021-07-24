package com.csgo.Mem;

import java.util.ArrayList;
import java.util.HashMap;

import com.csgo.Utils.*;
import com.csgo.Utils.Enums.*;

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

    boolean DangerZone()
    {
        long pGameRules = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwGameRulesProxy);
        int iSurvivalGRDT = MemManager.Get().ReadInt(pGameRules + m_SurvivalGameRuleDecisionTypes);
        return (iSurvivalGRDT != 0);
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

    public boolean isValidTarget(long pEnt)
    {
        return isValidTarget(pEnt, true);
    }

    public boolean isValidTarget(long pEnt, boolean bGunGameCheck)
    {
        boolean bDormant = Dormant(pEnt);
        if(bDormant)
            return false;

        int iTeam = Team(pEnt);

        if(iTeam <= 1 || iTeam == Team(LocalPlayer()) && !DangerZone())
            return false;

        if(!Alive(pEnt))
            return false;

        if(!bGunGameCheck)
            return true;

        boolean bGunGameImmune = GunGameImmune(pEnt);

        if(bGunGameImmune)
            return false;

        return true;
    }

    public boolean Alive(long pEnt)
    {
        return Health(pEnt) > 0;
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

    public boolean Reloading(long pEnt)
    {
        //Only works for LocalPlayer AFAIK
        boolean bIsReloading = MemManager.Get().ReadBool(pEnt + m_bInReload);
        return bIsReloading;
    }
    public EItemDefinitionIndex CurrentWeaponId(long pEnt)
    {
        long dwWeapAddress = MemManager.Get().ReadDWORD(pEnt + m_hActiveWeapon) & 0xFFF;
        long dwWeapEnt = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (dwWeapAddress - 0x01) * 0x10);
        short iWeapId = MemManager.Get().ReadShort(dwWeapEnt + m_iItemDefinitionIndex);
        return EItemDefinitionIndex.valueOf(iWeapId);
    }

    public int CurrentWeaponClip(long pEnt)
    {
        long dwWeapAddress = MemManager.Get().ReadDWORD(pEnt + m_hActiveWeapon) & 0xFFF;
        long dwWeapEnt = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (dwWeapAddress - 0x01) * 0x10);
        int iClip = MemManager.Get().ReadInt(dwWeapEnt + m_iClip1);
        return iClip;
    }

    public Vector Origin(long pEnt)
    {
        Vector vecOrigin = MemManager.Get().ReadVector(pEnt + m_vecOrigin);
        return vecOrigin;
    }

    public Vector EyePos(long pEnt)
    {
        Vector vecOrigin = Origin(pEnt);
        Vector vecViewOffset = MemManager.Get().ReadVector(pEnt + m_vecViewOffset);
        return Vector.Add(vecOrigin, vecViewOffset);
    }

    public QAngle ClientViewAngles()
    {
        long dwCState = MemManager.Get().ReadDWORD(MemManager.Get().Engine() + dwClientState);
        QAngle QViewAngles = new QAngle(MemManager.Get().ReadVector(dwCState + dwClientState_ViewAngles));
        return QViewAngles;
    }

    public boolean Spotted(long pEnt)
    {
        boolean bSpotted = MemManager.Get().ReadBool(pEnt + m_bSpotted);
        return bSpotted;
    }

    public void Spotted(long pEnt, boolean bNewSpotted)
    {
        boolean bSpotted = Spotted(pEnt);

        if(bSpotted == bNewSpotted)
            return;

        MemManager.Get().WriteBool(pEnt + m_bSpotted, bNewSpotted);
    }

    public Matrix3x4[] BoneMatrix(long pEnt)
    {
        long dwBoneMatrix = MemManager.Get().ReadDWORD(pEnt + m_dwBoneMatrix);
        return MemManager.Get().ReadMatrix3x4Array(dwBoneMatrix);
    }

    public ArrayList<MStudioBox> Hitboxes(long pEnt)
    {
        long pStudioHdr = MemManager.Get().ReadDWORD(pEnt + m_pStudioHdr);

        if(pStudioHdr == 0)
            return null;

        long dwModel = MemManager.Get().ReadDWORD(pEnt + 0x6C);

        String szModelName = MemManager.Get().ReadString(dwModel + 0x4, 128);

        if(szModelName.isEmpty())
            return null;

        return Parse(pStudioHdr, szModelName);
    }

    private ArrayList<MStudioBox> Parse(long pStudioHdr, String szModelName)
    {
        if(mapModelHitboxes.containsKey(szModelName))
            return mapModelHitboxes.get(szModelName);

        long HitboxSetIdx = MemManager.Get().ReadDWORD(pStudioHdr + 0xB0);

        if(HitboxSetIdx < 0)
            return null;

        long StudioHitboxSet = MemManager.Get().ReadDWORD(pStudioHdr + HitboxSetIdx);
        int iHitboxesNum = MemManager.Get().ReadInt(StudioHitboxSet + 0x4);

        if(iHitboxesNum <= 0)
            return null;

        int iHitboxIndex = MemManager.Get().ReadInt(StudioHitboxSet + 0x8);

        ArrayList<MStudioBox> vecModelHitboxes = new ArrayList<MStudioBox>();

        for (int i = 0; i < iHitboxesNum; i++)
        {
            MStudioBox ModelHitbox = MemManager.Get().ReadMStudioBox(0x44 * i + iHitboxIndex + StudioHitboxSet);

            if(ModelHitbox.flRadius != -1.f)
            {
                ModelHitbox.vecBBMin.Sub(ModelHitbox.flRadius);
                ModelHitbox.vecBBMax.Add(ModelHitbox.flRadius);
            }

            vecModelHitboxes.add(ModelHitbox);
        }

        if(vecModelHitboxes.isEmpty())
            return null;

        mapModelHitboxes.put(szModelName, vecModelHitboxes);
        return mapModelHitboxes.get(szModelName);
    }

    public Vector GetHitboxPos(long pEnt, EHitboxIndex eBone)
    {
        return null;
    }

    private HashMap<String, ArrayList<MStudioBox>> mapModelHitboxes = new HashMap<String, ArrayList<MStudioBox>>();
}
