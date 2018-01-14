package cn.dxkite.common.crashhandler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *  异常显示列表
 * Created by DXkite on 2018/1/14 0014.
 */

public class ExceptionView extends BaseExpandableListAdapter {

    List<Throwable> throwables;
    LayoutInflater inflater;
    public ExceptionView(Throwable throwable,LayoutInflater layoutInflater){
        throwables=new ArrayList<Throwable>();
        inflater=layoutInflater;
        for(Throwable tmp=throwable;tmp!=null;tmp=tmp.getCause()){
            throwables.add(tmp);
        }
    }

    @Override
    public int getGroupCount() {
        return throwables.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return throwables.get(i).getStackTrace().length;
    }

    @Override
    public Object getGroup(int i) {
        return throwables.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return throwables.get(i).getStackTrace()[i];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_exception_name, null);
        }
        view.setTag(R.layout.list_exception_name, i);
        view.setTag(R.layout.list_exception_trace, -1);
        TextView name = (TextView) view.findViewById(R.id.exception_name);
        name.setText(throwables.get(i).getClass().getName());
        TextView message = (TextView) view.findViewById(R.id.exception_message);
        message.setText(throwables.get(i).getMessage());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_exception_trace, null);
        }
        view.setTag(R.layout.list_exception_name, i);
        view.setTag(R.layout.list_exception_trace, i1);
        TextView message = (TextView) view.findViewById(R.id.exception_info);
        message.setText(throwables.get(i).getStackTrace()[i].toString());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
