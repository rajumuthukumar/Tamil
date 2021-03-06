package tamil.lang;


import common.lang.SimpleCharacter;
import my.interest.lang.tamil.impl.FeatureSet;
import my.interest.lang.tamil.impl.rx.TamilGlyphRx;
import tamil.lang.api.regex.RXIncludeCanonicalEquivalenceFeature;
import tamil.lang.api.regex.RXKeLaAsKouFeature;
import tamil.lang.exception.NoMeiPartException;
import tamil.lang.exception.NoUyirPartException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Simple characters are those that are represented with a single code
 * http://www.unicode.org/charts/PDF/U0B80.pdf
 * http://en.wikipedia.org/wiki/Tamil_script
 * <p/>
 * <pre>
 * Represents all two bytes unicode characters
 * அ -  ஔ    12
 * க - ன      18
 * ஃ           1
 *
 * </pre>
 *
 * @author velsubra
 * @see TamilCharacterLookUpContext#lookup(int)
 * @see TamilWord#from(String)
 */
public final class TamilSimpleCharacter extends TamilCharacter implements SimpleCharacter {

    static List<TamilSimpleCharacter> simplecharacters = new ArrayList<TamilSimpleCharacter>();
    private String eng = null;

    private TamilSimpleCharacter(int value, String eng) {
        this.value = value;
        this.eng = eng;
        simplecharacters.add(this);

    }


    void init() {
        this.typeSpecification |= _isUyirezhuththu() ? UYIR : 0;
        this.typeSpecification |= _isUyirMeyyezhuththu() ? UYIR_MEI : 0;
        this.typeSpecification |= _isMeyyezhuththu() ? MEI : 0;
        this.typeSpecification |= _isAaythavezhuththu() ? AAYTHAM : 0;
        this.typeSpecification |= _isVadaMozhiYezhuththu() ? VADA_MOZHI : 0;
        this.typeSpecification |= _isKurilezhuththu() ? KURIL : 0;
        this.typeSpecification |= !_isKurilezhuththu() && !_isAaythavezhuththu() ? NEDIL : 0;
        this.typeSpecification |= _isVallinam() ? VALLINAM : 0;
        this.typeSpecification |= _isMellinam() ? MELLINAM : 0;
        this.typeSpecification |= _isIdaiyanam() ? IDAIYINAM : 0;

    }


    @Override
    public int getValue() {
        return value;
    }


    private int value;


    private boolean _isMeyyezhuththu() {
        return false;
    }

    private boolean _isAaythavezhuththu() {
        return isAaythavezhuththu(getValue());
    }


    public static final TamilSimpleCharacter AKTHU = new TamilSimpleCharacter('\u0B83', "ak");

    public static boolean isAaythavezhuththu(int value) {
        return value == AKTHU.value;
    }


    private boolean _isUyirezhuththu() {
        return isUyirezhuththu(this.value);

    }

    public static boolean isUyirezhuththu(int val) {
        return isa(val) || isaa(val) || isE(val) || isEE(val) || isU(val) || isUU(val) || isA(val) || isAA(val) || isI(val) || isO(val) || isOO(val) || isOU(val);

    }


    //அ
    public boolean isa() {
        return isa(getValue());
    }


    // ஆ
    public boolean isaa() {
        return isaa(getValue());
    }

    //3 e   இ
    public boolean isE() {
        return isE(getValue());
    }

    //4 ee   ஈ
    public boolean isEE() {
        return isEE(getValue());
    }

    //5 u   உ
    public boolean isU() {
        return isU(getValue());
    }

    //6 uu ஊ
    public boolean isUU() {
        return isUU(getValue());
    }

    //7 A எ
    public boolean isA() {
        return isA(getValue());
    }

    //8 AA ஏ
    public boolean isAA() {
        return isAA(getValue());
    }

    //9 i  ஐ
    public boolean isI() {
        return isI(getValue());
    }

    //10 o  ஒ
    public boolean isO() {
        return isO(getValue());
    }

    //11  oo  ஓ
    public boolean isOO() {
        return isOO(getValue());
    }

    //12 OU  ஔ
    public boolean isOU() {
        return isOU(getValue());
    }


