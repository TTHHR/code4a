package cn.atd3.code4a.view.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.OnClick;
import cn.atd3.code4a.R;
import butterknife.BindView;
import cn.atd3.code4a.model.SignModel;
import cn.atd3.code4a.mvpbase.BaseActivity;
import cn.atd3.code4a.mvpbase.BaseView;
import cn.atd3.code4a.presenter.SignupPresenter;
import cn.atd3.code4a.presenter.interfaces.SignupContract;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   9:37
 **/
public class SignupActivity extends BaseActivity<SignModel, SignupPresenter> implements SignupContract.View {

    @BindView(R.id.user_name)
    BootstrapEditText userName;
    @BindView(R.id.email)
    BootstrapEditText email;
    @BindView(R.id.password)
    BootstrapEditText password;
    @BindView(R.id.password_confirm)
    BootstrapEditText passwordConfirm;
    @BindView(R.id.signup_button)
    BootstrapButton signupButton;
    @BindView(R.id.code)
    BootstrapEditText code;
    @BindView(R.id.code_image)
    ImageView codeImage;
    @BindView(R.id.code_layout)
    LinearLayout codeLayout;
    private ProgressDialog progressDialog;
    private ImageLoader imageLoader;
    private static final String codeUrl="http://code4a.atd3.cn/user/verify";

    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);
        //QMUIStatusBarHelper.translucent(this);//沉浸式状态栏

        imageLoader= ImageLoader.getInstance();
        refreshCodeImg();
    }

    @OnClick(R.id.code_image)
    public void refreshCodeImg(){
        imageLoader.displayImage(codeUrl,codeImage);
    }

    @OnClick(R.id.signup_button)
    public void onSignup(){
        showProgressDialog();
        mPresenter.signupButtonClick(userName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                passwordConfirm.getText().toString(),
                code.getText().toString());
    }

    @Override
    public void showErrorWithStatus(String msg) {
        closeProgressDialog();
        Toast.makeText(this,getString(R.string.error)+msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userNameError(String message) {
        closeProgressDialog();
        userName.setError("用户名错误:" + message);
    }

    @Override
    public void emailError(String message) {
        closeProgressDialog();
        email.setError("邮箱错误:" + message);
    }

    @Override
    public void passwordError(String message) {
        closeProgressDialog();
        password.setError("密码错误:" + message);
    }

    @Override
    public void codeError(String message) {
        closeProgressDialog();
        code.setError(message);
        refreshCodeImg();
    }

    @Override
    public void signupSuccessful() {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    /**   * 显示进度对话框   */
    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
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
