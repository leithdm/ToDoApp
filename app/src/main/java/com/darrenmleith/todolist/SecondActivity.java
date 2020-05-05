package com.darrenmleith.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView _textView;
    int _index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        _index = intent.getIntExtra("indexOfNote", 0);

        _textView = findViewById(R.id.noteText);
        _textView.setText(MainActivity._notes.get(_index));
    }

    @Override
    public void onBackPressed() {
        MainActivity._notes.set(_index, _textView.getText().toString());
        try {
            MainActivity._sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity._notes)).apply();
            //using a HashSet
            //HashSet<String> set = new HashSet<>(MainActivity.notes);
            //sharedPreferences.edit().putStringSet("notes", set).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainActivity._arrayAdapter.notifyDataSetChanged();
        finish();
    }
}