    public static final TamilSimpleCharacter a = new TamilSimpleCharacter('\u0B85', "a");

    //அ
    public static boolean isa(int value) {
        return value == a.value;
    }

    public static final TamilSimpleCharacter aa = new TamilSimpleCharacter('\u0B86', "aa");

    // ஆ
    public static boolean isaa(int value) {
        return value == aa.value;
    }

    public static final TamilSimpleCharacter E = new TamilSimpleCharacter('\u0B87', "i");

    //3 e   இ
    public static boolean isE(int value) {
        return value == E.value;
    }

    public static final TamilSimpleCharacter EE = new TamilSimpleCharacter('\u0B88', "ee");

    //4 ee   ஈ
    public static boolean isEE(int value) {
        return value == EE.value;
    }

    public static final TamilSimpleCharacter U = new TamilSimpleCharacter('\u0B89', "u");

    //5 u   உ
    public static boolean isU(int value) {
        return value == U.value;
    }

    public static final TamilSimpleCharacter UU = new TamilSimpleCharacter('\u0B8A', "oo");

    //6 uu ஊ
    public static boolean isUU(int value) {
        return value == UU.value;
    }

    public static final TamilSimpleCharacter A = new TamilSimpleCharacter('\u0B8E', "e");

    //7 A எ
    public static boolean isA(int value) {
        return value == A.value;
    }

    public static final TamilSimpleCharacter AA = new TamilSimpleCharacter('\u0B8F', "ea");

    //8 AA ஏ
    public static boolean isAA(int value) {
        return value == AA.value;
    }

    public static final TamilSimpleCharacter I = new TamilSimpleCharacter('\u0B90', "ai");

    //9 i  ஐ
    public static boolean isI(int value) {
        return value == I.value;
    }

    public static final TamilSimpleCharacter O = new TamilSimpleCharacter('\u0B92', "o");

    //10 o  ஒ
    public static boolean isO(int value) {
        return value == O.value;
    }

    public static final TamilSimpleCharacter OO = new TamilSimpleCharacter('\u0B93', "oa");

    //11  oo  ஓ
    public static boolean isOO(int value) {
        return value == OO.value;
    }

    public static final TamilSimpleCharacter OU = new TamilSimpleCharacter('\u0B94', "ou");

    //12 OU  ஔ
    public static boolean isOU(int value) {
        return value == OU.value;
    }

    @Override
    public String toString() {
        return Character.toString((char) getValue());
    }


    /**
     * Translates the real vowel into continuation Vowel.
     *
     * @return the getContinuationVowel, glyphs
     */
    int getContinuationVowel() {
        int val = getValue() + TamilCompoundCharacter.aa - TamilSimpleCharacter.aa.value;
        if (val >= TamilCompoundCharacter.aa && val <= TamilCompoundCharacter.OU) {
            return val;
        } else {
            throw new RuntimeException(getValue() + ":" + Integer.toHexString(getValue()) + " is not a valid vowel for extension.");
        }
    }


    //1 க
    public boolean isKa() {
        return isKa(getValue());
    }

    // 2  ங
    public boolean isNga() {
        return isNga(getValue());
    }

    // 3 ச
    public boolean isSa() {
        return isSa(getValue());
    }

    // 4 ஞ
    public boolean isNya() {
        return isNya(getValue());
    }

    // 5 ட
    public boolean isDa() {
        return isDa(getValue());
    }

    // 6 ண
    public boolean isNNNa() {
        return isNNNa(getValue());
    }

    // 7 த
    public boolean isTha() {
        return isTha(getValue());
    }

    // 8 ந
    public boolean isNtha() {
        return isNtha(getValue());
    }

    // 9 ப
    public boolean isPa() {
        return isPa(getValue());
    }

    // 10 ம
    public boolean isMa() {
        return isMa(getValue());
    }

    // 11 ய
    public boolean isYa() {
        return isYa(getValue());
    }

    // 12 ர
    public boolean isRa() {
        return isRa(getValue());
    }

    // 13 ல
    public boolean isLa() {
        return isLa(getValue());
    }

    // 14 வ
    public boolean isVa() {
        return isVa(getValue());
    }

    // 15 ழ
    public boolean isLLLa() {
        return isLLLa(getValue());
    }

