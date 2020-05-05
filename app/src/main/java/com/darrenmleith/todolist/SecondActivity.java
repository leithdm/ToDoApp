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
        MainActivity._notes.add(_index, _textView.getText().toString());
        MainActivity._notes.remove(_index+1);
        MainActivity._arrayAdapter.notifyDataSetChanged();
        finish();
    }
}
