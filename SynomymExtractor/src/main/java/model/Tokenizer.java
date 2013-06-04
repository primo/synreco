package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {

	/**
	 * @param args
	 */
	public static String[] tokenize(String text){
		
		try {
			
			InputStream is = new FileInputStream("en-token.bin");
 
			TokenizerModel model = new TokenizerModel(is);
 
			TokenizerME tokenizer = new TokenizerME(model);
 
			String tokens[] = tokenizer.tokenize(text);
 
		
			is.close();
			
			return tokens;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

}
