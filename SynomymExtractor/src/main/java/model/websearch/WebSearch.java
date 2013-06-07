package model.websearch;

import java.util.Map;

import java.util.List;

class Link{
	public String url;
	public String desc;
}

/** Provides information of possible results of web search queries.
 */
public interface WebSearch {

    /** Looks up the list of queries and returns found URLs.
     *
     * Map< Query, List<URL>>
     *
     * @param query string to be look up in web search
     * @return map between query and list of resultant URLs
     */
    public Map<String,List<String>> getLinks(List<String> query);

    /** Looks up the list of queries and returns found URLs and their short descriptions.
     *
     * Map< Query, List<Pair<URL, Description>>>
     *
     * @param query string to be look up in web search
     * @return map between query and list of resultant URLs and decriptions
     */
    public Map<String,List<Link>> getLinksAndSummary(List<String> query);
}
