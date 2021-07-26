package com.csgo.Utils.Enums;

public enum EWeaponType
{
	WEAPONTYPE_KNIFE,
	WEAPONTYPE_PISTOL,
	WEAPONTYPE_SUBMACHINEGUN,
	WEAPONTYPE_RIFLE,
	WEAPONTYPE_SHOTGUN,
	WEAPONTYPE_SNIPER,
	WEAPONTYPE_MACHINEGUN,
	WEAPONTYPE_HEAVYPISTOL,
	WEAPONTYPE_AUTONOOB;

    public static EWeaponType WeaponTypeFromId(EItemDefinitionIndex WeaponId)
    {
        switch(WeaponId)
        {
        case WEAPON_ELITE:
        case WEAPON_FIVESEVEN:
        case WEAPON_GLOCK:
        case WEAPON_TEC9:
        case WEAPON_HKP2000:
        case WEAPON_P250:
        case WEAPON_USP_SILENCER:
        case WEAPON_CZ75A: return WEAPONTYPE_PISTOL;
        case WEAPON_MAC10:
        case WEAPON_P90:
        case WEAPON_MP5SD:
        case WEAPON_UMP45:
        case WEAPON_BIZON:
        case WEAPON_MP7:
        case WEAPON_MP9: return WEAPONTYPE_SUBMACHINEGUN;
        case WEAPON_AK47:
        case WEAPON_AUG:
        case WEAPON_FAMAS:
        case WEAPON_GALILAR:
        case WEAPON_M4A1:
        case WEAPON_SG556:
        case WEAPON_M4A1_SILENCER: return WEAPONTYPE_RIFLE;
        case WEAPON_XM1014:
        case WEAPON_MAG7:
        case WEAPON_SAWEDOFF:
        case WEAPON_NOVA: return WEAPONTYPE_SHOTGUN;
        case WEAPON_AWP:
        case WEAPON_SSG08: return WEAPONTYPE_SNIPER;
        case WEAPON_G3SG1:
        case WEAPON_SCAR20: return WEAPONTYPE_AUTONOOB;
        case WEAPON_DEAGLE:
        case WEAPON_REVOLVER: return WEAPONTYPE_HEAVYPISTOL;
        case WEAPON_KNIFE_T:
        case WEAPON_KNIFE:
        case WEAPON_MELEE:
        case WEAPON_AXE:
        case WEAPON_HAMMER:
        case WEAPON_SPANNER: return WEAPONTYPE_KNIFE;
        default: return null;
        }
    }

    public int Index()
    {
        return ordinal();
    }
};
