package model.metrics;

/**
 * Created with IntelliJ IDEA.
 * User: primo
 * Date: 6/7/13
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntitySimilarity {
    public final String reference;
    public final String candidate;
    public final double  similarity;

    EntitySimilarity(String reference, String candidate, double similarity) {
        this.reference = reference;
        this.candidate = candidate;
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        //return super.toString();
        return "Ref: " + reference + " cand:" + candidate + " pdsim:=" + similarity;
    }
}
