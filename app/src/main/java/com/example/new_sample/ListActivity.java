package com.example.new_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adpumb.lifecycle.Adpumb;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    ArrayList<String> contentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prepareContentList();
        adapter = new MyRecyclerViewAdapter(this, contentList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(Adpumb.TAG, "adding more rows in recyclerview");
                    extendContentList();
                    adapter.notifyItemRangeChanged(contentList.size()-10, contentList.size());
                }
            }
        });
    }

    private List<String> prepareData(List<String> data) {
        for (int index = 0; index < data.size() / 3; index++) {
            data.add(index*3, "native_ad_"+index);
        }
        return data;
    }

    private void extendContentList() {
        int currentItemCount = contentList.size();
        for (int i = currentItemCount; i < currentItemCount + 10; i++) {
            addItem(i);
        }
    }

    private void prepareContentList() {

        if (contentList.size() == 0){
            for (int i = 0; i < 10; i++) {
                addItem(i);
            }
        }

    }

    private void addItem(int index) {
        String itemTitle = "Content "+index;

        if (index % 3 == 0){
            itemTitle = "native_ad_"+index;
        }

        contentList.add(itemTitle);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}