package cn.qingyuyu.code4droid.library.fileselect.adapter.file;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingyuyu.code4droid.R;
import cn.qingyuyu.code4droid.library.fileselect.event.ViewEvent;
import cn.qingyuyu.code4droid.library.fileselect.model.domain.HeaderData;
import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by liwei on 2017/4/28.
 */

public class FileHeaderBinder extends ItemViewBinder<HeaderData,FileHeaderBinder.HeaderHolder> {

    private Context context;

    public FileHeaderBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected HeaderHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.list_header,parent,false);
        return new HeaderHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull HeaderHolder holder, @NonNull HeaderData item) {
        holder.headerData = item;
        holder.name.setText(item.getPath());
    }

    static class HeaderHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.fileName_header)
        TextView name;
        @BindView(R.id.itemView_header)
        RelativeLayout itemView;

        HeaderData headerData;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ViewEvent.Keys.BACK_PATH_HEADER,headerData);
                    EventBus.getDefault().post(new ViewEvent(ViewEvent.EvenType.backPath,bundle));
                }
            });
        }
    }
}
