package com.example.android.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class StartActivity extends AppCompatActivity {

    RadioButton easy,medium,difficult;
    static int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        easy = (RadioButton)findViewById(R.id.easy);
        medium = (RadioButton)findViewById(R.id.medium);
        difficult = (RadioButton)findViewById(R.id.difficult);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("level",6);
                startActivity(intent);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("level",8);
                startActivity(intent);
            }
        });

        difficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("level",10);
                startActivity(intent);
            }
        });
    }

//    public  void  clicked(View view){
//        if (size == 0){
//            Toast.makeText(StartActivity.this,"Choose a level first",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//    }
}
