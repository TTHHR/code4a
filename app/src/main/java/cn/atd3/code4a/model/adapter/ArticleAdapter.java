package cn.atd3.code4a.model.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.ArticleModel;

/**
 * Created by harry on 2018/1/14.
 */

public class ArticleAdapter extends ArrayAdapter<ArticleModel> {

    private int resourceId;

    public ArticleAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, List<ArticleModel> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleModel a = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView articleTitle = view.findViewById(R.id.articletitle);
        articleTitle.setText(a.getTitle());
        TextView articleAbstract = view.findViewById(R.id.articleabstract);
        articleAbstract.setText(a.getAbstract());
        TextView articleUser = view.findViewById(R.id.articleuser);
        articleUser.setText(a.getUser().toString());
        TextView articleModify = view.findViewById(R.id.articlemodify);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long lt = new Long(a.getModify());
        Date date = new Date(lt);
        articleModify.setText(simpleDateFormat.format(date));
        return  view;
    }
}