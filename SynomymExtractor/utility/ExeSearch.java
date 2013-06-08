package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;



import java.util.*;


public class ExeSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		PrintWriter out = new PrintWriter("wynik.txt");
		char tab[] = new char[65540];
	    Map<String, String> mapa  = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line = new String();
		while((line = br.readLine()) != null && !line.isEmpty()){
			String word = new String();
			for(int i=0;i<line.length();++i){
				if(Character.isLetter(line.charAt(i))){
					word += line.charAt(i);
				}else{
					if(word.length()>3)
						mapa.put(word.toLowerCase(), "");
					word = new String();
				}
				
			}
		}
		Iterator it = mapa.entrySet().iterator();
	    String google = "http://www.faroo.com/api?start=1&length=20&l=en&src=web&i=false&f=json&q=";
	    String charset = "UTF-8";
	    int cc = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String search = pairs.getKey().toString();
		    out.println(search);
		    URL url = new URL(google + URLEncoder.encode(search, charset));
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    while(true){
		    	int len = reader.read(tab, 0, 65536);
		    	if(len<0){ break;}
		    	out.write(tab, 0, len);
		    }
		    out.println();
		    out.flush();
		    Thread.sleep(2000);
	        it.remove();
	    }
	    out.close();
	}
}
