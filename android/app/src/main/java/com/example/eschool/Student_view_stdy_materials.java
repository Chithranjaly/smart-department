package com.example.eschool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Student_view_stdy_materials extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener{

    ListView l1;
    String[] material_id,staff_id,title,material_path,staff_name;
    public static String material_ids="",staff_ids,material_paths;
    SharedPreferences sh;
    String dwnldfilepath = "";
    ProgressDialog mProgressDialog;
    String[] subid,name,file,plag,details;
    public static String sid,sname,qpath;
    String dwld_path,path1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_stdy_materials);
        l1=(ListView)findViewById(R.id.lvstdy);
        l1.setOnItemClickListener(this);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Student_view_stdy_materials.this;
        String q = "/student_view_stdy_materials";
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
            if(method.equalsIgnoreCase("student_view_stdy_materials")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    material_id=new String[ja1.length()];
                    staff_id=new String[ja1.length()];
                    title=new String[ja1.length()];
                    material_path=new String[ja1.length()];
                    staff_name=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        material_id[i]=ja1.getJSONObject(i).getString("material_id");
                        staff_id[i]=ja1.getJSONObject(i).getString("staff_id");
                        title[i]=ja1.getJSONObject(i).getString("title");
                        material_path[i]=ja1.getJSONObject(i).getString("material_path");
                        staff_name[i]=ja1.getJSONObject(i).getString("staff_name");


                    }
                    Custimage clist=new Custimage(this,material_path,title,staff_name);
                    l1.setAdapter(clist);

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
        staff_ids=staff_id[arg2];
        material_ids=material_id[arg2];
        material_paths=material_path[arg2];

        final CharSequence[] items = {"Download","Message","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Student_view_stdy_materials.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Download"))
                {

                   sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            		Toast.makeText(getApplicationContext(), sh.getString("ip", ""), Toast.LENGTH_LONG).show();
            		dwld_path = "http://"+sh.getString("ip","")+"/"+material_paths;
            		dwld_path = dwld_path.replace(" ", "%20");
            		String[] temp=material_paths.split("\\/");
            		material_paths=temp[2];
            		Toast.makeText(getApplicationContext(), qpath, Toast.LENGTH_LONG).show();
                    downloadFile();
                }

                if (items[item].equals("Message"))
                {

                    startActivity(new Intent(getApplicationContext(),Student_message_staff.class));
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

    void downloadFile()
    {
        // declare the dialog as a member field of your activity
//        ProgressDialog mProgressDialog;

        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(Student_view_stdy_materials.this);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        // execute this when the downloader must be fired
        final DownloadTask downloadTask = new DownloadTask(getApplicationContext()); //YourActivity.this
        downloadTask.execute(dwld_path);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
    // that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/"+ qpath);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            mWakeLock.release();
            mProgressDialog.dismiss();
            if (s != null)
                Toast.makeText(context,"Download error : "+s, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
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
