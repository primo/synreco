


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Test - Wartoœæ miary Context Similarity dla s³ów komputer i PC");
		
		DBConnection connection  = new DBConnection();
		connection.open();
		System.out.println("czekaj.. wykonywanie zapytania..");
		String res = connection.executeQueryAsString("SELECT GET_CONTEXT_SIM(\'computer\',\'pc\', 1)");
		System.out.println("Zapytanie zakoñczone. Wartoœæ miary: "+res);
		connection.close();
	}

}
