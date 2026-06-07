package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Parent_make_payment extends AppCompatActivity  implements JsonResponse{

    EditText e1,e2,e3,e4,e5,e6;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_make_payment);
        e1=(EditText)findViewById(R.id.editText1);
        e2=(EditText)findViewById(R.id.editText2);
        e3=(EditText)findViewById(R.id.editText3);
        e4=(EditText)findViewById(R.id.editText4);
        e5=(EditText)findViewById(R.id.editText5);
        e6=(EditText)findViewById(R.id.editText6);
        b1=(Button)findViewById(R.id.button1);

        e6.setText(Parent_view_fees.fee_amounts);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) Parent_make_payment.this;
                String q = "/parent_make_payment?login_id="+Login.logid+"&fee_ids="+Parent_view_fees.fee_ids+"&fee_amounts="+Parent_view_fees.fee_amounts;
                q=q.replace(" ","%20");
                JR.execute(q);

            }
        });

    }


    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method=jo.getString("method");

            if(method.equalsIgnoreCase("parent_make_payment"))
            {
                String status=jo.getString("status");
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
                if(status.equalsIgnoreCase("success"))
                {

                    Toast.makeText(getApplicationContext(),"Payment Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Parent_home.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Payment Faild", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Parent_view_fees.class);
        startActivity(b);
    }
}
