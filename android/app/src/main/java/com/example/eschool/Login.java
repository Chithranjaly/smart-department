package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity  implements JsonResponse{

    Button b1;
    EditText e1,e2;
    String uname,pass;
    SharedPreferences sh;
    public static String logid,usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1=(Button)findViewById(R.id.btnlogin);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1=(EditText)findViewById(R.id.etunm);
        e2=(EditText)findViewById(R.id.etpass);
//        startService(new Intent(getApplicationContext(),LocationService.class));
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uname=e1.getText().toString();
                pass=e2.getText().toString();


                if(uname.equalsIgnoreCase(""))
                {
                    e1.setError("please enter username");
                    e1.setFocusable(true);
                }
                else if(pass.equalsIgnoreCase(""))
                {
                    e2.setError("enter correct Password");
                    e2.setFocusable(true);
                }

                else{
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) Login.this;
                    String q = "/login?username="+uname+"&password="+pass;
                    q=q.replace(" ","%20");
                    JR.execute(q);
                }



            }
        });



    }




    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {
            String status=jo.getString("status");
            Log.d("pearl",status);
            //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();

            if(status.equalsIgnoreCase("success")){
                JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                logid=ja1.getJSONObject(0).getString("login_id");
                usertype=ja1.getJSONObject(0).getString("usertype");

				SharedPreferences.Editor e=sh.edit();
				e.putString("log_id", logid);
				e.commit();

                if(usertype.equals("parent"))
                {


                    Toast.makeText(getApplicationContext()," You are Login Successfully!...,",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Parent_home.class));

                }

                if(usertype.equals("student"))
                {


                    Toast.makeText(getApplicationContext()," You are Login Successfully!...,",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Student_home.class));

                }



            }
            else {
                Toast.makeText(getApplicationContext(),"Login failed..!Please enter correct username or password ",Toast.LENGTH_LONG).show();
//				Intent i=new Intent(getApplicationContext(),MainLogin.class);
                startActivity(new Intent(getApplicationContext(),Login.class));
            }


        }catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }


    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),IPSetting.class);
        startActivity(b);
    }

}
