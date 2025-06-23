package com.mrakaki.api;

import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;

public class AllianceTest {

    @Test
    public void testGetPublicInformation() {
        Common.SetUserAgent("MrAkaki Tests");
        var response = Alliance.getPublicInformation(1354830081);
        assertEquals(response.error(), Optional.empty());
        assertNotNull(response.data());
        assertEquals(response.data().name(), "Goonswarm Federation");
    }

    @Test
    public void testGetAllAlliances() {
        Common.SetUserAgent("MrAkaki Tests");
        var response = Alliance.getAllAlliances();
        assertEquals(response.error(), Optional.empty());
        assertTrue(response.data().size() > 0);
        assertTrue(response.data().contains(1354830081)); // Goonswarm Federation
    }

    @Test
    public void testGetAllianceCorporations() {
        Common.SetUserAgent("MrAkaki Tests");
        var response = Alliance.getAllianceCorporations(1354830081);
        assertEquals(response.error(), Optional.empty());
        assertTrue(!response.data().isEmpty());
        assertTrue(response.data().contains(98659319)); // Example corporation ID
    }

    @Test
    public void testGetIcons() {
        Common.SetUserAgent("MrAkaki Tests");
        var response = Alliance.getIcons(1354830081);
        assertEquals(response.error(), Optional.empty());
        assertNotNull(response.data());
        assertNotNull(response.data().px64x64());
        assertNotNull(response.data().px128x128());
    }
}