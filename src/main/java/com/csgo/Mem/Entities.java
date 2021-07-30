package com.csgo.Mem;

import java.util.ArrayList;
import java.util.HashMap;

import com.csgo.Utils.*;
import com.csgo.Utils.Enums.*;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.AWTException;

public class Entities extends Offsets
{
    private Entities() { }

    private static Entities INSTANCE = new Entities();

    public static Entities Get()
    {
        return INSTANCE;
    }

    public void ForceAttack()
    {
        try
        {
            int iPreFire = 0, iPostFire = 0;
            EItemDefinitionIndex WeaponId = CurrentWeaponId(LocalPlayer());
            switch(EWeaponType.WeaponTypeFromId(WeaponId))
            {
            case WEAPONTYPE_PISTOL:
            iPreFire = 30;
            iPostFire = 10;
            break;
            case WEAPONTYPE_SUBMACHINEGUN:
            iPreFire = 20;
            iPostFire = 10;
            break;
            case WEAPONTYPE_RIFLE:
            iPreFire = 50;
            iPostFire = 500;
            break;
            case WEAPONTYPE_SHOTGUN:
            iPreFire = 20;
            iPostFire = 10;
            break;
            case WEAPONTYPE_MACHINEGUN:
            iPreFire = 20;
            iPostFire = 10;
            break;
            case WEAPONTYPE_SNIPER:
            iPreFire = 20;
            iPostFire = 1500;
            break;
            case WEAPONTYPE_AUTONOOB:
            iPreFire = 20;
            iPostFire = 500;
            break;
            case WEAPONTYPE_HEAVYPISTOL:
            iPreFire = 30;
            iPostFire = 1000;
            default: return;
            }
            Thread.sleep(iPreFire);
            Robot bot = new Robot();
            bot.mousePress(InputEvent.BUTTON1_MASK);
            Thread.sleep(10);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
            Thread.sleep(iPostFire);
        }
        catch (AWTException Ex)
        {
            Ex.printStackTrace();
        }
        catch (InterruptedException Ex)
        {
            Ex.printStackTrace();
        }
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
        if(pEnt == 0)
            return false;

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
        return (Health(pEnt) > 0);
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

    public boolean Scoped(long pEnt)
    {
        boolean bIsScoped = MemManager.Get().ReadBool(pEnt + m_bIsScoped);
        return bIsScoped;
    }

    public int nTickBase()
    {
        int iTickBase = MemManager.Get().ReadInt(LocalPlayer() + m_nTickBase);
        return iTickBase;
    }

    public long CurrentWeaponEnt(long pEnt)
    {
        long dwWeapAddress = MemManager.Get().ReadDWORD(pEnt + m_hActiveWeapon) & 0xFFF;
        long dwWeapEnt = MemManager.Get().ReadDWORD(MemManager.Get().Client() + dwEntityList + (dwWeapAddress - 0x01) * 0x10);
        return dwWeapEnt;
    }

    public EItemDefinitionIndex CurrentWeaponId(long pEnt)
    {
        long dwWeapEnt = CurrentWeaponEnt(pEnt);
        short iWeapId = MemManager.Get().ReadShort(dwWeapEnt + m_iItemDefinitionIndex);
        return EItemDefinitionIndex.valueOf(iWeapId);
    }

    public float GetCurrentWeaponNextAttack(long pEnt)
    {
        long dwWeapEnt = CurrentWeaponEnt(pEnt);
        float flNextPrimaryAttack = MemManager.Get().ReadInt(dwWeapEnt + m_flNextPrimaryAttack);
        return flNextPrimaryAttack;
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
        QAngle qViewAngles = new QAngle(MemManager.Get().ReadVector(dwCState + dwClientState_ViewAngles));
        return qViewAngles;
    }

    public void ClientViewAngles(QAngle qViewAngles)
    {
        long dwCState = MemManager.Get().ReadDWORD(MemManager.Get().Engine() + dwClientState);
        MemManager.Get().WriteAngles(dwCState + dwClientState_ViewAngles, qViewAngles);
    }

    public float IntervalPerTick()
    {
        long pGlobalVars = MemManager.Get().ReadDWORD(MemManager.Get().Engine() + dwGlobalVars);
        float flIntervalPerTick = MemManager.Get().ReadFloat(pGlobalVars + 0x20);
        return flIntervalPerTick;
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

        if(dwBoneMatrix == 0)
            return null;

        return MemManager.Get().ReadMatrix3x4Array(dwBoneMatrix);
    }

    public ArrayList<MStudioBox> Hitboxes(long pEnt)
    {
        long pStudioHdr = MemManager.Get().ReadDWORD(pEnt + m_pStudioHdr);

        if(pStudioHdr == 0)
            return null;

        long dwStudioHdr = MemManager.Get().ReadDWORD(pStudioHdr);

        if(dwStudioHdr == 0)
            return null;

        long dwModel = MemManager.Get().ReadDWORD(pEnt + 0x6C);

        String szModelName = MemManager.Get().ReadString(dwModel + 0x4, 128);

        if(szModelName.isEmpty())
            return null;

        return Parse(dwStudioHdr, szModelName);
    }

    private ArrayList<MStudioBox> Parse(long dwStudioHdr, String szModelName)
    {
        if(mapModelHitboxes.containsKey(szModelName))
            return mapModelHitboxes.get(szModelName);

        int HitboxSetIdx = MemManager.Get().ReadInt(dwStudioHdr + 0xB0);

        if(HitboxSetIdx < 0)
            return null;

        long StudioHitboxSet = dwStudioHdr + HitboxSetIdx;
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

    private HashMap<String, ArrayList<MStudioBox>> mapModelHitboxes = new HashMap<String, ArrayList<MStudioBox>>();
}
