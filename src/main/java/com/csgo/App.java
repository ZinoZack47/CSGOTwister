package com.csgo;

import com.csgo.Features.Radar;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32VK;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        for(;;)
        {
            if((User32.INSTANCE.GetAsyncKeyState(Win32VK.VK_MENU.code) & 0x8000) != 0)
            {
                Radar.Execute();
                Thread.sleep(10);
            }
        }
    }

}
