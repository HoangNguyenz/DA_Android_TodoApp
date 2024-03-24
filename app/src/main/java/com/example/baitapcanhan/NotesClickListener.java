package com.example.baitapcanhan;

import androidx.cardview.widget.CardView;

import com.example.baitapcanhan.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);

}
