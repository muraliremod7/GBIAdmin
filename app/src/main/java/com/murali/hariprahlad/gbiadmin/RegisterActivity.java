package com.murali.hariprahlad.gbiadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{
    private EditText admName, admMobile, admEmail,userPin,userConPin;
    private ImageView iView;
    private Button upimage,regPersInfo;
    private Cursor cursor;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String uploadImagePath = "";
    private ProgressDialog pDialog;
    private String KEY_IMAGE = "uploadfile";
    private String UPLOAD_URL ="http://www.gbiconnect.com/walletbabaservices/uploadFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        admName = (EditText)findViewById(R.id.regadmName);
        admMobile = (EditText)findViewById(R.id.regadmMobile);
        admEmail = (EditText)findViewById(R.id.regadmEmail);
        userPin = (EditText)findViewById(R.id.regadmPin);
        userConPin = (EditText)findViewById(R.id.regadmConPin);
        regPersInfo = (Button)findViewById(R.id.regadmInfo);
        iView = (ImageView) findViewById(R.id.UploadImage);
        upimage = (Button) findViewById(R.id.uploadimage);
        regPersInfo.setOnClickListener(this);
        iView.setOnClickListener(this);
        upimage.setOnClickListener(this);
    }
    public void timerDelayRemoveDialog(long time, final android.app.AlertDialog d){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);

    }
    private void UploadImage() {
        final File fileToUpload = new File(uploadImagePath);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading Image.....");
        pDialog.setCancelable(false);
        pDialog.show();
        timerDelayRemoveDialog(10*1000,pDialog);
        Ion.with(this)
                .load("POST", UPLOAD_URL)
                .setMultipartParameter("teamId", "15")
                .setMultipartFile(KEY_IMAGE, "image/jpeg", fileToUpload)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            pDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error uploading file", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(RegisterActivity.this, "File upload complete", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);

                    bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    iView.setImageBitmap(bm);
                    uploadImagePath = f.getAbsolutePath();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, this);

                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                iView.setImageBitmap(bm);
                uploadImagePath = tempPath;

            }
        }
    }
    @SuppressWarnings("deprecation")
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.UploadImage:
                selectImage();
                break;
            case R.id.uploadimage:
                UploadImage();
                break;
            case R.id.regadmInfo:
                String username = admName.getText().toString().replace("","%20");
                String mobilenum = admMobile.getText().toString().replace("","%20");
                String email = admEmail.getText().toString().replace("","%20");
                String pin = userPin.getText().toString().replace("","%20");
                String conpin = userConPin.getText().toString().replace("","%20");
                //registerUser(username,mobilenum,email,pin);
                Intent personal = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(personal);
                finish();
        }
    }

    private void registerUser(String username, String mobilenum, String email, String pin) {

    }
}
