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

    public List<Synonym> discoverSynonyms(
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

            // QueryContext is symmetric - so we need to create also opposite relation object
            // because it would not be created
            if (!metrics.containsKey(e.candidate)) {
                temp  = new HashMap<String, Metrics>();
                metrics.put(e.candidate, temp);
            } else {
                temp = metrics.get(e.candidate);
            }
            temp.put(e.reference, new Metrics(e.similarity));
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
            if (null == m) {
                // QContextSim = 0 so no synonym anyway
                // TODO how could it happen

                m = new Metrics();
                temp.put(e.candidate, m);
            }
            m.clickSim = e.similarity;
        }

        // Look for synonyms
        List<Synonym> output = new ArrayList<Synonym>();
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
                    output.add(new Synonym(ref, candidate, clickSim, queryContextSim));
                }
            }
        }
        return output;
    }

}

class Metrics {
    public double clickSim;
    public double queryContextSim;

    public Metrics(){}

    public Metrics(double queryContextSim){
        this.queryContextSim = queryContextSim;
    }

    public Metrics(double clickSim, double queryContextSim) {
        this.clickSim = clickSim;
        this.queryContextSim = queryContextSim;
    }
}

