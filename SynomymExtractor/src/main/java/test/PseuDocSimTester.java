package test;

import model.metrics.PseuDocMetric;
import model.metrics.PseuDocSim;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs simple test of PseuDocSim metric implementation.
 * Provides a small set of candidates and pseudocs.
 */
public class PseuDocSimTester {
    public static void main(String[] args) {
        String reference = "ms excel";
        List<String> synonymCandidates = new ArrayList<String>();
        synonymCandidates.add("microsoft excel");
        synonymCandidates.add("microsoft spreadsheet");
        synonymCandidates.add("excel");
        synonymCandidates.add("excel tutorials");
        List<String> refPseuDocs = new ArrayList<String>();
        refPseuDocs.add("microsoft ms excel spreadsheet");
        refPseuDocs.add("microsoft office spreadsheet");
        refPseuDocs.add("ms excel microsoft spreadsheet");
        refPseuDocs.add("microsoft ms office excel word draw m$ tutorials");
        refPseuDocs.add("excel lowest prize");
        refPseuDocs.add("newest things in excel 2013 ms microsoft m$ spreadsheet");

        List<PseuDocSim> pseuDocSims = PseuDocMetric.dualIndex(reference, synonymCandidates, refPseuDocs);
        for (PseuDocSim p : pseuDocSims) {
            System.out.println(p.toString());
        }

    }

}
