package com.example.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProvinceDetailActivity extends AppCompatActivity {


    String country;
    String provinceName;
    String caseNumber;
    String date;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_detail);
        //get all the date from intent
        Intent fromIntent = getIntent();
        country = fromIntent.getStringExtra("Country");
        provinceName = fromIntent.getStringExtra("Province");
        caseNumber = fromIntent.getStringExtra("Cases");
        date = fromIntent.getStringExtra("Date");
        id = fromIntent.getLongExtra("id", -1);

        //find all the views
        TextView countryTextView = findViewById(R.id.country_text_view);

        TextView provinceTextView = findViewById(R.id.province_text_view);
        TextView caseTextView = findViewById(R.id.case_text_view);
        TextView dateTextView = findViewById(R.id.date_text_view);

        //set text and display
        countryTextView.setText(country);

        provinceTextView.setText(provinceName);
        caseTextView.setText(caseNumber);
        dateTextView.setText(date);


    }
}