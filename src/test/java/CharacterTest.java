import com.mrakaki.api.Character;
import com.mrakaki.api.Common;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

public class CharacterTest {

    @Test
    public void TestCharacterPublicInformation() {
        Common.SetUserAgent("MrAkaki Tests");
        var publicInfo = Character.getPublicInformation(2120599058);
        Assert.assertEquals(publicInfo.error(), Optional.empty());
        Assert.assertEquals(publicInfo.actualPage(), 0);
        Assert.assertEquals(publicInfo.totalPages(), 1);
        Assert.assertEquals(publicInfo.data().name(), "MrAkaki");
    }

    @Test
    public void TestCharacterPortrait() {
        Common.SetUserAgent("MrAkaki Tests");
        var portraitInfo = Character.getCharacterPortrait(2120599058);
        Assert.assertEquals(portraitInfo.error(), Optional.empty());
        Assert.assertEquals(portraitInfo.actualPage(), 0);
        Assert.assertEquals(portraitInfo.totalPages(), 1);
        Assert.assertEquals(portraitInfo.data().px512x512(), "https://images.evetech.net/characters/2120599058/portrait?tenant=tranquility&size=512");
    }
}
