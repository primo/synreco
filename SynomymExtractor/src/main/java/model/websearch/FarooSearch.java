package model.websearch;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


public class FarooSearch implements WebSearch {
	Map<String, List<Link>> words = new HashMap<String, List<Link>>();
	
	private List<Link> farooQuery(String search){
	    String google = "http://www.faroo.com/api?start=1&length=20&l=en&src=web&i=false&f=json&q=";
	    String charset = "UTF-8";
	    List<Link> ret = new ArrayList<Link>();
		try {
		    URL url;
			url = new URL(google + URLEncoder.encode(search, charset));
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    ResultJ results = new Gson().fromJson(reader, ResultJ.class);
		    Thread.sleep(2000);
			for(EntinyJ item : results.results ){
				Link l = new Link();
				l.url = item.domain;
				l.desc = item.kwic;
				ret.add(l);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ret;
	}
	
	
	public Map<String, List<String>> getLinks(List<String> query) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		for(String item : query ){
			if(!words.containsKey(item)){
				List<Link> val = farooQuery(item);
				words.put(item, val);
			}
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
			if(!words.containsKey(item)){
				List<Link> val = farooQuery(item);
				words.put(item, val);
			}
			ret.put(item, words.get(item));
		}
		return ret;
	}
}
