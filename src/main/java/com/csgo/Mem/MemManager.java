package com.csgo.Mem;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.Win32VK;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinBase;

public final class MemManager
{
    private final Kernel32 kernel32 = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    //private final static User32 user32 = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    private final int PROCESS_ALL_ACCESS = 0x1F1FFB;

    private final String szTarget = "csgo.exe";

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
    }

    public long Client()
    {
        return Pointer.nativeValue(pClient);
    }

    public long Engine()
    {
        return Pointer.nativeValue(pEngine);
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

    public int ReadInt(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Memory mOutput = new Memory(Integer.BYTES);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, Integer.BYTES, rRead);
        return mOutput.getInt(0);
    }

    public float ReadFloat(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Memory mOutput = new Memory(Float.BYTES);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, Float.BYTES, rRead);
        return mOutput.getFloat(0);
    }

    public boolean ReadBool(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Memory mOutput = new Memory(Byte.BYTES);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, Byte.BYTES, rRead);
        return mOutput.getByte(0) == 1;
    }

    public long ReadDWORD(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Memory mOutput = new Memory(DWORD.SIZE);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, DWORD.SIZE, rRead);
        return Pointer.nativeValue(mOutput.getPointer(0));
    }

    public void WriteInt(long dwAddress, int iValue)
    {
        Memory mOutput = new Memory(Integer.BYTES);
        mOutput.setInt(0, iValue);
        kernel32.WriteProcessMemory(hProc, new Pointer(dwAddress), mOutput, Integer.BYTES, null);
    }

    public void WriteFloat(long dwAddress, float flValue)
    {
        Memory mOutput = new Memory(Float.BYTES);
        mOutput.setFloat(0, flValue);
        kernel32.WriteProcessMemory(hProc, new Pointer(dwAddress), mOutput, Float.BYTES, null);
    }

    public void WriteBool(long dwAddress, boolean bValue)
    {
        Memory mOutput = new Memory(Byte.BYTES);
        mOutput.setByte(0, (byte)(bValue ? 1 : 0));
        kernel32.WriteProcessMemory(hProc, new Pointer(dwAddress), mOutput, Byte.BYTES, null);
    }

    public boolean isKeyPressed(Win32VK VK_KEY)
    {
        return (User32.INSTANCE.GetAsyncKeyState(VK_KEY.code) & 0x8000) != 0;
    }

    public static MemManager Get() {
        return INSTANCE;
    }

    private static final MemManager INSTANCE = new MemManager();
}
