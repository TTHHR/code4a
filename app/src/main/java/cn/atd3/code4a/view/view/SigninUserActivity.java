package cn.atd3.code4a.view.view;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.OnClick;
import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.SigninUserManager;
import cn.atd3.code4a.model.SigninUserModel;
import cn.atd3.code4a.mvpbase.BaseActivity;
import cn.atd3.code4a.mvpbase.BaseView;
import cn.atd3.code4a.presenter.SigninUserPresenter;
import cn.atd3.code4a.presenter.interfaces.SigninUserContract;

public class SigninUserActivity extends BaseActivity<SigninUserModel,SigninUserPresenter> implements SigninUserContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.avatar)
    QMUIRadiusImageView qmuiRadiusImageView;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_id)
    TextView userId;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.change_email)
    TextView changeEmail;
    @BindView(R.id.change_password)
    TextView changePassword;
    @BindView(R.id.logout)
    TextView logout;

    private ImageLoader imageLoader;
    private ProgressDialog progressDialog;

    @Override
    public void initView(Bundle s) {
        setSupportActionBar(toolBar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageLoader=ImageLoader.getInstance();

        imageLoader.displayImage(Constant.avatar+ SigninUserManager.getUser(this).getId(),qmuiRadiusImageView);
        userName.setText(SigninUserManager.getUser(this).getName());
        userId.setText(SigninUserManager.getUser(this).getId());
        userEmail.setText(SigninUserManager.getUser(this).getEmail());
    }

    @OnClick(R.id.change_email)
    public void changeEmail(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setPlaceholder(getString(R.string.edit_email))
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(getString(R.string.button_change), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            showProgressDialog();
                            mPresenter.changeEmail(text.toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "邮箱格式错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.change_password)
    public void changePwd(){
        Toast.makeText(this,"敬请期待",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.logout)
    public void logout(){
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(getString(R.string.affirm_logout))
                .addAction(getString(R.string.button_ok),new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        SigninUserManager.deleteUser(getApplicationContext());
                        finish();
                    }
                })
                .addAction(getString(R.string.button_cancel),new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showErrorWithStatus(String msg) {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.error)+msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEmailSuccessful() {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.change_email_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAvatarSuccessful(String message) {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.change_avatar_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordSuccessful(String message) {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.change_password_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin_user;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId()==android.R.id.home){
            finish();
        }
        return true;
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
