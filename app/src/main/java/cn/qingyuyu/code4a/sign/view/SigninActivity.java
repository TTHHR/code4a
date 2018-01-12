package cn.qingyuyu.code4a.sign.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.BindView;
import cn.qingyuyu.code4a.R;
import cn.qingyuyu.code4a.sign.mvpbase.BaseActivity;
import cn.qingyuyu.code4a.sign.mvpbase.BaseView;
import cn.qingyuyu.code4a.sign.model.SigninModel;
import cn.qingyuyu.code4a.sign.presenter.impl.SigninPresenter;
import cn.qingyuyu.code4a.sign.presenter.interfaces.SigninContract;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：主要负责处理UI逻辑
 * 备注消息：
 * 创建时间：2018/01/10   20:18
 **/
public class SigninActivity extends BaseActivity<SigninModel,SigninPresenter> implements SigninContract.View{
    private static final String TAG = "SigninActivity";
    @BindView(R.id.account)
    BootstrapEditText account;
    @BindView(R.id.password)
    BootstrapEditText password;
    @BindView(R.id.login_in_button)
    BootstrapButton signinButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.signinButtonClick(account.getText().toString(),password.getText().toString());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    public void accountError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void passwordError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void remoteError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signinSuccessful() {
        Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void showErrorWithStatus(String msg) {

    }
}
