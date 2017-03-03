package com.murali.hariprahlad.gbiadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.murali.hariprahlad.gbiadmin.Services.AlertDialogManager;
import com.murali.hariprahlad.gbiadmin.Services.ConnectionDetector;
import com.murali.hariprahlad.gbiadmin.Services.SessionManager;
import com.murali.hariprahlad.gbiadmin.model.PeopleCommonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SessionManager session;
    ProgressDialog progressDialog;
    private ListView listView;
    private List<PeopleCommonClass> arrayList = new ArrayList<PeopleCommonClass>();
    PeopleCommonClass peopleCommonClass;
    PeoplesListRow peoplesListRow;
    String Profilename,IdeaName,IdeaDesc,PhoneNum,Email;
    AlertDialogManager alert;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();

        peopleCommonClass =new PeopleCommonClass();
        // Inflate the layout for this fragment
        peoples();
        cd = new ConnectionDetector(this);
        listView = (ListView) findViewById(R.id.list);
        peoplesListRow = new PeoplesListRow(this,arrayList);
        peoplesListRow.notifyDataSetChanged();
        alert = new AlertDialogManager();

    }
    private void peoples() {
        Ion.with(this)
                .load("http://www.gbiconnect.com/walletbabaservices/getTeams")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e!=null){
                            if (!cd.isNetworkOn(MainActivity.this)) {
                                // Internet Connection is Present
                                // make HTTP requests
                                alert.showAlertDialog(MainActivity.this,"Sorry There is Network Problem",false);
                            }

                        }else{
                            try {
                                JSONObject jSONObject = new JSONObject(result);
                                int status = jSONObject.getInt("status");
                                if (status == 1) {
                                    JSONArray array = jSONObject.getJSONArray("teams");
                                    for(int i =0;i<array.length();i++){
                                        JSONObject j = array.getJSONObject(i);
                                        PeopleCommonClass peopleCommonClass = new PeopleCommonClass();
                                        if(j.has("leadName")||!j.isNull("leadName")){
                                            peopleCommonClass.setName(j.getString("leadName"));
                                        }
                                        if(j.has("ideaName")||!j.isNull("ideaName")){
                                            peopleCommonClass.setIdeaName(j.getString("ideaName").toString());
                                        }
                                        if(j.has("ideaDescription")||!j.isNull("ideaDescription")){
                                            peopleCommonClass.setIdeaDescription(j.getString("ideaDescription").toString());
                                        }
                                        if(j.has("profile")||!j.isNull("profile")){
                                            peopleCommonClass.setImage(j.getString("profile"));
                                        }
                                        if(j.has("phone")||!j.isNull("phone")){
                                            peopleCommonClass.setPhoneNumber(j.getString("phone"));
                                        }
                                        if(j.has("email")||!j.isNull("email")){
                                            peopleCommonClass.setEmail(j.getString("email"));
                                        }
                                        arrayList.add(peopleCommonClass);
                                    }
                                    listView.setAdapter(peoplesListRow);

                                } else {

                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Profilename = ((TextView)view.findViewById(R.id.ProfileName)).getText().toString();
                                IdeaName = ((TextView)view.findViewById(R.id.IdeaName)).getText().toString();
                                IdeaDesc = ((TextView)view.findViewById(R.id.Ideadesc)).getText().toString();
                                PhoneNum = ((TextView)view.findViewById(R.id.phonenumber)).getText().toString();
                                Email = ((TextView)view.findViewById(R.id.ProfileEmail)).getText().toString();
//                                Intent singlpeople = new Intent(MainActivity.this, SinglePeopleActivity.class);
//                                Bundle peoplessbundle = new Bundle();
//                                peoplessbundle.putString("ProfileName", Profilename);
//                                peoplessbundle.putString("IdeaName", IdeaName);
//                                peoplessbundle.putString("IdeaDesc", IdeaDesc);
//                                peoplessbundle.putString("ideaDescription",PhoneNum);
//                                peoplessbundle.putString("Email",Email);
//                                singlpeople.putExtras(peoplessbundle);
//                                if(singlpeople!=null){
//                                    startActivity(singlpeople);
//                                }
                            }
                        });
                    }

                });
    }
}