    // 16 ள
    public boolean isLLa() {
        return isLLa(getValue());
    }

    // 17 ற
    public boolean isRRa() {
        return isRRa(getValue());
    }

    // 18  ன
    public boolean isNa() {
        return isNa(getValue());
    }


    // 19  ஜ
    public boolean isJa_() {
        return isJa_(getValue());
    }


    // 20  ஹ
    public boolean isHa_() {
        return isHa_(getValue());
    }


    // 21  ஷ்
    public boolean isSHa_() {
        return isSHa_(getValue());
    }


    // 22  ஸ
    public boolean isSSa_() {
        return isSSa_(getValue());
    }


    // 23  ஶ
    public boolean isSSSa_() {
        return isSSSa_(getValue());
    }


    public static final TamilSimpleCharacter KA = new TamilSimpleCharacter('\u0B95', "ka");


    //1 க
    public static boolean isKa(int value) {
        return value == KA.value;
    }

    public static final TamilSimpleCharacter NGA = new TamilSimpleCharacter('\u0B99', "nga");

    // 2  ங
    public static boolean isNga(int value) {
        return value == NGA.value;
    }

    public static final TamilSimpleCharacter SA = new TamilSimpleCharacter('\u0B9A', "sa");

    // 3 ச
    public static boolean isSa(int value) {
        return value == SA.value;
    }

    public static final TamilSimpleCharacter NYA = new TamilSimpleCharacter('\u0B9E', "nya");

    // 4 ஞ
    public static boolean isNya(int value) {
        return value == NYA.value;
    }


    public static final TamilSimpleCharacter DA = new TamilSimpleCharacter('\u0B9F', "da");

    // 5 ட
    public static boolean isDa(int value) {
        return value == DA.value;
    }


    public static final TamilSimpleCharacter NNNA = new TamilSimpleCharacter('\u0BA3', "nha");

    // 6 ண
    public static boolean isNNNa(int value) {
        return value == NNNA.value;
    }

    public static final TamilSimpleCharacter THA = new TamilSimpleCharacter('\u0BA4', "tha");
    // 7 த

    public static boolean isTha(int value) {
        return value == THA.value;
    }

    public static final TamilSimpleCharacter NTHA = new TamilSimpleCharacter('\u0BA8', "ntha");

    // 8 ந
    public static boolean isNtha(int value) {
        return value == NTHA.value;
    }

    public static final TamilSimpleCharacter PA = new TamilSimpleCharacter('\u0BAA', "pa");

    // 9 ப
    public static boolean isPa(int value) {
        return value == PA.value;
    }

    public static final TamilSimpleCharacter MA = new TamilSimpleCharacter('\u0BAE', "ma");

    // 10 ம
    public static boolean isMa(int value) {
        return value == MA.value;
    }

    public static final TamilSimpleCharacter YA = new TamilSimpleCharacter('\u0BAF', "ya");

    // 11 ய
    public static boolean isYa(int value) {
        return value == YA.value;
    }

    public static final TamilSimpleCharacter RA = new TamilSimpleCharacter('\u0BB0', "ra");

    // 12 ர
    public static boolean isRa(int value) {
        return value == RA.value;
    }

    public static final TamilSimpleCharacter LA = new TamilSimpleCharacter('\u0BB2', "la");

    // 13 ல
    public static boolean isLa(int value) {
        return value == LA.value;
    }

    public static final TamilSimpleCharacter VA = new TamilSimpleCharacter('\u0BB5', "va");

    // 14 வ
    public static boolean isVa(int value) {
        return value == VA.value;
    }

    public static final TamilSimpleCharacter LLLA = new TamilSimpleCharacter('\u0BB4', "zha");

    // 15 ழ
    public static boolean isLLLa(int value) {
        return value == LLLA.value;
    }

    public static final TamilSimpleCharacter LLA = new TamilSimpleCharacter('\u0BB3', "lha");

    // 16 ள
    public static boolean isLLa(int value) {
        return value == LLA.value;
    }

    public static final TamilSimpleCharacter RRA = new TamilSimpleCharacter('\u0BB1', "rra");

    // 17 ற
    public static boolean isRRa(int value) {
        return value == RRA.value;
    }

