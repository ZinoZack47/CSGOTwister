package com.csgo.Mem;

import com.csgo.Features.FlickAWP;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.Structure;

public class CWMouseHook
{
    private final User32 USER32INST;
    private final Kernel32 KERNEL32INST;
    private CWMouseHook()
    {
        USER32INST = User32.INSTANCE;
        KERNEL32INST = Kernel32.INSTANCE;
        mouseHook = hookTheMouse();
        Native.setProtected(true);
    }

    private static LowLevelMouseProc mouseHook;
    private HHOOK hhk;
    private Thread thrd;
    private boolean threadFinish = true;
    private boolean isHooked = false;
    private static final int WM_MOUSEMOVE = 512;
    private static final int WM_LBUTTONDOWN = 513;
    private static final int WM_LBUTTONUP = 514;
    private static final int WM_RBUTTONDOWN = 516;
    private static final int WM_RBUTTONUP = 517;
    private static final int WM_MBUTTONDOWN = 519;
    private static final int WM_MBUTTONUP = 520;

    public void unsetMouseHook()
    {
        threadFinish = true;
        if (thrd.isAlive())
        {
            thrd.interrupt();
            thrd = null;
        }
        isHooked = false;
    }
    public boolean isIsHooked()
    {
        return isHooked;
    }
    public void setMouseHook()
    {
        thrd = new Thread(new Runnable() {
            @Override
            public void run()
                {
                    try
                    {
                        if(!isHooked)
                        {
                            hhk = USER32INST.SetWindowsHookEx(14, mouseHook, KERNEL32INST.GetModuleHandle(null), 0);
                            isHooked = true;
                            MSG msg = new MSG();
                            while ((USER32INST.GetMessage(msg, null, 0, 0)) != 0)
                            {
                                USER32INST.TranslateMessage(msg);
                                USER32INST.DispatchMessage(msg);
                                System.out.print(isHooked);
                                if (!isHooked)
                                    break;
                            }
                        }
                        else
                            System.out.println("The Hook is already installed.");
                    }
                    catch (Exception e)
                    {   System.err.println(e.getMessage());
                        System.err.println("Caught exception in MouseHook!");
                    }
            }
        },"Named thread");
        threadFinish = false;
        thrd.start();

    }
    private interface LowLevelMouseProc extends HOOKPROC
    {
        LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
    }
    public LowLevelMouseProc hookTheMouse()
    {
        return new LowLevelMouseProc()
        {
            @Override
            public LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT info) {
                if (nCode >= 0)
                {
                    switch(wParam.intValue())
                    {
                        case CWMouseHook.WM_LBUTTONDOWN:
                            FlickAWP.Execute();
                            break;
                        case CWMouseHook.WM_RBUTTONDOWN:
                            //do stuff
                            break;
                        case CWMouseHook.WM_MBUTTONDOWN:
                            //do other stuff
                            break;
                        case CWMouseHook.WM_LBUTTONUP:
                             //do even more stuff
                             break;
                        case CWMouseHook.WM_MOUSEMOVE:

                            break;
                        case CWMouseHook.WM_RBUTTONUP:

                            break;

                        case CWMouseHook.WM_MBUTTONUP:

                            break;
                        default:
                            break;
                    }
                     /****************************DO NOT CHANGE, this code unhooks mouse *********************************/
                    if (threadFinish == true)
                    {
                        USER32INST.PostQuitMessage(0);
                    }
                    /***************************END OF UNCHANGABLE *******************************************************/
                }
                return USER32INST.CallNextHookEx(hhk, nCode, wParam, new LPARAM(Pointer.nativeValue(info.getPointer())));
            }
        };
    }

    public class Point extends Structure
    {
        public class ByReference extends Point implements Structure.ByReference {};
        public NativeLong x;
        public NativeLong y;
    }

    public class MOUSEHOOKSTRUCT extends Structure
    {
        public class ByReference extends MOUSEHOOKSTRUCT implements Structure.ByReference {};
        public POINT pt;
        public HWND hwnd;
        public int wHitTestCode;
        public ULONG_PTR dwExtraInfo;
    }

    private static CWMouseHook INSTANCE = new CWMouseHook();
    public static CWMouseHook Get()
    {
        return INSTANCE;
    }
}
