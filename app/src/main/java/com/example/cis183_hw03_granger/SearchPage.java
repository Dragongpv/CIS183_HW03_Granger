package com.example.cis183_hw03_granger;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {

    EditText et_j_fname;
    EditText et_j_lname;
    Spinner sp_j_majors;
    EditText et_j_gpaLow;
    Spinner sp_j_mathSigns;
    EditText et_j_gpaHigh;
    ListView lv_j_searchedStudents;
    Button btn_j_back;
    Button btn_j_search;
    DatabaseHelper db;
    ArrayList<StudentInfo> studentsFound;
    SearchedStudentAdapter adapter;
    ArrayList<MajorInfo> majors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_fname = findViewById(R.id.et_v_search_fname);
        et_j_lname = findViewById(R.id.et_v_search_lname);
        sp_j_majors = findViewById(R.id.sp_v_search_majors);
        et_j_gpaLow = findViewById(R.id.et_v_search_gpaLow);
        sp_j_mathSigns = findViewById(R.id.sp_v_search_mathSigns);
        et_j_gpaHigh = findViewById(R.id.et_v_search_gpaHigh);
        lv_j_searchedStudents = findViewById(R.id.lv_v_search_studentsSearched);
        btn_j_back = findViewById(R.id.btn_v_search_back);
        btn_j_search = findViewById(R.id.btn_v_search_search);
        db = new DatabaseHelper(this);



        buttonclickListeners();
        mathSignsAdpater();
        fillMajorsSpinner();


    }

    //we need to pull all the info filled in and then send it to the database to search
    //we need to fill the mathSigns spinner with =, <, >

    private void fillMajorsSpinner()
    {
        majors = db.getDistinctMajors();
        ArrayAdapter<MajorInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,majors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_j_majors.setAdapter(adapter);
    }

     private void buttonclickListeners()
     {
        btn_j_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchForStudents();
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SearchPage.this, MainActivity.class));
            }
        });
     }

     private void mathSignsAdpater()
     {
         String[] options = {"=", "<", ">"};

         ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,options);

         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         sp_j_mathSigns.setAdapter(adapter);
     }

     private void searchForStudents()
     {
         String fname = et_j_fname.getText().toString();
         String lname = et_j_lname.getText().toString();
         Integer majorId = sp_j_majors.getSelectedItemPosition() + 1;
         String gpa = et_j_gpaLow.getText().toString();
         String mathSign = sp_j_mathSigns.getSelectedItem().toString();

         studentsFound = new ArrayList<>();
         studentsFound = db.findStudentByCriteria(fname, lname, majorId.toString(), gpa, mathSign);

         adapter = new SearchedStudentAdapter(this, studentsFound);
         lv_j_searchedStudents.setAdapter(adapter);


     }




}