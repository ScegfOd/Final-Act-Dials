package com.sss.jjcombs.finalactdials;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void green(View view){
        Intent intent = new Intent(this, UsableDials.class);
        intent.putExtra("COLOR", ContextCompat.getColor(this, R.color.colorGreen));
        startActivity(intent);
    }
    public void red(View view){
        Intent intent = new Intent(this, UsableDials.class);
        intent.putExtra("COLOR", ContextCompat.getColor(this, R.color.colorRed));
        startActivity(intent);
    }
}
