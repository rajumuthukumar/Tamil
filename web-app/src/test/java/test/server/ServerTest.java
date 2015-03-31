package test.server;


import com.sun.jersey.api.container.grizzly2.servlet.GrizzlyWebContainerFactory;
import com.sun.jersey.api.json.JSONConfiguration;
import tamil.lang.TamilCharacterLookUpContext;
import tamil.lang.TamilCompoundCharacter;
import tamil.lang.TamilSimpleCharacter;
import tamil.lang.TamilWord;
import my.interest.lang.tamil.multi.ExecuteManager;
import my.interest.lang.tamil.translit.EnglishToTamilCharacterLookUpContext;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */

public class ServerTest {


    @Test
    public void testStartServe0() throws Exception {

        TamilWord w = TamilWord.from("செந்தமிழ்ஃ");
        System.out.println("getCharacterTypeDigest\t: " + w.getCharacterTypeDigest().toString());
        System.out.println("getConsonantDigest\t\t: " + w.getConsonantDigest().toString());
        System.out.println("getSoundSizeDigest\t\t: " + w.getSoundSizeDigest().toString());
        System.out.println("getSoundStrengthDigest\t: " + w.getSoundStrengthDigest().toString());
        System.out.println("getVowelDigest\t\t\t: " + w.getVowelDigest().toString());


      w = TamilWord.from("தமிழ்");
        System.out.println(w.suggestionHashCode());

        w = TamilWord.from("தமில்");
        System.out.println(w.suggestionHashCode());

        w = TamilWord.from("பெண்ணடிமைத்தனத்தைப்போற்றினானென்று");
        System.out.println(w.suggestionHashCode());

        w = TamilWord.from("பெண்ணடிமைத்தனத்தைப்போற்றி");
        System.out.println(w.suggestionHashCode());


        w = EnglishToTamilCharacterLookUpContext.getBestMatch("palli");
        System.out.println(w.suggestionHashCode());

        w = EnglishToTamilCharacterLookUpContext.getBestMatch("palhlhi");
        System.out.println(w.suggestionHashCode());

    }

    @Test
    public void testStartServe1() throws Exception {
        System.out.println();

        System.out.println(TamilSimpleCharacter.AKTHU.getNumericStrength());
        Assert.assertEquals(1,TamilSimpleCharacter.AKTHU.getNumericStrength());
        System.out.println(TamilSimpleCharacter.a.getNumericStrength());
        Assert.assertEquals(201,TamilSimpleCharacter.a.getNumericStrength());
        System.out.println(TamilSimpleCharacter.aa.getNumericStrength());
        System.out.println(TamilSimpleCharacter.OU.getNumericStrength());
        Assert.assertEquals(1701,TamilSimpleCharacter.OU.getNumericStrength());

        System.out.println();

        System.out.println(TamilCompoundCharacter.IK.getNumericStrength());
        Assert.assertEquals(1800, TamilCompoundCharacter.IK.getNumericStrength());
        System.out.println(TamilSimpleCharacter.KA.getNumericStrength());
        Assert.assertEquals(1801,TamilSimpleCharacter.KA.getNumericStrength());

        System.out.println(TamilCompoundCharacter.IK_aa.getNumericStrength());
        System.out.println(TamilCompoundCharacter.IK_OU.getNumericStrength());
        Assert.assertEquals(1816,TamilCompoundCharacter.IK_OU.getNumericStrength());

        System.out.println(TamilCompoundCharacter.IN_OU.getNumericStrength());
        Assert.assertEquals(3816,TamilCompoundCharacter.IN_OU.getNumericStrength());

    }

    @Test
    public void testStartServer() throws Exception {
        TamilCharacterLookUpContext.lookup(0);

      if (true) return;
        final String baseUri = "http://localhost:8080/xyz";
        final Map<String, String> initParams =
                new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "my.interest.tamil.rest.resources.api, my.interest.tamil.rest.resources.apps,my.interest.tamil.rest.resources,my.interest.tamil.rest.resources.filters");
        initParams.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

        System.out.println("Starting grizzly...");
        HttpServer threadSelector =
                GrizzlyWebContainerFactory.create(baseUri, initParams);
        threadSelector.start();

        System.in.read();
        ExecuteManager.stop();
        threadSelector.stop();
        System.exit(0);
    }
}
