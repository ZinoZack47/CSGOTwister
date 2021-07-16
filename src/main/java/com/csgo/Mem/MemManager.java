package com.csgo.Mem;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinBase;

public final class MemManager
{
    private final static Kernel32 kernel32 = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    private final static User32 user32 = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    private final static int PROCESS_VM_READ= 0x0010;
    private final static int PROCESS_VM_WRITE = 0x0020;
    private final static int PROCESS_VM_OPERATION = 0x0008;

    private HANDLE hProc = null;
    private Pointer pClient = null;
    private DWORD dwClientSize = null;

    private MemManager() {
        int pid = GetProcessId("csgo.exe");
        hProc = OpenProcess(pid);
        Tlhelp32.MODULEENTRY32W modClient = CatchModule(pid, "client.dll");

        if(modClient == null)
        {
            System.out.println("Couldn't find client module");
            System.exit(1);
        }

        pClient = modClient.modBaseAddr;
        dwClientSize = modClient.dwSize;
    }

    public Pointer Client()
    {
        return pClient;
    }

    public long ClientSize()
    {
        return dwClientSize.longValue();
    }

    public HANDLE Proc()
    {
        return hProc;
    }

    private Tlhelp32.MODULEENTRY32W CatchModule(int pid, String module)
    {
        Tlhelp32.MODULEENTRY32W MEntry = new Tlhelp32.MODULEENTRY32W.ByReference();
        HANDLE hHandle = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE, new DWORD(pid));

        if (hHandle == WinBase.INVALID_HANDLE_VALUE) {
            return null;
        }

        Tlhelp32.MODULEENTRY32W match = null;

        if (Kernel32.INSTANCE.Module32FirstW(hHandle, MEntry)) {
            do {
                if (module.equals(new String(MEntry.szModule))) {
                    match = MEntry;
                    break;
                }
            } while (Kernel32.INSTANCE.Module32NextW(hHandle, MEntry));
        }

        Kernel32.INSTANCE.CloseHandle(hHandle);
        return match;
    }

    private static int GetProcessId(String window)
    {
        IntByReference pid = new IntByReference(0);
        user32.GetWindowThreadProcessId(user32.FindWindow(null, window), pid);

        return pid.getValue();
    }

    private static HANDLE OpenProcess(int pid)
    {
        HANDLE hProc = kernel32.OpenProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION, true, pid);
        return hProc;
    }

    public static Memory RPM(HANDLE Process, Pointer dwAdress, int bytesToRead)
    {
        IntByReference read = new IntByReference(0);
        Memory mOutput = new Memory(bytesToRead);
        kernel32.ReadProcessMemory(Process, dwAdress, mOutput, bytesToRead, read);
        return mOutput;
    }

    public static boolean WPM(HANDLE Process, Pointer dwAddress, byte[] data)
    {
        int size = data.length;
        Memory toWrite = new Memory(size);

        for(int i = 0; i < size; i++)
        {
            toWrite.setByte(i, data[i]);
        }

        return kernel32.WriteProcessMemory(Process, dwAddress, toWrite, size, null);
    }

    public static MemManager Get() {
        return INSTANCE;
    }

    private static final MemManager INSTANCE = new MemManager();
}