    public static final TamilSimpleCharacter NA = new TamilSimpleCharacter('\u0BA9', "na");

    // 18  ன
    public static boolean isNa(int value) {
        return value == NA.value;
    }


    public static final TamilSimpleCharacter JA_ = new TamilSimpleCharacter('\u0B9C', "ja");

    // 19  ஜ
    public static boolean isJa_(int value) {
        return value == JA_.value;
    }

    public static final TamilSimpleCharacter HA_ = new TamilSimpleCharacter('\u0BB9', "ha");

    // 20  ஹ
    public static boolean isHa_(int value) {
        return value == HA_.value;
    }

    public static final TamilSimpleCharacter SHA_ = new TamilSimpleCharacter('\u0BB7', "sha");

    // 21  ஷ்
    public static boolean isSHa_(int value) {
        return value == SHA_.value;
    }


    public static final TamilSimpleCharacter SSA_ = new TamilSimpleCharacter('\u0BB8', "ssa");

    // 22  ஸ
    public static boolean isSSa_(int value) {
        return value == SSA_.value;
    }

    public static final TamilSimpleCharacter SSSA_ = new TamilSimpleCharacter('\u0BB6', "sssa");

    // 23  ஶ
    public static boolean isSSSa_(int value) {
        return value == SSSA_.value;
    }


    public static final TamilSimpleCharacter OM = new TamilSimpleCharacter('\u0BD0', "om");
    public static final TamilSimpleCharacter ZERO = new TamilSimpleCharacter('\u0BE6', "zero");
    public static final TamilSimpleCharacter ONE = new TamilSimpleCharacter('\u0BE7', "one");
    public static final TamilSimpleCharacter TWO = new TamilSimpleCharacter('\u0BE8', "two");
    public static final TamilSimpleCharacter THREE = new TamilSimpleCharacter('\u0BE9', "three");
    public static final TamilSimpleCharacter FOUR = new TamilSimpleCharacter('\u0BEA', "four");
    public static final TamilSimpleCharacter FIVE = new TamilSimpleCharacter('\u0BEB', "five");
    public static final TamilSimpleCharacter SIX = new TamilSimpleCharacter('\u0BEC', "six");
    public static final TamilSimpleCharacter SEVEN = new TamilSimpleCharacter('\u0BED', "seven");
    public static final TamilSimpleCharacter EIGHT = new TamilSimpleCharacter('\u0BEE', "eight");
    public static final TamilSimpleCharacter NINE = new TamilSimpleCharacter('\u0BEF', "nine");
    public static final TamilSimpleCharacter TEN = new TamilSimpleCharacter('\u0BF0', "ten");
    public static final TamilSimpleCharacter HUNDRED = new TamilSimpleCharacter('\u0BF1', "hundred");
    public static final TamilSimpleCharacter THOUSAND = new TamilSimpleCharacter('\u0BF2', "thousand");
    public static final TamilSimpleCharacter RS = new TamilSimpleCharacter('\u0BF9', "rs");


    private boolean _isVadaMozhiYezhuththu() {
        return isJa_() || isSHa_() || isHa_() || isSSa_() || isSSSa_();
    }


    private boolean _isUyirMeyyezhuththu() {
        return isUyirMeyyezhuththu(this.value);

    }


    public static boolean isUyirMeyyezhuththu(int value) {
        return isJa_(value) || isSHa_(value) || isHa_(value) || isSSa_(value) || isSSSa_(value) || isKa(value) || isNga(value) || isSa(value) || isNya(value) || isDa(value) || isNNNa(value) || isTha(value) || isNtha(value) || isPa(value) || isMa(value) || isYa(value) || isRa(value) || isLa(value) || isVa(value) || isLLLa(value) || isLLa(value) || isRRa(value) || isNa(value);

    }

    public static boolean isVadaMozhiYezhuththu(int value) {
        return isJa_(value) || isSHa_(value) || isHa_(value) || isSSa_(value) || isSSSa_(value);
    }


    private boolean _isKurilezhuththu() {
        return isUyirMeyyezhuththu() || isa() || isE() || isU() || isA() || isO();
    }


    private boolean _isVallinam() {
        return isKa() || isSa() || isDa() || isTha() || isPa() || isRRa();
    }

