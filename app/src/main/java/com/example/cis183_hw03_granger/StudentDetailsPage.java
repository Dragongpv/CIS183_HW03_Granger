package com.example.cis183_hw03_granger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StudentDetailsPage extends AppCompatActivity {

    TextView tv_j_id;
    EditText et_j_fname;
    EditText et_j_lname;
    EditText et_j_email;
    EditText et_j_age;
    EditText et_j_gpa;
    EditText et_j_major;
    Button btn_j_back;
    Button btn_j_update;
    DatabaseHelper dbHelper;
    StudentInfo student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_details_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_j_id = findViewById(R.id.tv_v_details_id);
        et_j_fname = findViewById(R.id.et_v_addStudent_fname);
        et_j_lname = findViewById(R.id.et_v_addStudent_lname);
        et_j_email = findViewById(R.id.et_v_addStudent_email);
        et_j_age = findViewById(R.id.et_v_addStudent_age);
        et_j_gpa = findViewById(R.id.et_v_addStudent_gpa);
        et_j_major = findViewById(R.id.et_v_addStudent_major);
        btn_j_back = findViewById(R.id.btn_v_details_back);
        btn_j_update = findViewById(R.id.btn_v_details_update);
        dbHelper = new DatabaseHelper(this);

        student = DatabaseHelper.clickedStudent;
        tv_j_id.setText(student.getId());
        et_j_fname.setText(student.getFname());
        et_j_lname.setText(student.getLname());
        et_j_email.setText(student.getEmail());
        et_j_age.setText(student.getAge());
        et_j_gpa.setText(student.getGpa());
        et_j_major.setText(student.getMajor());

        setButtonClicklisteners();

    }

    private void setButtonClicklisteners()
    {
        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(StudentDetailsPage.this, MainActivity.class);
                startActivity(intent);
            }
        });



        btn_j_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dbHelper.updateStudentInfo(tv_j_id.getText().toString(), et_j_fname.getText().toString(), et_j_lname.getText().toString(), et_j_email.getText().toString(), et_j_age.getText().toString(), et_j_gpa.getText().toString(), et_j_major.getText().toString());

                Intent intent = new Intent(StudentDetailsPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}