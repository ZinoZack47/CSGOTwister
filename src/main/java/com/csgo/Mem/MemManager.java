package com.csgo.Mem;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
//import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinBase;

public final class MemManager
{
    private final static Kernel32 kernel32 = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    //private final static User32 user32 = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    private final static int PROCESS_ALL_ACCESS = 0x1F1FFB;

    private final String szTarget = "csgo.exe";
    private final boolean bDebugMode = true;

    private HANDLE hProc = null;
    private Pointer pClient = null;
    private Pointer pEngine = null;
    private int iPid = -1;

    private MemManager()
    {
        if(!CatchGame(szTarget))
        {
            System.out.println("Couldn't find " + szTarget);
            System.exit(1);
        }

        if((pClient = CatchModule(iPid, "client.dll")) == null)
        {
            System.out.println("Couldn't find client module");
            System.exit(1);
        }

        if((pEngine = CatchModule(iPid, "engine.dll")) == null)
        {
            System.out.println("Couldn't find engine module");
            System.exit(1);
        }

        if(bDebugMode)
        {
            System.out.println("Client at: " + pClient.toString());
            System.out.println("Engine at: " + pEngine.toString());
        }
    }

    public Pointer Client()
    {
        return pClient;
    }

    public Pointer Engine()
    {
        return pEngine;
    }

    public HANDLE Proc()
    {
        return hProc;
    }

    private Pointer CatchModule(int pid, String module)
    {
        Tlhelp32.MODULEENTRY32W MEntry = new Tlhelp32.MODULEENTRY32W.ByReference();
        HANDLE hHandle = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE, new DWORD(pid));

        if (hHandle == WinBase.INVALID_HANDLE_VALUE) {
            System.out.println("Invalid Handle: " + Kernel32.INSTANCE.GetLastError());
            return null;
        }

        try
        {
            while (Kernel32.INSTANCE.Module32NextW(hHandle, MEntry))
            {
                if (module.equals(MEntry.szModule()))
                {
                    return MEntry.modBaseAddr;
                }
            }
        }
        finally
        {
            Kernel32.INSTANCE.CloseHandle(hHandle);
        }

        return null;
    }

    private boolean CatchGame(String process)
    {
        Tlhelp32.PROCESSENTRY32 MEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        HANDLE hHandle = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new DWORD());

        try
        {
            if (Kernel32.INSTANCE.Process32First(hHandle, MEntry))
            {
                while (Kernel32.INSTANCE.Process32Next(hHandle, MEntry))
                {
                    if (Native.toString(MEntry.szExeFile).equals(process))
                    {
                        iPid = MEntry.th32ProcessID.intValue();
                        hProc = kernel32.OpenProcess(PROCESS_ALL_ACCESS,
                        false, iPid);
                        return true;
                    }
                }
            }
        }

        finally
        {
            Kernel32.INSTANCE.CloseHandle(hHandle);
        }

        return false;
    }

    public static Memory RPM(HANDLE Process, Pointer dwAddress, int bytesToRead)
    {
        IntByReference read = new IntByReference(0);
        Memory mOutput = new Memory(bytesToRead);
        kernel32.ReadProcessMemory(Process, dwAddress, mOutput, bytesToRead, read);
        return mOutput;
    }

    public static boolean WPM(HANDLE Process, Pointer dwAddress, Memory toWrite, int size)
    {
        return kernel32.WriteProcessMemory(Process, dwAddress, toWrite, size, null);
    }

    public static MemManager Get() {
        return INSTANCE;
    }

    private static final MemManager INSTANCE = new MemManager();
}
