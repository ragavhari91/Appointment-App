package pebi.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import pebi.appointment.model.User;

public class LauncherActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        user = new User();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        Button b = (Button) findViewById(R.id.btn_getuser);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText firstName = (EditText) findViewById(R.id.firstName);
                EditText lastName  = (EditText) findViewById(R.id.lastName);
                EditText emailID   = (EditText) findViewById(R.id.emailID);
                EditText mobileNo  = (EditText) findViewById(R.id.mobileNo);
                EditText password  = (EditText) findViewById(R.id.password);

                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setEmailID(emailID.getText().toString());
                user.setMobileNo(mobileNo.getText().toString());
                user.setPassword(password.getText().toString());

                new LoginAsyncService(user).BackGroundTask();
            }
        });
    }

    public void showDialog()
    {
        if(!progressDialog.isShowing())
        {
            progressDialog.show();
        }
    }

    public void hideDialog()
    {
        if(progressDialog.isShowing())
        {
            progressDialog.hide();
        }
    }

    private class LoginAsyncService
    {
        User users = new User();
        JSONObject jsonObject;

        String URL = "http://9e854dcb.ngrok.io/users/create";

        LoginAsyncService(User user)
        {
            this.users = user;
        }

        public void BackGroundTask()
        {
            JSONObject obj = new JSONObject();
            try {
                obj.put("firstName", users.getFirstName());
                obj.put("lastName", users.getLastName());
                obj.put("emailID",users.getEmailID());
                obj.put("mobileNo",users.getMobileNo());
                obj.put("password",users.getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            Log.d("RESPONSEEEE", response.toString());
                            Toast.makeText(LauncherActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", "Error: " + error.getCause());
                            Toast.makeText(LauncherActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders()  {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            Volley.newRequestQueue(LauncherActivity.this).add(req);
        }
    }
}
