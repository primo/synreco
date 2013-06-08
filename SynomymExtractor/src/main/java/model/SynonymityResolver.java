package model;

import model.metrics.ClickSim;
import model.metrics.ContextSimilarity;
import model.metrics.EntitySimilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.StrictMath.min;

/**
 */
public class SynonymityResolver {
    private Map<String, List<String>> urls;


    public SynonymityResolver(Map<String, List<String>> urls){
        this.urls = urls;
    }

    public List<Synonyms> discoverSynonyms(
            List<List<String>> input,
            Map<String, List<String>> candidates,
            double clickSimThreshold,
            double queryContextSimThreshold
    ) {
        HashMap<String, HashMap<String, Metrics>> metrics = new HashMap<String, HashMap<String, Metrics>>();
        ContextSimilarity cs = new ContextSimilarity();
        List<EntitySimilarity> o1 = cs.getSynonyms(input);
        List<EntitySimilarity> o2 = new ArrayList<EntitySimilarity>();

        ClickSim click = new ClickSim();
        for (Entry<String,List<String>> e : candidates.entrySet()) {
            Map<String,List<String>> candidatesUrls = new HashMap<String, List<String>>();
            for ( String s : e.getValue()) {
                candidatesUrls.put(s, urls.get(s));
            }
            List<EntitySimilarity> temp = click.getSynonyms(e.getKey(), urls.get(e.getKey()), candidatesUrls);
            o2.addAll(temp);
        }
        for (EntitySimilarity e: o1) {
            HashMap<String,Metrics> temp = null;
            if (!metrics.containsKey(e.reference)) {
                temp  = new HashMap<String, Metrics>();
                metrics.put(e.reference, temp);
            } else {
                temp = metrics.get(e.reference);
            }
            temp.put(e.candidate, new Metrics(e.similarity));
        }
        for (EntitySimilarity e: o2) {
            HashMap<String,Metrics> temp = null;
            if (!metrics.containsKey(e.reference)) {
                temp  = new HashMap<String, Metrics>();
                metrics.put(e.reference, temp);
            } else {
                temp = metrics.get(e.reference);
            }
            Metrics m = temp.get(e.candidate);
            m.clickSim = e.similarity;
        }

        // Look for synonyms
        List<Synonyms> output = new ArrayList<Synonyms>();
        for (Entry<String, List<String>> e : candidates.entrySet()) {
            for (String candidate : e.getValue()) {
                final String ref = e.getKey();
                Metrics m1 = metrics.get(ref).get(candidate);
                Metrics m2 = metrics.get(candidate).get(ref);
                double clickSim = min(m1.clickSim, m2.clickSim);
                double queryContextSim = min(m1.queryContextSim, m2.queryContextSim);
                if ( clickSim >= clickSimThreshold && queryContextSim >= queryContextSimThreshold)
                {
                    // e and candidate are synonyms
                    output.add(new Synonyms(ref, candidate, clickSim, queryContextSim));
                }
            }
        }
        return output;
    }

}

class Metrics {
    public double clickSim;
    public double queryContextSim;

    public Metrics(double queryContextSim){
        this.queryContextSim = queryContextSim;
    }

    public Metrics(double clickSim, double queryContextSim) {
        this.clickSim = clickSim;
        this.queryContextSim = queryContextSim;
    }
}

class Synonyms {
    public String word1;
    public String word2;
    public double clickSim;
    public double queryContextSim;

    Synonyms(String word1, String word2, double clickSim, double queryContextSim) {
        this.word1 = word1;
        this.word2 = word2;
        this.clickSim = clickSim;
        this.queryContextSim = queryContextSim;
    }

}