package com.example.baitapcanhan.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.baitapcanhan.Models.Completions;
import com.example.baitapcanhan.Models.Notes;

import java.util.List;



//DAO class thực hiện việc queries đến SQLite


@Dao
public interface MainDAO {
    //insert data to database
    //onConflict: Nếu đã tồn tại một bản ghi có cùng khóa chính (primary key), nó sẽ được thay thế bằng đối tượng mới
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);


    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();


    @Query("UPDATE notes SET title = :title, notes = :notes, date = :date WHERE ID = :id")
    void update(int id, String title, String notes, String date);

    @Delete
    void delete(Notes notes);


    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);

    //@Insert
    //void insertComlpetion(Completions completion);

    //@Query("SELECT * FROM completions ORDER BY id DESC")
    //List<Completions> getCompletions();

}
