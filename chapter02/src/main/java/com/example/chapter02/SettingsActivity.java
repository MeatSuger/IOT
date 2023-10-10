package com.example.chapter02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("设置");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.setClass(SettingsActivity.this, MainActivity.class);
        setIntent(intent);
        finish();
        return super.onSupportNavigateUp();
    }
}