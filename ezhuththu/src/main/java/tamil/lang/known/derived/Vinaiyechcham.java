package tamil.lang.known.derived;


import my.interest.lang.tamil.generated.types.PaalViguthi;
import my.interest.lang.tamil.generated.types.RootVerbDescription;
import my.interest.lang.tamil.generated.types.SimpleTense;
import my.interest.lang.tamil.punar.PropertyDescriptionContainer;
import my.interest.lang.tamil.punar.TamilWordPartContainer;
import my.interest.lang.tamil.punar.handler.VinaiMutruCreationHandler;
import tamil.lang.*;
import tamil.lang.known.derived.inhaippu.VinaiIf;
import tamil.lang.known.non.derived.IVinaiyechcham;
import tamil.lang.known.non.derived.Vinaiyadi;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * வினையெச்சம் எ.கா) வர, வந்து
 * </p>
 *
 * @author velsubra
 */
public final class Vinaiyechcham extends DerivativeWithTense implements IVinaiyechcham {

    static final TamilWord ulh = TamilWord.from("ulh");
    static final TamilWord illai = TamilWord.from("illai");


    static final TamilWord chol = TamilWord.from("சொல்");

    static final TamilWord aagu = TamilWord.from("ஆகு");


    private static final Map<PaalViguthi, TamilWord> ulhlhana = new HashMap<PaalViguthi, TamilWord>();

    static RootVerbDescription iruDescription = null;

    private static void fillMap() {
        for (PaalViguthi v : PaalViguthi.values()) {
            VinaiMutruCreationHandler handler = new VinaiMutruCreationHandler();
            handler.add(ulh);

            if (v == PaalViguthi.THU) {
                handler.add(new TamilWord(TamilSimpleCharacter.a));
            }

            if (v == PaalViguthi.AR) {
                handler.add(new TamilWord(TamilSimpleCharacter.a, TamilCompoundCharacter.IN));
            }
            handler.add(TamilWord.from(v.value().toLowerCase()));

            if (v == PaalViguthi.A) {
                handler.add(new TamilWord(TamilSimpleCharacter.NA));
            }
            ulhlhana.put(v, handler.getVinaiMutru());
            // System.out.println("__________:" + handler.getVinaiMutru());
        }
    }

    public Vinaiyechcham(TamilWord word, Vinaiyadi vinaiyadi, SimpleTense tense) {
        this(word, vinaiyadi, tense, false);
    }


