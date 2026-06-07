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

public class Student_view_timetable extends AppCompatActivity  implements JsonResponse{

    ListView l1;
    String[] table_id,subject,day,session,val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_timetable);
        l1=(ListView)findViewById(R.id.lvtime);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Student_view_timetable.this;
        String q = "/Student_view_timetable?login_id="+Login.logid;
        q=q.replace(" ","%20");
        JR.execute(q);


    }


    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try{
            String method=jo.getString("method");

            if(method.equalsIgnoreCase("Student_view_timetable")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    table_id=new String[ja1.length()];
                    subject=new String[ja1.length()];
                    day=new String[ja1.length()];
                    session=new String[ja1.length()];
                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        table_id[i]=ja1.getJSONObject(i).getString("table_id");
                        subject[i]=ja1.getJSONObject(i).getString("subject");
                        day[i]=ja1.getJSONObject(i).getString("day");
                        session[i]=ja1.getJSONObject(i).getString("session");
                        val[i]="subject :  "+subject[i]+"\nday : "+day[i]+"\nsession :  "+session[i];


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



    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Student_home.class);
        startActivity(b);
    }


}
