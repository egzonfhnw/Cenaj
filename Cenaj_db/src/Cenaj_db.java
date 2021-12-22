
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Cenaj_db {

	private static String file = "src/Resources/Software Engineering Assignement.csv";
	private static ArrayList<List<String>> data = new ArrayList<>();
	private static Connection con = null;

	public Cenaj_db() {
		con = connect();
		readFromFile();
		System.out.println("read " + data.size() + " entries");
		List<String> header = data.remove(0);
		createTable(header, con);
		insert(data);
		printCount();
		close();
	}

	private void readFromFile() {
		File fi = null;
		try {
			fi = new File(file);
			Scanner scan = new Scanner(fi);
			while (scan.hasNext()) {
				String line = scan.nextLine();
				String[] myarr = line.split(";");
				List<String> lineArray = new ArrayList<String>();
				for (String st : myarr) {
					lineArray.add(st);
				}

				data.add(lineArray);

			}
			scan.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createTable(List<String> list, Connection con) {
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate("drop table if exists table1");
			String sql = "create table table1 (\n";
			sql += "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n";
			sql += "PERIOD TEXT,";
			sql += "WAGE_TYPE TEXT,";
			sql += "VALUE_P INT,";
			sql += "STATUS TEXT)";
			System.out.println(sql);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void insert(ArrayList<List<String>> data2) {

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("INSERT INTO table1 (PERIOD, WAGE_TYPE, VALUE_P, STATUS) VALUES (?, ?, ?, ?)");
			for (List<String> st : data2) {
				ps.setString(1, st.get(0));
				ps.setString(2, st.get(1));
				ps.setString(3, st.get(2));
				ps.setString(4, st.get(3));
				ps.execute();
			}
			System.out.println("Data has been inserted");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void printCount() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "select count(*) from table1";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			int countrow = rs.getInt(1);
			System.out.println("Length of rows: " + countrow + "\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void close() {
		if (con != null) {
			try {
				con.close();
				System.out.println("Connection was closed");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static Connection connect() {
		String serverInfo = "jdbc:sqlite:se.db";
		System.out.println("Opening connection to " + serverInfo + "\n");
		try {
			con = DriverManager.getConnection(serverInfo);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return con;
	}

	public static void main(String[] args) {
		Cenaj_db db2 = new Cenaj_db();
	}
}