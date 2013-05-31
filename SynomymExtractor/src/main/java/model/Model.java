package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import database.DBConnection;

public class Model {

	private ArrayList<ArrayList<String>> synonymSet = new ArrayList<ArrayList<String>> ();
	
	public ArrayList<ArrayList<String>> getSynonyms (String text, String POS, double d) {
		
		Set<String> tokenSet = new HashSet<String>();
		synonymSet.clear();
		
		String[] tokens = Tokenizer.tokenize(text);
		String[] tags = POSTagger.POSTag(tokens);
		
		for (int i = 0; i < tags.length; ++i) {
			if (tags[i].equals(POS)) {
			  tokenSet.add(tokens[i].toLowerCase());    
			}
		}
		
		Iterator<String> iterator = tokenSet.iterator();
		while (iterator.hasNext()) {
		    String element = iterator.next();
		    boolean found = false;
		    for (ArrayList<String> subset : synonymSet){
		    	if (checkSynonym(subset.get(0), element)) {
		    		found = true; 
		    		subset.add(element);
		    		break;
		    	}
		    }
		    if (!found) {
		    	ArrayList<String> newSet = new ArrayList<String>();
		    	newSet.add(element);
		    	synonymSet.add(newSet);
		    }
		    		
		}
		return synonymSet;
	}
	
	private boolean checkSynonym (String w1, String w2) {
		DBConnection connection  = new DBConnection();
		connection.open();
		w1 = w1.replace("\'", "\'\'");
		w2 = w2.replace("\'", "\'\'");
		boolean result = (connection.executeQueryAsString("SELECT ARE_SYNONYMS(\'film\',\'movie\')")).equals("TRUE") ? true : false;
		connection.close();
		return result;
	}
}
