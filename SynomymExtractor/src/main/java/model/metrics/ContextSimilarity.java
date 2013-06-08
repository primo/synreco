package model.metrics;

import java.util.*;

/** Calaculates QueryContextSim metric of synonym relation.
 */
public class ContextSimilarity {

    // Size of the word surrounding considered its context
    public static final int DEFAULT_CONTEXT_RADIUS = 2;

    /** Calculates similarity of every pair of words in given text using QueryContextSimilarity.
     *
     * @param text Input text, a list o sentences, modeled as a list of string
     * @return List of similarity descriptions
     */
    public List<EntitySimilarity> getSynonyms (List<List<String>> text) {
        return getSynonyms(text, DEFAULT_CONTEXT_RADIUS);
    }

    /** Calculates similarity of every pair of words in given text using QueryContextSimilarity.
     *
     * @param text Input text, a list o sentences, modeled as a list of string
     * @param contextRadius  Size of the word surrounding considered its context
     * @return List of similarity descriptions
     */
    public List<EntitySimilarity> getSynonyms (List<List<String>> text, final int contextRadius) {
        HashMap<String, HashSet<String>> contextMap = new HashMap<String, HashSet<String>>();

        for(List<String> l : text) {
            for( String token : l) {
                HashSet<String> contexts = null;
                if (!contextMap.containsKey(token)) {
                    contexts = new HashSet<String>();
                    contextMap.put(token, contexts);
                }
                contexts = contextMap.get(token);
                ArrayList<String> newContexts = getContexts(token, l, contextRadius);
                contexts.addAll(newContexts);
            }
        }

        List<EntitySimilarity> result = new ArrayList<EntitySimilarity>();
        String[] words = contextMap.keySet().toArray(new String[0]);

        for (int i = 0; i < words.length; ++i)
            for (int j = i+1; j< words.length; ++j) {
                double similarity = getRelation(words[i], words[j], contextMap);
                EntitySimilarity e = new EntitySimilarity(words[i],words[j], similarity);
                result.add(e);
            }
        return result;
    }


    private ArrayList<String> getContexts(String token, List<String> tokens, final int contextRadius) {
        ArrayList<String> results = new ArrayList<String>();
        final int index = tokens.indexOf(token);
        final int listLimit = tokens.size()-1;
        int i = index - contextRadius;
        i = (i>=0) ? i:0;
        int k = index + contextRadius;
        k = (k <= tokens.size()-1) ? k:listLimit;
        for (int j = i; j <= k;++j) {
            String temp = tokens.get(j);
            if (j!=index) {
                // Ignoring temp = token
                results.add(temp);
            }
        }
        return results;
    }

    private double getRelation(String string, String string2,
                                HashMap<String, HashSet<String>> contextMap) {
        HashSet<String> c1 = contextMap.get(string);
        HashSet<String> c2 = contextMap.get(string2);

        Set<String> intersection = new HashSet<String>(c1);
        Set<String> union = new HashSet<String>(c1);

        intersection.retainAll(c2);
        union.addAll(c2);

        return ((double)intersection.size())/((double)union.size());

    }
}
