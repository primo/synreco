


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Test - Warto�� miary Context Similarity dla s��w komputer i PC");
		
		DBConnection connection  = new DBConnection();
		connection.open();
		System.out.println("czekaj.. wykonywanie zapytania..");
		String res = connection.executeQueryAsString("SELECT GET_CONTEXT_SIM(\'computer\',\'pc\', 1)");
		System.out.println("Zapytanie zako�czone. Warto�� miary: "+res);
		connection.close();
	}

}
