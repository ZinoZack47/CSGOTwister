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

import com.csgo.Utils.*;
import com.csgo.Utils.Enums.EHitboxIndex;

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

    public short ReadShort(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Memory mOutput = new Memory(Short.BYTES);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, Short.BYTES, rRead);
        return mOutput.getShort(0);
    }

    public Vector ReadVector(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        int iSize = Float.BYTES * 3;
        Memory mOutput = new Memory(iSize);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, iSize, rRead);
        return new Vector(mOutput.getFloat(0), mOutput.getFloat(0x4), mOutput.getFloat(0x8));
    }

    public String ReadString(long dwAddress, int iLen)
    {
        IntByReference rRead = new IntByReference(0);
        int iSize = iLen;
        Memory mOutput = new Memory(iSize);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, iSize, rRead);
        return mOutput.getString(0);
    }

    public MStudioBox ReadMStudioBox(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        int iSize = Integer.BYTES * 3 + Float.BYTES * 3 * 3 + Float.BYTES;
        Memory mOutput = new Memory(iSize);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, iSize, rRead);
        return new MStudioBox(
            mOutput.getInt(0),
            mOutput.getInt(0x4),
            new Vector(
                mOutput.getFloat(0x8),
                mOutput.getFloat(0xC),
                mOutput.getFloat(0x10)
            ),
            new Vector(
                mOutput.getFloat(0x14),
                mOutput.getFloat(0x18),
                mOutput.getFloat(0x1C)
            ),
            mOutput.getInt(0x20),
            new Vector(
                mOutput.getFloat(0x24),
                mOutput.getFloat(0x28),
                mOutput.getFloat(0x2C)
            ),
            mOutput.getFloat(0x30)
        );
    }

    public Matrix3x4[] ReadMatrix3x4Array(long dwAddress)
    {
        IntByReference rRead = new IntByReference(0);
        Matrix3x4[] matResult = new Matrix3x4[EHitboxIndex.MAXSTUDIOBONES];
        int iSize = EHitboxIndex.MAXSTUDIOBONES * Matrix3x4.SIZE;
        Memory mOutput = new Memory(iSize);
        kernel32.ReadProcessMemory(hProc, new Pointer(dwAddress), mOutput, iSize, rRead);

        for(int i = 0; i < EHitboxIndex.MAXSTUDIOBONES; i++)
            matResult[i] = new Matrix3x4(mOutput.getFloatArray(i * Matrix3x4.SIZE, Matrix3x4.Length));

        return matResult;
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

    public void WriteAngles(long dwAddress, QAngle qAngles)
    {
        Memory mOutput = new Memory(Float.BYTES * 3);
        mOutput.setFloat(0, qAngles.Pitch());
        mOutput.setFloat(0x4, qAngles.Yaw());
        mOutput.setFloat(0x8, qAngles.Roll());
        kernel32.WriteProcessMemory(hProc, new Pointer(dwAddress), mOutput, Float.BYTES * 3, null);
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
