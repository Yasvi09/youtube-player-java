package com.example.youtube_java.ui.theme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtube_java.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerview);
        adapter=new Adapter(MainActivity.this,list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        fetchData();
        System.out.println("Called 2");
    }

    private void fetchData(){
        System.out.println("Fetched Data");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("YouTube", "Request queue created");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&key=AIzaSyBDmOhRM42CfZCxu-Z4OSeDTlV-VD0pnGo&type=video",
                response -> {
                    System.out.println("Response : "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("Response", jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                        for(int i=0;i< jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                            JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");
                            JSONObject jsonThumbnail = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                            Model model = new Model();
                            model.setVideoId(jsonVideoId.getString("videoId"));
                            model.setTitle(jsonObjectSnippet.getString("title"));
                            model.setUrl(jsonThumbnail.getString("url"));

                            list.add(model);
                        }

                        if(list.size() > 0){
                            Log.e("Response", list.toString());
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        System.out.println("Error : "+e.getMessage());
                    }
                }, error -> {
            System.out.println("Error : "+error.getMessage());
            Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(stringRequest);
    }
}