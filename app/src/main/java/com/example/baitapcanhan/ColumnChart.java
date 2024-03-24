package com.example.baitapcanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.baitapcanhan.Database.RoomDB;
import com.example.baitapcanhan.Models.Notes;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ColumnChart extends AppCompatActivity {

    RoomDB database;
    List<Notes> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_chart);

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        // Tạo một đối tượng Intent để lấy dữ liệu được chuyển từ Activity trước đó.
        Intent intent = getIntent();

        // Lấy năm hiện tại
        Calendar calendarYear = Calendar.getInstance();
        int currentYear = calendarYear.get(Calendar.YEAR);


        if (intent != null) {
            // Lấy giá trị từ Intent, mặc định là năm hiện tại
            int selectedYear = intent.getIntExtra("selectedYear", currentYear);

            try {
                int[] months = new int[12];
                for(int i=0; i<12; i++){
                    months[i] = 0;
                }

                for(int i=0; i < notes.size(); i++){
                    String dateString = notes.get(i).getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = sdf.parse(dateString);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Java bắt đầu từ 0

                    if(year == selectedYear){
                        months[month-1]+=1;
                    }
                }

                BarChart barChart = findViewById(R.id.Chart);

                // Ẩn nhãn trục phải của biểu đồ
                barChart.getAxisRight().setDrawLabels(false);

                // Tạo danh sách các dữ liệu cột:
                // Mỗi BarEntry đại diện cho một cột trong biểu đồ cột,
                // với tham số thứ nhất là vị trí trục x (tháng)
                // tham số thứ hai là giá trị y (số công việc hoàn thành).
                ArrayList<BarEntry> entries = new ArrayList<>();
                entries.add(new BarEntry(1, months[0]));  // Tháng 1, 5 công việc hoàn thành
                entries.add(new BarEntry(2, months[1]));  // Tháng 2, 8 công việc hoàn thành
                entries.add(new BarEntry(3, months[2])); // Tháng 3, 12 công việc hoàn thành
                entries.add(new BarEntry(4, months[3])); // Tháng 4, 1 công việc hoàn thành
                entries.add(new BarEntry(5, months[4])); // Tháng 5, 2 công việc hoàn thành
                entries.add(new BarEntry(6, months[5])); // Tháng 6, 14 công việc hoàn thành
                entries.add(new BarEntry(7, months[6])); // Tháng 7, 2 công việc hoàn thành
                entries.add(new BarEntry(8, months[7])); // Tháng 8, 7 công việc hoàn thành
                entries.add(new BarEntry(9, months[8])); // Tháng 9, 8 công việc hoàn thành
                entries.add(new BarEntry(10, months[9])); // Tháng 10, 7 công việc hoàn thành
                entries.add(new BarEntry(11, months[10])); // Tháng 11, 5 công việc hoàn thành
                entries.add(new BarEntry(12, months[11])); // Tháng 12, 10 công việc hoàn thành

                // Cấu hình trục Y bằng cách đặt giá trị tối thiểu, tối đa, màu sắc và số lượng nhãn.
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setAxisMinimum(0);
                yAxis.setAxisMaximum(30);
                yAxis.setAxisLineColor(Color.BLACK);
                yAxis.setLabelCount(20);

                // Tạo đối tượng BarDataSet - đại diện cho tập dữ liệu của các cột.
                BarDataSet dataSet = new BarDataSet(entries, "Công việc hoàn thành");

                // Cấu hình BarDataSet
                // setColors: Đặt màu sắc cho các cột.
                // setValueTextSize: Đặt kích thước của văn bản trên cột.
                // setBarBorderWidth: Đặt độ rộng của đường biên cột.
                // setValueFormatter: Định dạng giá trị hiển thị trên cột.
                //dataSet.setColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.LTGRAY);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueTextSize(15f); // Kích thước của text, có thể điều chỉnh theo mong muốn
                dataSet.setBarBorderWidth(1);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) value); // Chuyển đổi giá trị thành số nguyên
                    }
                });

                // Tạo đối tượng BarData và thiết lập dữ liệu cho biểu đồ:
                BarData barData = new BarData(dataSet);
                barChart.setData(barData);

                // Cấu hình các thuộc tính khác của biểu đồ:
                // getDescription().setEnabled(false): Tắt mô tả của biểu đồ.
                // invalidate(): Làm mới biểu đồ để hiển thị các thay đổi mới.
                //setExtraOffsets: Đặt khoảng cách giữa label và biểu đồ.
                //getXAxis(): Lấy trục X và cấu hình các thuộc tính của nó.
                barChart.getDescription().setEnabled(false);
                // Đặt khoảng cách giữa label và biểu đồ,
                // có thể điều chỉnh giá trị theo mong muốn
                barChart.setExtraOffsets(0, 0, 0, 20f);
                //barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(values));
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setGranularityEnabled(true);

                // Làm mới biểu đồ để hiển thị các thay đổi mới:
                barChart.invalidate();

                Toast.makeText(this, "Thống kê công việc năm : " + selectedYear, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace(); // In thông báo lỗi hoặc xử lý ngoại lệ theo nhu cầu của bạn
            }
        }
    }
}