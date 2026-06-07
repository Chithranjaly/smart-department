package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Student_home extends AppCompatActivity {

    Button b1,b2,b3,b5,b6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        b1=(Button)findViewById(R.id.statt);
        b2=(Button)findViewById(R.id.stexam);
        b3=(Button)findViewById(R.id.stmark);
//        b4=(Button)findViewById(R.id.ststdy);
        b5=(Button)findViewById(R.id.stlogout);
        b6=(Button)findViewById(R.id.timetable);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Student_view_attendance.class));
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Student_view_exam_details.class));
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Student_view_marklist.class));
            }
        });

//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(getApplicationContext(),Student_view_stdy_materials.class));
//            }
//        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Student_view_timetable.class));
            }
        });

    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Student_home.class);
        startActivity(b);
    }
}