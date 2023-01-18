package twitter.src.main.java.api;

import java.util.List;

public interface DPDatabaseAPI {

    /**
     * Register a new patient
     * @param p The patient
     * @return The newly created patient ID or -1 if patient already exists
     */

    public int registerPatient(Patient p) ;

    /**
     * Insert one doctor
     * @param d
     * @return
     */
    public int insertDoctor(Doctor d);

    /**
     * Insert many doctors.  Demonstrates using prepared statements
     * @param drlist A List of Doctor objects
     */
    public void insertDoctors(List<Doctor> drlist);

    /**
     * Get or insert on specialty term
     * @param specialty The specialty
     * @return ID of a new or existing specialty.
     */
    public int getOrInsertSpecialty(String specialty);


    /**
     * Get or insert on hospital term
     * @param hospital The name of the hospital
     * @return ID of a new or existing hospital.
     */
    public int getOrInsertHospital(String hospital);


    /**
     * Find doctors accepting new patients for a given patient
     * @param specialty Required specialty.  Better would be to accept null as "any"
     * @return A list of doctors
     */
    public List<Doctor> acceptingNewPatients(String specialty);
    public List<Doctor> acceptingNewPatientsSP(String specialty); // alternate using a stored procedure


    /**
     * Set connection settings
     * @param url
     * @param user
     * @param password
     */
    public void authenticate(String url, String user, String password);

    /**
     * Close the connection when application finishes
     */
    public void closeConnection();

}
