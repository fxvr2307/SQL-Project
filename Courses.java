package org.example;

import java.util.Scanner;
import java.sql.*;

public class Courses
{
    Scanner scnr = new Scanner(System.in);
    int selector, CID, SID;
    int dataString, idCheck;
    String courseName, courseTitle, courseDescription;
    ResultSet data, dataTest, dataTest2;
    String courseGetter = "SELECT * FROM courses ";
    String getCourses = "SELECT * FROM courses WHERE CID = ";
    String getStudent = "SELECT * FROM students ";
    String updateCourse, deleteCourse;
    String course = null, lName = null;
    boolean isTrue;


    public Courses(Database db, Scanner scanner) throws SQLException
    {
        do{
            displayInitialMenu();
            while(!scanner.hasNextInt())
            {
                displayInitialMenu();
                scanner.next();
            }
            this.selector = scanner.nextInt();
            System.out.println();
        }while(this.selector < 1 || this.selector > 5);

        switch(this.selector)
        {
            //INSERT CASE
            case 1:
                //SETTING COURSE NAME
                this.courseName = askingCourseName(scanner);
                System.out.println(this.courseName.toUpperCase());

                //SETTING COURSE TITLE
                this.courseTitle = askingCourseTitle();
                System.out.println(this.courseTitle);

                //SETTING COURSE DESCRIPTION
                this.courseDescription = askingDescription(scanner);
                System.out.println(this.courseDescription);

                //SQL STRING TO INSERT A COURSE
                String courseInsert = "INSERT INTO courses (CID, COURSE, TITLE, DESCRIPTION) VALUES (NULL,'" +
                        this.courseName.toUpperCase() + "', \"" + this.courseTitle.trim() + "\", \"" +this.courseDescription.trim() + "\")";
                this.dataString = db.executeQuery(courseInsert);
                //SHOWING COURSE LIST
                displayCourses(db);
                System.out.println("");
                break;

            //UPDATE CASE
            case 2:

                char verifier;
                displayCourses(db);
                System.out.println("\n");
                this.CID = cidChecker(scanner, db);
                System.out.println();

                //UPDATING COURSE NAME
                System.out.print("Would you like to update course name?\nEnter 'Y' for yes and 'N' for no: ");
                verifier = askingYesOrNo(scanner);

                if(verifier == 'Y')
                {
                    this.courseName = askingCourseName(scanner);
                }
                else
                {
                    this.data = db.getData("SELECT * FROM courses WHERE CID = " + this.CID);
                    while(this.data.next())
                    {
                        this.courseName = this.data.getString("COURSE");
                    }
                }

                //UPDATING COURSE TITLE
                System.out.println();
                System.out.print("Would you like to update course title?\nEnter 'Y' for yes and 'N' for no: ");
                verifier = askingYesOrNo(scanner);


                if(verifier == 'Y')
                {
                    this.courseTitle = askingCourseTitle();
                }
                else
                {
                    this.data = db.getData("SELECT * FROM courses WHERE CID = " + this.CID);
                    while(this.data.next())
                    {
                        this.courseTitle = this.data.getString("TITLE");
                    }
                }

                //UPDATE COURSE DESCRIPTION
                System.out.println();
                System.out.print("Would you like to update course description?\nEnter 'Y' for yes and 'N' for no: ");
                verifier = askingYesOrNo(scanner);

                if(verifier == 'Y')
                {
                    this.courseDescription = askingDescription(scanner);
                }
                else
                {
                    this.data = db.getData("SELECT * FROM courses WHERE CID = " + this.CID);
                    while(this.data.next())
                    {
                        this.courseDescription = this.data.getString("DESCRIPTION");
                    }
                }

                //STRING TO UPDATE THE COURSE
                this.updateCourse = "UPDATE `courses` SET `COURSE` =\"" + this.courseName.toUpperCase().trim() + "\", `TITLE` = \""
                        + this.courseTitle.trim() + "\", `DESCRIPTION` = \"" + this.courseDescription.trim() +
                        "\"WHERE `courses`.`CID` = " + this.CID;

                System.out.println();
                System.out.println("COURSE UPDATED\n");
                dataString = db.executeQuery(this.updateCourse);
                displayCourses(db);
                System.out.println("\n");
                break;

            case 3:
                System.out.println("COURSE LIST");
                displayCourses(db);
                System.out.println();
                this.deleteCourse = "DELETE FROM courses WHERE `courses`.`CID` = " + cidChecker(scanner, db);
                this.dataString = db.executeQuery(this.deleteCourse);
                System.out.println("COURSE LIST UPDATED!");
                displayCourses(db);
                System.out.println();
                break;

            case 4:
                displayCourses(db);
                System.out.println("\n");
                String sql = "SELECT * FROM `registrations` WHERE CID = " + cidChecker(scanner, db);
                this.data = db.getData(sql);

                System.out.println("CLASS ROSTER");
                System.out.printf("\n %-10s %-1s %-20s %-1s %-10s %-1s %-20s",
                        "CID", "|", "COURSE","|", "SID", "|", "LAST NAME");
                System.out.print("\n-------------------------------------------------------------");


                while(data.next()){
                    this.CID =  data.getInt("CID");
                    this.SID= data.getInt("SID");
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

                    System.out.printf("\n %-10s %-1s %-20s %-1s %-10s %-1s %-20s",
                             this.CID, "|", this.course.trim(),"|", this.SID, "|", this.lName.trim());
                }
                System.out.println("\n");
                break;

            case 5:
                break;

        }
    }

