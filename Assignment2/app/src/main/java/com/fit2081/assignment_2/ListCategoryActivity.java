package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

public class ListCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListCategory.newInstance("d", "3s"))
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbarCommon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Categories");
    }

}