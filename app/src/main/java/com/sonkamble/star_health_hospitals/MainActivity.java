package com.sonkamble.star_health_hospitals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt_name;
    ArrayList<HashMap<String, String>> post_arryList;
    Button btn_operation_cancle,btn_update,btn_del;
    ProgressBar progressBar;
    AlertDialog dialog;
    SearchView searchView;
    TextView edit_num,edit_std;
    private static final int PERMISSION_REQUEST_CODE = 1;
    //String stud_url="https://vsproi.com/VSPI/stud_list.php?";
   // String stud_url="https://vsproi.com/WS_API/hospital_search.php";
    String stud_url="https://codingseekho.in/APP/WS_API/hospital_search.php?city=";
    ProgressDialog progressDoalog;
    vehical_recyclerAdapter demo_recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager_demo;
    private RecyclerView recyclerView_demo;
    String SubCodeStr,num,std;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------------Search Items----------------
        searchView = (SearchView) findViewById(R.id.grid_searchView);
        //------------------------------------------------------------------------------------------
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 0) {
                    SubCodeStr = newText;
                    SubCodeStr = SubCodeStr.replaceAll(" ", "%" + " ").toLowerCase();
                    Log.d("ssss", SubCodeStr);
                    load_data(SubCodeStr);
                } else if (TextUtils.isEmpty(newText)) {
                    load_data("");
                } else {
                    load_data("");
                }
                return false;
            }
        });
       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("START HEALTH HOSPITAL LIST");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.purple_700));
        }
        progressBar=(ProgressBar)findViewById(R.id.pg);
        post_arryList = new ArrayList<HashMap<String, String>>();

        recyclerView_demo=(RecyclerView)findViewById(R.id.recycler_vehical);
        //--------for linear layout--------------
        layoutManager_demo = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView_demo.setLayoutManager(layoutManager_demo);
        //---------for grid layout--------------
        // recyclerView_demo.setLayoutManager(new GridLayoutManager(View_Complaint.this,2));

        //------------------------------------------
        demo_recyclerAdapter=new vehical_recyclerAdapter(MainActivity.this,post_arryList);
        recyclerView_demo.setAdapter(demo_recyclerAdapter);

        load_data("");
        //------------------------------------------------------------------------------------------
    }
    public void load_data(String nm)
    {
        {   progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.show();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, stud_url+nm, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressDoalog.dismiss();
                    //Toast.makeText(getApplicationContext(),"Responce"+response,Toast.LENGTH_LONG).show();
                    try
                    {
                        if(response != null){
                            progressBar.setVisibility(View.INVISIBLE);
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject postobject = jsonObject.getJSONObject("posts");
                            String status = postobject.getString("status");
                            if (status.equals("200")) {
                                post_arryList.clear();
                                // Toast.makeText(getApplicationContext(),"Success:"+status,Toast.LENGTH_LONG).show();
                                JSONArray jsonArray=postobject.getJSONArray("post");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.optJSONObject(i);
                                    if (c != null) {
                                        HashMap<String, String> map = new                                                     HashMap<String, String>();
                                        String  ID = c.getString("S_NO");
                                        String  nm = c.getString("Hospital_Name");
                                        String  addr1 = c.getString("ADDRESS1");
                                        String addr2=c.getString("ADDRESS2");
                                        String area=c.getString("AREA");
                                        String city=c.getString("City");
                                        String pin=c.getString("Pin_code");
                                        String dist=c.getString("DISTRICT");
                                        String state=c.getString("STATE");
                                        String zone=c.getString("Zone");
                                        String city_cat=c.getString("City_Category");
                                        String std=c.getString("STD_CODE");
                                        String contact_no=c.getString("CONTACT_NO");

                                        map.put("HID", ID);
                                        map.put("Hospital_Name", nm);
                                        map.put("ADDRESS1", addr1);
                                        map.put("ADDRESS2", addr2);
                                        map.put("AREA", area);
                                        map.put("City", city);
                                        map.put("Pin_code", pin);
                                        map.put("DISTRICT", dist);
                                        map.put("STATE", state);
                                        map.put("Zone", zone);
                                        map.put("City_Category", city_cat);
                                        map.put("STD_CODE", std);
                                        map.put("CONTACT_NO", contact_no);

                                        post_arryList.add(map);
                                        //json_responce.setText(""+post_arryList);
                                    }
                                }
                            }
                        }
                    }catch (Exception e){}
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getInstance(MainActivity.this).addToRequestque(jsonObjectRequest);
        }
        if (demo_recyclerAdapter != null) {
            demo_recyclerAdapter.notifyDataSetChanged();

            System.out.println("Adapter " + demo_recyclerAdapter.toString());
        }
    }

    public class vehical_recyclerAdapter extends RecyclerView.Adapter<vehical_recyclerAdapter.DemoViewHolder>
    {
        Context context;
        ArrayList<HashMap<String, String>> img_list;

        public vehical_recyclerAdapter(Context context, ArrayList<HashMap<String, String>> quans_list) {
            this.img_list = quans_list;
            this.context = context;
        }

        @Override
        public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospt_list, parent, false);
            DemoViewHolder ViewHolder = new DemoViewHolder(view);
            return ViewHolder;
        }

        @Override
        public void onBindViewHolder(DemoViewHolder merchantViewHolder, final int position)
        {
       merchantViewHolder.txt_d1.setText(img_list.get(position).get("Hospital_Name"));
            merchantViewHolder.txt_d2.setText(img_list.get(position).get("ADDRESS1"));
            merchantViewHolder.txt_d3.setText(img_list.get(position).get("ADDRESS2"));
            merchantViewHolder.txt_d4.setText(img_list.get(position).get("AREA"));
            merchantViewHolder.txt_d5.setText(img_list.get(position).get("City"));
            merchantViewHolder.txt_d6.setText(img_list.get(position).get("STATE"));
            merchantViewHolder.img_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    num=img_list.get(position).get("STD_CODE");
                    std=img_list.get(position).get("CONTACT_NO");
                    expnc_popup_alert();
                }
            });

        }

        @Override
        public int getItemCount() {
            return img_list.size();
        }

        public class DemoViewHolder extends RecyclerView.ViewHolder
        {    LinearLayout lin;
            ImageView img_call;
            TextView txt_d1,txt_d2,txt_d3,txt_d4,txt_d5,txt_d6;
            public DemoViewHolder(View itemView) {
                super(itemView);
                this.lin = (LinearLayout) itemView.findViewById(R.id.lin);
                this.txt_d1 = (TextView) itemView.findViewById(R.id.txt_d1);
                this.txt_d2 = (TextView) itemView.findViewById(R.id.txt_d2);
                this.txt_d3 = (TextView) itemView.findViewById(R.id.txt_d3);
                this.txt_d4 = (TextView) itemView.findViewById(R.id.txt_d4);
                this.txt_d5 = (TextView) itemView.findViewById(R.id.txt_d5);
                this.txt_d6 = (TextView) itemView.findViewById(R.id.txt_d6);
                this.img_call = (ImageView) itemView.findViewById(R.id.img_call);

            }
        }
    }
    public void expnc_popup_alert()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.contact_popup_form, null);


        edit_num=(TextView)alertLayout.findViewById(R.id.edit_num);
        edit_num.setText(num);
        edit_std=(TextView)alertLayout.findViewById(R.id.edit_std);
        edit_std.setText(std);

        btn_operation_cancle=(Button)alertLayout.findViewById(R.id.btn_cust_popup_cancle);
        btn_operation_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.show();

    }
}