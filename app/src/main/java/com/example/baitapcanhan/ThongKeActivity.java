package com.example.baitapcanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.baitapcanhan.ColumnChart;
import com.example.baitapcanhan.LineChart;
import com.example.baitapcanhan.R;

public class ThongKeActivity extends AppCompatActivity {

    Button btnColChart, btnLineChart;
    NumberPicker yearPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        btnColChart = findViewById(R.id.btnColChart);
        btnLineChart = findViewById(R.id.btnLineChart);
        yearPicker = findViewById(R.id.yearPicker);

        // Thiết lập giá trị tối thiểu và tối đa cho NumberPicker
        yearPicker.setMinValue(2020);
        yearPicker.setMaxValue(2100);

        btnColChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongKeActivity.this, ColumnChart.class);

                // Lấy giá trị được chọn từ yearPicker
                int selectedYear = yearPicker.getValue();

                // Đặt giá trị vào Intent với một key
                intent.putExtra("selectedYear", selectedYear);

                startActivity(intent);
            }
        });

        btnLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongKeActivity.this, LineChart.class);

                // Lấy giá trị được chọn từ yearPicker
                int selectedYear = yearPicker.getValue();

                // Đặt giá trị vào Intent với một key
                intent.putExtra("selectedYear", selectedYear);

                startActivity(intent);
            }
        });

    }
}