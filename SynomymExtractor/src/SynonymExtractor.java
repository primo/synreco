import java.io.IOException;

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

import database.DBConnection;



public class SynonymExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String text = "The Vikings (from Old Norse víkingr) were seafaring north Germanic people who raided, " +
				"traded, explored, and settled in wide areas of Europe, Asia, and the North Atlantic islands from the " +
				"late 8th to the mid-11th centuries. The Vikings employed wooden longships with wide, shallow-draft " +
				"hulls, allowing navigation in rough seas or in shallow river waters. The ships could be landed on " +
				"beaches, and their light weight enabled them to be hauled over portages. These versatile ships " +
				"allowed the Vikings to travel as far east as Constantinople and the Volga River in Russia, as far west " +
				"as Iceland, Greenland, and Newfoundland, and as far south as Nekor. This period of Viking expansion, " +
				"known as the Viking Age, constitutes an important element of the medieval history of Scandinavia, " +
				"Great Britain, Ireland, Russia, and the rest of Europe.Popular conceptions of the Vikings often " +
				"differ from the complex picture that emerges from archaeology and written sources. A romanticised " +
				"picture of Vikings as noble savages began to take root in the 18th century, and this developed and " +
				"became widely propagated during the 19th-century Viking revival. The received views of the Vikings " +
				"as violent brutes or intrepid adventurers owe much to the modern Viking myth that had taken shape by " +
				"the early 20th century. Current popular representations are typically highly clichéd, presenting the " +
				"Vikings as familiar caricatures.";
		
		String POS = "NN";
		
		Model model = new Model();
		ArrayList<ArrayList<String>> res = model.getSynonyms(text, POS, 0.1);
		
		for (ArrayList<String> list : res) {
			if (res.size()== 1) continue;
			for (String w : list) {
				System.out.print(w+"; ");				
			}
			System.out.print("\n");
		}
		
		//DBConnection connection  = new DBConnection();
		//connection.open();
		//System.out.print(connection.executeQueryAsString("SELECT ARE_SYNONYMS(\'film\',\'movie\')"));
		//connection.close();
		
			//POSTagger.POSTag(Tokenizer.tokenize());

	}

}
