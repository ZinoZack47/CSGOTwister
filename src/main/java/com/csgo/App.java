package com.csgo;

import com.csgo.Features.*;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        for(;;)
        {
            Radar.Execute();
            RayTrig.Execute();
            FlickAWP.Execute();
            Thread.sleep(50);
        }
    }

}
