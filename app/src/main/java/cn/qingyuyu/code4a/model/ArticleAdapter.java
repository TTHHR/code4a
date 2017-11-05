package cn.qingyuyu.code4a.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.qingyuyu.code4a.R;
import cn.qingyuyu.code4a.remote.bean.Article;

/**
 * Created by harrytit on 2017/11/5.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    private int resourceId;

    public ArticleAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, List<Article> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Article a = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView articleTitle = view.findViewById(R.id.articletitle);
        articleTitle.setText(a.getTitle());
        TextView articleAbstract = view.findViewById(R.id.articleabstract);
        articleAbstract.setText(a.getAbstract());
        TextView articleUser = view.findViewById(R.id.articleuser);
        articleUser.setText(a.getUser().toString());
        TextView articleModify = view.findViewById(R.id.articlemodify);
        articleModify.setText(a.getModify().toString());
        return  view;
    }
}