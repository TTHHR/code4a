package cn.atd3.code4a.model.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.scrat.app.richtext.glide.GlideApp;
import java.util.List;
import cn.atd3.code4a.Constant;
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
        QMUIRadiusImageView avatar = view.findViewById(R.id.itemAvatar);

        // 用户名
        if (a.getUser() != null)
            user.setText(a.getUser().getName());
        modify.setText(ArticleModel.time(getContext(), a.getModify()));
        // 设置头像缓存
        GlideApp.with(getContext())
                .load(Constant.avatar + a.getUser().getId())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar);

        TextView itemTitle = view.findViewById(R.id.itemTitle);
        TextView itemAbstract = view.findViewById(R.id.itemAbstract);

        itemTitle.setText(a.getTitle());
        itemAbstract.setText(a.getAbstract());


        TextView views = view.findViewById(R.id.itemViews);
        views.setText(String.valueOf(a.getViews()));

        TextView category = view.findViewById(R.id.itemCategory);
        if (showCategory) {
            category.setText(ArticleModel.category(a.getCategory().getId()));
        } else {
            category.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}