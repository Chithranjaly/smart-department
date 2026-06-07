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

public class Parent_view_fees extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    String[] fee_id,course_name,fee_amount,due_date,val;
    public static String fee_ids,fee_amounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_fees);
        l1=(ListView)findViewById(R.id.lvfees);
        l1.setOnItemClickListener(this);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Parent_view_fees.this;
        String q = "/parent_view_fees";
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
            if(method.equalsIgnoreCase("parent_view_fees")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    fee_id=new String[ja1.length()];
                    course_name=new String[ja1.length()];
                    fee_amount=new String[ja1.length()];
                    due_date=new String[ja1.length()];
                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        fee_id[i]=ja1.getJSONObject(i).getString("fee_id");
                        course_name[i]=ja1.getJSONObject(i).getString("course_name");
                        fee_amount[i]=ja1.getJSONObject(i).getString("fee_amount");
                        due_date[i]=ja1.getJSONObject(i).getString("due_date");
                        val[i]="Course Name :  "+course_name[i]+"\nFee Amount : "+fee_amount[i]+"\nDue Date :  "+due_date[i];


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
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        fee_ids=fee_id[arg2];
        fee_amounts=fee_amount[arg2];

        final CharSequence[] items = {"Make Payment","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Parent_view_fees.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Make Payment"))
                {

                    startActivity(new Intent(getApplicationContext(),Parent_make_payment.class));
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
