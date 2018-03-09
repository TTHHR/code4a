package cn.atd3.code4a.view.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @BindView(R.id.avatar)
    QMUIRadiusImageView qmuiRadiusImageView;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_id)
    TextView userId;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.change_password)
    TextView changePassword;
    @BindView(R.id.logout)
    TextView logout;

    private ImageLoader imageLoader;

    @Override
    public void initView(Bundle s) {
        imageLoader=ImageLoader.getInstance();

        imageLoader.displayImage(Constant.avatar+ SigninUserManager.getUser(this).getId(),qmuiRadiusImageView);
        userName.setText(SigninUserManager.getUser(this).getName());
        userId.setText(SigninUserManager.getUser(this).getId());
        userEmail.setText(SigninUserManager.getUser(this).getEmail());
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

    }

    @Override
    public void setEmailSuccessful(String message) {

    }

    @Override
    public void setAvatarSuccessful(String message) {

    }

    @Override
    public void setPasswordSuccessful(String message) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin_user;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }
}
