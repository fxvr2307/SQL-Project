package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws SQLException
    {
        boolean isRunning = false;
        int var;
        Scanner scanner = new Scanner(System.in);

        String url = "jdbc:mariadb://3.89.197.22/university";
        String user = "";
        String pass = "";
        Database db = new Database(url, user, pass);

        System.out.println("Welcome to my University SQLDataBase\n");

        while(!isRunning)
        {
            do{
                displayMenu();
                System.out.print("Select one of the options shown above: ");
                while(!scanner.hasNextInt())
                {
                    displayMenu();
                    System.out.println("Input mismatch. Please try again:");
                    scanner.next();
                }
                var = scanner.nextInt();
                System.out.println();
            }while (var < 1 || var > 4);


            if (var == 1)
            {new Student(db, scanner);}

            else if (var == 2)
            {new Courses(db, scanner);}

            else if (var == 3)
            {new Addresses(db, scanner);}

            else {isRunning = true;}
        }
    }

    public static void displayMenu()
    {
        System.out.println("1. Students");
        System.out.println("2. Courses");
        System.out.println("3. Addresses");
        System.out.println("4. Exit program\n");
    }
}
