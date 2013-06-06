package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceDetector {

	/**
	 * @param args
	 */
	public static String [] getSentences(String text){
		
		String sentences[] = null;
		
		try{
			// always start with a model, a model is learned from training data
			InputStream is = new FileInputStream("en-sent.bin");
			SentenceModel model = new SentenceModel(is);
			SentenceDetectorME sdetector = new SentenceDetectorME(model);
	
			sentences = sdetector.sentDetect(text);
			is.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentences;
	}
}
