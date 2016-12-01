package test.letter;

import junit.framework.Assert;
import org.junit.Test;
import tamil.lang
        .TamilCharacter;
import tamil.lang.TamilFactory;

import java.util.Set;

/**
 * Created by velsubra on 11/30/16.
 */
public class LetterSetTest {

    static {
        TamilFactory.init();
    }

    @Test
    public void test0DirectVariable() {
     //   Set<TamilCharacter> set  = TamilFactory.getTamilCharacterSetCalculator().find("எழுத்து");
        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("எழுத்து");
        System.out.println(set1);
        Assert.assertEquals(247,set1.size());
    }

    @Test
    public void test1SimpleAdd() {
        //   Set<TamilCharacter> set  = TamilFactory.getTamilCharacterSetCalculator().find("எழுத்து");
        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("உயிர்+மெய்");
        System.out.println(set1);
        Assert.assertEquals(30,set1.size());
    }

    @Test
    public void test2SimpleAdd() {
        //   Set<TamilCharacter> set  = TamilFactory.getTamilCharacterSetCalculator().find("எழுத்து");
        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("மெய்+!மெய்");
        System.out.println(set1);
        Assert.assertEquals(247,set1.size());
    }

    @Test
    public void test2SimpleIntersection() {
        //   Set<TamilCharacter> set  = TamilFactory.getTamilCharacterSetCalculator().find("எழுத்து");
        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("உயிர்&மெய்");
        System.out.println(set1);
        Assert.assertEquals(0,set1.size());
    }

    @Test
    public void test3SimpleIntersection() {

        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("(uyir|(mey&uyir))");
        System.out.println(set1);
        Assert.assertEquals(12,set1.size());
    }

    @Test
    public void test4SimpleIntersection() {

        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("(uyir|mey)-uyir");
        System.out.println(set1);
        Assert.assertEquals(18,set1.size());
    }

    @Test
    public void test5SimpleIntersection() {

        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("!(uyir|mey)");
        System.out.println(set1);
        Assert.assertEquals(217,set1.size());
    }

    @Test
    public void test6SimpleIntersection() {

        Set<TamilCharacter> set1 = TamilFactory.getTamilCharacterSetCalculator().evaluate("!uyir|mey");
        System.out.println(set1);
        Assert.assertEquals(235,set1.size());
    }

}


