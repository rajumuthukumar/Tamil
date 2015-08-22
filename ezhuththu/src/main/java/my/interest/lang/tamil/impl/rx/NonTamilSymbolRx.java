package my.interest.lang.tamil.impl.rx;

import my.interest.lang.tamil.internal.api.PatternGenerator;
import tamil.lang.TamilCharacter;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public class NonTamilSymbolRx implements PatternGenerator {
    public String generate() {
        return  "[\\u0000-\\u0B01\\u0BCC-\\uFFFF]";
    }

    public String getName() {
        return "!எழுத்துவடிவம்";
    }

    public String getDescription() {
        return "Characters outside Tamil block";
    }

    public boolean isCharacterSet() {
        return false;
    }

    public Set<TamilCharacter> getCharacterSet() {
        return null;
    }
}
