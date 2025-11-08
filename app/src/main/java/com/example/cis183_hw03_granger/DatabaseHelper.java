package com.example.cis183_hw03_granger;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        super(c, database_name,null,8);
    }
    //this is called when a new database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //this is where we will create the tables in our database
        //create table in the database
        //execute the sql statement on the database that was passed to the function called db
        //this can be tricky because we have to write sql statements as strings
        db.execSQL("CREATE TABLE " + students_table_name + " (username varchar(50) primary key not null, fname varchar(50), lname varchar(50), email varchar(50), age integer, gpa double, majorId Integer, FOREIGN KEY (majorId) REFERENCES " + majors_table_name + "(majorId));");
        db.execSQL("CREATE TABLE " + majors_table_name + " (majorId integer primary key autoincrement not null, majorName varchar(50), majorPrefix varchar(50));");
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
            db.execSQL("INSERT INTO " + students_table_name + " (username, fname, lname, email, age, gpa, majorId) VALUES ('GVaughan', 'Granger', 'Vaughan', 'Gray@gmail.com', '21', '3.0', '1');");
            db.execSQL("INSERT INTO " + students_table_name + " (username, fname, lname, email, age, gpa, majorId) VALUES ('AVaughan', 'Alex', 'Vaughan', 'ACharles@gmail.com', '15', '2.1', '2');");
            db.execSQL("INSERT INTO " + students_table_name + " (username, fname, lname, email, age, gpa, majorId) VALUES ('CVaughan', 'Caleb', 'Vaughan', 'DOneAthlete@gmail.com', '18', '3.4', '3');");
            db.execSQL("INSERT INTO " + students_table_name + " (username, fname, lname, email, age, gpa, majorId) VALUES ('BBrown', 'Brett', 'Brown', 'WavyB@gmail.com', '23', '3.2', '1');");

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
            db.execSQL("INSERT INTO " + majors_table_name + " (majorName, majorPrefix) VALUES ('Computer Science', 'CIS');");
            db.execSQL("INSERT INTO " + majors_table_name + " (majorName, majorPrefix) VALUES ('Biology', 'BIOL');");
            db.execSQL("INSERT INTO " + majors_table_name + " (majorName, majorPrefix) VALUES ('Business', 'BUS');");
            db.execSQL("INSERT INTO " + majors_table_name + " (majorName, majorPrefix) VALUES ('Computer Science', 'CIS');");

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

    public ArrayList<MajorInfo> getDistinctMajors()
    {
        ArrayList<MajorInfo> majors = new ArrayList<>();

        String selectStatement = "SELECT DISTINCT majorId, majorName, majorPrefix FROM " + majors_table_name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String prefix = cursor.getString(2);

                majors.add(new MajorInfo(id, name, prefix));
            }
            while(cursor.moveToNext());
        }
        db.close();
        return majors;

    }

    public MajorInfo getStudentsMajor(String username)
    {
        MajorInfo major = new MajorInfo(null, null, null);

        String majorNameCol = majors_table_name + ".majorName";
        String studentUsername = students_table_name + ".username";
        String studentMajorId = students_table_name + ".majorId";
        String majorId = majors_table_name + ".majorId";
        String majorPrefix = majors_table_name + ".majorPrefix";
        String selectStatement = "SELECT " + majorId + ", " + majorNameCol + ", " + majorPrefix + " FROM " + students_table_name + " INNER JOIN " + majors_table_name + " ON " + studentMajorId + " = " + majorId + " WHERE " + studentUsername + " = '" + username + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst())
        {
            major.setMajorId(cursor.getString(0));
            major.setMajorName(cursor.getString(1));
            major.setMajorPrefix(cursor.getString(2));

        }
        db.close();
        Log.d("grabbed major", major + "");
        return major;

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


    //need a SQL statement to update data in database
    //UPDATE students_table_name SET fname = '', lname = '', etc, etc WHERE username = (the username)

    //username, fname, lname, email, age, gpa, major
    public void updateStudentInfo(String username, String firstname, String lastname, String email, String age, String gpa, Integer majorId)
    {
        //we will get passed everything from the edit texts in the details page
        //we then want to update all info where the username matches

        String updateStudent = "UPDATE " + students_table_name + " SET fname = '" + firstname + "', lname = '" + lastname + "', email = '" + email + "', age = '" + age + "', gpa = '" + gpa + "', majorId = '" + majorId + "' WHERE username = '" + username + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateStudent);


        db.close();
    }

    public Integer checkMajorExists(String mName, String mPre)
    {
        // 0 will be the major slot is open
        // 1 will be the name exists already
        // 2 will be the prefix exists already
        // 3 will be both exist already
        Integer condition = 0;
        Boolean nameExists = false;
        Boolean prefixExists = false;
        String majorName = null;
        String majorPrefix = null;

        String searchMajorName = "SELECT majorName FROM " + majors_table_name + " WHERE majorName = '" +mName + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(searchMajorName, null);

        if(cursor1.moveToFirst() )
        {
            majorName = cursor1.getString(0);
            //so if it was found grab it
            Log.d("Found Major: ", majorName);
            nameExists = true;
        }
        cursor1.close();

        String searchMajorPrefix = "SELECT majorPrefix FROM " + majors_table_name + " WHERE majorPrefix = '" + mPre + "';";

        Cursor cursor2 = db.rawQuery(searchMajorPrefix, null);

        if(cursor2.moveToFirst())
        {
            majorPrefix = cursor2.getString(0);
            //so if it was found grabn it
            Log.d("Found Prefix: ", majorPrefix);
            prefixExists = true;
        }
        cursor2.close();

        if(nameExists && prefixExists)
        {
            db.close();
            return 3;
        }
        else if(nameExists)
        {
            db.close();
            return 1;
        }
        else if(prefixExists)
        {
            db.close();
            return 2;
        }
        else
        {
            db.close();
            return 0;
        }


    }

    public void addMajor(String mName, String mPre)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + majors_table_name + " (majorName, majorPrefix) VALUES ('" + mName + "', '" + mPre + "');");

        db.close();
    }

    public Boolean checkUsernameExists(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String checkUsername = "SELECT username FROM " + students_table_name + " WHERE username = '" + username + "';";

        Cursor cursor = db.rawQuery(checkUsername, null);

        if(cursor.moveToFirst())
        {
            //something must have been found so user exists
            db.close();
            return true;
        }
        else
        {
            //nothing shouldve been found
            db.close();
            return false;
        }


    }

    public void addStudent(String username, String fname, String lname, String email, String age, String gpa, String majorId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " + students_table_name + " (username, fname, lname, email, age, gpa, majorId) VALUES ('" + username + "', '" + fname + "', '" + lname + "', '" + email + "', '" + age + "', '" + gpa + "', '" + majorId +"');");

        db.close();
    }

    public String findMajorNameGivenId(String id)
    {
        String name;
        SQLiteDatabase db = this.getReadableDatabase();
        String findStatement = "SELECT majorName FROM " + majors_table_name + " WHERE majorId = '" + id + "';";
        Cursor cursor = db.rawQuery(findStatement, null);

        if(cursor.moveToFirst())
        {
            name = cursor.getString(0);
            db.close();
            return name;
        }
        return "error finding major name";
    }

    public void deleteStudent(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(students_table_name, "username = '" + username + "'", null);
        db.close();
    }

    public ArrayList<StudentInfo> findStudentByCriteria(String fname, String lname, String major, String gpaLow, String mathSign)
    {
        //build select statement in sections
        //SELECT * FROM Students INNER JOIN Majors ON StudentMajorId = majorId WHERE
        //SELECT * FROM Students WHERE
        //major can be taken from the spinner and search off of the majorId already in students

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<StudentInfo> studentsFound = new ArrayList<>();

        String selectByCritera = "SELECT * FROM " + students_table_name + " WHERE ";

        if(fname.isEmpty())
        {
            selectByCritera += "fname is not null and ";
        }
        else
        {
            selectByCritera += "fname = '" + fname + "' and ";
        }

        if(lname.isEmpty())
        {
            selectByCritera += "lname is not null and ";
        }
        else
        {
            selectByCritera += "lname = '" + lname + "' and ";
        }

        if(major.isEmpty())
        {
            selectByCritera += "majorId is not null and ";
        }
        else
        {
            selectByCritera += "majorId = '" + major + "' and ";
        }

        if(gpaLow.isEmpty())
        {
            selectByCritera += "gpa is not null;";
        }
        else
        {
            selectByCritera += "gpa " + mathSign +" " + gpaLow + ";";
        }

        Cursor cursor = db.rawQuery(selectByCritera, null);

        if(cursor.moveToFirst())
        {
            do
            {
                StudentInfo student = new StudentInfo(null, null, null, null, null, null, null);
                //(username, fname, lname, email, age, gpa, majorId)
                student.setId(cursor.getString(0));
                student.setFname(cursor.getString(1));
                student.setLname(cursor.getString(2));
                student.setEmail(cursor.getString(3));
                student.setAge(cursor.getString(4));
                student.setGpa(cursor.getString(5));
                student.setMajor(cursor.getString(6));

                studentsFound.add(student);

            }
            while(cursor.moveToNext());
        }

        db.close();
        return studentsFound;



    }

}
