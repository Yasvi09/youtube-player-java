package com.example.youtube_java.ui.theme;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtube_java.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView categoryRecyclerView;
    Adapter adapter;
    CategoryTagAdapter categoryTagAdapter;
    ArrayList<Model> list = new ArrayList<>();
    List<String> categoryTags = Arrays.asList(
            "Explore", "All", "New to you", "Gaming", "Test Cr", "Music", "Mixes",
            "Live", "Comedy", "Podcasts", "Computer Science", "Recently uploaded");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up main video RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new Adapter(MainActivity.this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Set up category tags RecyclerView
        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        categoryTagAdapter = new CategoryTagAdapter(this, categoryTags);
        categoryRecyclerView.setAdapter(categoryTagAdapter);

        // Fetch YouTube data
        fetchData();
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&key=AIzaSyBDmOhRM42CfZCxu-Z4OSeDTlV-VD0pnGo&type=video",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                        for (int i = 0; i < jsonArray.length(); i++) {
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

                        if (list.size() > 0) {
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        Log.e("YouTube", "JSON parsing error: " + e.getMessage());
                    }
                }, error -> {
            Log.e("YouTube", "Network error: " + error.getMessage());
            Toast.makeText(MainActivity.this, "Error loading videos!", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(stringRequest);
    }

    // To match the screenshot better, let's update the Adapter class to add view and time info
    // This would be updated in the Adapter.java file, but showing here for clarity
    /*
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model model = list.get(position);
        holder.textView.setText(model.getTitle());
        Picasso.get().load(model.getUrl()).into(holder.imageView);

        // Generate random view count and time for demo purposes
        Random random = new Random();
        int viewCount = random.nextInt(1000);
        String displayViews;

        if (viewCount > 900) {
            displayViews = (viewCount / 100) + " lakh views";
        } else {
            displayViews = viewCount + "K views";
        }

        int days = random.nextInt(30) + 1;

        holder.channelName.setText("Channel " + (position + 1));
        holder.viewsCount.setText(displayViews);
        holder.publishTime.setText(days + " days ago");
    }
    */
}