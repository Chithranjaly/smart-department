package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Parent_view_students extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    String[] student_id,st_name,batch,gender,dob,phone,email,val;
    public static String student_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_students);

        l1=(ListView)findViewById(R.id.lvchild);
        l1.setOnItemClickListener(this);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Parent_view_students.this;
        String q = "/parent_view_students?login_id="+Login.logid;
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
            if(method.equalsIgnoreCase("parent_view_students")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    student_id=new String[ja1.length()];
                    st_name=new String[ja1.length()];
                    batch=new String[ja1.length()];
                    gender=new String[ja1.length()];
                    dob=new String[ja1.length()];
                    phone=new String[ja1.length()];
                    email=new String[ja1.length()];
                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        student_id[i]=ja1.getJSONObject(i).getString("student_id");
                        st_name[i]=ja1.getJSONObject(i).getString("st_name");
                        batch[i]=ja1.getJSONObject(i).getString("batch_name");
                        gender[i]=ja1.getJSONObject(i).getString("gender");
                        dob[i]=ja1.getJSONObject(i).getString("dob");
                        phone[i]=ja1.getJSONObject(i).getString("phone");
                        email[i]=ja1.getJSONObject(i).getString("email");
                        val[i]="Child Name:  "+st_name[i]+"\nBatch : "+batch[i]+"\nGender:  "+gender[i]+"\nDob:  "+dob[i]+"\nPhone:  "+phone[i]+"\nEmail:  "+email[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    l1.setAdapter(ar);
                    //startActivity(new Intent(getApplicationContext(),User_Post_Disease.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "No Complaints!!", Toast.LENGTH_LONG).show();

                }
            }

        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        student_ids=student_id[arg2];

        final CharSequence[] items = {"Attendance","Mark List","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Parent_view_students.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Attendance"))
                {

                    startActivity(new Intent(getApplicationContext(),Parent_view_attendance.class));
                }

                else if (items[item].equals("Mark List"))
                {

                    startActivity(new Intent(getApplicationContext(),Parent_view_marklist.class));
                }



                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, GALLERY_CODE);
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Parent_home.class);
        startActivity(b);
    }


}
