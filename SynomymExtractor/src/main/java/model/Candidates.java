package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class Candidates {

	public static Map<String, List<String>> generate(Map<String, List<String>> urls) {
		Set<String> set = new HashSet<String>();
		Map<String, List<String>> pivot = new HashMap<String, List<String>>();
		Map<String, List<String>> ret = new HashMap<String, List<String>>();

	    Iterator it = urls.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String word = pairs.getKey().toString();
			for(String domain : urls.get(word)){
				List<String> com;
				if(pivot.containsKey(domain)){
					com = pivot.get(domain);
				}else{
					com = new ArrayList<String>();
				}
				com.add(word);
				pivot.put(domain, com);
			}
	    }
	    it = pivot.entrySet().iterator();
	    System.out.println("Domen: "+pivot.size());
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String word = pairs.getKey().toString();
	        List<String> group = pivot.get(word);
	        if(group.size()>urls.size()/2){
	        	System.out.println(word+" = "+group.size());
	        	continue;
	        }
	        for(int i=0;i<group.size();++i){
	        	String a = group.get(i);
		        for(int j=i+1;j<group.size();++j){
		        	String b = group.get(j);
		        	if(set.add(a+"#"+b)){
		        		List<String> tmp;
		        		if(ret.containsKey(a))	tmp = ret.get(a);
		        		else 		tmp = new ArrayList<String>();
		        		tmp.add(b);
		        		ret.put(a, tmp);
		        		if(ret.containsKey(b))	tmp = ret.get(b);
		        		else 		tmp = new ArrayList<String>();
		        		tmp.add(a);
		        		ret.put(b, tmp);
		        	}
		        }
	        }
	    }
	    System.out.println("kandydatów: "+set.size());
		return ret;
	}
}
