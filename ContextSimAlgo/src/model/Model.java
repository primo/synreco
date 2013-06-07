package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Model {

	//private ArrayList<ArrayList<String>> synonymSet = new ArrayList<ArrayList<String>> ();
	
	public /*ArrayList<ArrayList<String>> */ ArrayList<String[]> getSynonyms (String pText, String pPOS, double pTau) {
		
		HashMap<String, HashSet<String>> contextMap = new HashMap<String, HashSet<String>>();
		
		String [] sentences = SentenceDetector.getSentences(pText);
		
		for (String sentence : sentences) {
			String[] tokens = Tokenizer.tokenize(sentence);
			String[] tags = POSTagger.POSTag(tokens);
			for (int i = 0; i<tags.length; ++i) {
				String token = tokens[i];
				if (tags[i].equals(pPOS)) {
					HashSet<String> contexts = null;
					if (!contextMap.containsKey(token)) {
						contexts = new HashSet<String>();
						contextMap.put(token, contexts);
					}
					contexts = contextMap.get(tags[i]);
					ArrayList<String> newContexts = getContexts(token, tokens, tags);
					for (String newContext : newContexts) 
						contexts.add(newContext);
					
				}
			}
			
			//find tags of certain pos
			//get their contexts, put in HashMap<String, Set<String>>
			
		}
		
		ArrayList<String[]> synonyms = new ArrayList<String[]>();
		String[] words = contextMap.keySet().toArray(new String[0]);
		
		for (int i = 0; i < words.length; ++i)
			for (int j = i+1; j< words.length; ++j) {
				if (areSynonyms(words[i], words[j], contextMap, pTau)){ 
					String[] newSyns = new String[2];
					newSyns[0] = words[i];
					newSyns[1] = words[j];
					synonyms.add(newSyns);
					newSyns = null;
					
				}
				
			}
				
		return synonyms;
		
		//run algorithm
		
/*		Set<String> tokenSet = new HashSet<String>();
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
		    		System.out.println(subset.get(0)+" i "+element+" s¹ synonimami!");
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
*/	}

	private ArrayList<String> getContexts(String token, String[] tokens, String[] tags) {
		// TODO Auto-generated method stub
		ArrayList<String> results = new ArrayList<String>(); 
		for (int i = 0; i<tags.length; ++i) {
			if ((tags[i].equals("NN") || tags[i].equals("NNS")) && !tokens[i].equals(token)) {
				results.add(tokens[i]);
			}
		}
		return results;
	}

	private boolean areSynonyms(String string, String string2,
			HashMap<String, HashSet<String>> contextMap, double tau) {
		// TODO Auto-generated method stub
		HashSet<String> c1 = contextMap.get(string);
		HashSet<String> c2 = contextMap.get(string2);
		
		Set<String> intersection = new HashSet<String>(c1);
		Set<String> union = new HashSet<String>(c1);
		
		intersection.retainAll(c2);
		union.addAll(c2);
		
		return ((double)intersection.size())/((double)union.size()) > tau;
		
	}
	
/*	private boolean checkSynonym (String w1, String w2) {
		DBConnection connection  = new DBConnection();
		connection.open();
		w1 = w1.replace("\'", "\'\'");
		w2 = w2.replace("\'", "\'\'");
		boolean result = (connection.executeQueryAsString("SELECT ARE_SYNONYMS(\'"+w1+"\',\'"+w2+"\')")).equals("TRUE") ? true : false;
	
		connection.close();
		return result;
	}
*/
}

