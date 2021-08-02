package com.csgo;

import com.csgo.Features.*;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        FlickAWP.Start();
        for(;;)
        {
            Radar.Execute();
            RayTrig.Execute();
            Thread.sleep(50);
        }
    }

}
