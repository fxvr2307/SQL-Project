package org.example;

import java.util.Scanner;
import java.sql.*;

public class Student
{
    Scanner scnr = new Scanner(System.in);
    ResultSet data, dataTest, dataTest2;
    boolean isTrue = true, isFalse = true, isRunning = true;
    int SID = 0, age, selector, idCheck,checkHelper, dataString = 0, option, RID, CID, AID;
    double gpa;
    String course = null, lName = null, firstN = null, lastN = null, email, title, address, city, state, zipCode, country, updateAdd;
    String getStudent = "SELECT * FROM students ";
    String addressGetter = "SELECT * FROM `addresses`";
    String addressColumns = "SELECT * FROM `addresses`WHERE `addresses`.`AID` = ";
    String getRegistration = "SELECT * FROM registrations";
    String getCourses = "SELECT * FROM courses WHERE CID = ";
    double gpa_rounded;

    public Student(Database db, Scanner scanner) throws SQLException
    {
        do{
            //DISPLAYING MENU SELECTION FOR STUDENTS
            printMenuSelection();

            while(!scanner.hasNextInt()){
                //INPUT VALIDATION FOR MENU
                printMenuSelection();
                scanner.next();
            }
            this.option = scanner.nextInt();
            System.out.println();
        }while(this.option < 1 || this.option > 7);

        //INPUT SELECTION OF THE INPUT FROM THE USER
        switch (this.option) {
            //INSERT CASE
            case 1:
                System.out.println("*DISCLAIMER*:");
                System.out.println("""
                        ID number that you decide to assign a student will be unique.You will not be able to change it in the future.
                        However, if all of the student's records are deleted, the ID may become available once again
                        """);

                //ASKING NECESSARY INPUTS
                this.SID = idPre_Existence(db, scanner);
                this.firstN = askingFirstName(scanner);
                this.lastN = askingLastName(scanner);
                this.email = askingEmail(scanner);
                this.age= askingAge(scanner);
                this.gpa = askingGPA(scanner);
                this.gpa_rounded = (double) Math.round(this.gpa * 100) / 100;
                String insertStudent = "INSERT INTO students (`SID`, `FIRSTNAME`, `LASTNAME`, `EMAIL`, `AGE`, `GPA`) VALUES " +
                        "('" + this.SID + "','" + this.firstN + "','" + this.lastN + "','" + this.email + "','" +
                        this.age + "','" + this.gpa_rounded + "')";
                this.dataString = db.executeQuery(insertStudent);
                System.out.println();
                displayRoster(db);
                System.out.println("\nSTUDENT SUCCESSFULLY INSERTED!\n");
                break;

            //UPDATE CASE
            case 2:

                do {
                    //UPDATE MENU
                    printMenuUpdate();

                    //INPUT VALIDATION
                    while (!scanner.hasNextInt()) {
                        printMenuUpdate();
                        scanner.next();
                    }
                    this.selector = scanner.nextInt();
                } while (this.selector < 1 || this.selector > 6);


                //DISPLAYING STUDENT ROSTER IF THE SELECTION IS ANY OTHER THAN UPDATING ADDRESS
                if(this.selector != 6)
                {
                    displayRoster(db);
                    System.out.println();
                    this.SID = idChecker(db, scanner);
                }
                else{

                    //DISPLAYING ROSTER OF STUDENTS THAT HAVE ADDRESS ALREADY ASSIGNED
                    this.data = db.getData(this.addressGetter);

                    System.out.println("*DISCLAIMER*:");
                    System.out.println("""
                            It is understood that a student can have multiple addresses.
                            In consequence, a unique address ID has been assigned to each address.\s
                            A student with multiple addresses may only update one at a time. Please use the AID to make any changes.""");

                    System.out.println("Addresses that can be updated:");
                    System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s\n",
                            "Address ID","|", "SID","|", "Address 1", "|", "City", "|", "State", "|", "ZipCode", "|", "Country");
                    System.out.print("-------------------------------------------------------------------------------------------------------------------------------");
                    displayAddresses(this.data);
                    System.out.println("\n");
                }

                switch (this.selector) {
                    //UPDATE FIRST NAME
                    case 1:

                        this.firstN = askingFirstName(scanner);
                        String fNameUpdated = "UPDATE  `students` SET `FIRSTNAME` = '" + this.firstN + " ' WHERE " +
                                "`students`.`SID` = " + this.SID;
                        System.out.println();
                        System.out.println("STUDENT FIRST NAME UPDATED\n");
                        this.dataString = db.executeQuery(fNameUpdated);
                        displayRoster(db);
                        System.out.println();

                        break;

                    //UPDATE LAST NAME
                    case 2:
                        this.lastN = askingLastName(scanner);
                        String lNameUpdated = "UPDATE  `students` SET `LASTNAME` = '" + this.lastN + " ' WHERE " +
                                "`students`.`SID` = " + this.SID;
                        System.out.println();
                        System.out.println("STUDENT LAST NAME UPDATED\n");
                        this.dataString = db.executeQuery(lNameUpdated);
                        displayRoster(db);
                        System.out.println();
                        break;

                    //UPDATE AGE
                    case 3:
                        this.age = askingAge(scanner);
                        String ageUpdated ="UPDATE  `students` SET `AGE` = '" + this.age + " ' WHERE " +
                                "`students`.`SID` = " + this.SID;
                        System.out.println();
                        System.out.println("STUDENT AGE UPDATED\n");
                        this.dataString = db.executeQuery(ageUpdated);
                        displayRoster(db);
                        System.out.println();
                        break;

                    //UPDATE EMAIL
                    case 4:
                        this.email = askingEmail(scanner);
                        String emailUpdated = "UPDATE  `students` SET `EMAIL` = '" + this.email + " ' WHERE " +
                                "`students`.`SID` = " + this.SID;
                        System.out.println();
                        System.out.println("STUDENT EMAIL UPDATED\n");
                        this.dataString = db.executeQuery(emailUpdated);
                        displayRoster(db);
                        System.out.println();
                        break;

                    //UPDATE GPA
                    case 5:
                        this.gpa = askingGPA(scanner);
                        this.gpa_rounded = (double) Math.round(this.gpa * 100) / 100;
                        String GPAUpdated ="UPDATE  `students` SET `GPA` = '" + this.gpa_rounded + " ' WHERE " +
                                "`students`.`SID` = " + this.SID;
                        System.out.println();
                        System.out.println("STUDENT GPA UPDATED\n");
                        this.dataString = db.executeQuery(GPAUpdated);
                        break;

                    //UPDATE ADDRESS
                    case 6:
                        String add1 = "", add2 = "", add3 = "";
                        char addAddress;

                        this.SID = idChecker(db, scanner);
                        String addressRetrieve = "SELECT * FROM `addresses` WHERE SID = " + this.SID;
                        this.data = db.getData(addressRetrieve);

                        System.out.println("Addresses related to SID "+ this.SID +": ");
                        System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s\n",
                                "Address ID","|", "SID","|", "Address 1", "|", "City", "|", "State", "|", "ZipCode", "|", "Country");
                        System.out.print("-------------------------------------------------------------------------------------------------------------------------------");
                        displayAddresses(this.data);
                        System.out.println("\n");
                        String addressGetterSID = "SELECT * FROM `addresses` WHERE SID = " + this.SID;

                        this.CID = cidChecker(db,scanner, addressGetterSID);

                        //UPDATE ADDY1
                        System.out.print("Would you like to update address 1?\nEnter 'Y' for yes and 'N' for no: ");
                        addAddress = askingYesOrNo(scanner);

                        if(addAddress == 'Y')
                        {
                            System.out.print("Enter address 1: ");
                            add1 = this.scnr.nextLine();
                            scanner.nextLine();
                        }
                        else
                        {
                            this.data = db.getData(this.addressColumns + this.AID);
                            while(this.data.next())
                            {
                                add1 = this.data.getString("ADDY1");
                            }
                        }

                        //UPDATE ADDY2
                        System.out.print("Would you like to update address 2?\nEnter 'Y' for yes and 'N' for no: ");
                        addAddress = askingYesOrNo(scanner);

                        if(addAddress == 'Y'){
                            System.out.print("Enter address 2: ");
                            add2 = this.scnr.nextLine();
                            scanner.nextLine();

                        }
                        else{
                            this.data = db.getData(this.addressColumns + this.AID);
                            while(this.data.next()){
                                add2 = this.data.getString("ADDY2");
                            }
                        }

                        //UPDATE ADDY3
                        System.out.print("Would you like to update address 3?\nEnter 'Y' for yes and 'N' for no: ");
                        addAddress = askingYesOrNo(scanner);

                        if(addAddress == 'Y'){
                            System.out.print("Enter address 3: ");
                            add3 = this.scnr.nextLine();
                            scanner.nextLine();
                        }
                        else{
                            this.data = db.getData(this.addressColumns + this.AID);
                            while(this.data.next()){
                                add3 = this.data.getString("ADDY3");
                            }
                        }

                        //UPDATE CITY
                        System.out.print("Enter your city: ");
                        this.city = scnr.nextLine();

                        //UPDATE STATE
                        do{
                            System.out.print("Enter your state: ");
                            this.state = scanner.next();
                            if(this.state.length() > 2)
                            {System.out.println("State must not be longer than 2 characters!");}
                        }while (this.state.length() > 2);


                        //UPDATE COUNTRY
                        do{
                            System.out.print("Enter your country: ");
                            this.country = this.scnr.next();
                            if(this.country.length() > 5)
                            {System.out.println("Country must not exceed 5 characters!");}
                        }while(this.country.length() > 5);


                        //UPDATE ZIPCODE
                        System.out.print("Enter your zipcode: ");
                        this.zipCode = scanner.next();

                        this.updateAdd = "UPDATE `addresses` SET `ADDY1` =\"" + add1 + "\", `ADDY2` = \"" + add2 + "\", `ADDY3` = \"" + add3 + "\",`CITY` = \"" +
                                this.city + "\",`STATE` = \"" + this.state + "\",`ZIP`= '" + this.zipCode +
                                "',`COUNTRY` = '" + this.country + "' WHERE `addresses`.`AID` =" + this.AID;
                        System.out.println();
                        System.out.println("STUDENT ADDRESS UPDATED\n");
                        dataString = db.executeQuery(updateAdd);
                        this.data = db.getData("SELECT * FROM addresses ");
                        System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s\n",
                                "Address ID","|", "SID","|", "Address 1", "|", "City", "|", "State", "|", "ZipCode", "|", "Country");
                        System.out.print("-------------------------------------------------------------------------------------------------------------------------------");
                        displayAddresses(this.data);
                        System.out.println("\n");
                        break;
                }
                break;

            //DELETE CASE
            case 3:
                //DELETING OPTION FOR STUDENTS WILL DELETE ANY RECORD RELATED TO SID RECEIVED
                char deleteOption;
                displayRoster(db);

                System.out.println("""
                    *DISCLAIMER*
                    Deleting a student information will delete all their information including addresses records,\s
                    personal information and any course registration! Be advised to proceed with caution
                    Would you like to continue?
                    Enter 'Y' for yes and 'N' for no:\s""");

                deleteOption = askingYesOrNo(scanner);
                if(deleteOption == 'N') {break;}
                else
                {
                    this.SID = idChecker(db, scanner);
                    String deleteStudent = "DELETE FROM students WHERE `students`.`SID` = " + this.SID;
                    System.out.println();
                    System.out.println("STUDENT SUCCESSFULLY DELETED");
                    this.dataString = db.executeQuery(deleteStudent);
                    this.data = db.getData(this.getStudent);


                    System.out.println("\nUpdated student's roster: ");
                    displayRoster(db);
                    System.out.println();
                }
                break;

            //REGISTERING STUDENTS TO A CLASS
            case 4:

                displayRegistrationsList(db);
                displayRoster(db);
                System.out.println();
                this.SID = idChecker(db, scanner);

                this.data = db.getData("SELECT * FROM courses");
                displayCourses(this.data);
                this.CID = cidCheckerWithCourseRepetition(db, scanner);

                String insertRegistration = "INSERT INTO `registrations` (`RID`, `CID`, `SID`, `Date`)" +
                        " VALUES (NULL,'"  + this.CID + "', '" + this.SID + "', current_timestamp())";

                this.dataString = db.executeQuery(insertRegistration);
                System.out.println("STUDENT SUCCESSFULLY REGISTER TO " + this.course);
                System.out.println();
                break;

            //DROPPING A STUDENT FROM A CLASS
            case 5:

                //DISPLAYING STUDENTS THAT ARE ALREADY REGISTERED TO A CLASS
                displayRegistrationsList(db);
                this.SID = idChecker(db, scanner);

                this.data = db.getData("SELECT * FROM registrations where SID = " + this.SID);
                this.dataTest = db.getData("SELECT * FROM students where SID = " + this.SID);

                while(this.dataTest.next())
                {
                    this.firstN = this.dataTest.getString("FIRSTNAME");
                    this.lastN = this.dataTest.getString("LASTNAME");
                }

                System.out.println();
                System.out.println("STUDENT " + this.firstN + " " + this.lastN + " IS REGISTERED TO THE FOLLOWING CLASSES:");

                System.out.printf("\n%-30s %-1s %-10s %-1s %-20s %-1s %-10s \n",
                        "Registration Number", "|", "CID", "|", "COURSE","|", "SID");
                System.out.print("------------------------------------------------------------------------------------------");

                while(this.data.next())
                {
                    this.SID = this.data.getInt("SID");
                    this.CID = this.data.getInt("CID");
                    this.RID = this.data.getInt("RID");
                    this.dataTest2 = db.getData(this.getCourses + this.CID);
                    while (this.dataTest2.next())
                    {
                        this.course = this.dataTest2.getString("COURSE");

                    }
                    System.out.printf("\n%-30s %-1s %-10s %-1s %-20s %-1s %-10s",
                            this.RID, "|", this.CID, "|", this.course,"|", this.SID);
                }
                System.out.println("\n");

                do {
                    System.out.print("Enter course's ID you wish to drop: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Input mismatch! Please try again: ");
                        scanner.next();
                    }
                    this.CID = scanner.nextInt();
                    this.data = db.getData("SELECT CID FROM `registrations` WHERE SID = " + this.SID);

                    while (this.data.next()) {
                        this.idCheck = this.data.getInt("CID");
                        if(this.CID == this.idCheck)
                        {
                            this.dataTest = db.getData("SELECT COURSE FROM courses WHERE CID = " + this.CID);
                            while (this.dataTest.next()){
                                this.course = this.dataTest.getString("COURSE");
                            }
                            this.isRunning = false;
                        }
                    }
                    if(this.isRunning)
                    {
                        System.out.println("Student is not registered to this class");
                    }

                }while(this.isRunning);

                this.dataString = db.executeQuery("DELETE FROM registrations WHERE SID = " + this.SID +
                        " AND CID = " + this.CID);
                System.out.println();
                System.out.println(this.firstN + " " + this.lastN + " HAS DROPPED " + this.course);
                System.out.println();
                break;

            //PRINT STUDENT'S SCHEDULE
            case 6:
                displayRoster(db);
                System.out.println();
                this.SID = idChecker(db,scanner);
                System.out.println("\nSTUDENT SCHEDULE");
                displayStudentSchedule(db, this.SID);
                break;
            //GO BACK TO PREVIOUS MENU
            case 7:
                break;

        }

    }

    //ID VERIFIER METHODS
    private int idPre_Existence(Database db, Scanner scanner) throws SQLException
    {
        do {
            System.out.print("Enter student's ID you wish to insert: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }

            this.SID = scanner.nextInt();
            this.data = db.getData("SELECT * FROM students WHERE SID = '" + this.SID + "'");

            if(this.data.next()){System.out.println("ID already exists.\nPlease try again!");}
            else{this.isTrue = false;}

        } while (this.isTrue);
        return this.SID;
    }

    private int idChecker(Database db, Scanner scanner) throws SQLException
    {
        do {
            //CHECKING WHETHER ID EXISTS OR NOT
            System.out.print("Enter student's ID: ");
            while (!scanner.hasNextInt())
            {
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }
            this.SID = scanner.nextInt();
            this.data = db.getData(this.getStudent);

            while(this.data.next())
            {
                this.idCheck = data.getInt("SID");
                if (this.SID == this.idCheck) {this.isTrue = false;}
            }
            if (this.isTrue) {System.out.println("No record of student with ID: " + this.SID + " was found. \nPlease try again!");}

        } while (this.isTrue);

        return this.SID;
    }

    private int cidCheckerWithCourseRepetition(Database db, Scanner scanner) throws  SQLException
    {
        do{
            System.out.print("Enter course's ID: ");
            while(!scanner.hasNextInt()){
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }
            this.CID = scanner.nextInt();
            this.data = db.getData("SELECT * FROM courses");

            while (this.data.next())
            {
                this.idCheck = this.data.getInt("CID");
                if (this.CID == this.idCheck)
                {
                    this.course = this.data.getString("COURSE");
                    this.isRunning = false;
                }

            }
            if(!this.isRunning)
            {
                this.dataTest = db.getData("SELECT * FROM registrations WHERE CID = " + this.CID);
                while(this.dataTest.next())
                {
                    this.checkHelper = this.dataTest.getInt("SID");

                    if (this.checkHelper == this.SID) {

                        this.isRunning = true;
                        System.out.println("Student already registered for that course");
                    }
                }

            }
            else {System.out.println("Course does not exist");}
        }while(this.isRunning);

        return this.CID;

    }

    private int cidChecker(Database db, Scanner scanner, String addressGetterSID) throws SQLException
    {
        do {

            System.out.print("Enter address's ID you wish to update: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }
            this.AID = scanner.nextInt();
            this.data = db.getData(addressGetterSID);

            while (this.data.next()) {
                this.idCheck = data.getInt("AID");
                if (this.AID == this.idCheck) {
                    this.isFalse = false;
                }
            }
            if(this.isFalse){
                System.out.print("No record of address with ID: " + this.AID + " was found.\nPlease try again!");
            }
        } while (this.isFalse);

        return this.AID;
    }

    //MENU METHODS
    private void printMenuSelection()
    {
        System.out.println("1. Insert");
        System.out.println("2. Update");
        System.out.println("3. Delete");
        System.out.println("4. Register for a class");
        System.out.println("5. Drop a class");
        System.out.println("6. Show student's schedule");
        System.out.println("7. Go back to previous page\n");
        System.out.print("Select one of the options shown above: ");
    }

    private void printMenuUpdate()
    {
        System.out.println("1. Update First Name");
        System.out.println("2. Update Last Name");
        System.out.println("3. Update Age");
        System.out.println("4. Update Email");
        System.out.println("5. Update GPA");
        System.out.println("6. Update Address\n");
        System.out.print("What would you like to update?.\nChoose one of the options above:");
    }

    //ASKING METHODS
    private String askingFirstName(Scanner scanner)
    {
        System.out.print("Enter first name of the student: ");
        this.firstN = scanner.next();
        return this.firstN;
    }

    private String askingLastName(Scanner scanner)
    {
        System.out.print("Enter last name of  the student: ");
        this.lastN = scanner.next();
        return this.lastN;
    }

    private String askingEmail(Scanner scanner)
    {
        System.out.print("Enter student's email: ");
        this.email = scanner.next();
        return this.email;
    }

    private int askingAge(Scanner scanner)
    {
        do {
            //AGE CANNOT BE A NEGATIVE VALUE OR ZERO
            System.out.print("Enter student's age: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Input mismatch. Please try again!: ");
                scanner.next();
            }
            this.age = scanner.nextInt();
        }while(this.age <= 0);
        return this.age;
    }

    private double askingGPA(Scanner scanner)
    {
        do {
            //SETTING GPA LIMITATIONS FROM 0 TO 4.0
            System.out.print("Enter student's gpa: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Input mismatch. Please try again!: ");
                scanner.next();
            }
            this.gpa = scanner.nextDouble();
        }while(this.gpa < 0 || this.gpa > 4);

        return this.gpa;
    }

    private char askingYesOrNo(Scanner scanner)
    {
        char validation;
        while(!scanner.hasNext("[YN]"))
        {
            System.out.print("Invalid option!\nPlease try again: ");
            scanner.next();
        }
        validation = scanner.next().charAt(0);
        return  validation;
    }

    //DISPLAYING METHODS
    private void displayAddresses(ResultSet data) throws SQLException
    {
        while(this.data.next())
        {
            this.AID = data.getInt("AID");
            this.address = data.getString("ADDY1");
            this.city = data.getString("CITY");
            this.state = data.getString("STATE");
            this.zipCode = data.getString("ZIP");
            this.country = data.getString("COUNTRY");
            this.SID = data.getInt("SID");
            System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s",
                    this.AID, "|", this.SID, "|", this.address.trim(), "|", this.city.trim(), "|", this.state.trim()
                    , "|", this.zipCode,"|",this.country.trim());
        }
    }

    private void displayCourses(ResultSet data) throws SQLException
    {
        //DISPLAYING COURSES AVAILABLE TO REGISTER
        System.out.printf("\n%-10s %-1s %-20s %-1s %-30s\n", "CID","|", "COURSES", "|", "TITLE");
        System.out.print("-----------------------------------------------------------------------------------------------------------------");

        while (data.next())
        {
            this.course = data.getString("COURSE");
            this.CID = data.getInt("CID");
            this.title = data.getString("TITLE");
            System.out.printf("\n%-10s %-1s %-20s %-1s %-30s", this.CID, "|", this.course.trim(), "|", this.title.trim());
        }
        System.out.println("\n");
    }

    private void displayRegistrationsList(Database db) throws SQLException
    {
        this.data = db.getData(this.getRegistration);

        //DISPLAYING STUDENTS THAT ARE ALREADY REGISTERED TO A CLASS
        System.out.println("STUDENTS ALREADY REGISTERED IN A CLASS!");
        System.out.printf("\n%-30s %-1s %-10s %-1s %-20s %-1s %-10s %-1s %-20s\n",
                "Registration Number", "|", "CID", "|", "COURSE","|", "SID", "|", "LAST NAME");
        System.out.print("--------------------------------------------------------------------------------------------------------");
        displayStudentRegistration(db);
        System.out.println("\n");


    }

    private void displayStudentSchedule(Database db, int sid) throws SQLException
    {
        this.data = db.getData("SELECT * FROM registrations WHERE SID = " + sid);
        System.out.printf("\n%-30s %-1s %-10s %-1s %-20s %-1s %-10s %-1s %-20s\n",
                "Registration Number", "|", "CID", "|", "COURSE","|", "SID", "|", "LAST NAME");
        System.out.print("--------------------------------------------------------------------------------------------------------");
        displayStudentRegistration(db);
        System.out.println("\n");
    }

    private void displayRoster(Database db) throws SQLException
    {
        System.out.println("Student's roster: ");
        this.data = db.getData(this.getStudent);

        System.out.printf("\n%-20s %-1s %-20s %-1s %-20s %-1s %-10s %-1s %-30s\n",
                "Student ID", "|", "FIRST NAME", "|", "LAST NAME", "|", "AGE", "|", "EMAIL");
        System.out.print("-----------------------------------------------------------------------------------------------------------------");


        while (this.data.next()) {
            this.SID = this.data.getInt("SID");
            this.firstN = this.data.getString("FIRSTNAME");
            this.lastN = this.data.getString("LASTNAME");
            this.email = this.data.getString("EMAIL");
            this.age = this.data.getInt("AGE");
            System.out.printf("\n%-20s %-1s %-20s %-1s %-20s %-1s %-10s %-1s %-30s",
                    this.SID, "|", this.firstN.trim(), "|", this.lastN.trim(),"|", this.age, "|", this.email.trim());
        }
        System.out.println();
    }

    private void displayStudentRegistration(Database db) throws SQLException
    {

        while (this.data.next())
        {
            this.SID = this.data.getInt("SID");
            this.CID = this.data.getInt("CID");
            this.RID = this.data.getInt("RID");
            this.dataTest = db.getData(this.getCourses + this.CID);
            this.dataTest2 = db.getData( this.getStudent +  "WHERE SID = " + this.SID);
            while (this.dataTest.next())
            {
                this.course = this.dataTest.getString("COURSE");

            }
            while (this.dataTest2.next())
            {
                this.lName = this.dataTest2.getString("LASTNAME");
            }

            System.out.printf("\n%-30s %-1s %-10s %-1s %-20s %-1s %-10s %-1s %-20s",
                    this.RID, "|", this.CID, "|", this.course.trim(),"|", this.SID, "|", this.lName.trim());
        }

    }

}
