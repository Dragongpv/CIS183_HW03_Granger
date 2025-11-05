package com.example.cis183_hw03_granger;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String database_name = "Students.db";
    private static final String students_table_name = "Students";
    private static final String majors_table_name = "Majors";
    static StudentInfo clickedStudent;
    public DatabaseHelper(Context c)
    {
        //we will use this to create the database
        //it accepts: the context, the name of the database, factory (leave null), and version number
        //if the database becomes corrupt or the information in the database is wrong change the version number
        //super is used to call the functionality of the base class SQLiteOpenHelper and then executes the extended (DatabaseHelper)
        super(c, database_name,null,1);
    }
    //this is called when a new database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //this is where we will create the tables in our database
        //create table in the database
        //execute the sql statement on the database that was passed to the function called db
        //this can be tricky because we have to write sql statements as strings
        db.execSQL("CREATE TABLE " + students_table_name + " (studentId integer primary key autoincrement not null, fname varchar(50), lname varchar(50), email varchar(50), age integer, gpa double, major varchar(50));");
        db.execSQL("CREATE TABLE " + majors_table_name + " (majorId integer primary key autoincrement not null, studentId integer, majorName varchar(50), majorPrefix varchar(50), foreign key (studentId) references " + students_table_name + " (studentId));");
    }

    //this is called when a new database version is created
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + students_table_name + ";");
        db.execSQL("DROP TABLE IF EXISTS " + majors_table_name + ";");

        //recreate the tables
        onCreate(db);
    }

    public String getStudentsTableName()
    {
        return students_table_name;
    }

    public String getMajorsTableName()
    {
        return majors_table_name;
    }

    //this function will be used once to add dummy data to the students table
    private void initStudents()
    {
        //will do its own error checking
        //only add the data if nothing is in the table
        if(countRecordsFromTable(students_table_name) == 0)
        {
            //get writeable version of the database
            SQLiteDatabase db = this.getWritableDatabase();

            //add dummy data to database
            // (studentId integer primary key autoincrement not null, fname varchar(50), lname varchar(50), email varchar(50), age integer, gpa double, major varchar(50));");
            db.execSQL("INSERT INTO " + students_table_name + " (fname, lname, email, age, gpa, major) VALUES ('Granger', 'Vaughan', 'Gray@gmail.com', '21', '3.0', 'Computer Science');");
            db.execSQL("INSERT INTO " + students_table_name + " (fname, lname, email, age, gpa, major) VALUES ('Alex', 'Vaughan', 'ACharles@gmail.com', '15', '2.1', 'Biology');");
            db.execSQL("INSERT INTO " + students_table_name + " (fname, lname, email, age, gpa, major) VALUES ('Caleb', 'Vaughan', 'DOneAthlete@gmail.com', '18', '3.4', 'Business');");
            db.execSQL("INSERT INTO " + students_table_name + " (fname, lname, email, age, gpa, major) VALUES ('Brett', 'Brown', 'WavyB@gmail.com', '23', '3.2', 'Computer Science');");

            //close the database
            db.close();
        }
    }

    private void initMajors()
    {
        if(countRecordsFromTable(majors_table_name) == 0)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //(majorId integer primary key autoincrement not null, studentId integer, majorName varchar(50), majorPrefix varchar(50)
            db.execSQL("INSERT INTO " + majors_table_name + " (studentId, majorName, majorPrefix) VALUES (1, 'Computer Science', 'CIS');");
            db.execSQL("INSERT INTO " + majors_table_name + " (studentId, majorName, majorPrefix) VALUES (2, 'Biology', 'BIOL');");
            db.execSQL("INSERT INTO " + majors_table_name + " (studentId, majorName, majorPrefix) VALUES (3, 'Business', 'BUS');");
            db.execSQL("INSERT INTO " + majors_table_name + " (studentId, majorName, majorPrefix) VALUES (4, 'Computer Science', 'CIS');");

            db.close();
        }

    }

    public int countRecordsFromTable(String tableName)
    {
        //get an instance of the a readable database
        //we only need readable because we are not adding anything to the database with this action
        SQLiteDatabase db = this.getReadableDatabase();

        //count the number of entries in the table that was passed to the function
        //this is a built-in function
        int numRows = (int) DatabaseUtils.queryNumEntries(db, tableName);

        //whenever you open the database you need to close it
        db.close();

        return numRows;
    }

    public void initAllTables()
    {
        initStudents();
        initMajors();
    }

    public ArrayList<StudentInfo> getAllStudents()
    {
        ArrayList<StudentInfo> students = new ArrayList<>();

        //select all from students table
        String selectStatement = "SELECT * FROM " + students_table_name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst())
        {
            do
            {
                //layout of table
                //(studentId , fname, lname, email, age, gpa, major
                String fname = cursor.getString(1);
                String lname = cursor.getString(2);
                String id = cursor.getString(0);
                String email = cursor.getString(3);
                String age = cursor.getString(4);
                String gpa = cursor.getString(5);
                String major = cursor.getString(6);

                students.add(new StudentInfo(fname, lname, id, email, age, gpa, major));
            }
            while(cursor.moveToNext());


        }
        db.close();
        return students;

    }


}
