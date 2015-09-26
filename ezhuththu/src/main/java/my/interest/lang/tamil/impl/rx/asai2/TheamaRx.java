package my.interest.lang.tamil.impl.rx.asai2;

import my.interest.lang.tamil.impl.FeatureSet;
import my.interest.lang.tamil.impl.yaappu.YaappuBaseRx;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public class TheamaRx extends YaappuBaseRx {

    public TheamaRx() {
        super("தேமா");
    }
    public String generate(FeatureSet featureSet) {
        return  "(?:(?!(?:${ntirai}))(?:${ntear}{2}))";
        //return  "(?:(?!(?:${ntirai}))(?:${ntear}{2})|(?:${kurril}${mey}+${ntear})|(?:${ntedil}${mey}+${ntear}))";
    }
}