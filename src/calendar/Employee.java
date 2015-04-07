package DB3;

import java.util.Scanner;
import java.sql.*;

public class Employee {

	private String testString;
	private String username;
	public Scanner scanner;
	public ResultSet myRs;
	Connection myConn;
	Statement myStmt;


	public Employee(String username, Scanner scanner, ResultSet myRs, Connection myConn, Statement myStmt) {
		this.username = username;
		this.scanner = scanner;
		this.myRs = myRs;
		this.myConn = myConn;
		this.myStmt = myStmt;
	}


	public void run() {
		while (true) {
			welcome();
			int taskNumber = scanner.nextInt();
			if (taskNumber == -1) {
				break;
			}
			executeTask(taskNumber);
		}
		System.out.print("CalendarProgram exited.");
	}

	public void executeTask(int taskNumber) {
		switch (taskNumber) {
		case 1: 		getNextAppointment();
		break;
		case 2: 		getNextSevenAppointments();
		break;
		case 3: 		getAllAppointments();
		break;
		case 4: 		addNewUser();
		break;
		case 5: 		makeNewAppointment();
		break;
		case 6: 		deleteUser();
		break;
		case 7: 		getAppointmentsWithAnotherUser();
		break;
		case 8: 		getCommonAppointmentsWithAnotherUser();
		break;
		case 9: 		addUserToAppointment();
		break;
		case 10:		editAppointment(); 
		break;
		default:	 	break;
		}
	}

	public void getNextAppointment() {
		return;
	}


	public void getNextSevenAppointments() {
		return;
	}

	public void getAllAppointments() {
		return;
	}

	public void addNewUser() {
		return;
	}

	public void makeNewAppointment() {
		
		String avtaleStart = "";
		String avtaleSlutt = "";
		String avtaleDato = "";


		boolean sjekk1 = false;
		while (!sjekk1){
			System.out.println("Når starter avtalen? (HH:MM)");
			avtaleStart = scanner.next();
			sjekk1 = tidSjekk(avtaleStart);
			if (!sjekk1){
				System.out.println("Ugyldig tid mfer");
			}
		}
		
		boolean sjekk2 = false;
		while (!sjekk2){
			System.out.println("Når slutter avtalen? (HH:MM)");
			avtaleSlutt = scanner.next();
			sjekk2 = tidSjekk(avtaleSlutt);
			if (!sjekk2){
				System.out.println("Ugyldig tid mfer");
			}
		}

		boolean sjekk3 = false;
		while (!sjekk3){
			System.out.println("Hvilken dato er avtalen? (DD/MM/YY)");
			avtaleDato = scanner.next();
			sjekk3 = datoSjekk(avtaleDato);
			if (!sjekk3){
				System.out.println("Ugyldig dato mfer");
			}
		}
		System.out.println("Gi en kort beskrivelse av avtalen (Sted, osv.)");
		String beskrivelse = scanner.next();
		ResultSet idSet = getRs("select AVTALEID from avtale");
		int sisteId = 0;
		try {
			while (idSet.next()) {
				if (sisteId<Integer.valueOf(idSet.toString())){
					sisteId = Integer.valueOf(idSet.toString());
				}
			}
		}
		catch (Exception e){}


		doStatement("INSERT INTO `db3`.`avtale` (`AVTALEID`, `AVTALEDATO`, `STARTTIDSPUNKT`, `SLUTTIDSPUNKT`, `BESKRIVELSE`, `AKTIV`, `ANTALL`, `ROMID1`, `OPPRETTERID`) VALUES ('"+sisteId+"', '2015-10-07', '2015-10-07 01:23:45', '2015-10-07 06:59:59', '123asdaf', '1', '2', '1', '1')");

		System.out.println("Vil du legge til andre brukere til avtalen? (Ja/Nei)");
		String svar = scanner.next();
		while (svar.equalsIgnoreCase("ja") || svar.equalsIgnoreCase("j")) {
			System.out.println("Skriv inn brukernavn til brukeren du vil legge til:");
			String addBruker = scanner.next();
		}

		return;
	}

	public void deleteUser() {
		try {
			System.out.println( "===========" );
			System.out.println( "Delete user" );
			System.out.println( "===========" );
			System.out.println( "Please enter the username of the user you want to delete: " );
			String toBeDeleted = scanner.next();
			String query = "DELETE FROM ansatt WHERE brukernavn = '" + toBeDeleted + "';";
			ResultSet rs = myStmt.executeQuery(query);
			System.out.println( "User " + rs.getString("brukernavn") +  " successfully deleted.");
		}
		catch (Exception e) {

		}
	}

	public void getAppointmentsWithAnotherUser() {
		return;
	}

	public void getCommonAppointmentsWithAnotherUser() {
		return;
	}

	public void addUserToAppointment() {
		return;
	}

	public void editAppointment() {
		return;
	}

	public void welcome() {
		System.out.println("\nHi " + username + "! Here is the menu:");
		System.out.println("1 - My next appointment");
		System.out.println("2 - My appointments the next 7 days");
		System.out.println("3 - List all my appointments");
		System.out.println("4 - Add new user");
		System.out.println("5 - Make new appointment");
		System.out.println("6 - Delete user");
		System.out.println("7 - Find participants in appointment");
		System.out.println("8 - Common appointments with another user");
		System.out.println("9 - Add user to appointment");
		System.out.println("10 - Edit appointment");
		System.out.println("-1 - Exit CalendarProgram");
	}

	public String getUsername() {
		return username;
	}

	private void doStatement(String input){
		try{
			myStmt.executeQuery(input);
		}
		catch (Exception e){

		}
	}

	private ResultSet getRs(String input) {
		try{

			ResultSet output = myStmt.executeQuery(input);
			return output;
		}
		catch (Exception e) {
			return null;
		}
	}
	boolean datoSjekk(String dato) {
		int counter = 0;
		for (int i = 0; i < dato.length(); i++) {
			if (dato.charAt(i) == '/'){
				counter++;
			}
		}
		if (counter==2){
			String[] parts = dato.split("/");
			String dag = parts[0];
			String mnd = parts[1];
			String aar = parts[2];
			if (Integer.valueOf(dag)<1 ||Integer.valueOf(dag)>31 || Integer.valueOf(mnd)<1 ||Integer.valueOf(mnd)>12 || Integer.valueOf(aar)<0 || Integer.valueOf(aar)>99){
				return false;
			}
			return true;
		}
		return false;
	}
	
	boolean tidSjekk(String tid) {
		int counter = 0;
		for (int i = 0; i < tid.length(); i++) {
			if (tid.charAt(i) == ':'){
				counter++;
			}
		}
		if (counter==2){
			String[] parts = tid.split(":");
			String hr = parts[0];
			String min = parts[1];
			if (Integer.valueOf(hr)<0 ||Integer.valueOf(hr)>24 || Integer.valueOf(min)<0 ||Integer.valueOf(min)>59){
				return false;
			}
			return true;
		}
		return false;
	}

}
