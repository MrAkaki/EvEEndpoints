package com.mrakaki.api;

import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ZKillboardTests {
    @Test
    public void testZKillboard() throws InterruptedException {
        var zKillboard = new ZKillboard();
        AtomicBoolean messageReceived = new AtomicBoolean(false);
        zKillboard.connect();
        assertEquals(zKillboard.getStatus(), ZKillboard.ClientStatus.CONNECTED);
        zKillboard.subscribe(ZKillboard.ChannelFilterType.ALL, "*", (JSONObject obj) -> {
            System.out.println("Received message: " + obj.toString(2));
            messageReceived.set(true);
        });
        int times = 0;
        while (zKillboard.getStatus().equals(ZKillboard.ClientStatus.CONNECTED) && !messageReceived.get() && times < 12) {
            times++;
            System.out.println("Waiting for messages... Attempt " + times);
            Thread.sleep(10*1000); // Wait for messages to be received
        }
        zKillboard.disconnect();
        assertEquals(zKillboard.getStatus(), ZKillboard.ClientStatus.DISCONNECTED);
        assertTrue(messageReceived.get());
    }
}
