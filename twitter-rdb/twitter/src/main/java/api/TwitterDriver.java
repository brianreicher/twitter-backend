package api;

/**
 * This program exercises the DPDatabaseAPI (MySQL implementation).
 * Notice that nothing other than the instantiation of the API shows us that
 * the underlying database is Relational, or MySQL.

 */
public class TwitterDriver {

    private static DPDatabaseAPI api = new DPDatabaseMysql();

    public static void main(String[] args) throws Exception {

    	// Authenticate your access to the server.
		String url =  "jdbc:mysql://localhost:3306/twitter?serverTimezone=EST5EDT";
		String user = System.getenv("TWITTER_USER");
		String password = System.getenv("TWITTER_PASSWORD");

		api.authenticate(url, user, password); // DON'T HARDCODE PASSWORDS!
/**
		List<Doctor> doctors = api.acceptingNewPatients("oncology");
		System.out.println("Number of matches: " + doctors.size());
		if (doctors.size()>0)
			System.out.println("\n\nAvailable oncologists");
			for (Doctor d : doctors)
				System.out.println(d.toString());


		// Insert a test patient
		// Exception thrown if patient already exists


		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	    System.out.println("\n\nRegistering a patient");
	    Patient p = new Patient("Gates", "Billy", 'M', sdf.parse("10-28-1955"));
		int pid = api.registerPatient(p);

		if (pid != -1)
			System.out.println("Patient " + p.getFirstName() + " " + p.getLastName() + " registered (ID = "+pid+").");




		//	     Test adding specialities
		//		 Note the repeats!
//	    System.out.println("Adding specialties");
//	    int sid1 = api.getOrInsertSpecialty("oncology");
//		int sid2 = api.getOrInsertSpecialty("cardiology");
//		int sid3 = api.getOrInsertSpecialty("pediatrics");
//		int sid4 = api.getOrInsertSpecialty("ent");            // This one doesn't yet exist
//		int sid5 = api.getOrInsertSpecialty("cardiology");     // repeat request
//		System.out.println("Specialty IDs: "+sid1+" "+sid2+" "+sid3+" "+sid4+" "+sid5);

		// add some doctors to a list collection and then add them all to the database

//		List<Doctor> drlist = new ArrayList<Doctor>();
//		drlist.add(new Doctor("House", "Gregory", true, "diagnostics", "MGH"));
//		drlist.add(new Doctor("No", "Dr.", false, "oncology", "MGH"));
//		drlist.add(new Doctor("Holmes", "Sherlock", true, "ent", "MGH"));
//		drlist.add(new Doctor("Rachlin", "John", true, "brain surgery", "Mayo Clinic"));
//		api.insertDoctors(drlist);
**/
		api.closeConnection();

	}
}
