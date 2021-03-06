package my.interest.lang.tamil.punar.handler.verrrrumai;

import my.interest.lang.tamil.punar.TamilWordPartContainer;
import my.interest.lang.tamil.punar.handler.AbstractPunharchiHandler;
import tamil.lang.TamilCompoundCharacter;
import tamil.lang.TamilWord;
import tamil.lang.known.derived.PeyarchCholThiribu;
import tamil.lang.known.non.derived.IPeyarchchol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public abstract class AbstractVearrrrumaiHandler extends AbstractPunharchiHandler {



    private static Map<TamilWord, PeyarchCholThiribu> pronoun_thiribu = new HashMap<TamilWord, PeyarchCholThiribu>();

    private static Set<TamilWord> thiribus = new java.util.HashSet<TamilWord>();


    public abstract TamilWord getUrubu();

    public abstract int getNumber();

//    private static TamilWord nteengalh = TamilWord.from("நீங்கள்");
//    private static TamilWord ungalh = TamilWord.from("உங்கள்");
//    private static TamilWord ntaangalh = TamilWord.from("நாங்கள்");
//    private static TamilWord engalh = TamilWord.from("எங்கள்");


    public static void addPeyarchCholThiribu(PeyarchCholThiribu t) {
        pronoun_thiribu.put(t.getRealWord().getWord(), t);
        thiribus.add(t.getWord());
    }

    private static Map<TamilWord, IPeyarchchol> uyarthinais = new HashMap<TamilWord, IPeyarchchol>();

    public static void addUyarThinai(IPeyarchchol uyar) {
        uyarthinais.put(uyar.getWord(), uyar);
    }

    public static boolean isUyarThinhaipPeyar(TamilWord w) {
        return uyarthinais.containsKey(w);
    }


    /**
     * Naan -> en
     *
     * @param word
     * @return
     */
    public static TamilWord getProNounThiribu(TamilWord word) {
//
//        System.out.println("pronoun_thiribu:"+ pronoun_thiribu.size());
//        System.out.println(pronoun_thiribu);
        PeyarchCholThiribu t = pronoun_thiribu.get(word);
        if (t != null) {
            return t.getWord();
        }

        if (thiribus.contains(word)) {
            return word;
        }

        return null;

    }


    public abstract boolean isOnRightViguthi(TamilWordPartContainer varum);

    public TamilWordPartContainer translateForProNoun(TamilWordPartContainer nilai) {
        return nilai;
    }

    public abstract TamilWordPartContainer handleJoin(TamilWordPartContainer nilai, TamilWordPartContainer varum, boolean pronoun);

    @Override
    public TamilWordPartContainer join(TamilWordPartContainer nilai, TamilWordPartContainer varum) {
        if(!nilai.isEndingFine()) return null;
        if (!isOnRightViguthi(varum)) return null;

        TamilWordPartContainer v = varum;
        TamilWordPartContainer n = nilai;
        boolean pronouwn = false;
        TamilWord t = getProNounThiribu(nilai.getWord());
        if (t != null) {
            pronouwn = true;
            nilai = new TamilWordPartContainer(t);
        }

        if (pronouwn) {
            n = translateForProNoun(nilai);
        }

        return handleJoin(n, v, pronouwn);

    }

    protected boolean endsWithNedilAndIM(TamilWordPartContainer nilai) {
        if (nilai.size() < 2) {
            return false;

        }
        return  nilai.getWord().getLast() == TamilCompoundCharacter.IM && nilai.getWord().get(nilai.size()-2).asTamilCharacter().isNtedilezhuththu();
    }
}