package my.interest.lang.tamil.punar.handler.verrrrumai;

import tamil.lang.TamilCompoundCharacter;
import tamil.lang.TamilSimpleCharacter;
import tamil.lang.TamilWord;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public class V31Handler extends CommonHandler {

    public static final TamilWord oadu = new TamilWord(TamilSimpleCharacter.OO, TamilCompoundCharacter.IDD_U);
    @Override
    public TamilWord getUrubu() {
        return oadu;
    }

    @Override
    public int getNumber() {
        return 3;
    }

    @Override
    public String getName() {
        return "மூன்றாம்வேற்றுமை";
    }
}