    private boolean _isMellinam() {
        return isNga() || isNya() || isNNNa() || isNtha() || isMa() || isNa();
    }

    private boolean _isIdaiyanam() {
        return isYa() || isRa() || isLa() || isVa() || isLLLa() || isLLa();
    }

//    @Override
//    public TamilCharacter duplicate() {
//        return new TamilSimpleCharacter(value);
//    }

    @Override
    public TamilCompoundCharacter getMeiPart() {
        if (isUyirMeyyezhuththu()) {
            return (TamilCompoundCharacter) TamilCharacterLookUpContext.lookup(getValue()).next(TamilCompoundCharacter.PULLI).currentChar;// new TamilCompoundCharacter(getValue(), TamilCompoundCharacter.PULLI);
        } else {
            throw new NoMeiPartException("Invalid call. There is no Mei part in this letter:" + this.toString());
        }
    }

    @Override
    public TamilSimpleCharacter getUyirPart() {
        if (isUyirMeyyezhuththu()) {
            return a;
        } else if (!isAaythavezhuththu()) {
            return this;
        } else {
            throw new NoUyirPartException("Invalid call. There is no Uyir part in this letter:" + this.toString());
        }
    }

    @Override
    public TamilCharacter addUyir(TamilSimpleCharacter s) {
        if (!s.isUyirezhuththu()) {
            throw new RuntimeException(s + " is not Uyir");
        }
        if (!isUyirMeyyezhuththu()) {
            throw new RuntimeException(this + " is not UyiMei");
        }
        if (s.isa()) {
            return this;// this.duplicate();
        } else {
            return TamilCharacterLookUpContext.lookup(this.getValue()).next(s.getContinuationVowel()).currentChar;  // new TamilCompoundCharacter(this.getValue(), s.getContinuationVowel());
        }
    }

    @Override
    public String translitToEnglish() {
        return eng;
    }

    @Override
    public int getNumericStrength() {
        return (getValue() - AKTHU.getValue()) * 100 + 1;
    }


    private String soundStrength = null;

    @Override
    public String getSoundStrengthDigest() {
        if (soundStrength == null) {
            if (isUyirMeyyezhuththu()) {
                if (isVallinam()) {
                    soundStrength = DIGEST_SOUND_STRENGTH._V_.toString();
                } else if (isMellinam()) {
                    soundStrength = DIGEST_SOUND_STRENGTH._M_.toString();
                } else if (isIdaiyinam()) {
                    soundStrength = DIGEST_SOUND_STRENGTH._I_.toString();
                } else {
                    soundStrength = super.getSoundStrengthDigest();
                }
            } else {
                soundStrength = super.getSoundStrengthDigest();
            }
        }
        return soundStrength;

    }


    @Override
    public List<int[]> getCodePoints(FeatureSet set) {
        boolean includeCanon = set.isFeatureEnabled(RXIncludeCanonicalEquivalenceFeature.class);

        List<int[]> list = new ArrayList<int[]>();
        int[] ret = new int[1];
        ret[0] = getValue();
        list.add(ret);
        if (includeCanon && this == TamilSimpleCharacter.OU) {
            ret = new int[2];
            ret[0] = TamilSimpleCharacter.O.getValue();
            ret[1] = TamilCompoundCharacter.OU_;
            list.add(ret);
        }
        return list;
    }

    @Override
    public int getMinCodePointsCount() {
        return 1;
    }


    private String consonantDigest = null;

    @Override
    public String getConsonantDigest() {
        if (consonantDigest == null) {
            if (isAaythavezhuththu()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._AKTHU_.toString();
            } else if (isKa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._KA_.toString();
            } else if (isNga()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._NGA_.toString();
            } else if (isSa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._SA_.toString();
            } else if (isNya()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._NYA_.toString();
            } else if (isDa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._DA_.toString();
            } else if (isNNNa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._NNNA_.toString();
            } else if (isTha()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._THA_.toString();
            } else if (isNtha()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._NTHA_.toString();
            } else if (isPa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._PA_.toString();
            } else if (isMa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._MA_.toString();
            } else if (isYa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._YA_.toString();
            } else if (isRa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._RA_.toString();
            } else if (isLa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._LA_.toString();
            } else if (isVa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._VA_.toString();
            } else if (isLLLa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._LLLA_.toString();
            } else if (isLLa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._LLA_.toString();
            } else if (isRRa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._RRA_.toString();
            } else if (isNa()) {
                consonantDigest = DIGEST_CONSONANT_TYPE._NA_.toString();
            } else {
                consonantDigest = super.getConsonantDigest();
            }
        }
        return consonantDigest;
    }

