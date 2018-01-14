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
import android.widget.ListView;
import android.widget.Toast;

import com.hitomi.refresh.view.FunGameRefreshView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.atd3.code4a.R;
import cn.atd3.code4a.model.adapter.ArticleAdapter;
import cn.atd3.code4a.model.model.ArticleModel;
import cn.atd3.code4a.presenter.ArticleFragmentPresenter;
import cn.atd3.code4a.view.inter.ArticleFragmentInterface;
import es.dmoral.toasty.Toasty;

import static cn.atd3.code4a.Constant.ERROR;
import static cn.atd3.code4a.Constant.INFO;
import static cn.atd3.code4a.Constant.NORMAL;
import static cn.atd3.code4a.Constant.SUCESS;
import static cn.atd3.code4a.Constant.WARNING;

/**
 * Created by harry on 2018/1/14.
 */

@SuppressLint("ValidFragment")
public class ArticleFragment extends Fragment implements ArticleFragmentInterface {

    private int kind=0;
    private ArticleAdapter aad=null;

    private ListView listView;
    private ArticleFragmentPresenter afp;

    public ArticleFragment(int kind){
        this.kind=kind;
        afp=new ArticleFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, null, false);//实例化
        Log.e("kind", "" + kind);

        FunGameRefreshView refreshView = view.findViewById(R.id.refresh);
        refreshView.setLoadingText(getString(R.string.info_loadingtext));
        refreshView.setGameOverText(getString(R.string.info_gaveover));
        refreshView.setLoadingFinishedText(getString(R.string.info_loadingfinish));
        refreshView.setTopMaskText(getString(R.string.info_pulltorefresh));
        refreshView.setBottomMaskText(getString(R.string.info_howtogame));
         listView = view.findViewById(R.id.list_view);
        afp.setAdapterData(kind);//设置适配器
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent();
                //intent.setClass(getActivity(), ViewArticleActivity.class);
                afp.setIntentData(intent);
               // startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new FunGameRefreshView.FunGameRefreshListener() {
            @Override
            public void onPullRefreshing() {
                    afp.requestData(kind);
            }

            @Override
            public void onRefreshComplete() {
                    afp.update();
            }

        });

        return view;
    }


    @Override
    public void upDate(ArrayList<ArticleModel> al) {

                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                aad.notifyDataSetChanged();
                                Toasty.success(getContext(), getString(R.string.info_loadingfinish), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    @Override
    public void setAdapter(@NotNull ArrayList<ArticleModel> al) {
            aad=new ArticleAdapter(getContext(),R.layout.articlelist_item,al);
            listView.setAdapter(aad);
    }
    @Override
    public void showToast(final int infotype, final String info) {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (infotype)
                        {
                            case SUCESS:
                                Toasty.success(getContext(),info, Toast.LENGTH_SHORT).show();
                                break;
                            case INFO:Toasty.info(getContext(),info, Toast.LENGTH_SHORT).show();
                                break;
                            case NORMAL:Toasty.normal(getContext(),info, Toast.LENGTH_SHORT).show();
                                break;
                            case WARNING:Toasty.warning(getContext(),info, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:Toasty.error(getContext(),info, Toast.LENGTH_SHORT).show();
                                break;
                            default:

                        }
                    }
                }
        );

    }
}
