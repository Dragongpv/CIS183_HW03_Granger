package com.example.cis183_hw03_granger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddStudentPage extends AppCompatActivity {

    EditText et_j_username;
    EditText et_j_firstName;
    EditText et_j_lastName;
    EditText et_j_email;
    EditText et_j_age;
    EditText et_j_gpa;
    Spinner sp_j_major;
    Button btn_j_back;
    Button btn_j_add;
    TextView tv_j_error_fillAllFields;
    DatabaseHelper db;
    TextView tv_j_error_usernameExists;
    ArrayList<MajorInfo> majors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_username = findViewById(R.id.et_v_add_username);
        et_j_firstName = findViewById(R.id.et_v_add_fname);
        et_j_lastName = findViewById(R.id.et_v_add_lname);
        et_j_email = findViewById(R.id.et_v_add_email);
        et_j_age = findViewById(R.id.et_v_add_age);
        et_j_gpa = findViewById(R.id.et_v_add_gpa);
        sp_j_major = findViewById(R.id.sp_v_add_major);
        btn_j_add = findViewById(R.id.btn_v_add_addStudent);
        btn_j_back = findViewById(R.id.btn_v_add_back);
        tv_j_error_fillAllFields = findViewById(R.id.tv_v_add_error_fillAllFields);
        tv_j_error_usernameExists = findViewById(R.id.tv_v_add_error_usernameExists);
        db = new DatabaseHelper(this);

        buttonClickListeners();

        fillAdapterWithMajors();
    }

    private void buttonClickListeners()
    {
        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AddStudentPage.this, MainActivity.class));
            }
        });

        btn_j_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checkAddStudent();
            }
        });
    }

    private void fillAdapterWithMajors()
    {
        majors = db.getDistinctMajors();
        ArrayAdapter<MajorInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,majors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_j_major.setAdapter(adapter);

    }

    private void checkAddStudent()
    {
        tv_j_error_fillAllFields.setVisibility(View.INVISIBLE);
        tv_j_error_usernameExists.setVisibility(View.INVISIBLE);

        //make sure fields are full
        //check username
        //if yes to both then add
        String username = et_j_username.getText().toString();
        String fname = et_j_firstName.getText().toString();
        String lname = et_j_lastName.getText().toString();
        String email = et_j_email.getText().toString();
        String age = et_j_age.getText().toString();
        String gpa = et_j_gpa.getText().toString();
        String major = sp_j_major.getSelectedItem().toString();
        Integer majorId = sp_j_major.getSelectedItemPosition() + 1;

        if(username.isEmpty() || fname.isEmpty() || lname.isEmpty() || email.isEmpty() || age.isEmpty() || gpa.isEmpty() || major.isEmpty())
        {
            //fields are empty produce error
            tv_j_error_fillAllFields.setVisibility(View.VISIBLE);
        }
        else
        {
            //fields are full check username

            if(!db.checkUsernameExists(username))
            {
                //if it returns false then the username does not exist yet
                //so add
                db.addStudent(username, fname, lname, email, age, gpa, majorId.toString());
                startActivity(new Intent(AddStudentPage.this, MainActivity.class));
            }
            else
            {
                //the username exists so dont add
                //produce error
                tv_j_error_usernameExists.setVisibility(View.VISIBLE);
            }

        }
    }
}