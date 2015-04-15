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
	String usernameID;


	public Employee(String username, Scanner scanner, ResultSet myRs, Connection myConn, Statement myStmt) {
		this.username = username;
		this.scanner = scanner;
		this.myRs = myRs;
		this.myConn = myConn;
		this.myStmt = myStmt;
		this.usernameID = getPnr();
		System.out.println(usernameID);

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
		case 4: 		deleteAppointment();
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

	public void deleteAppointment() {
		
		
		
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
			System.out.println("Hvilken dato er avtalen? (DD/MM/YYYY)");
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
				if (sisteId<Integer.valueOf(idSet.getInt("avtaleid"))){
					sisteId = Integer.valueOf(idSet.getInt("avtaleid"));
				}
			}
			sisteId++;
		}
		catch (Exception e){}


		doStatement("INSERT INTO `db3`.`avtale` (`AVTALEID`, `AVTALEDATO`, `STARTTIDSPUNKT`, `SLUTTIDSPUNKT`, `BESKRIVELSE`, `ROMID1`, `OPPRETTERID`) VALUES ('"+sisteId+"', '"+genDato(avtaleDato)+"', '"+genTime(avtaleStart, avtaleDato)+"', '"+genTime(avtaleSlutt, avtaleDato)+"', '"+beskrivelse+"', '1', '"+usernameID+"')");
		doStatement("INSERT INTO `db3`.`deltaker` (`BRUKERNAVN`, `AVTALEID`) VALUES ('"+username+"', '"+sisteId+"');");
		System.out.println("Vil du legge til andre brukere til avtalen? (Ja/Nei)");
		String svar = scanner.next();
		if (svar.equalsIgnoreCase("ja") || svar.equalsIgnoreCase("j")) {
			addUser(sisteId);
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
			String query = "DELETE FROM bruker WHERE brukernavn = '" + toBeDeleted + "';";
			doStatement(query);
			System.out.println( "User " + toBeDeleted +  " successfully deleted.");
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
		System.out.println("Skriv inn avtaleID på avtalen: ");
		int id = Integer.parseInt(scanner.next());
		addUser(id);
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
		System.out.println("4 - Delete one of your appointments");
		System.out.println("5 - Make new appointment");
		System.out.println("6 - Delete user");
		System.out.println("7 - Find participants in appointment");
		System.out.println("8 - Common appointments with another user");
		System.out.println("9 - Add user to appointment");
		System.out.println("10 - Edit appointment");
		System.out.println("-1 - Exit CalendarProgram");
	}


	private void doStatement(String input){
		try{
			myStmt.executeUpdate(input);
		}
		catch (Exception e){
			System.out.println("Statement feilet");
			e.printStackTrace();
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
			if (Integer.valueOf(dag)<1 ||Integer.valueOf(dag)>31 || Integer.valueOf(mnd)<1 ||Integer.valueOf(mnd)>12 || Integer.valueOf(aar)<0 || Integer.valueOf(aar)>9999){
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
		if (counter==1){
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

	void addUser(int avtaleId){
		boolean kjor = true;
		while (kjor){
			System.out.println("Skriv inn brukernavnet til brukernavnet du vil legge til:");
			String user = scanner.next();
			ResultSet pnrSet = getRs("select BRUKERNAVN from bruker");

			try {
				while (pnrSet.next()) {
					if (user.equalsIgnoreCase((pnrSet.getString("brukernavn")))){
						doStatement("INSERT INTO `db3`.`deltaker` (`BRUKERNAVN`, `AVTALEID`) VALUES ('"+user+"', '"+avtaleId+"');");
						System.out.println(user+" er lagt til avtalen.\n");
					}
				}
			}
			catch (Exception e){}
			System.out.println("Vil du legge til en ny bruker?");
			String svar = scanner.next();
			if(!(svar.equalsIgnoreCase("ja") || svar.equalsIgnoreCase("j") || svar.equalsIgnoreCase("yes") || svar.equalsIgnoreCase("y"))){
				kjor = false;
			}
		}
	}

	String genDato(String inn){
		String[] parts = inn.split("/");
		String dag = parts[0];
		String mnd = parts[1];
		String aar = parts[2];
		String ut = aar+"-"+mnd+"-"+dag;
		return ut;
	}
	String genTime(String tid, String dato){
		String d = genDato(dato);
		String t = d+" "+tid+":00";
		return t;
	}
	String getPnr(){
		ResultSet rs = getRs("select * from BRUKER WHERE BRUKERNAVN='"+username+"'");
		try {
			if (rs.next()){
				String ut = rs.getString("PERSONNUMMER");
				return ut;
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}
}
