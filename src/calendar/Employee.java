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
		case 2: 		getAllAppointments();
		break;
		case 3: 		getAppointmentsOrdered();
		break;
		case 4: 		deleteAppointment();
		break;
		case 5: 		makeNewAppointment();
		break;
		case 6: 		deleteUser();
		break;
		case 7: 		updateAlarm();
		break;
		case 8: 		getCommonAppointmentsWithAnotherUser();
		break;
		case 9: 		addUserToAppointment();
		break;
		case 10:		editAppointment(); 
		break;
		case 11:		getUsersAlphabetical(); 
		break;
		case 12:		updateAppointmentTime();
		break;
		default:	 	break;
		}
	}

	public void getNextAppointment() {

		ResultSet rs = getRs("SELECT avtale.*, moeterom.* FROM avtale JOIN moeterom ON moeterom.ROMID = avtale.ROMID");
		try{
			while (rs.next()){
				System.out.println(rs.getString("ANTALL") +" "+rs.getString("Beskrivelse"));
			}
		}
		catch (Exception e){}


	}

	// returns all appointments sorted by start time
	public void getAppointmentsOrdered() {
		System.out.println( "===========" );
		System.out.println( "Appointments sorted by start time" );
		System.out.println( "===========" );
		String query = " SELECT * FROM avtale GROUP BY STARTTIDSPUNKT ;";
		ResultSet rs = getRs(query);
		String ut = "";
		try {
			while (rs.next()){
				ut += "Starttidspunkt: " + rs.getString("STARTTIDSPUNKT") + "\nBeskrivelse: " + rs.getString("BESKRIVELSE") + "\nRomID: " + rs.getString("ROMID") + "\n";
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ut);
	}

	// returns all users in alphabetical order
	public void getUsersAlphabetical() {
		System.out.println( "===========" );
		System.out.println( "User in alphabetical order" );
		System.out.println( "===========" );
		String query = " SELECT brukernavn FROM bruker GROUP BY bruker ;";
		ResultSet rs = getRs(query);
		String ut = "";
		try {
			if (rs.next()){
				ut += rs.getString("brukernavn");
				ut += "\n";
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ut);
	}

	public void updateAppointmentTime(){


		String avtaleStart = "";
		String avtaleSlutt = "";
		String avtaleDato = "";

		System.out.println("Skriv avtaleID til avtalen du vil endre");
		String avtaleID = scanner.next();


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

		doStatement("UPDATE `db3`.`avtale` SET `AVTALEDATO`='"+genDato(avtaleDato)+"';");
		doStatement("UPDATE `db3`.`avtale` SET `STARTTIDSPUNKT`='"+genTime(avtaleStart,avtaleDato)+"';");

	}

	public void getAllAppointments() {
		ResultSet rs = getRs("SELECT avtale.*, deltaker.*,bruker.* FROM avtale JOIN deltaker ON deltaker.AVTALEID = avtale.AVTALEID JOIN bruker ON bruker.BRUKERNAVN = deltaker.BRUKERNAVN");
		try {
			while (rs.next()){
				if (Integer.valueOf(rs.getString("PERSONNUMMER")) == Integer.valueOf(usernameID)){
					System.out.println(rs.getString("BESKRIVELSE")+" "+rs.getString("STARTTIDSPUNKT"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	// returns all appointments sorted by start time
	public void getAppointmentsOrderedEndTime() {
		System.out.println( "===========" );
		System.out.println( "Appointments sorted by end time" );
		System.out.println( "===========" );
		String query = " SELECT * FROM avtale GROUP BY SLUTTIDSPUNKT ;";
		ResultSet rs = getRs(query);
		String ut = "";
		try {
			while (rs.next()){
				ut += "Sluttidspunkt: " + rs.getString("SLUTTIDSPUNKT") + "\nBeskrivelse: " + rs.getString("BESKRIVELSE") + "\nRomID: " + rs.getString("ROMID") + "\n";
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ut");
	}

	// returns meeting rooms with capacity >9
	public void getMeetingRoomsHighCapacity() {
		System.out.println( "===========" );
		System.out.println( "Meeting rooms with a capacity of 10 or more" );
		System.out.println( "===========" );
		String query = " SELECT antall, ROMID, MIN(antall) FROM moeterom HAVING MIN(antall) > 9 ;";
		ResultSet rs = getRs(query);
		String ut = "";
		try {
			if (rs.next()){
				ut += rs.getString("ROMID") + "\n";
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ut);
	}

	public void getRoomCapacity() {
		System.out.println( "===========" );
		System.out.println( "Next appointment" );
		System.out.println( "===========" );
		String query =  "SELECT moeterom.antall FROM moeterom INNER JOIN (SELECT avtale.ROMID FROM deltaker WHERE brukernavn = '" + username + "' INNER JOIN avtale ON deltaker.avtaleID = avtale.avtaleID) deltakerAvtale ON moeterom.ROMID = deltakerAvtale.ROMID ;";
		System.out.println("1");
		ResultSet rs = getRs(query);
		System.out.println("2");
		String ut = "";
		try {
			while (rs.next()){
				System.out.println("bob");
				ut += rs.getString("ANTALL");
				ut += "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(ut);	
	}

	public void deleteAppointment() {
		System.out.println("Hvilken avtale vil du slette? (avtaleID)");
		String id = scanner.next();

		doStatement("DELETE w FROM avtale w INNER JOIN deltaker e ON w.AVTALEID=e.AVTALEID Where w.AVTALEID ='"+id+"'"); 
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

	public void updateAlarm() {

		doStatement("update alarm u inner join avtale s on u.AVTALEID = s.AVTALEID set u.TID = s.STARTTIDSPUNKT");

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
		System.out.println("3 - getAppointmentsOrdered()");
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
			e.printStackTrace();
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
