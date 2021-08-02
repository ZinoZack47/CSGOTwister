package com.csgo;

import com.csgo.Features.*;
import com.csgo.Mem.CWMouseHook;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        CWMouseHook.Get().setMouseHook();
        for(;;)
        {
            Radar.Execute();
            RayTrig.Execute();
            Thread.sleep(50);
        }
    }

}
