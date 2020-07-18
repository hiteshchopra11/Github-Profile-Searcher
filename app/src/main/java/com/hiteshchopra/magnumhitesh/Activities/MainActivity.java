package com.hiteshchopra.magnumhitesh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hiteshchopra.magnumhitesh.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = findViewById(R.id.editTextBox);
        ImageButton imageButton = findViewById(R.id.searchButton);

        imageButton.setOnClickListener(view -> {
            if (editText.length() == 0) {
                Toast.makeText(MainActivity.this, "Please Enter at least 1 character before proceeding", Toast.LENGTH_SHORT).show();
            } else {
                Intent newScreen = new Intent(MainActivity.this, SearchResultsActivity.class);
                newScreen.putExtra("userName", editText.getText().toString());
                startActivity(newScreen);
            }
        });
    }
}