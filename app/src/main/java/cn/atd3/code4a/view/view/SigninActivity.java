package cn.atd3.code4a.view.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindView;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.SignModel;
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
    private static final String TAG = "SigninActivity";
    @BindView(R.id.account)
    BootstrapEditText account;
    @BindView(R.id.password)
    BootstrapEditText password;
    @BindView(R.id.signin_button)
    BootstrapButton signinButton;
    @BindView(R.id.signup_button)
    BootstrapButton signupButton;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);//沉浸式状态栏
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                mPresenter.signinButtonClick(account.getText().toString(), password.getText().toString());
            }
        });

        final Intent i = new Intent(this, SignupActivity.class);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });
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
    }

    @Override
    public void remoteError(String message) {
        closeProgressDialog();

    }

    @Override
    public void signinSuccessful() {
        closeProgressDialog();

        finish();
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void showErrorWithStatus(String msg) {

    }
    /**   * 显示进度对话框   */
    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.signing));
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**   * 关闭进度对话框   */
    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
