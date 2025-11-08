package com.example.cis183_hw03_granger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    static ArrayList<StudentInfo> studentlist;
    StudentInfoAdapter adapter;
    ListView lv_j_studentList;
    Button btn_j_addMajor;
    Button btn_j_search;
    Button btn_j_addStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        dbHelper.initAllTables();
        checkTableRecordCount();

        lv_j_studentList = findViewById(R.id.lv_v_main_studentList);
        btn_j_addMajor = findViewById(R.id.btn_v_main_addMajor);
        btn_j_search = findViewById(R.id.btn_v_main_search);
        btn_j_addStudent = findViewById(R.id.btn_v_main_addStudent);

        fillListView();

        setListClickListener();
        setButtonClickListeners();


    }

    private void setButtonClickListeners()
    {
        btn_j_addMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, AddMajorPage.class);
                startActivity(intent);
            }
        });

        btn_j_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, AddStudentPage.class);
                startActivity(intent);
            }
        });

        btn_j_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, SearchPage.class));
            }
        });


    }

    private void setListClickListener()
    {
        lv_j_studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHelper.clickedStudent = (StudentInfo) parent.getItemAtPosition(position);

                //open StudentDetailsPage
                Intent intent = new Intent(MainActivity.this, StudentDetailsPage.class);
                startActivity(intent);
            }
        });

        lv_j_studentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                StudentInfo clickedStudent = (StudentInfo) parent.getItemAtPosition(position);

                dbHelper.deleteStudent(clickedStudent.getId());
                studentlist.remove(position);
                adapter.notifyDataSetChanged();

                return true;

            }
        });



    }


    private void fillListView()
    {
        //we want to fill data about a student into a studentInfo object
        //then send that object to the listview

        studentlist = dbHelper.getAllStudents();
        adapter = new StudentInfoAdapter(this, studentlist);
        lv_j_studentList.setAdapter(adapter);

    }



    private void checkTableRecordCount()
    {
        Log.d("Students Record Count: ", dbHelper.countRecordsFromTable(dbHelper.getStudentsTableName()) + "");
        Log.d("Majors Record Count: ", dbHelper.countRecordsFromTable(dbHelper.getMajorsTableName()) + "");
    }
}