package com.example.baitapcanhan.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baitapcanhan.Models.Notes;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "NoteApp";


    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            //tạo csdl
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries() //thực hiện truy vấn trực tiếp trên main thread
                    .fallbackToDestructiveMigration() //hiết lập chiến lược cập nhật cơ sở dữ liệu khi có thay đổi cấu trúc.
                    .build();
        }
        return database;
    }

    public abstract MainDAO mainDAO();
}
