package model.metrics;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: primo
 * Date: 6/4/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PseuDocMetric {

    public final String logTable = "CLICK_LOG_S_UQ";

    // THis select should find all pseudo documents for links clicked by the reference query (entity string)
    public final String selectDForReferenceEntity = "SELECT QUERY FROM "+logTable+
            "where clickUrl in ( SELECT clickUrl where query = ? ) group by clickUrl" ; // ? - reference entity string

    //public final String pseuDoc = "SELECT QUERY FROM "+logTable+" WHERE CLICKURL = ?";
    //public final String aux = "SELECT CLICKURL FROM" +logTable+" WHERE QUERY = ?";

    /** TODO
     * 1. Find set of the synonym candidates Se
     * 2. Find the set of pseudocuments for the links clicked by the reference entity string
     *
     * dla zapytania referencyjnego re:
     *  mamy zbiór kandydatów Se= List<String>
     *  znajdujemy De czyli aux(re): select clickUrl where query = re --> SELECT
     *

     *
     *
     */

    /** Reuturn only distinct tokens from the input string list.
     *
     * @param input
     * @return
     */
    protected static List<String> disTok(List<String> input) {
        Set<String> output = new HashSet<String>();
        for (String s : input) {
            String[] tokens = s.split(" ");
            for (String t : tokens) {
                output.add(t);
            }
        }
        return new LinkedList<String>(output);
    }

    /** Computes PseuDocSimilarity metric for a given reference string and a list of synonym candidates.
     *
     * @param reference the reference entity string the relation is checked against
     * @param synonymCandidates a collection of candidate synonyms
     * @param referencePseudodocs a collection of pseudo documents of clickUrls, the reference string caused a click to
     * @return
     */
    public static List<PseuDocSimilarity> dualIndex(String reference, List<String> synonymCandidates, List<String> referencePseudodocs) {
        final List<String> se = synonymCandidates;
        final List<String> de = referencePseudodocs;

        // FIXME Or maybe set ?
        HashMap<String, List<Integer>> candIndex = new HashMap<String, List<Integer>>();
        HashMap<String, List<Integer>> pdIndex = new HashMap<String, List<Integer>>();

        // Create inverted indexes for candidates and pseudoc
        int i = 0;
        for (String s : se) {
            String[] tokens = s.split(" ");
            for (String t : tokens) {
                List<Integer> list = candIndex.get(t);
                if (null == list) {
                    list = new LinkedList<Integer>();
                    candIndex.put(t,list);
                }
                list.add(i);
            }
            ++i;
        }
        i = 0;
        for (String s : de) {
            String[] tokens = s.split(" ");
            for (String t : tokens) {
                List<Integer> list = pdIndex.get(t);
                if (null == list) {
                    list = new LinkedList<Integer>();
                    pdIndex.put(t,list);
                }
                list.add(i);
            }
            ++i;
        }

        // FIXME create CDMatrix using more efficient class
        // Default initialization to 0 - making use of it
        int[][] cdmatrix = new int[se.size()][de.size()];

        //Main algorithm
        if (se.size() < de.size()) { // Iterate over the shorter list
            List<String> tokens = disTok(se);
            for (String s : tokens) {
                List<Integer> st = candIndex.get(s);
                List<Integer> dt = pdIndex.get(s);
                for (Integer x : st) {
                    for (Integer j : dt) {
                        cdmatrix[x][j] += 1;
                    }
                }
            }
        } else {
            List<String> tokens = disTok(de);
            for (String s : tokens) {
                List<Integer> st = candIndex.get(s);
                List<Integer> dt = pdIndex.get(s);
                for (Integer x : st) {
                    for (Integer j : dt) {
                        cdmatrix[x][j] += 1;
                    }
                }
            }
        }

        // Find the metrics
        /* Sim(e, s) := (|d|d∈D(e)∧CD[ind(s),ind(d)]=|s||)/|D(e)| */
        List<PseuDocSimilarity> ret = new LinkedList<PseuDocSimilarity>();
        i = 0;
        final int deLen = de.size();
        for (String s : se) {
            final int slen = s.split(" ").length;
            int matchedDocs = 0;
            for (int k = 0; k < de.size(); ++k) {
                if (slen == cdmatrix[i][k]) {
                    ++matchedDocs;
                }
            }
            ++i;
            ret.add( new PseuDocSimilarity(reference, s, (float)matchedDocs / deLen));
        }
        return ret;
    }
} // PseuDocMetric class

