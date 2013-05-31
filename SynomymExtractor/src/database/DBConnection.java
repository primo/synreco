package database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	Connection connection = null;
	
	public void open() {
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/synonyms_db", "postgres",
					"hadin1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String executeQueryAsString(String query) {
		String s = null;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			s = rs.getString(1);
			rs.close();
			st.close();
			return s;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
