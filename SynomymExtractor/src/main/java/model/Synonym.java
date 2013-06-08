package model;

/**
 */
public class Synonym {
    public String word1;
    public String word2;
    public double clickSim;
    public double queryContextSim;

    Synonym(String word1, String word2, double clickSim, double queryContextSim) {
        this.word1 = word1;
        this.word2 = word2;
        this.clickSim = clickSim;
        this.queryContextSim = queryContextSim;
    }
}
