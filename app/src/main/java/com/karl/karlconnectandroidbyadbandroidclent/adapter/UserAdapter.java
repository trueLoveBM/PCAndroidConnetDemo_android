package com.karl.karlconnectandroidbyadbandroidclent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karl.karlconnectandroidbyadbandroidclent.R;
import com.karl.karlconnectandroidbyadbandroidclent.dbmodel.UserModel;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private List<UserModel> userModels;

    public UserAdapter(List<UserModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public UserModel getItem(int position) {
        return userModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userModels.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserModel user = userModels.get(position);
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
            holder=new ViewHolder();
            holder.tv_email=convertView.findViewById(R.id.tv_email);
            holder.tv_userName=convertView.findViewById(R.id.tv_userName);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();

        }

        holder.tv_userName.setText(user.getUsername());
        holder.tv_email.setText(user.getEmail());

        return convertView;
    }


    static class ViewHolder{
        TextView tv_userName;
        TextView tv_email;
    }
}
