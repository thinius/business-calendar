package calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.*;

public class CalendarProgram {

	public Scanner scanner;

	String dbUrl = "jdbc:mysql://localhost:3306/db3";
	String user = "db3";
	String pass = "123456";


	public void init() {
		scanner = new Scanner(System.in);

	}

	public void run() {

		
		System.out.println("Enter username: ");
		String username = scanner.next();
		try {
			System.out.println("1");
			Connection myConn = DriverManager.getConnection(dbUrl, user, pass);
			System.out.println("2");

			Statement myStmt = myConn.createStatement();
			System.out.println("3");

			ResultSet myRs = myStmt.executeQuery("select * from bruker WHERE BRUKERNAVN ='" + username+"'");
			System.out.println("4");
			if (!myRs.next()){
				System.out.println("Ugyldig bruker");
				return;
			}
			System.out.println("Enter password: ");
			String password = scanner.next();
			String sqlPassword = myRs.getString("PASSORD");
			System.out.println("5");
			if (sqlPassword.equals(password)) {
				System.out.println("Innlogging fullført!");
				Employee employee = new Employee(username, scanner, myRs, myConn, myStmt);
				employee.run();
			}
			System.out.println("Innlogging feilet!");

		}
		catch (Exception e) {
			System.out.println("Innlogging feilet 2!");

		}
	}

	public static void main(String[] args) {
		CalendarProgram program = new CalendarProgram();
		program.init();
		program.run();
	}

}