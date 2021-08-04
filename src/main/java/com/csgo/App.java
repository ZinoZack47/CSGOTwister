package com.csgo;

import com.csgo.Features.*;

public final class App
{
    private App() {}

    public static void main(String[] args) throws Exception
    {
        FlickAWP.Setup();
        Thread glowThread = new Thread() {
            @Override
            public void run() {
                for(;;)
                {
                    Glow.Execute();
                    try
                    {
                        Thread.sleep(5);
                    }
                    catch(InterruptedException ex)
                    {
                        //Don't do anything
                    }
                }
            }
        };
        Thread restThread = new Thread() {
            @Override
            public void run() {
                for(;;)
                    {
                    Radar.Execute();
                    RayTrig.Execute();
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch(InterruptedException ex)
                    {
                        //Don't do anything
                    }
                }
            }
        };
        glowThread.start();
        restThread.start();
    }

}
