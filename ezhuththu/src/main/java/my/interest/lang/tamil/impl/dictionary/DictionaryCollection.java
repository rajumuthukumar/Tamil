package my.interest.lang.tamil.impl.dictionary;

import tamil.lang.TamilWord;
import tamil.lang.api.dictionary.DictionaryFeature;
import tamil.lang.api.dictionary.TamilDictionary;
import tamil.lang.known.IKnownWord;
import tamil.lang.spi.TamilDictionaryProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public class DictionaryCollection implements TamilDictionary {

    private List<TamilDictionary> list = null;

    public DictionaryCollection(Iterator<TamilDictionaryProvider> iterator) {
        list = new ArrayList<TamilDictionary>();
        while (iterator.hasNext()) {
            list.add(iterator.next().create());
        }

    }

    /**
     * looks up for a known tamil word.
     * <p>Please note a single character sequence can have multiple meanings each of which corresponds to an instance of {@link tamil.lang.known.IKnownWord}
     * </p>
     *
     * @param word the word to be looked at. Please see {@link tamil.lang.TamilWord#from(String)} to get a tamil word from a string.
     * @return the list of  tamil words  known to the system, empty list if the given character sequence is not known.
     * @see tamil.lang.TamilFactory#getSystemDictionary()
     */
    @Override
    public List<IKnownWord> lookup(TamilWord word) {
        List<IKnownWord> list = new ArrayList<IKnownWord>();
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            List<IKnownWord> dlist = d.lookup(word);
            if (dlist != null) {
                list.addAll(dlist);
            }

        }
        return list;
    }

    /**
     * Gives a quick peek to see if there is any word.
     *
     * @param word to be looked up.
     * @return the first known word identified.
     */
    @Override
    public IKnownWord peek(TamilWord word) {
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            IKnownWord dlist = d.peek(word);
            if (dlist != null) {
                return dlist;
            }

        }
        return null;
    }

    /**
     * Searches for a specific word and of specific type.
     *
     * @param word         the word or first part of word to be searched.
     * @param exactMatch   flag to say if exact match has be performed. When false, words starting with the given word may be returned.
     * @param maxCount     the max count expected. The search returns after the maxCount is reached or when nothing is found.
     * @param includeTypes the list of classes indicating the types of word to be returned.
     * @return the list of words found matching the search criteria.
     */
    @Override
    public List<IKnownWord> search(TamilWord word, boolean exactMatch, int maxCount, List<Class<? extends IKnownWord>> includeTypes) {
        List<IKnownWord> list = new ArrayList<IKnownWord>();
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            List<IKnownWord> dlist = d.search(word, exactMatch, maxCount - list.size(), includeTypes);
            if (dlist != null) {
                list.addAll(dlist);
            }

        }
        return list;
    }

    @Override
    public List<IKnownWord> search(TamilWord word, int maxCount, List<Class<? extends IKnownWord>> includeTypes, DictionaryFeature... features) {
        List<IKnownWord> list = new ArrayList<IKnownWord>();
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            List<IKnownWord> dlist = d.search(word, maxCount - list.size(), includeTypes, features);
            if (dlist != null) {
                list.addAll(dlist);
            }

        }
        return list;
    }

    /**
     * Peeks a quick Tamil word from an english word
     *
     * @param english the english word
     * @return the known tamil word.
     */
    @Override
    public IKnownWord peekEnglish(String english) {
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            IKnownWord dlist = d.peekEnglish(english);
            if (dlist != null) {
                return dlist;
            }

        }
        return null;

    }

    /**
     * Suggests words for a specific word, and of specific type.
     *
     * @param word         the word or first part of word to be searched.
     * @param maxCount     the max count expected. The suggestion search returns after the maxCount is reached or when the search is finished.
     * @param includeTypes the list of classes indicating the types of word to be returned.
     * @return the list of words that are suggested in the  context.
     */
    @Override
    public List<IKnownWord> suggest(TamilWord word, int maxCount, List<Class<? extends IKnownWord>> includeTypes) {
        List<IKnownWord> list = new ArrayList<IKnownWord>();
        for (TamilDictionary d : this.list) {
            if (d == this) continue;
            List<IKnownWord> dlist = d.suggest(word, maxCount - list.size(), includeTypes);
            if (dlist != null) {
                list.addAll(dlist);
            }

        }
        return list;
    }
}