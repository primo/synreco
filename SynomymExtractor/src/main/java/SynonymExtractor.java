import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.*;
import model.websearch.FileSearch;

import java.util.*;


public class SynonymExtractor {
    public final static String DEFAULT_POS = "NN";
    public final static int DEFAULT_MIN_WORD_LENGTH = 3;
    public final static double DEFAULT_CLICK_SIM_THRESHOLD = 0.5;
    public final static double DEFAULT_QUERY_CONTXEXT_SIM_THRESHOLD = 0.5;
    public final static String DEFAULT_OUTPUT_FILENAME = "out.xml";

    /**
     * @param args
     */
	public static void main(String[] args) {
        // Parse the arguments
        Map<String, String> opts = new HashMap<String, String>();
        opts.put("pos",DEFAULT_POS);
        opts.put("minWordLength",String.valueOf(DEFAULT_MIN_WORD_LENGTH));
        opts.put("clickSimThreshold", String.valueOf(DEFAULT_CLICK_SIM_THRESHOLD));
        opts.put("qContextSimThreshold",String.valueOf(DEFAULT_QUERY_CONTXEXT_SIM_THRESHOLD));
        opts.put("output",DEFAULT_OUTPUT_FILENAME);

        List<String> fileList = new ArrayList<String>();
        for( int i = 0; i < args.length; ++i) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2) {
                        throw new IllegalArgumentException("Invalid argument: " + args[i]);
                    } else if (args.length - 1 == i) {
                        // Assuming that there is a space between -pos and the arg value
                        throw  new IllegalArgumentException("Expected an argument after: " + args[i]);
                    } else if(args[i].equalsIgnoreCase("--help")) {
                        printHelp();
                    } else if (args[i].charAt(1) == '-') {
                        final String opt = args[i].substring(2);
                        final String value = args[i+i];
                        ++i;
                        opts.put(opt,value);
                    } else {
                        throw new IllegalArgumentException("Invalid argument: " + args[i]);
                    }
                    break;
                default:
                    fileList.add(args[i]);
                    break;
            }
        }

        if (0 == fileList.size()) {
            System.out.println("Specify input files !");
            printHelp();
            return;
        }

        // TODO Validate the POS tag value ?
        List<List<String>> text = null;
        try {
            text = loadText(fileList, opts.get("pos"), Integer.valueOf(opts.get("minWordLength")));
        } catch (IOException e) {
            System.out.println("Failed opening input files.");
            System.out.print(e);
            return;
        }
        Set<String> words = new HashSet<String>();
        for (List<String> l : text) {
            for(String s: l) {
                words.add(s);
            }
        }
        FileSearch fs = new FileSearch();
        Map<String,List<String>> urls = fs.getLinks(new ArrayList<String>(words));
        Map<String, List<String>> candidates = Candidates.generate(urls);
        SynonymityResolver sr = new SynonymityResolver(urls);
        List<Synonym> synonyms = sr.discoverSynonyms(
                text,
                candidates,
                Double.valueOf(opts.get("clickSimThreshold")),
                Double.valueOf(opts.get("qContextSimThreshold"))
        );
        try {
            SynonymsXmlWriter.write(opts.get("output"), synonyms);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed writing output file: " + opts.get("output"));
        }
	}

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("java SynonymExtractor [--opt Value] inputFile ... ");
        System.out.print("\nAvailable options:\n\t--help\t- prints this help message\n");
        System.out.print("\t--pos\t- specifies the part of speech we want to consider\n");
        System.out.print("\t--output\t- Output file in xml format containg the discovered synonyms\n");
        System.out.print("\t--minWordLength\t- minimal length of word selected for synonym discovery\n");
        System.out.print("\t--clickSimThreshold\t- threshold for ClickSim metric\n");
        System.out.print("\t--qContextSimThreshold\t- threshold for QueryContextSim metric\n\n");
    }

    /** Loades the files, checks if the input was valid and then
     * filters out words we are not interested in.
     *
     * It manages loading the files, validation is passed to a utility class.
     *
     * @param files filenames to load the input from
     * @return list of
     */
    private static List<List<String>> loadText(List<String> files, String pos, int wordMinLen) throws IOException {
        InputFilter inputFilter = new InputFilter();
        List<List<String>> output = new LinkedList<List<String>>();
        for (String name : files) {
            FileReader in = new FileReader(name);
            BufferedReader r = new BufferedReader(in);
            String input = "";
            String line = null;
            while((line=r.readLine()) != null) {
                input += line;
            }
            List<List<String>> temp = inputFilter.filter(input, pos, wordMinLen);
            output.addAll(temp);
            }
      return output;
    }
}
