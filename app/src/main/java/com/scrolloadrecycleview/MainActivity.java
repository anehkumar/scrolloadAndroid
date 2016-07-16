package com.scrolloadrecycleview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetResponse {
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<GetterSetter> arrayList;
    private ProgressBar marker_progress;
    private ProductAdapter adapter;
    private CheckInternet checkInternet;
    private int i;
    private boolean loading;
    private AsyncReuse requestServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        arrayList = new ArrayList<>();
        marker_progress = (ProgressBar) findViewById(R.id.marker_progress);
        adapter = new ProductAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        checkInternet = new CheckInternet();
        Context context = this;
        i = 1;
        checkInternet.isNetworkAvailable(context, this);
        if (checkInternet.isConnected) {
            loadData();
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("album_id", "132");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestServer.getObjectQ(jObject);
            requestServer.execute();
        }
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                    }
                })
        );
        // Add Scroll Listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            marker_progress.setVisibility(View.VISIBLE);
                            if (checkInternet.isConnected) {
                                scrollLoad();
                                JSONObject jObject = new JSONObject();
                                try {
                                    i = 2;
                                    jObject.put("page", i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestServer.getObjectQ(jObject);
                                requestServer.execute();
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadData() {
        requestServer = new AsyncReuse(URLs.catalogProducts, true, this);
        requestServer.getResponse = this;
    }

    private void scrollLoad() {
        requestServer = new AsyncReuse(URLs.catalogProducts, false, this);
        requestServer.getResponse = this;
    }

    @Override
    public void getData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.getString("status").equals("1")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    GetterSetter getterSetter = new GetterSetter();
                    getterSetter.setProduct(object1.getString("product"));
                    getterSetter.setImage(object1.getString("Image"));
                    arrayList.add(getterSetter);
                }
                adapter.notifyDataSetChanged();
                loading = true;
                marker_progress.setVisibility(View.GONE);
            } else {
                loading = false;
                marker_progress.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
