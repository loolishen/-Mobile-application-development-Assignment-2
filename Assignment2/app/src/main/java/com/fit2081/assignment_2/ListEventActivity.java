package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListEvent.newInstance("", ""))
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbarCommon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Events page");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}