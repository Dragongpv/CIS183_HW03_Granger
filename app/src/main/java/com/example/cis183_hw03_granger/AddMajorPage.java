package com.example.cis183_hw03_granger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddMajorPage extends AppCompatActivity {

    EditText et_j_majorName;
    EditText et_j_majorPrefix;
    TextView tv_j_error_majorNameExists;
    TextView tv_j_error_majorPrefixExists;
    Button btn_j_back;
    Button btn_j_add;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_major_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_j_majorName = findViewById(R.id.et_v_addMajor_majorName);
        et_j_majorPrefix = findViewById(R.id.et_v_addMajor_majorPrefix);
        tv_j_error_majorNameExists = findViewById(R.id.tv_v_addMajor_errorMajorNameExists);
        tv_j_error_majorPrefixExists = findViewById(R.id.tv_v_addMajor_errorMajorPrefixExists);
        btn_j_add = findViewById(R.id.btn_v_addMajor_add);
        btn_j_back = findViewById(R.id.btn_v_addMajor_back);
        db = new DatabaseHelper(this);

        buttonClickListeners();
    }

    //when add is clicked we want to first check if the name and prefix exist already in the database
    //if they do produce an error and do not add or change intents
    //if they do NOT exist we want to add them and change intents

    private void buttonClickListeners()
    {
        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AddMajorPage.this, MainActivity.class));
            }
        });

        btn_j_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String mName = et_j_majorName.getText().toString();
                String mPre = et_j_majorPrefix.getText().toString();
                Log.d("Major Name 1: ", mName + "");
                Log.d("Major prefix 1: ", mPre + "");

                Integer check = db.checkMajorExists(mName, mPre);
                //0 is its open
                //1 is the name exists
                //2 is the prefix exists
                //3 is they both exist

                //set both to invisible incase this is second click
                tv_j_error_majorNameExists.setVisibility(View.INVISIBLE);
                tv_j_error_majorPrefixExists.setVisibility(View.INVISIBLE);


                mName = et_j_majorName.getText().toString();
                mPre = et_j_majorPrefix.getText().toString();

                if(check == 0)
                {
                    Log.d("checked : ", "both are open");
                    Log.d("Major Name 2: ", mName + "");
                    Log.d("Major prefix 2: ", mPre + "");

                    db.addMajor(mName, mPre);

                    startActivity(new Intent(AddMajorPage.this, MainActivity.class));
                }
                else if(check == 1)
                {
                    tv_j_error_majorNameExists.setVisibility(View.VISIBLE);
                }
                else if(check == 2)
                {
                    tv_j_error_majorPrefixExists.setVisibility(View.VISIBLE);
                }
                else if(check == 3)
                {
                    tv_j_error_majorNameExists.setVisibility(View.VISIBLE);
                    tv_j_error_majorPrefixExists.setVisibility(View.VISIBLE);
                }


            }
        });
    }


}