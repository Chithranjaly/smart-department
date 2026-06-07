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

public class Parent_view_marklist extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    String[] mark_id,course_name,subject_name,exam_type,internal_mark,mark_awarded,val;
    public static String mark_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_marklist);
        l1=(ListView)findViewById(R.id.lvmark);
        l1.setOnItemClickListener(this);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Parent_view_marklist.this;
        String q = "/parent_view_marklist?stid="+Parent_view_students.student_ids;
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
            if(method.equalsIgnoreCase("parent_view_marklist")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    mark_id=new String[ja1.length()];
                    course_name=new String[ja1.length()];
                    subject_name=new String[ja1.length()];
                    exam_type=new String[ja1.length()];
                    internal_mark=new String[ja1.length()];
                    mark_awarded=new String[ja1.length()];
                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        mark_id[i]=ja1.getJSONObject(i).getString("mark_id");
                        course_name[i]=ja1.getJSONObject(i).getString("course_name");
                        subject_name[i]=ja1.getJSONObject(i).getString("subject_name");
                        exam_type[i]=ja1.getJSONObject(i).getString("exam_type");
                        internal_mark[i]=ja1.getJSONObject(i).getString("internal_mark");
                        mark_awarded[i]=ja1.getJSONObject(i).getString("mark_awarded");
                        val[i]="Course Name :  "+course_name[i]+"\nSubject Name : "+subject_name[i]+"\nExam Type :  "+exam_type[i]+"\nInternal Mark :  "+internal_mark[i]+"\nMark Awarded :  "+mark_awarded[i];


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

        mark_ids=mark_id[position];


    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Parent_view_students.class);
        startActivity(b);
    }


}
