package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Addresses
{
    int selector, AID, SID, idCheck;
    Scanner scnr = new Scanner(System.in);
    String add1, add2, add3, city, country, state, zipCode, insertAddress, deleteAddress;
    ResultSet data;
    int dataString;
    boolean isTrue = true;

    public Addresses(Database db, Scanner scanner) throws SQLException
    {
        System.out.println("""
                *DISCLAIMER*
                If you wish to update any existing address, please refer to student section.
                Address tab is solely dedicated to insert new address or delete existing ones.
                """);

        do{
            displayMenu();
            while(!scanner.hasNextInt())
            {
                displayMenu();
                scanner.next();
            }
            this.selector = scanner.nextInt();
            System.out.println();
        }while(this.selector < 1 || this.selector > 3);

        switch (selector)
        {
            //INSERT CASE
            case 1:
                char addAddress;

                //INSERT ADDY1
                System.out.print("Would you like to insert address 1? (St)\nEnter 'Y' for yes and 'N' for no: ");
                addAddress = askingYesOrNo(scanner);

                if(addAddress == 'Y')
                {
                    System.out.print("Enter address 1: ");
                    this.add1 = this.scnr.nextLine();
                    scanner.nextLine();
                }
                else
                {
                    this.add1 = "";
                }

                //INSERT ADDY2
                System.out.print("Would you like to update address 2? (Building)\nEnter 'Y' for yes and 'N' for no: ");
                addAddress = askingYesOrNo(scanner);

                if(addAddress == 'Y')
                {
                    System.out.print("Enter address 2: ");
                    add2 = this.scnr.nextLine();
                    scanner.nextLine();
                }
                else
                {
                    this.add2 = "";
                }

                //INSERT ADDY3
                System.out.print("Would you like to update address 3? (APT)\nEnter 'Y' for yes and 'N' for no: ");
                addAddress = askingYesOrNo(scanner);

                if(addAddress == 'Y'){
                    System.out.print("Enter address 3: ");
                    add3 = this.scnr.nextLine();
                    scanner.nextLine();
                }
                else
                {
                    this.add3 = "";
                }


                //INSERT CITY
                System.out.print("Enter your city: ");
                this.city = scnr.nextLine();

                //INSERT STATE
                do{
                    System.out.print("Enter your state: ");
                    this.state = scanner.next();
                    if(this.state.length() > 2)
                    {System.out.println("State must not be longer than 2 characters!");}
                }while (this.state.length() > 2);


                //INSERT COUNTRY
                do{
                    System.out.print("Enter your country: ");
                    this.country = this.scnr.next();
                    if(this.country.length() > 5)
                    {System.out.println("Country must not exceed 5 characters!");}
                }while(this.country.length() > 5);


                //INSERT ZIPCODE
                System.out.print("Enter your zipcode: ");
                this.zipCode = scanner.next();



                this.insertAddress = "INSERT INTO `addresses` (`AID`,`ADDY1`,`ADDY2`,`ADDY3`,`CITY`,`STATE`,`ZIP`" +
                        ",`COUNTRY`,`SID`) VALUES (NULL, '" + this.add1 + "', '" + this.add2 + "','" +
                        this.add3 + "', '" + this.city + "','" + this.state + "', '" + this.zipCode + "', '"+
                        this.country + "','" + idChecker(db, scanner) + "')";
                this.dataString = db.executeQuery(this.insertAddress);
                System.out.println("ADDRESS SUCCESSFULLY INSERTED!");
                this.data = db.getData("SELECT * FROM addresses");
                displayAddresses(this.data);
                System.out.println("\n");

                break;

            //DELETE CASE
            case 2:

                this.data = db.getData("SELECT * FROM addresses");
                displayAddresses(this.data);
                System.out.println("\n");
                this.deleteAddress = "DELETE FROM addresses WHERE AID = " + cidChecker(db,scanner);
                this.dataString = db.executeQuery(this.deleteAddress);
                System.out.println("ADDRESS SUCCESSFULLY DELETED");
                this.data = db.getData("SELECT * FROM addresses");
                displayAddresses(this.data);
                System.out.println("\n");


                break;

            //GO BACK TO MAIN PAGE
            case 3:
                break;


        }

    }

    //MENU METHOD
    public void displayMenu()
    {

        System.out.println("1. Insert new address");
        System.out.println("2. Delete existing address");
        System.out.println("3. Go back to previous menu\n");
        System.out.print("Choose one of the options above: ");
    }

    //ASKING METHOD
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

    //VERIFIERS METHODS
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
            this.data = db.getData("SELECT * FROM students");

            while(this.data.next())
            {
                this.idCheck = data.getInt("SID");
                if (this.SID == this.idCheck) {this.isTrue = false;}
            }
            if (this.isTrue) {System.out.println("No record of student with ID: " + this.SID + " was found. \nPlease try again!");}

        } while (this.isTrue);

        return this.SID;
    }

    private int cidChecker(Database db, Scanner scanner) throws SQLException
    {

        do {

            System.out.print("Enter address's ID you wish to delete: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }
            this.AID = scanner.nextInt();
            this.data = db.getData("SELECT * FROM addresses");

            while (this.data.next()) {
                this.idCheck = data.getInt("AID");
                if (this.AID == this.idCheck) {
                    this.isTrue = false;
                }
            }
            if(this.isTrue){
                System.out.print("No record of address with ID: " + this.AID + " was found.\nPlease try again!");
            }
        } while (this.isTrue);

        return this.AID;
    }

    //DISPLAY METHOD
    private void displayAddresses(ResultSet data) throws SQLException
    {
        String address;
        System.out.println("ADDRESSES LIST");
        System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s\n",
                "Address ID","|", "SID","|", "Address 1", "|", "City", "|", "State", "|", "ZipCode", "|", "Country");
        System.out.print("-------------------------------------------------------------------------------------------------------------------------------");

        while(this.data.next())
        {
            this.AID = data.getInt("AID");
            address = data.getString("ADDY1");
            this.city = data.getString("CITY");
            this.state = data.getString("STATE");
            this.zipCode = data.getString("ZIP");
            this.country = data.getString("COUNTRY");
            this.SID = data.getInt("SID");
            System.out.printf("\n%-10s %-1s %-10s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s %-1s %-20s",
                    this.AID, "|", this.SID, "|", address, "|", this.city, "|", this.state, "|", this.zipCode,"|",this.country);
        }
    }
}
