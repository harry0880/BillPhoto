package com.billphoto;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.utils.Dbhandler;
import com.utils.RVAdapter;

public class List_BillInfo extends AppCompatActivity {

    RecyclerView lvItems;
    RelativeLayout rl;
    Dbhandler dbh;
    RVAdapter rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvItems = (RecyclerView) findViewById(R.id.rv);
        rl=(RelativeLayout) findViewById(R.id.rlNoRecords);
        setSupportActionBar(toolbar);
        dbh = new Dbhandler(this);
        InitializeAdapter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(List_BillInfo.this, Scnner_Activity.class).putExtra("ID","-1"));
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        lvItems.setLayoutManager(llm);
        lvItems.setHasFixedSize(true);

        rv.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String Id) {

                startActivity(new Intent(List_BillInfo.this,Scnner_Activity.class).putExtra("ID",Id));
            }
        });
    }

    void InitializeAdapter()
    {
        if(dbh.getCardData()!=null) {
            rl.setVisibility(View.INVISIBLE);
            lvItems.setVisibility(View.VISIBLE);
            rv = new RVAdapter(dbh.getCardData());
            lvItems.setAdapter(rv);
        }
        else
        {
            rl.setVisibility(View.VISIBLE);
            lvItems.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        InitializeAdapter();
        super.onResume();
    }

}
