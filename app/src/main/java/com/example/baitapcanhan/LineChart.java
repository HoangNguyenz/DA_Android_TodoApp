package com.example.baitapcanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.baitapcanhan.Database.RoomDB;
import com.example.baitapcanhan.Models.Notes;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LineChart extends AppCompatActivity {

    RoomDB database;
    List<Notes> notes = new ArrayList<>();
    private com.github.mikephil.charting.charts.LineChart lineChart;

    private List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

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

                lineChart = findViewById(R.id.LineChart);

                // Cấu hình mô tả (Description) của biểu đồ
                Description description = new Description();
                description.setText("Ngày");
                description.setPosition(150f, 15f);
                lineChart.setDescription(description);
                // Ẩn nhãn trục phải của biểu đồ
                lineChart.getAxisRight().setDrawLabels(false);
                //Đặt khoảng cách giữa label và biểu đồ
                lineChart.setExtraOffsets(0, 0, 0, 20f);

                // Cấu hình trục X
                // values: Danh sách các giá trị của trục X (tháng).
                // setPosition: Đặt vị trí của trục X.
                // setValueFormatter: Định dạng giá trị trục X với các giá trị từ danh sách values.
                // setLabelCount: Đặt số lượng nhãn trên trục X.
                // setGranularity: Đặt độ chia nhỏ nhất trên trục X.
                values = Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12");
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(values));
                xAxis.setLabelCount(12);
                xAxis.setGranularity(1f);

                // Cấu hình trục Y
                // setAxisMinimum và setAxisMaximum: Đặt giá trị tối thiểu và tối đa của trục Y.
                // setAxisLineWidth và setAxisLineColor: Đặt độ rộng và màu sắc của đường trục Y.
                // setLabelCount: Đặt số lượng nhãn trên trục Y.
                YAxis yAxis = lineChart.getAxisLeft();
                yAxis.setAxisMinimum(0f);
                yAxis.setAxisMaximum(30f);
                yAxis.setAxisLineWidth(1f);
                yAxis.setAxisLineColor(Color.BLACK);
                yAxis.setLabelCount(20);

                // Tạo danh sách các điểm dữ liệu (Entries) cho biểu đồ đường
                // Mỗi Entry đại diện cho một điểm trên biểu đồ đường
                // với tham số thứ nhất là vị trí trục x (tháng)
                // tham số thứ hai là giá trị y (số công việc hoàn thành).
                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(0, months[0]));  // Tháng 1, 5 công việc hoàn thành
                entries.add(new Entry(1, months[1]));  // Tháng 2, 8 công việc hoàn thành
                entries.add(new Entry(2, months[2])); // Tháng 3, 12 công việc hoàn thành
                entries.add(new Entry(3, months[3])); // Tháng 4, 1 công việc hoàn thành
                entries.add(new Entry(4, months[4])); // Tháng 5, 2 công việc hoàn thành
                entries.add(new Entry(5, months[5])); // Tháng 6, 14 công việc hoàn thành
                entries.add(new Entry(6, months[6])); // Tháng 7, 2 công việc hoàn thành
                entries.add(new Entry(7, months[7])); // Tháng 8, 7 công việc hoàn thành
                entries.add(new Entry(8, months[8])); // Tháng 9, 8 công việc hoàn thành
                entries.add(new Entry(9, months[9])); // Tháng 10, 7 công việc hoàn thành
                entries.add(new Entry(10, months[10])); // Tháng 11, 5 công việc hoàn thành
                entries.add(new Entry(11, months[11])); // Tháng 12, 10 công việc hoàn thành

                //Tạo đối tượng LineDataSet - LineDataSet đại diện cho tập dữ liệu của đường
                LineDataSet dataSet = new LineDataSet(entries, "Công việc hoàn thành");
                // Cấu hình LineDataSet
                // setColor: Đặt màu sắc cho đường.
                //setValueTextSize: Đặt kích thước của văn bản trên đường.
                //setLineWidth: Đặt độ rộng của đường.
                //setValueFormatter: Định dạng giá trị hiển thị trên đường.
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextSize(20f); // Kích thước của text, có thể điều chỉnh theo mong muốn
                dataSet.setLineWidth(1);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) value); // Chuyển đổi giá trị thành số nguyên
                    }
                });

                // Tạo đối tượng LineData và thiết lập dữ liệu cho biểu đồ
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);

                // Làm mới biểu đồ để hiển thị các thay đổi mới:
                lineChart.invalidate();

                Toast.makeText(this, "Thống kê công việc năm : " + selectedYear, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace(); // In thông báo lỗi hoặc xử lý ngoại lệ theo nhu cầu của bạn
            }
        }
    }
}