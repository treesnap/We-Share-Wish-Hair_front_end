package com.example.wishhair.MyPage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishhair.MyPage.PointHistory;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {
    private ArrayList<PointHistory> pointItems = new ArrayList<PointHistory>();
    final static private String point_url = UrlConst.URL + "/api/my_page";
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView PointlistTitle, PointlistNum, PointlistDate;

        ViewHolder(View view) {
            super(view);

            PointlistTitle = view.findViewById(R.id.point_title);
            PointlistNum = view.findViewById(R.id.point_num);
            PointlistDate = view.findViewById(R.id.point_date);
        }

        public void setItem(PointHistory item) {

//            if(item.getPointType().equals("CHARGE")) {
//                PointlistNum.setText("+" + Integer.toString(item.getDealAmount()));
//                PointlistNum.setTextColor(Color.RED);
//            }
//            else {
//                PointlistNum.setText("-" + Integer.toString(item.getDealAmount()));
//                PointlistNum.setTextColor(Color.BLUE);
//            }
//            PointlistDate.setText(item.getDealDate().toString());
        }
    }

    @NonNull
    @Override
    public PointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.point_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PointHistory item = pointItems.get(position);
        if (item.getPointType() == "충전") {
            holder.PointlistNum.setText("+" + item.getDealAmount());
            holder.PointlistNum.setTextColor(Color.BLUE);
        } else {
            holder.PointlistNum.setText("-" + item.getDealAmount());
            holder.PointlistNum.setTextColor(Color.RED);
        }

        holder.PointlistDate.setText(parseDate(item.getDealDate()));
    }

    @Override
    public int getItemCount() {
        return pointItems.size();
    }

    public void addItem(PointHistory e) {
        pointItems.add(e);
    }
    public void setItems(ArrayList<PointHistory> items) {
        this.pointItems = items;
    }
    private String parseDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "failParseDate";
    }

}
