package cn.qingyuyu.code4a.library.fileselect.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingyuyu.code4a.R;
import cn.qingyuyu.code4a.library.fileselect.tool.SwipeRefreshDelegate;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by liwei on 2017/4/28.
 */

public abstract class ListBaseFragment extends Fragment
        implements SwipeRefreshDelegate.OnSwipeRefreshListener {

    private static final String TAG = ListBaseFragment.class.getSimpleName();

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private SwipeRefreshDelegate refreshDelegate;

    protected MultiTypeAdapter adapter;
    protected Items items;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        refreshDelegate = new SwipeRefreshDelegate(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        refreshDelegate.attach(root);
        initData();
        return root;
    }

    protected abstract void initData();


    protected abstract void loadData(boolean clear);


    protected void setRefresh(boolean refresh) {
        refreshDelegate.setRefresh(refresh);
    }


    @Override public void onSwipeRefresh() {
        loadData(true);
    }

    protected boolean isShowingRefresh() {
        return refreshDelegate.isShowingRefresh();
    }


    protected void setSwipeToRefreshEnabled(boolean enable) {
        refreshDelegate.setEnabled(enable);
    }


    public void smoothScrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

}
