package twitter.src.main.java.api;

public class Doctor {

    private int doctorID;
    private String lastName;
    private String firstName;
    private boolean newPatients;
    private String specialty;
    private String hospital;


    public Doctor(String lastName, String firstName, boolean newPatients, String specialty, String hospital) {
        this.doctorID = -1;
        this.lastName = lastName;
        this.firstName = firstName;
        this.newPatients = newPatients;
        this.specialty = specialty;
        this.hospital = hospital;
    }

    public Doctor(int doctorID, String lastName, String firstName, boolean newPatients, String specialty, String hospital) {
        this.doctorID = doctorID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.newPatients = newPatients;
        this.specialty = specialty;
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorID=" + doctorID +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", newPatients=" + newPatients +
                ", specialty='" + specialty + '\'' +
                ", hospital='" + hospital + '\'' +
                '}';
    }




    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isAcceptingNewPatients() {
        return newPatients;
    }

    public void setAcceptingNewPatients(boolean newPatients) {
        this.newPatients = newPatients;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getHospital() { return hospital; }

    public void setHospital(String hospital) { this.hospital = hospital; }
}
