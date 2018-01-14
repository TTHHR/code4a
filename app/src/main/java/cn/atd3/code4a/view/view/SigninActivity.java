package cn.atd3.code4a.view.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.BindView;
import cn.atd3.code4a.R;
import cn.atd3.code4a.model.model.SignModel;
import cn.atd3.code4a.mvpbase.BaseActivity;
import cn.atd3.code4a.mvpbase.BaseView;
import cn.atd3.code4a.presenter.SigninPresenter;
import cn.atd3.code4a.presenter.interfaces.SigninContract;
import es.dmoral.toasty.Toasty;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：主要负责处理UI逻辑
 * 备注消息：
 * 创建时间：2018/01/10   20:18
 **/
public class SigninActivity extends BaseActivity<SignModel,SigninPresenter> implements SigninContract.View{
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                mPresenter.signinButtonClick(account.getText().toString(),password.getText().toString());
            }
        });

        final Intent i=new Intent(this,SignupActivity.class);
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
        //我知道要显示的字符应该放在R.string,下次改哈
        account.setError("用户名错误:"+message);
    }

    @Override
    public void passwordError(String message) {
        closeProgressDialog();
        password.setError("密码错误:"+message);
    }

    @Override
    public void remoteError(String message) {
        closeProgressDialog();
        Toasty.error(this,"出现错误:"+message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signinSuccessful() {
        closeProgressDialog();
        Toasty.success(this, "登陆成功", Toast.LENGTH_SHORT).show();
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
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登陆中");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**   * 关闭进度对话框   */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
