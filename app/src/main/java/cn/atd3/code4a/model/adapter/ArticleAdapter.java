package cn.atd3.code4a.model.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;

/**
 * Created by harry on 2018/1/14.
 */

public class ArticleAdapter extends ArrayAdapter<ArticleModel> {

    private int resourceId;
    private boolean showCategory = false;

    public void setShowCategory(boolean showCategory) {
        this.showCategory = showCategory;
    }

    public ArticleAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, List<ArticleModel> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleModel a = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView user = view.findViewById(R.id.itemUser);
        TextView modify = view.findViewById(R.id.itemModify);
        ImageView avatar = view.findViewById(R.id.itemAvatar);

        // 用户名
        user.setText("UserID" + a.getUser().toString());
        modify.setText(timeString(a.getModify()));

        TextView itemTitle = view.findViewById(R.id.itemTitle);
        TextView itemAbstract = view.findViewById(R.id.itemAbstract);

        itemTitle.setText(a.getTitle());
        itemAbstract.setText(a.getAbstract());


        TextView views = view.findViewById(R.id.itemViews);
        views.setText(String.valueOf(a.getViews()));

        TextView category = view.findViewById(R.id.itemCategory);
        if (showCategory) {
            category.setText(String.valueOf(a.getCategory()));
        } else {
            category.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private String timeString(int unix) {
        long time=Long.valueOf(unix) * 1000;
        String showTime=null;
        String hTime=new SimpleDateFormat("H:m").format(time);
        String rTime=new SimpleDateFormat("M-d H:m").format(time);
        long passTime=(System.currentTimeMillis() - time)/1000;
        if (passTime < 60){
            showTime="刚刚";
        }else if (passTime < 60*60 ){
            showTime = Math.floor(passTime/60)+"分钟前";
        }else if (passTime < 60*60*24){
            showTime= Math.floor(passTime/(60*60)) + "小时前 "+hTime;
        }else if (passTime < 60*60*24*3) {
            double day=Math.floor(passTime/(60*60*24));
            if (day == 1) {
                showTime = "昨天 "+hTime;
            }else{
                showTime = "前天 "+hTime;
            }
        }else{
            showTime =rTime;
        }
        return showTime;
    }
}