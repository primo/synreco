package model.websearch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

class EntinyJ {
	public String domain;
	public String kwic;
};
class ResultJ {
	public  List<EntinyJ> results;
}

public class FileSearch implements WebSearch {
	Map<String, List<Link>> words = new HashMap<String, List<Link>>();
	public FileSearch(String file){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = new String();
			while((line = br.readLine()) != null && !line.isEmpty()){
				String word = line;
				String result = br.readLine();
				ResultJ ret = new Gson().fromJson(result, ResultJ.class);
				List<Link> list = new ArrayList<Link>();
				for(EntinyJ item : ret.results ){
					Link l = new Link();
					l.url = item.domain;
					l.desc = item.kwic;
					list.add(l);
				}
				words.put(word, list);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String, List<String>> getLinks(List<String> query) {
        System.out.println("Getting links from provided text file..");
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
		for(String item : query ){
			if(words.containsKey(item)){
				List<String> val = new ArrayList<String>();
				for(Link link : words.get(item) ){
					val.add(link.url);
				}
				ret.put(item, val);
			}else{
				ret.put(item, new ArrayList<String>());
			}
		}
		return ret;
	}
	
	public Map<String, List<String>> getAllLinks() {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
	    Iterator it = words.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String item = pairs.getKey().toString();
			List<String> val = new ArrayList<String>();
			for(Link link : words.get(item) ){
				val.add(link.url);
			}
			ret.put(item, val);
		}
		return ret;
	}
	public Map<String, List<Link>> getLinksAndSummary(List<String> query) {
		Map<String, List<Link>> ret = new HashMap<String, List<Link>>();
		for(String item : query ){
			if(words.containsKey(item)){
				ret.put(item, words.get(item));
			}else{
				ret.put(item, new ArrayList<Link>());
			}
		}
		return ret;
	}

	
}