    public Vinaiyechcham(TamilWord word, Vinaiyadi vinaiyadi, SimpleTense tense, boolean derived) {
        super(word, vinaiyadi, tense);

        if (!derived) {

            if (this.tense == SimpleTense.PRESENT) {
                if (new TamilWordPartContainer(vinaiyadi.getWord()).isUkkurralh()) {
                    //பற்றுகை
                    TamilWord w = vinaiyadi.getWord().duplicate();
                    w.add(TamilCompoundCharacter.IK_I);
                    ThozhirrPeyar th = new ThozhirrPeyar(w, vinaiyadi);
                    TamilFactory.getSystemDictionary().add(th);
                } else if (word.endsWith(TamilSimpleCharacter.KA)) {
                    //பார்க்கை
                    TamilWord w = word.duplicate();
                    w.removeLast();
                    w.add(TamilCompoundCharacter.IK_I);
                    ThozhirrPeyar th = new ThozhirrPeyar(w, vinaiyadi);
                    TamilFactory.getSystemDictionary().add(th);
                } else {

                    //காண்கை

                    TamilWord w = vinaiyadi.getWord().duplicate();
                    w.add(TamilCompoundCharacter.IK_I);
                    ThozhirrPeyar th = new ThozhirrPeyar(w, vinaiyadi);
                    TamilFactory.getSystemDictionary().add(th);
                }

            }

            if (tense == SimpleTense.PRESENT) {
                //viyangolh

                if (word.endsWith(TamilSimpleCharacter.KA) && !new TamilWordPartContainer(vinaiyadi.getWord()).isUkkurralh()) {
                    Viyangoalh v = new Viyangoalh(word, vinaiyadi);
                    TamilFactory.getSystemDictionary().add(v);
                } else {
                    TamilWord viyangoalh = vinaiyadi.getWord().duplicate();
                    viyangoalh.add(TamilSimpleCharacter.KA);
                    Viyangoalh v = new Viyangoalh(viyangoalh, vinaiyadi);
                    TamilFactory.getSystemDictionary().add(v);
                }

            }

            if (ulhlhana.isEmpty()) {
                fillMap();
                if (iruDescription == null) {
                    iruDescription = TamilFactory.getPersistenceManager().getRootVerbManager().findRootVerbDescription("இரு");
                }

                if (iruDescription == null) {
                    throw new RuntimeException("Verb இரு not found");
                }
                PropertyDescriptionContainer container = new PropertyDescriptionContainer(iruDescription);
                for (PaalViguthi v : PaalViguthi.values()) {
                    VinaiMuttu vm = new VinaiMuttu(ulhlhana.get(v), Vinaiyadi.get(TamilWord.from("இரு"), container, false), SimpleTense.PRESENT, v, true);
                    TamilFactory.getSystemDictionary().add(vm);
                }

            }
            if (tense == SimpleTense.FUTURE) {
                throw new RuntimeException("Cannot be in future");
            }


            for (PaalViguthi v : PaalViguthi.values()) {


                VinaiMutruCreationHandler handler = new VinaiMutruCreationHandler();
                handler.add(word);
                handler.add(ulhlhana.get(v));
                //வரவுள்ளது
                VinaiMuttu vm = new VinaiMuttu(handler.getVinaiMutru(), vinaiyadi, tense == SimpleTense.PRESENT ? SimpleTense.FUTURE : SimpleTense.PAST, v, true);
                if (tense == SimpleTense.PAST) {
                    vm.addProperty("muttu", "true");
                }
                TamilFactory.getSystemDictionary().add(vm);


                if (tense == SimpleTense.PRESENT) {
                    VinaiMutruCreationHandler neghandler = new VinaiMutruCreationHandler();
                    neghandler.add(word);
                    neghandler.add(illai);
                    //வரவில்லை
                    EthirMarraiVinaiMuttu nvm = new EthirMarraiVinaiMuttu(neghandler.getVinaiMutru(), vinaiyadi, SimpleTense.PAST, v, true);
                    TamilFactory.getSystemDictionary().add(nvm);
                }


            }

            if (tense == SimpleTense.PAST) {

//                if (aagu.equals(vinaiyadi.getWord())) {
//                    TamilFactory.getSystemDictionary().add( new Vinaiyechcham(TamilWord.from("ஆய்"),vinaiyadi, tense, true));
//                }

                //வந்திற்று
                TamilWord vm = null;
                TamilCharacter last = word.getLast().asTamilCharacter();
                if (last.isUyirMeyyezhuththu() && last.getUyirPart().equals(TamilSimpleCharacter.E)) {
                    vm = word.duplicate();
                    vm.add(TamilCompoundCharacter.IRR);
                    vm.add(TamilCompoundCharacter.IRR_U);
                } else {
                    VinaiMutruCreationHandler handler = new VinaiMutruCreationHandler();
                    TamilWord ITTU = new TamilWord(TamilSimpleCharacter.E, TamilCompoundCharacter.IRR, TamilCompoundCharacter.IRR_U);
                    handler.add(word);
                    handler.add(ITTU);
                    vm = handler.getVinaiMutru();
                }
                TamilFactory.getSystemDictionary().add(new VinaiMuttu(vm, vinaiyadi, SimpleTense.PAST, PaalViguthi.THU, true));
                TamilFactory.getSystemDictionary().add(new VinaiMuttu(vm, vinaiyadi, SimpleTense.PAST, PaalViguthi.A, true));


                //சொன்னால்
                //ஓடினால்
                //ஒடித்தால்


                if (last.isUyirMeyyezhuththu() && last.getUyirPart().equals(TamilSimpleCharacter.E)) {
                    //சொல்லு is also accounted
                    TamilWord ifaal = null;
                    if (vinaiyadi.getWord().startsWith(chol)) {
                        ifaal = TamilWord.from("சொன்னால்");
                    } else {
                        ifaal = word.duplicate();
                        ifaal.add(TamilCompoundCharacter.IN_aa);
                        ifaal.add(TamilCompoundCharacter.IL);
                    }

                    TamilFactory.getSystemDictionary().add(new VinaiIf(ifaal, vinaiyadi));

                } else if (last.isUyirMeyyezhuththu() && last.getUyirPart().equals(TamilSimpleCharacter.U)) {
                    TamilWord ifaal = word.duplicate();
                    ifaal.add(ifaal.removeLast().asTamilCharacter().getMeiPart().addUyir(TamilSimpleCharacter.aa));
                    ifaal.add(TamilCompoundCharacter.IL);
                    TamilFactory.getSystemDictionary().add(new VinaiIf(ifaal, vinaiyadi));
                }

            }


        }
    }

}