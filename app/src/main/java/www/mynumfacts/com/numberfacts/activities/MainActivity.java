package www.mynumfacts.com.numberfacts.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import www.mynumfacts.com.numberfacts.R;

public class MainActivity extends AppCompatActivity {

    /*Final variables*/
    private static final String TAG = "MainActivity";
    private static final String STRING_RANDOM_REQUEST_URL = "http://numbersapi.com/random/";
    private static final String STRING_QUEST_REQUEST_URL = "http://numbersapi.com/";

    /*UI ELements*/
    ProgressDialog progressDialog;
    private Spinner spinner;
    private Button submit;
    private TextView outputTextView;
    private TextView mTextFact;
    private View showDialogView;
    private TextView mtextview_number;
    private EditText meditText_number;
    /*Default params*/
    private String category = "trivia";
    private String mQuery_param_number = "10";
    private String mQuery_param_category = "date";
    private boolean randomRequest = true;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    randomRequest = true;
                    mtextview_number.setVisibility(View.GONE);
                    meditText_number.setVisibility(View.GONE);
                    return true;

                case R.id.navigation_dashboard:
                    randomRequest = false;
                    mtextview_number.setVisibility(View.VISIBLE);
                    meditText_number.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        submit = (Button) findViewById(R.id.button_submit);
        mTextFact = (TextView) findViewById(R.id.textView_fact);
        mtextview_number = (TextView) findViewById(R.id.textView_number);
        meditText_number = (EditText) findViewById(R.id.editText_number);
        spinner = (Spinner) findViewById(R.id.spinner);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        List<String> list = new ArrayList<String>();
        list.add("trivia");
        list.add("math");
        list.add("date");
        list.add("year");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        addListenerOnSpinnerItemSelection();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (randomRequest == true) {
                    String queryAppendedURL = STRING_RANDOM_REQUEST_URL + category;
                    volleyStringRequst(queryAppendedURL);

                } else {
                    mQuery_param_category = spinner.getSelectedItem().toString();
                    mQuery_param_number = meditText_number.getText().toString();
                    String queryAppendedURL = STRING_QUEST_REQUEST_URL + mQuery_param_number + "/" + mQuery_param_category;
                    volleyStringRequst(queryAppendedURL);

                }


            }
        });

    }

    public void addListenerOnSpinnerItemSelection() {


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                /*Used for debugging.*/
//                Toast.makeText(parent.getContext(),
//                        "OnItemSelectedListener RISHI : " + parent.getItemAtPosition(pos).toString(),
//                        Toast.LENGTH_SHORT).show();
                category = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });
    }

    public void volleyStringRequst(String url) {

        String REQUEST_TAG = "com.mynumfacts.www";
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                showDialogView = li.inflate(R.layout.show_dialog, null);
                outputTextView = showDialogView.findViewById(R.id.text_view_dialog);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(showDialogView);
                alertDialogBuilder
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setCancelable(false)
                        .create();
                outputTextView.setText(response.toString());
                mTextFact.setText(response.toString());
                alertDialogBuilder.show();
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }


}
