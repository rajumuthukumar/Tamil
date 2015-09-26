package my.interest.lang.tamil.impl.rx.asai2;

import my.interest.lang.tamil.impl.FeatureSet;
import my.interest.lang.tamil.impl.yaappu.YaappuBaseRx;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public final class Karuvilham extends YaappuBaseRx {

    public Karuvilham() {
        super("கருவிளம்");
    }
    public String generate(FeatureSet featureSet) {
        return  "(?:${ntirai}{2})";
    }
}