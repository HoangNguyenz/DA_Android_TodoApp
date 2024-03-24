package com.example.baitapcanhan.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcanhan.Models.Notes;
import com.example.baitapcanhan.NotesClickListener;
import com.example.baitapcanhan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.widget.Toast;




//sử dụng để hiển thị danh sách các tasks trong một RecyclerView.


public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notes> list; //Danh sách các ghi chú cần hiển thị.
    NotesClickListener listener; //Interface để xử lý sự kiện khi người dùng tương tác với các ghi chú.

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));

    }




    //Phương thức này được gọi khi RecyclerView cần hiển thị dữ liệu cho một item tại một vị trí cụ thể.
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNotes());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);


        // 01/01/2024 01:25
        String noteDate = list.get(position).getDate();

        // Lấy ngày giờ hiện tại theo định dạng "MM/dd/yyyy HH:mm"
        String currentTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // Kiểm tra trùng khớp giờ và ngày
        if (noteDate.equals(currentTime)) {
            String titleText = list.get(position).getTitle();
            // Hiển thị thông báo hoặc thực hiện hành động cần thiết
            showNotification("Lời nhắc: " + titleText);
            Toast.makeText(context, "Lời nhắc: " + titleText, Toast.LENGTH_SHORT).show();

            // Gạch ngang tiêu đề
            holder.textView_title.setPaintFlags(holder.textView_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // Nếu không trùng khớp, làm mới lại kiểu chữ (không gạch ngang)
            holder.textView_title.setPaintFlags(holder.textView_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            // Kiểm tra nếu thời gian đã qua so với thời gian hiện tại
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
                Date currentDate = sdf.parse(currentTime);
                Date noteDateTime = sdf.parse(noteDate);

                if (noteDateTime != null && noteDateTime.before(currentDate)) {
                    // Gạch ngang tiêu đề nếu thời gian đã qua
                    holder.textView_title.setPaintFlags(holder.textView_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    // Đổi màu sắc của tiêu đề thành màu đỏ
                    holder.textView_title.setTextColor(Color.RED);
                } else {
                    // Nếu không trùng khớp và thời gian chưa qua, làm mới lại kiểu chữ (không gạch ngang)
                    holder.textView_title.setPaintFlags(holder.textView_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    // Đặt màu sắc về mặc định
                    holder.textView_title.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //nếu task này có pin thì
        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.ic_pin);
        }
        else {
            holder.imageView_pin.setImageResource(0);
        }

        // Tạo một mã màu ngẫu nhiên từ danh sách mã màu được định trước.
        int color_code = getRandomColor();

        //Đặt màu nền của notes_container thành màu được tạo ra ngẫu nhiên.
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });


    }

    private void showNotification(String message) {
        // Tạo NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra nếu đang chạy trên phiên bản Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo NotificationChannel cho Android 8.0 trở lên
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo Notification
        Notification notification = new Notification.Builder(context, "channel_id")
                .setContentTitle("Your App Name")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_pin)
                .setAutoCancel(true)
                .build();

        // Hiển thị thông báo
        notificationManager.notify(0, notification);
    }

    //random color for each notes
    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void filterList(List<Notes> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}


class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textView_title, textView_notes, textView_date;
    ImageView imageView_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}
