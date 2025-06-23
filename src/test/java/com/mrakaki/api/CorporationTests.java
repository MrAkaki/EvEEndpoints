package com.mrakaki.api;

import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;

public class CorporationTests {

    @Test
    public void testGetPublicInformation() {
        Common.SetUserAgent("MrAkaki Tests");
        var publicInfo = Corporation.getPublicInformation(98659319);
        assertEquals(publicInfo.error(), Optional.empty());
        assertEquals(publicInfo.actualPage(), 0);
        assertEquals(publicInfo.totalPages(), 1);
        assertEquals(publicInfo.data().name(), "Suspicious Intentions");
    }

    @Test
    public void testGetIcons() {
        Common.SetUserAgent("MrAkaki Tests");
        var icons = Corporation.getIcons(98659319);
        assertEquals(icons.error(), Optional.empty());
        assertEquals(icons.actualPage(), 0);
        assertEquals(icons.totalPages(), 1);
        assertNotNull(icons.data().px64x64());
        assertNotNull(icons.data().px128x128());
    }

    @Test
    public void testGetAllianceHistory() {
        Common.SetUserAgent("MrAkaki Tests");
        var allianceHistory = Corporation.getAllianceHistory(98659319);
        assertEquals(allianceHistory.error(), Optional.empty());
        assertEquals(allianceHistory.actualPage(), 0);
        assertTrue(allianceHistory.totalPages() > 0);
        assertFalse(allianceHistory.data().isEmpty());
        assertNotNull(allianceHistory.data().get(0).start_date());
        assertNotNull(allianceHistory.data().get(0).alliance_id());
    }

    @Test
    public void testGetNpcCorporations() {
        Common.SetUserAgent("MrAkaki Tests");
        var npcCorporations = Corporation.getNpcCorporations();
        assertEquals(npcCorporations.error(), Optional.empty());
        assertTrue(npcCorporations.actualPage() >= 0);
        assertTrue(npcCorporations.totalPages() > 0);
        assertFalse(npcCorporations.data().isEmpty());
    }
}