import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.InputFilter;
import opennlp.tools.postag.POSDictionary;
import opennlp.tools.util.InvalidFormatException;
import model.Model;
import model.POSTagger;
import model.Tokenizer;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.DBConnection;



public class SynonymExtractor {
    public final static String DEFAULT_POS = "NN";
    public final static int DEFAULT_MIN_WORD_LENGTH = 3;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        // Understand the arguments
        String POS = DEFAULT_POS;
        int minWordLength = DEFAULT_MIN_WORD_LENGTH;

        List<String> fileList = new ArrayList<String>();
        for( int i = 0; i < args.length; ++i) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2) {
                        throw new IllegalArgumentException("Invalid argument: " + args[i]);
                    }
                    else if (args.length - 1 == i) {
                        // Assuming that there is a space between -pos and the arg value
                        throw  new IllegalArgumentException("Expected an argument after: " + args[i]);
                    } else if (args[i].equalsIgnoreCase("--pos")) {
                        POS = args[i+1];
                    } else if(args[i].equalsIgnoreCase("--minLen")) {
                        minWordLength = Integer.valueOf(args[i+1]);
                    } else {
                        throw new IllegalArgumentException("Invalid argument: " + args[i]);
                    }
                    break;
                default:
                    fileList.add(args[i]);
                    break;
            }
        }

        // TODO Validate the POS ?
        List<List<String>> words = null;
        try {
            words = loadWords(fileList, POS, minWordLength);
        } catch (IOException e) {
            System.out.println("Failed opening input files.");
            System.out.print(e);
            return;
        }
		
//		Model model = new Model();
//		ArrayList<ArrayList<String>> res = model.getSynonyms(text, POS, 0.1);
//
//		for (ArrayList<String> list : res) {
//			System.out.print("Synonimy: ");
//			if (res.size()== 0) continue;
//			for (String w : list) {
//				System.out.print(w+"; ");
//			}
//			System.out.print("\n");
//		}


	}

    /** Loades the files, checks if the input was valid and then
     * filters out words we are not interested in.
     *
     * It manages loading the files, validation is passed to a utility class.
     *
     * @param files filenames to load the input from
     * @return list of
     */
    private static List<List<String>> loadWords(List<String> files, String pos, int wordMinLen) throws IOException {
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
