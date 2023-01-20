package api;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import database.DBUtils;


public class DPDatabaseMysql implements DPDatabaseAPI {


    // For demonstration purposes. Better would be a constructor that takes a file path
    // and loads parameters dynamically.
    DBUtils dbu;


    /**
     * Register a patient - no recovery of newly created patient_id
     * @param p The patient
     */
    public void registerPatientMethod1(Timeline p)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO patient (lastname,firstname,sex,dob) VALUES" +
                "('"+p.getLastName()+"','"+p.getFirstName()+"','"+p.getSex()+"','"+sdf.format(p.getDob())+"')";

        try {

            // get connection and initialize statement
            Connection con = dbu.getConnection(); // get the active connection
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);

            // Cleanup
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: "+sql);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Register a patient - fetch patient_id that was created
     * @param p The Patient
     * @return patient_id
     */
    public int registerPatientMethod2(Timeline p)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO patient (lastname,firstname,sex,dob) VALUES" +
                "('"+p.getLastName()+"','"+p.getFirstName()+"','"+p.getSex()+"','"+sdf.format(p.getDob())+"')";
        int key = -1;
        try {

            // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            // extract auto-incremented ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) key = rs.getInt(1);

            // Cleanup
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: "+sql);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return key;
    }


    /**
     * Register a new patient
     * @param p The patient
     * @return The newly created patient ID or -1 if patient already exists
     */
    public int registerPatient(Timeline p)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO patient (lastname,firstname,sex,dob) VALUES" +
                    "('"+p.getLastName()+"','"+p.getFirstName()+"','"+p.getSex()+"','"+sdf.format(p.getDob())+"')";
        return dbu.insertOneRecord(sql);
    }


    /**
     * Get or insert on specialty term
     * @param specialty The specialty
     * @return ID of a new or existing specialty.
     */
    public int getOrInsertSpecialty(String specialty)
    {
      return dbu.getOrInsertTerm("specialty", "specialty_id", "specialty", specialty);
    }

    /**
     * Get or insert on specialty term
     * @param hospital The name of the hospital
     * @return ID of a new or existing hospital.
     */
    public int getOrInsertHospital(String hospital)
    {
        return dbu.getOrInsertTerm("hospital", "hospital_id", "name", hospital);
    }


    /**
     * Insert one doctor
     * @param d
     * @return
     */
    public int insertDoctor(Doctor d)
    {

        // Lookup specialty
        int sid = getOrInsertSpecialty(d.getSpecialty());
        int hid = getOrInsertHospital(d.getHospital());

        // build and run query
        String sql = "INSERT INTO doctor (lastname,firstname,new_patients,specialty_id) VALUES" +
                    "('"+d.getLastName()+"','"+d.getFirstName()+"',"+d.isAcceptingNewPatients()+","+sid+","+hid+")";

            // Return new doctor ID
        return dbu.insertOneRecord(sql);

    }


    /**
     * Insert many doctors.  Demonstrates using prepared statements
     * @param drlist A List of Doctor objects
     */
    public void insertDoctors(List<Doctor> drlist)
    {
        // This seems simplest but is not really the most efficient when inserting many records

//         for (Doctor d : drlist)
//            insertDoctor(d);


        String sql = "INSERT INTO doctor (lastname,firstname,new_patients,specialty_id,hospital_id) VALUES (?,?,?,?,?)";
        try {
            Connection con = dbu.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);

            for (Doctor d : drlist) {
                pstmt.setString(1, d.getLastName());
                pstmt.setString(2, d.getFirstName());
                pstmt.setBoolean(3, d.isAcceptingNewPatients());
                pstmt.setInt(4, getOrInsertSpecialty(d.getSpecialty()));
                pstmt.setInt(5, getOrInsertHospital(d.getHospital()));
                pstmt.execute();
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Find doctors accepting new patients for a given patient
     * @param specialty Required specialty.  Better would be to accept null as "any"
     * @return A list of doctors
     */
    public List<Doctor> acceptingNewPatients(String specialty)
    {
        String sql = "select doctor_id, lastname, firstname, new_patients, specialty, h.name hospital "+
                     "from doctor d join specialty using (specialty_id) " +
                     "join hospital h using (hospital_id) "+
                     "where new_patients = 1 "+
                     "and specialty like '"+specialty.toUpperCase()+"'";

        List<Doctor> doctors = new ArrayList<Doctor>();


        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next() != false)
                doctors.add(new Doctor(rs.getInt("doctor_id"), rs.getString("lastname"), rs.getString("firstname"),
                        rs.getBoolean("new_patients"), rs.getString("specialty"), rs.getString("hospital")));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return doctors;
    }

    /**
     * Find doctors accepting new patients for a given patient using STORED PROCEDURE
     * @param specialty Required specialty.  Better would be to accept null as "any"
     * @return A list of doctors
     */
    public List<Doctor> acceptingNewPatientsSP(String specialty)
    {
        List<Doctor> doctors = new ArrayList<Doctor>();

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection();

            CallableStatement stmt = con.prepareCall("{call acceptingNewPatients(?)}");

            stmt.setString(1, specialty);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next() != false)
                doctors.add(new Doctor(rs.getInt("doctor_id"), rs.getString("lastname"), rs.getString("firstname"),
                        rs.getBoolean("new_patients"), rs.getString("specialty"), rs.getString("hospital")));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return doctors;
    }


    public void authenticate(String url, String user, String password) {

        dbu = new DBUtils(url, user, password);
    }


    /**
     * Close the connection when application finishes
     */
    public void closeConnection() { dbu.closeConnection(); }


    @Override
    public void postTweet(Tweet t) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void postTweets(List<Tweet> twlist) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public List<Tweet> getTimeline(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Integer> getFollowers(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Integer> getFollowees(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Tweet> getTweets(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }
}
