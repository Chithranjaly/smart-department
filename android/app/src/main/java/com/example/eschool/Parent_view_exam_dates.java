package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parent_view_exam_dates extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener{

    ListView l1;
    String[] exam_id,course_name,subject_name,exam_type,exam_date,exam_time,val;
    public static String exam_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_exam_dates);
        l1=(ListView)findViewById(R.id.lvexam);
        l1.setOnItemClickListener(this);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Parent_view_exam_dates.this;
        String q = "/parent_view_exam_dates";
        q=q.replace(" ","%20");
        JR.execute(q);


    }


    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try{
            String method=jo.getString("method");
//            if(method.equalsIgnoreCase("user_send_complaints")){
//                String status=jo.getString("status");
//                Log.d("pearl",status);
//                //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
//                if(status.equalsIgnoreCase("success")){
//
//                    Toast.makeText(getApplicationContext(), " SENT", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),User_send_complaints.class));
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "Something went wrong!Try Again.", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),Users_home.class));
//                }
//            }
            if(method.equalsIgnoreCase("parent_view_exam_dates")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    exam_id=new String[ja1.length()];
                    course_name=new String[ja1.length()];
                    subject_name=new String[ja1.length()];
                    exam_type=new String[ja1.length()];
                    exam_date=new String[ja1.length()];
                    exam_time=new String[ja1.length()];
                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        exam_id[i]=ja1.getJSONObject(i).getString("exam_id");
                        course_name[i]=ja1.getJSONObject(i).getString("course_name");
                        subject_name[i]=ja1.getJSONObject(i).getString("subject_name");
                        exam_type[i]=ja1.getJSONObject(i).getString("exam_type");
                        exam_date[i]=ja1.getJSONObject(i).getString("exam_date");
                        exam_time[i]=ja1.getJSONObject(i).getString("exam_time");
                        val[i]="Course Name:  "+course_name[i]+"\nSubject Name : "+subject_name[i]+"\nExam Type :  "+exam_type[i]+"\nExam Date :  "+exam_date[i]+"\nExam Time :  "+exam_time[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    l1.setAdapter(ar);
                    //startActivity(new Intent(getApplicationContext(),User_Post_Disease.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "No Data!!", Toast.LENGTH_LONG).show();

                }
            }

        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        exam_ids=exam_id[position];


    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Parent_home.class);
        startActivity(b);
    }


}