    private String vowelDigest = null;

    @Override
    public String getVowelDigest() {
        if (vowelDigest == null) {
            if (isUyirMeyyezhuththu()) {
                vowelDigest = DIGEST_VOWEL._a_.toString();
            } else if (isa()) {
                vowelDigest = DIGEST_VOWEL._a_.toString();
            } else if (isaa()) {
                vowelDigest = DIGEST_VOWEL._aa_.toString();
            } else if (isE()) {
                vowelDigest = DIGEST_VOWEL._E_.toString();
            } else if (isEE()) {
                vowelDigest = DIGEST_VOWEL._EE_.toString();
            } else if (isU()) {
                vowelDigest = DIGEST_VOWEL._U_.toString();
            } else if (isUU()) {
                vowelDigest = DIGEST_VOWEL._UU_.toString();
            } else if (isA()) {
                vowelDigest = DIGEST_VOWEL._A_.toString();
            } else if (isAA()) {
                vowelDigest = DIGEST_VOWEL._AA_.toString();
            } else if (isI()) {
                vowelDigest = DIGEST_VOWEL._I_.toString();
            } else if (isO()) {
                vowelDigest = DIGEST_VOWEL._O_.toString();
            } else if (isOO()) {
                vowelDigest = DIGEST_VOWEL._OO_.toString();
            } else if (isOU()) {
                vowelDigest = DIGEST_VOWEL._OU_.toString();
            } else {
                vowelDigest = super.getVowelDigest();
            }
        }
        return vowelDigest;
    }


    public boolean isUnicodeSequenceUnique() {
        if (isUyirMeyyezhuththu()) {
            return false;
        } else if (isUyirezhuththu()) {
            if (this == O) {
                return false;
            }
        }
        return true;
    }

    public String toUnicodeRegEXRepresentation(FeatureSet set) {

        boolean olhaAsOu = set.isFeatureEnabled(RXKeLaAsKouFeature.class);
        if (isUyirMeyyezhuththu()) {
            StringBuffer buffer = new StringBuffer("(?:");

            String val = Integer.toHexString(getValue());
            while (val.length() < 4) {
                val = "0" + val;
            }
            buffer.append("\\u" + val);
            buffer.append("(?!");
            buffer.append(new TamilGlyphRx().generate(FeatureSet.EMPTY));
            buffer.append(")");
            buffer.append(")");
            return buffer.toString();
        } else {
            if (isUyirezhuththu()) {
                if (this == O) {
                    // not an ஔ
                    StringBuffer buffer = new StringBuffer("(?:");
                    buffer.append(super.toUnicodeRegEXRepresentation(set));
                    buffer.append("(?!");
                    if (olhaAsOu) {
                        buffer.append("(?:");
                    }
                    buffer.append("\\u0BD7");
                    if (olhaAsOu) {
                        buffer.append("|");
                        buffer.append(TamilSimpleCharacter.LLA.toUnicodeRegEXRepresentation(FeatureSet.EMPTY));
                        buffer.append(")");
                    }
                    buffer.append(")");
                    buffer.append(")");
                    return buffer.toString();
                } else if (this == OU) {
                    if (olhaAsOu) {
                        StringBuffer buffer = new StringBuffer("(?:");
                        buffer.append(super.toUnicodeRegEXRepresentation(set));
                        buffer.append("|");
                        buffer.append(TamilSimpleCharacter.O.toUnicodeRegEXRepresentation(FeatureSet.EMPTY));
                        buffer.append(TamilSimpleCharacter.LLA.toUnicodeRegEXRepresentation(FeatureSet.EMPTY));
                        buffer.append(")");
                        return buffer.toString();
                    }

                }
            }
            return super.toUnicodeRegEXRepresentation(set);
        }
    }


}
