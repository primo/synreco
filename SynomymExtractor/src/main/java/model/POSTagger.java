package model;

import java.io.File;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class POSTagger {
	
	
	public static String[] POSTag(String [] tokens) {
		
		POSModel model = new POSModelLoader().load(new File("en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		String tags[] = tagger.tag(tokens);
		
		return tags;
		
	}
	
	
}
