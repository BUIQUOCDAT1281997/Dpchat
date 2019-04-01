package com.example.dpchat_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DataUsersAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private List<DataUsers> listData;

    public DataUsersAdapter(Context context, int layout, List<DataUsers> listData) {
        this.context = context;
        this.layout = layout;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        //anh xa
        TextView thuocTinhDataUsers = convertView.findViewById(R.id.setting_item_thuoctinh);
        TextView descripsion = convertView.findViewById(R.id.setting_item_descripsion);

        //set gia tri
        thuocTinhDataUsers.setText(listData.get(position).getThuocTinh());
        descripsion.setText(listData.get(position).getDescripsion());
        return convertView;
    }
}
