package com.example.baitapcanhan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.baitapcanhan.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_notes;
    Button btnSelectDateAndTime;
    TextView textView_selectedDateTime;
    Notes notes;
    boolean isOldNote = false;

    private Calendar calendar;
    private ImageView imageView_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        // Initialize views
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        btnSelectDateAndTime = findViewById(R.id.btnSelectDateAndTime);
        textView_selectedDateTime = findViewById(R.id.textView_selectedDateTime);
        imageView_save = findViewById(R.id.imageView_save);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Check if it's an old note
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set onClickListener for the date and time selection button
        btnSelectDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        // Set onClickListener for the save button
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
    }

    private void saveNotes() {
        String title = editText_title.getText().toString();
        String description = editText_notes.getText().toString();
        String date = textView_selectedDateTime.getText().toString();

        if (description.isEmpty()) {
            Toast.makeText(NotesTakerActivity.this, "Please add some tasks!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isOldNote) {
            notes = new Notes();
        }

        notes.setTitle(title);
        notes.setNotes(description);
        notes.setDate(date);

        Intent intent = new Intent();
        intent.putExtra("notes", notes);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    // Method to show the Date and Time Picker
    private void showDateTimePicker() {
        // Show DatePicker first
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // After setting the date, show TimePicker
                        showTimePicker();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Method to show the Time Picker
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Update the selected date and time in the TextView
                        updateSelectedDateTime();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    // Method to update the selected date and time in the TextView
    private void updateSelectedDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());

        textView_selectedDateTime.setText(formattedDate);
    }
}
