package model.metrics;

import java.util.*;

/** Calculates the ClickSim synonym similarity metric for a reference entity and
 * a set of candidates for synonyms.
 *
 *
 */
public class ClickSim {

    /** Calculates the ClickSim synonym similarity metric for a reference entity and
     * a set of candidates for synonyms.
     *
     * ClickSim is equal to the ratio of clicked links common for the reference entity and a candidate to the number of
     * reference clicked links.
     *
     * @param referenceString String for which synonyms we are looking for
     * @param refUrls Links clicked by the reference entity
     * @param candidates map between candidate entities and URLs clicked by them
     * @return List of ClickSim values
     */
    public List<EntitySimilarity> getSynonyms (String referenceString, List<String> refUrls, Map<String,List<String>> candidates) {
        // Mapping between candidate id and its full form
        HashMap<Integer, String> candidatesIds = new HashMap<Integer, String>();
        // Inverted index on URLs: URL -> candidates that clicked on it
        HashMap<String, List<Integer>> urlIndex = new LinkedHashMap<String, List<Integer>>();
        int freeCandidateId = 0;

        // Create the indexes
        Iterator it = candidates.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            // Create mapping between the candidate and assigned numerical ID
            final int id = freeCandidateId++;
            candidatesIds.put(id, (String) pair.getKey());
            // Update the inverted index
            for( String s: (List<String>)pair.getValue()) {
                List<Integer> candList = null;
                if (urlIndex.get(s) == null) {
                    candList = new ArrayList<Integer>();
                    urlIndex.put(s, candList);
                }
                candList = urlIndex.get(s);
                candList.add(id);
            }
        }

        // Calculate the coverage of reference links
        ArrayList<Integer> coverage = new ArrayList<Integer>(candidates.size());
        for(int i = 0; i < candidates.size(); ++i) {
            coverage.add(0);
        }
        for (String r : refUrls) {
            List<Integer> candList = urlIndex.get(r);
            if (null == candList) {
                continue;
            }
            for (int id : candList) {
                coverage.set(id, coverage.get(id));
            }
        }

        List<EntitySimilarity> output = new ArrayList<EntitySimilarity>();
        final double refLinksSum = refUrls.size();
        for (int i = 0; i < coverage.size(); ++i) {
            final String candidate = candidatesIds.get(i);
            final double sim = ((double)coverage.get(i)) / refLinksSum;
            EntitySimilarity e = new EntitySimilarity(referenceString, candidate, sim);
            output.add(e);
        }
        return output;
    }

}
