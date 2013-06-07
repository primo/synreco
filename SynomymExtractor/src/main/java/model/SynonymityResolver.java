package model;

import model.metrics.EntitySimilarity;
import model.websearch.WebSearch;

import java.util.List;

/**
 */
public class SynonymityResolver {

    protected WebSearch webSearcher;

    public SynonymityResolver( WebSearch ws){
        webSearcher = ws;
    }

    public List<EntitySimilarity> discoverSynonyms(List<List<String>> input) {
        return null;
    }

}
