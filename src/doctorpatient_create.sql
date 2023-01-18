
-- -----------------------------------------------------
-- Schema doctorpatient
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS doctorpatient;

-- -----------------------------------------------------
-- Schema doctorpatient
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS doctorpatient DEFAULT CHARACTER SET utf8 ;
USE doctorpatient;

-- -----------------------------------------------------
-- Table `patient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS patient ;

CREATE TABLE IF NOT EXISTS patient (
  patient_id INT PRIMARY KEY AUTO_INCREMENT,
  lastname VARCHAR(45)  NOT NULL,
  firstname VARCHAR(45) NOT NULL,
  sex CHAR(1) NOT NULL,
  dob DATE NOT NULL,

  UNIQUE KEY demographics (lastname, firstname, sex, dob)
);

-- -----------------------------------------------------
-- Table `specialty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS specialty ;

CREATE TABLE IF NOT EXISTS specialty (
  specialty_id INT PRIMARY KEY AUTO_INCREMENT,
  specialty VARCHAR(45) UNIQUE NOT NULL
);


-- -----------------------------------------------------
-- Table `hospital`
-- -----------------------------------------------------
DROP TABLE IF EXISTS hospital ;

CREATE TABLE IF NOT EXISTS hospital (
  hospital_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) UNIQUE NOT NULL
);


-- -----------------------------------------------------
-- Table `doctor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS doctor ;

CREATE TABLE IF NOT EXISTS doctor (
  doctor_id INT PRIMARY KEY AUTO_INCREMENT,
  lastname VARCHAR(45) NOT NULL,
  firstname VARCHAR(45) NOT NULL,
  new_patients TINYINT NOT NULL,
  specialty_id INT NOT NULL,
  hospital_id INT NOT NULL,

  CONSTRAINT fk_doctor_specialty FOREIGN KEY (specialty_id) REFERENCES specialty (specialty_id),
  CONSTRAINT fk_doctor_hospital FOREIGN KEY (hospital_id) REFERENCES hospital (hospital_id)
);

-- -----------------------------------------------------
-- Table `appointment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS appointment ;

CREATE TABLE IF NOT EXISTS appointment (
  patient_id INT NOT NULL AUTO_INCREMENT,
  doctor_id INT NOT NULL,
  appointment DATETIME NOT NULL,
  
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patient (patient_id),
  CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);


DROP TABLE IF EXISTS rating ;

CREATE TABLE IF NOT EXISTS rating (
  patient_id INT NOT NULL AUTO_INCREMENT,
  doctor_id INT NOT NULL,
  rating DOUBLE NOT NULL,
  
  CONSTRAINT fk_rating_patient FOREIGN KEY (patient_id) REFERENCES patient (patient_id),
  CONSTRAINT fk_rating_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

-- INSERT STATEMENTS TO POPULATE DATABASE WITH SOME DATA
INSERT INTO patient  (lastname, firstname, sex, dob) VALUES
('Doe', 'John', 'M', '2003-10-08'),
('Smith', 'Abby', 'F', '2006-04-22'),
('Johnson', 'Bob', 'M', '1950-10-08'),
('Adams', 'Charlie', 'M', '1999-10-08'),
('Dunkins', 'Michelle', 'F', '1966-10-08'),
('ODead', 'Dawn', 'F', '1966-10-08'),
('Bruins', 'Carla', 'F', '1960-10-08'),
('Kirk', 'John', 'M', '1961-10-08'),
('Fredericks', 'Mitch', 'M', '1955-10-08')
;

INSERT INTO hospital (name) VALUES ('MGH'), ('Newton-Wellesley'), ('Columbia Medical Center');

INSERT INTO specialty (specialty) VALUES ('ONCOLOGY'), ('OBGYN'), ('PEDIATRICS'), ('GP'), ('CARDIOLOGY');

INSERT INTO doctor (lastname,firstname, new_patients, specialty_id, hospital_id) VALUES
('Crusher', 'Bev', true, 1, 1),
('Spock', 'Joe', true, 2, 1),
('Smith', 'John', false, 3, 1),
('McCoy', 'Leonard', true, 4, 1),
('Oz', 'Mehmet', true, 1, 2),
('Who', 'Doctor', true, 2, 2),
('James', 'Jim', true, 3, 2),
('Hines', 'Ann', false, 4, 2),
('Parker', 'Peter', true, 1, 3),
('White', 'Betty', true, 2, 3),
('Adams', 'Bryan', true, 3, 3)
;


insert into rating values
(1,1,2.0),(1,4, 7.0),(2,3, 8.5), (2, 10, 5.0),(4,4, 10.0), (4,5, 10.0),
(5,1,8.0),(5,3, 9.0),(5,6, 9.0), (5, 11, 8.0),(6,1, 3.0), (6,2, 9.0),
(6,3,8.0),(6,4, 7.0),(6,5, 8.5), (7, 2, 6.0),(7,4, 8.0), (7,6, 10.0),
(7,8,4.0),(7,9, 8.0),(7,10, 9.5), (8, 2, 5.0),(8,3, 8.0), (8,4, 10.0),
(8,5,8.0),(8,6, 7.0),(8,7, 8.5), (9, 9, 6.9),(9,10, 6.0), (9,11, 10.0);

