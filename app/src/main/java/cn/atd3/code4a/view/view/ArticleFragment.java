package cn.atd3.code4a.view.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.database.ArticleDatabase;
import cn.atd3.code4a.model.adapter.ArticleAdapter;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.presenter.ArticleFragmentPresenter;
import cn.atd3.code4a.view.inter.ArticleFragmentInterface;
import cn.atd3.code4a.view.inter.HeadRefreshView;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCCESS;
import static cn.atd3.code4a.Constant.WARNING;

/**
 * Created by harry on 2018/1/14.
 */

@SuppressLint("ValidFragment")
public class ArticleFragment extends Fragment implements ArticleFragmentInterface {

    private int kind = 0;
    private ArticleAdapter aad = null;
    private View view;
    private ListView listView;
    private ArticleFragmentPresenter afp;
    private PullToRefreshLayout pullToRefreshLayout;
    private LinearLayout touchView;

    public void init(int kind) {
        this.kind = kind;
    }

    public static ArticleDatabase articleDatabase;
    private int requestId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            if (articleDatabase == null) {
                articleDatabase = new ArticleDatabase(getContext());
            }
            afp = new ArticleFragmentPresenter(this, articleDatabase);
            view = inflater.inflate(R.layout.fragment_article, null, false);//实例化
            Log.e("kind", "" + kind);

            pullToRefreshLayout = view.findViewById(R.id.pulltorefresh_layout);
            touchView = view.findViewById(R.id.itemShow);
            pullToRefreshLayout.setHeaderView(new HeadRefreshView(getContext()));////加载的视图

            listView = view.findViewById(R.id.list_view);
            afp.setAdapterData(kind);//设置适配器

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ViewArticleActivity.class);
                    afp.setIntentData(intent, i);
                    requestId = i;
                    startActivityForResult(intent, requestId);
                }
            });

            touchView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("touchView", "onTouch");
                            view.findViewById(R.id.showCircle).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.showText).setVisibility(View.GONE);
                            afp.refreshData(kind);
                        }
                    }
            );

            pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
                @Override
                public void refresh() {

                    Log.e("refresh", "...");
                    afp.refreshData(kind);

                }

                @Override
                public void loadMore() {
                    Log.e("load more", "...");
                    afp.loadMoreData(kind);

                }
            });


        }

        ViewGroup parent = (ViewGroup) view.getParent();

        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestId) {
            if (resultCode == 1) {
                afp.removeItem(requestId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void upDate() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        aad.notifyDataSetChanged();
                        showList();
                    }
                }
        );

    }

    @Override
    public void setAdapter(@NotNull final ArrayList<ArticleModel> al) {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        aad = new ArticleAdapter(getContext(), R.layout.articlelist_item, al);
                        aad.setShowCategory(kind == 0);
                        listView.setAdapter(aad);
                    }
                }
        );

    }

    @Override
    public void showToast(final int infotype, final String info) {

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final  QMUITipDialog tipDialog ;
                        switch (infotype) {
                            case SUCCESS:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case INFO:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case NORMAL:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case WARNING:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(info)
                                        .create();
                                break;
                            case ERROR:
                                tipDialog =  new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord(Constant.debugmodeinfo?info:getString(R.string.remote_error))
                                        .create();
                                break;
                            default:
                                tipDialog = new QMUITipDialog.Builder(getActivity())
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_NOTHING)
                                        .setTipWord(info)
                                        .create();
                        }
                        tipDialog.show();
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        finally {
                                            tipDialog.dismiss();
                                        }

                                    }
                                }
                        ).start();
                    }
                }
        );

    }

    @Override
    public void onfinishRefresh() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        // 结束刷新
                        pullToRefreshLayout.finishRefresh();
                    }
                }
        );
    }

    @Override
    public void onfinishLoadmore() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        // 结束
                        pullToRefreshLayout.finishLoadMore();
                    }
                }
        );
    }

    @Override
    public void showTouch() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshLayout.setVisibility(View.GONE);
                        touchView.setVisibility(View.VISIBLE);
                    }
                }
        );

    }

    @Override
    public void showList() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (pullToRefreshLayout != null && touchView != null) {
                            pullToRefreshLayout.setVisibility(View.VISIBLE);
                            touchView.setVisibility(View.GONE);
                        }
                    }
                }
        );

    }
}
