package model.websearch;

import javafx.util.Pair;

import java.util.List;

/**
 */
public interface WebSearch {
    public List<String> getLinks(String query);
    public List<Pair<String,String>> getLinksAndSummary(String query);
}
