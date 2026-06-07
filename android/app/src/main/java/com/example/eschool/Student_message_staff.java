package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Student_message_staff extends AppCompatActivity  implements JsonResponse{

    Button b1;
    EditText e1;
    ListView l1;
    public static String messages;
    public static String[] message_id,message,reply,message_date,value;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_message_staff);
        e1=(EditText)findViewById(R.id.etmessage);
        l1=(ListView)findViewById(R.id.lvmessage);
        b1=(Button)findViewById(R.id.btmessage);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                messages=e1.getText().toString();
                if(messages.equalsIgnoreCase(""))
                {
                    e1.setError("No value for Message");
                    e1.setFocusable(true);
                }
                else{
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) Student_message_staff.this;
                    String q = "/student_message_staff?loginid="+Login.logid+"&messages="+messages+"&staff_id="+Student_view_stdy_materials.staff_ids;
                    q=q.replace(" ","%20");
                    JR.execute(q);
                }
            }
        });
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Student_message_staff.this;
        String q = "/student_view_message_staff?staff_id="+Student_view_stdy_materials.staff_ids+"&loginid="+Login.logid;
        q=q.replace(" ","%20");
        JR.execute(q);
    }



    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try{
            String method=jo.getString("method");
            if(method.equalsIgnoreCase("student_message_staff")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("success")){

                    Toast.makeText(getApplicationContext(), " SENT", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Student_message_staff.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong!Try Again.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Student_view_stdy_materials.class));
                }
            }
            if(method.equalsIgnoreCase("student_view_message_staff")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    message_id=new String[ja1.length()];
                    message=new String[ja1.length()];
                    reply=new String[ja1.length()];
                    message_date=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        message_id[i]=ja1.getJSONObject(i).getString("message_id");
                        message[i]=ja1.getJSONObject(i).getString("message");
                        reply[i]=ja1.getJSONObject(i).getString("reply");
                        message_date[i]=ja1.getJSONObject(i).getString("message_date");
                        value[i]="Message :  "+message[i]+"\nReply :  "+reply[i]+"\nDate :  "+message_date[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,value);
                    l1.setAdapter(ar);
                    //startActivity(new Intent(getApplicationContext(),User_Post_Disease.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "No Message!!", Toast.LENGTH_LONG).show();

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
        Intent b=new Intent(getApplicationContext(),Student_view_stdy_materials.class);
        startActivity(b);
    }

}
