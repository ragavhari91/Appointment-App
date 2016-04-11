package pebi.appointment;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.StringReader;

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
                new LoginAsyncService().BackGroundTask();
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

        String URL = "http://9e854dcb.ngrok.io/users/all";


        public void BackGroundTask()
        {
            StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(LauncherActivity.this,response,Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ERROR"+error.toString());
                    Toast.makeText(LauncherActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(LauncherActivity.this);
            requestQueue.add(stringRequest);
        }
    }
}
