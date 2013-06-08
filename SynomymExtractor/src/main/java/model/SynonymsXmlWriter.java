package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 */
public class SynonymsXmlWriter {
    public static void write( String filename, List<Synonym> synonyms) throws IOException {
        FileWriter fw = new FileWriter(filename);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write("<?xml version=\"1.0\"?>\n");
        writer.write("<synonyms>\n");
        for (Synonym s: synonyms) {
            String temp = "\t<synonym>\n\t\t<word1>" + s.word1 +
                    "</word1>\n\t\t<word2>"+ s.word2 +
                    "</word2>\n\t\t<ClickSim>" + s.clickSim +
                    "</ClickSim>\n\t\t<QueryContextSim>" + s.queryContextSim +
                    "</QueryContextSim>\n" +
                    "\t</synonym>\n";
            writer.write(temp);
        }
        writer.write("</synonyms>\n");
        writer.close();
    }
}
