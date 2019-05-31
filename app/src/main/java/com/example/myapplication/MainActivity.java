package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    public static String URL = "http://www.arved.tk/uploads/uploadimage.php";
    public static String URLGetData = "http://www.arved.tk/uploads/takedata.php";
    private static final String IMAGE_CAPTURE_FOLDER = "uploads";
    private static final int CAMERA_PIC_REQUEST = 1111;
    private Button btnCamera;
    private Button btnGetData;
    private ImageView check, cross;
    private static File file;
    private TextView tcTextView, nameTextView,infoTextView;
    private static Uri _imagefileUri;
    private TextView resultText;
    private TextView realResultTex;
    private static String _bytes64Sting, _imageFileName,_imageFileNameSave;
    String arrTC[];
    String arrName[];
    String arrSurname[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _imageFileName = String.valueOf(System.currentTimeMillis());
        btnCamera = (Button) findViewById(R.id.button);
        btnGetData = (Button) findViewById(R.id.buttonGetData);
        resultText = (TextView) findViewById(R.id.textView);
        realResultTex = (TextView) findViewById(R.id.textView2);
        nameTextView = (TextView) findViewById(R.id.textView3);

        infoTextView = (TextView) findViewById(R.id.textView4);
        tcTextView = (TextView) findViewById(R.id.textView5);
        check=(ImageView)findViewById(R.id.imageView);
        cross=(ImageView)findViewById(R.id.imageView2);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        }); //to take a photo and save to the server
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJSONForName("http://arved.tk/uploads/informationForName.php");
                getJSON("http://arved.tk/uploads/information.php");


            }
        });

    }

    private void getJSONForName(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {//at the end of the process
                super.onPostExecute(s);

                try {
                    loadIntoListViewForName(s);  //Send the json data to loacIntoListView(the data is in the s)
                } catch (JSONException e) {
                    e.printStackTrace();//if there is problem with the s
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {//for connection we must start it in the background
                    java.net.URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    //
    private void loadIntoListViewForName(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json); //we save all the messages into a array and take it on java
        arrTC=new String[jsonArray.length()];//the name and the url of the message
        arrName=new String[jsonArray.length()]; //message that we want to get
        arrSurname=new String[jsonArray.length()];//all the TC informations saved in that array





        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            arrTC[i] = obj.getString("tc");
            //System.out.println(arrUrl[i]);
            arrName[i] = obj.getString("name");
            // System.out.println(arrMessage[i]);

            arrSurname[i] = obj.getString("surname");


        }


        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //listView.setAdapter(arrayAdapter);
    }
    //Get the data from the url
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {//at the end of the process
                super.onPostExecute(s);

                try {
                    loadIntoListView(s);  //Send the json data to loacIntoListView(the data is in the s)
                } catch (JSONException e) {
                    e.printStackTrace();//if there is problem with the s
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {//for connection we must start it in the background
                    java.net.URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    //
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json); //we save all the messages into a array and take it on java
        String arrUrl[]=new String[jsonArray.length()];//the name and the url of the message
        String arrMessage[]=new String[jsonArray.length()]; //message that we want to get
        String arrTc[]=new String[jsonArray.length()];//all the TC informations saved in that array
        String arrReal[]=new String[jsonArray.length()]; // is that real or not
        String arrFlag[]=new String[jsonArray.length()]; // is that real or not
        String name, surname, nameSurname="";
        String arr="";//empty string
        int a= 0;
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            arrUrl[i] = obj.getString("url");
            //System.out.println(arrUrl[i]);
            arrFlag[i] = obj.getString("flag");
            arrTc[i] = obj.getString("tc");
            // System.out.println(arrMessage[i]);

            arrMessage[i] = obj.getString("message");
            arrReal[i] = obj.getString("inquiry_results");
            //System.out.println(arrTc[i]);


        }
        for(int i =0; i<arrUrl.length;i++){
            if(arrUrl[i].equals(_imageFileNameSave+".jpg")) {
                for (int j=0; j<arrTC.length; j++){
                    if(arrTc[i].equals(arrTC[j])){
                        name=arrName[j];
                        surname=arrSurname[j];
                        nameSurname = name +" "+ surname;
                    }

                }
                infoTextView.setText(arrMessage[i]);
                tcTextView.setText( arrTc[i]);

                nameTextView.setText(nameSurname);
                if(arrReal[i].equals("gercek kimlik")){
                    check.setVisibility(View.VISIBLE);
                    cross.setVisibility(View.INVISIBLE);
                }
                if(arrReal[i].equals("sahte kimlik")){
                    check.setVisibility(View.INVISIBLE);
                    cross.setVisibility(View.VISIBLE);

                }
                if(arrFlag[i].equals("1")){
                    infoTextView.setText("waiting");
                    tcTextView.setText( "waiting");

                    nameTextView.setText("waiting");
                }

                //arr ="file name: "+arrUrl[i]+ "message : " + arrMessage[i] + " tc :" + arrTc[i];

            }
        }
        realResultTex.setText(arr);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //listView.setAdapter(arrayAdapter);
    }




    private void captureImage() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        _imagefileUri = Uri.fromFile(getFile());//with intent we can take a picture

        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imagefileUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {

                try{
                    uploadImage(_imagefileUri.getPath());//go to uploadImage to upload image as we understand :)
                    realResultTex.setText(_imagefileUri.getPath()); //the path will be showed on the second text line
                }
                catch(Exception e) {
                    realResultTex.setText(e.toString());//if there is a error you can easily see on the second txt line
                }

            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private void uploadImage(String picturePath) {
        Bitmap bm = BitmapFactory.decodeFile(picturePath); //with help of the path we can save as bitmap on our java code
        ByteArrayOutputStream bao = new ByteArrayOutputStream(); //this stream help us to send the data to our php code and it is in the URL string
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao); //compress the bitmap
        byte[] byteArray = bao.toByteArray();
        _bytes64Sting = Base64.encodeBytes(byteArray);
        RequestPackage rp = new RequestPackage();
        rp.setMethod("POST");
        rp.setUri(URL);
        rp.setSingleParam("base64", _bytes64Sting);
        rp.setSingleParam("ImageName", _imageFileName + ".jpg");

        // Upload image to server
        new uploadToServer().execute(rp);

    }

    public class uploadToServer extends AsyncTask<RequestPackage, Void, String> {

        private ProgressDialog pd = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            resultText = (TextView) findViewById(R.id.textView);
            _imageFileNameSave=_imageFileName;
            resultText.setText("New file "+_imageFileName+".jpg created\n");//your file na  me is shown in the text
            pd.setMessage("Image uploading!, please wait..");//for the progress dialog
            pd.setCancelable(false);//we can't cancel it, it is necessary
            pd.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = MyHttpURLConnection.getData(params[0]);
            return content;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                pd.hide();
                pd.dismiss();
            }
            catch (Exception e){
                realResultTex.setText(e.toString());
            }
            //resultText.append(result);
        }
    }

    private File getFile() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        file = new File(filepath, IMAGE_CAPTURE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return new File(file + File.separator + _imageFileName
                + ".jpg");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
