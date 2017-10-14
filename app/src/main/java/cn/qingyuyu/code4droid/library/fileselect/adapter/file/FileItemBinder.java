package cn.qingyuyu.code4droid.library.fileselect.adapter.file;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingyuyu.code4droid.R;
import cn.qingyuyu.code4droid.library.fileselect.FileList;
import cn.qingyuyu.code4droid.library.fileselect.event.ViewEvent;
import cn.qingyuyu.code4droid.library.fileselect.model.domain.FileData;
import es.dmoral.toasty.Toasty;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by liwei on 2017/4/27.
 */

public class FileItemBinder extends ItemViewBinder<FileData, FileItemBinder.FileHolder> {
    private Context context;

    public FileItemBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected FileHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.list_item, parent, false);
        return new FileHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull FileHolder holder, @NonNull FileData item) {
        holder.fileData = item;
        holder.name.setText(item.getName());
        holder.path.setText(item.getPath());
        if (item.isFolder()) {
            holder.thumb.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_folder_main));
        } else {
            holder.thumb.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_txt_icon));
        }
    }

    class FileHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fileName)
        TextView name;
        @BindView(R.id.fileInfo)
        TextView path;
        @BindView(R.id.itemView_file)
        RelativeLayout itemView;
        @BindView(R.id.item_thumb)
        ImageView thumb;

        FileData fileData;

        FileHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileData.isFolder()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ViewEvent.Keys.GOTO_PATH, fileData);
                        EventBus.getDefault().post(
                                new ViewEvent(ViewEvent.EvenType.gotoFileClickPosition, bundle));
                    } else {
                        String tip = context.getResources().getString(R.string.click_file_tip);
                        tip = String.format(tip, fileData.getName());
                        Toasty.success(context, tip, Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (fileData.isFolder()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ViewEvent.Keys.GOTO_PATH, fileData);
                        EventBus.getDefault().post(
                                new ViewEvent(ViewEvent.EvenType.gotoFileClickPosition, bundle));
                    } else {
                        Toasty.success(context, "你选中了" + fileData.getName(), Toast.LENGTH_SHORT, true).show();
                        FileList.fileList.add(fileData.getPath());
                    }

                    return true;
                }
            });
        }
    }

}
