package cn.atd3.code4a.view.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.SigninUserManager;
import cn.atd3.code4a.model.SignModel;
import cn.atd3.code4a.model.model.User;
import cn.atd3.code4a.mvpbase.BaseActivity;
import cn.atd3.code4a.mvpbase.BaseView;
import cn.atd3.code4a.presenter.SigninPresenter;
import cn.atd3.code4a.presenter.interfaces.SigninContract;


/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：主要负责处理UI逻辑
 * 备注消息：
 * 创建时间：2018/01/10   20:18
 **/
public class SigninActivity extends BaseActivity<SignModel, SigninPresenter> implements SigninContract.View {
    @BindView(R.id.account)
    BootstrapEditText account;
    @BindView(R.id.password)
    BootstrapEditText password;
    @BindView(R.id.signin_button)
    BootstrapButton signinButton;
    @BindView(R.id.signup_button)
    BootstrapButton signupButton;
    @BindView(R.id.code)
    BootstrapEditText code;
    @BindView(R.id.code_image)
    ImageView codeImage;
    @BindView(R.id.code_layout)
    LinearLayout codeLayout;
    private ProgressDialog progressDialog;


    @Override
    public void initView(Bundle s) {
        //QMUIStatusBarHelper.translucent(this);//沉浸式状态栏

        refreshCodeImg();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCodeImg();
    }

    @OnClick(R.id.code_image)
    public void refreshCodeImg() {
        mPresenter.updateCode();
    }

    @OnClick(R.id.signin_button)
    public void signin() {
        showProgressDialog();
        mPresenter.signinButtonClick(account.getText().toString(), password.getText().toString(), code.getText().toString());
    }

    @OnClick(R.id.signup_button)
    public void signup() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.reset_password)
    public void resetPassword() {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", Constant.resetPassword);
        startActivity(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    public void accountError(String message) {
        closeProgressDialog();
        account.setError(message);
    }

    @Override
    public void passwordError(String message) {
        closeProgressDialog();
        password.setError(message);
    }

    @Override
    public void codeError(String message) {
        closeProgressDialog();
        code.setError(message);
    }

    @Override
    public void signinSuccessful(User user) {
        closeProgressDialog();
        SigninUserManager.setUser(this, user);
        Toast.makeText(this, getString(R.string.signin_successful), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void showErrorWithStatus(String msg) {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.error) + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCodeImg(File file) {
        Uri i = Uri.fromFile(file);
        codeImage.setImageURI(i);
    }

    /**
     * 显示进度对话框
     */
    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.signing));
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
