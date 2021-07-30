package com.csgo.Utils.Enums;

public enum EWeaponType
{
    WEAPONTYPE_GLOVE,
	WEAPONTYPE_KNIFE,
	WEAPONTYPE_PISTOL,
	WEAPONTYPE_SUBMACHINEGUN,
	WEAPONTYPE_RIFLE,
	WEAPONTYPE_SHOTGUN,
	WEAPONTYPE_SNIPER,
	WEAPONTYPE_MACHINEGUN,
	WEAPONTYPE_HEAVYPISTOL,
	WEAPONTYPE_AUTONOOB,
    WEAPONTYPE_GRENADE,
    WEAPONTYPE_OTHER;

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
        case WEAPON_NEGEV:
        case WEAPON_M249: return WEAPONTYPE_MACHINEGUN;
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
        case WEAPON_FISTS:
        case WEAPON_KNIFE_T:
        case WEAPON_KNIFE:
        case WEAPON_MELEE:
        case WEAPON_AXE:
        case WEAPON_HAMMER:
        case WEAPON_KNIFE_BAYONET:
        case WEAPON_KNIFE_KARAMBIT:
        case WEAPON_KNIFE_CANIS:
        case WEAPON_KNIFE_BUTTERFLY:
        case WEAPON_KNIFE_M9_BAYONET:
        case WEAPON_KNIFE_CORD:
        case WEAPON_KNIFE_FALCHION:
        case WEAPON_KNIFE_CSS:
        case WEAPON_KNIFE_GUT:
        case WEAPON_KNIFE_GG:
        case WEAPON_KNIFE_FLIP:
        case WEAPON_KNIFE_GHOST:
        case WEAPON_KNIFE_GYPSY_JACKKNIFE:
        case WEAPON_KNIFE_OUTDOOR:
        case WEAPON_KNIFE_PUSH:
        case WEAPON_KNIFE_TACTICAL:
        case WEAPON_KNIFE_SKELETON:
        case WEAPON_KNIFE_STILETTO:
        case WEAPON_KNIFE_URSUS:
        case WEAPON_KNIFE_SURVIVAL_BOWIE:
        case WEAPON_KNIFE_WIDOWMAKER:
        case WEAPON_SPANNER: return WEAPONTYPE_KNIFE;
        case WEAPON_FRAG_GRENADE:
        case WEAPON_DECOY:
        case WEAPON_DIVERSION:
        case WEAPON_TAGRENADE:
        case WEAPON_HEGRENADE:
        case WEAPON_INCGRENADE:
        case WEAPON_SMOKEGRENADE:
        case WEAPON_MOLOTOV:
        case WEAPON_FIREBOMB:
        case WEAPON_SNOWBALL:
        case WEAPON_FLASHBANG: return WEAPONTYPE_GRENADE;
        case GLOVE_CT:
        case GLOVE_T:
        case GLOVE_HYDRA:
        case GLOVE_LEATHER_HANDWRAPS:
        case GLOVE_SLICK:
        case GLOVE_SPECIALIST:
        case GLOVE_SPORTY:
        case GLOVE_MOTORCYCLE:
        case GLOVE_STUDDED_BLOODHOUND: return WEAPONTYPE_GLOVE;
        default: return WEAPONTYPE_OTHER;
        }
    }

    public int Index()
    {
        return ordinal();
    }
};
