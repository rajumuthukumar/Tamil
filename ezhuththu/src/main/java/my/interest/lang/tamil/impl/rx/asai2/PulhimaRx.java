package my.interest.lang.tamil.impl.rx.asai2;

import my.interest.lang.tamil.impl.yaappu.AsaiRx;
import my.interest.lang.tamil.internal.api.PatternGenerator;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public final class PulhimaRx  extends AsaiRx {

    public PulhimaRx() {
        super("புளிமா");
    }
    public String generate() {
        return  "(${ntirai}${ntear})";
    }
}