    //MENU METHODS
    private void displayInitialMenu()
    {
        System.out.println("1. Insert new course");
        System.out.println("2. Update existing course");
        System.out.println("3. Delete existing course");
        System.out.println("4. Check course roster");
        System.out.println("5. Go back to previous menu\n");
        System.out.print("Select one of the options shown above: ");
    }

    //ASKING METHODS
    private String askingCourseName(Scanner scanner)
    {
        System.out.print("Please enter the course name in the form EET 101: ");
        this.courseName = this.scnr.nextLine();
        scanner.nextLine();
        return this.courseName;
    }

    private String askingCourseTitle()
    {
        System.out.print("Please enter the title of the course: ");
        this.courseTitle = this.scnr.nextLine();
        return this.courseTitle;
    }

    private String askingDescription(Scanner scanner)
    {
        System.out.print("Please enter course description: ");
        this.courseDescription = scanner.nextLine();
        return this.courseDescription;
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
    private void displayCourses(Database db) throws SQLException
    {
        this.data = db.getData(this.courseGetter);
        System.out.printf("\n%-20s %-1s %-20s %-1s %-20s\n",
                "COURSE ID", "|", "COURSE", "|", "TITLE");
        System.out.print("-----------------------------------------------------------------------------------------------------------------");

        while(this.data.next())
        {
            this.CID = this.data.getInt("CID");
            this.courseName = this.data.getString("COURSE");
            this.courseTitle = this.data.getString("TITLE");
            System.out.printf("\n%-20s %-1s %-20s %-1s %-20s",
                    this.CID, "|",  this.courseName.trim(), "|", this.courseTitle.trim());
        }
    }

    private int cidChecker(Scanner scanner, Database db) throws SQLException
    {
        do {
            System.out.print("Enter course's ID: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Input mismatch! Please try again: ");
                scanner.next();
            }

            this.CID = scanner.nextInt();
            this.data = db.getData("SELECT * FROM courses WHERE CID = '" + this.CID + "'");

            if(this.data.next())
            {
                this.isTrue = false;
            }
            else
            {
                System.out.println("Course with ID: " + this.CID + " does not exist.");
                System.out.println("Please try again!");
                this.isTrue = true;
            }

        } while (this.isTrue);
        return this.CID;
    }

}
