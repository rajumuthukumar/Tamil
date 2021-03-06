package my.interest.lang.tamil.impl;

import my.interest.lang.tamil.EzhuththuUtils;
import my.interest.lang.tamil.impl.dictionary.DictionaryCollection;
import tamil.lang.api.dictionary.TamilDictionary;
import tamil.lang.api.feature.Feature;
import tamil.lang.api.feature.FeatureConstants;
import tamil.lang.api.number.IgnoreNonDigitFeature;
import tamil.lang.api.number.PunharchiFeature;
import tamil.lang.api.parser.ParseWithDictionary;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public final class FeatureSet {

    private static Map<String, Feature> registeredFeatures = new HashMap<String, Feature>();

    static {
        Field[] fields = FeatureConstants.class.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                if (Modifier.isFinal(f.getModifiers())) {
                    if (Modifier.isPublic(f.getModifiers())) {
                        if (Feature.class.isAssignableFrom(f.getType())) {
                            int last = f.getName().lastIndexOf("_VAL_");
                            if (last < 0) continue;
                            String number = f.getName().substring(last + 5, f.getName().length());
                            if (registeredFeatures.get(number) != null) {
                                throw new RuntimeException("Duplicate feature id:" + number + " at " + f.getName());
                            }
                            try {
                                Feature val = (Feature) f.get(null);
                                if (val == null) continue;
                               // System.out.println("adding:" + number + ":feature " + val.getClass().getName());
                                registeredFeatures.put(number, val);
                            } catch (IllegalAccessException il) {

                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Finds features
     *
     * @param type the tye of the feature to find
     * @param list the comma separated list of feature ids.
     * @param <T>  the feature type
     * @return the array of features found.
     */
    public static <T extends Feature> List<T> findFeatures(Class<T> type, String list) {
        if (list == null) return null;
        List<T> featureList = new ArrayList<T>();
        List<String> listStr = EzhuththuUtils.parseString(list);
        for (String s : listStr) {
            s = s.trim();
            Feature f = registeredFeatures.get(s);
            if (f != null) {
                if (type == null || type.isAssignableFrom(f.getClass())) {
                    featureList.add((T) f);
                }
            }
        }
        return featureList;

    }

    /**
     * Parses the number list into a  feature set
     * @param cls
     * @param numberlist
     * @return
     */
    public static FeatureSet parse(Class<? extends  Feature>  cls ,String numberlist) {
        List<? extends  Feature> list = findFeatures(cls, numberlist);
        return  new FeatureSet(list.toArray(new Feature[0]));
    }

    public static final FeatureSet EMPTY = new FeatureSet(null);
    // Feature[] features = null;
    Map<Class<? extends Feature>, Feature> quickmap = null;
    Map<Class<? extends Feature>, Feature> removedFeatures = null;

    public FeatureSet(Feature... features) {

        if (features != null && features.length > 0) {
            for (Feature f : features) {
                addFeature(f);
            }
        }
    }

    public void addFeature(Feature f) {
        if (quickmap == null) {
            quickmap = new HashMap<Class<? extends Feature>, Feature>();
        }

        quickmap.put(f.getClass(), f);


    }

    public TamilDictionary getDictionary() {
        List<ParseWithDictionary> features = getFeatures(ParseWithDictionary.class);
        if (features == null || features.isEmpty()) {
            return null;
        } else {
            //List<TamilDictionary> list = new ArrayList<TamilDictionary>();
            for (ParseWithDictionary d: features) {
                return d.getDictionary();
               // list.add(d.getDictionary());
            }
           // return new DictionaryCollection(list);
        }
        return null;

    }

    public <T extends Feature> T getFeatureByType(Class<T> feature) {
        List<T> features = getFeatures(feature);
        if (features.isEmpty()) {
            return null;
        } else {
            return features.get(0);
        }
    }


    public <T extends Feature> List<T> getFeatures(Class<T> tClass) {

        List<T> list = new ArrayList<T>();
        if (quickmap != null) {
            for (Feature f : quickmap.values()) {
                if (tClass.isAssignableFrom(f.getClass())) {
                    list.add((T) f);
                }
            }
        }
        return list;


    }

    public synchronized <T extends Feature> T removeFeature(Class<? extends Feature> feature) {
        if (feature == null) return null;
        if (EMPTY == this) return null;
        if (this.quickmap == null) return null;
        T t = (T) quickmap.remove(feature);
        if (t != null) {
            if (removedFeatures == null) {
                removedFeatures = new HashMap<Class<? extends Feature>, Feature>();
                removedFeatures.put(feature, t);
            }
        }
        return t;
    }


    public <T extends Feature> T getRemovedFeature(Class<T> feature) {
        if (feature == null) return null;
        if (EMPTY == this) return null;
        if (this.removedFeatures == null) return null;
        Feature fq = removedFeatures.get(feature);
        if (fq != null) return (T) fq;
        return null;
    }

    public <T extends Feature> T getFeature(Class<T> feature) {
        if (feature == null) return null;
        if (EMPTY == this) return null;
        if (this.quickmap == null) return null;
        Feature fq = quickmap.get(feature);
        if (fq != null) return (T) fq;

        return null;
    }


    public <T extends Feature> boolean isFeatureEnabled(Class<T> feature) {
        return getFeature(feature) != null;
    }


    public boolean isToIgnoreNonDigit() {
        IgnoreNonDigitFeature f = getFeature(IgnoreNonDigitFeature.class);
        return f != null;
    }

    public boolean isToTreatNonDigitAsNumber() {
        IgnoreNonDigitFeature f = getFeature(IgnoreNonDigitFeature.class);
        if (f == null) return false;
        return f.isConsiderNonDigitAs0();
    }

    public boolean isNumberPurchchiFeaturePosition() {
        PunharchiFeature f = getFeature(PunharchiFeature.class);
        if (f == null) return false;
        return true;
    }

    public boolean isNumberPurchchiFeatureFull() {
        PunharchiFeature f = getFeature(PunharchiFeature.class);
        if (f == null) return false;
        return f.isFull();
    }


}
