package com.csgo;

import com.csgo.Features.Radar;
//import com.sun.jna.platform.win32.User32;
//import com.sun.jna.platform.win32.Win32VK;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        for(;;)
        {
            Radar.Execute();
            Thread.sleep(200);
        }
    }

}
