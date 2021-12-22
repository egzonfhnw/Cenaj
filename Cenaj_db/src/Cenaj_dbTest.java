import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Cenaj_dbTest {

	static long lines=-1;
	int countrow;
	private static String file = "src/Resources/Software Engineering Assignement.csv";
	private static ArrayList<List<String>> data = new ArrayList<>();
	private static Connection con = null;

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

	public void printCount() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "select count(*) from table1";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			countrow = rs.getInt(1);
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

	public static long countLineBufferedReader() {

		try (BufferedReader reader = new BufferedReader(new FileReader("src/Resources/Software Engineering Assignement.csv"))) {
			while (reader.readLine() != null)
				lines++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;

	}

	@Test
	void test() {
		try {
			con = connect();
			countLineBufferedReader();
			printCount();
			assertEquals(countrow, lines);
System.out.println(countrow);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
