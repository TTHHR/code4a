package cn.atd3.code4a.view.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.atd3.code4a.Constant;
import cn.atd3.code4a.R;
import cn.atd3.code4a.SigninUserManager;
import cn.atd3.code4a.model.SigninUserModel;
import cn.atd3.code4a.model.model.User;
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
    private final static int REQUEST_IMAGE_SELECT = 2;

    @Override
    public void initView(Bundle s) {
        setSupportActionBar(toolBar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageLoader=ImageLoader.getInstance();

        refreshUI();
    }

    private void refreshUI(){
        imageLoader.displayImage(Constant.avatar+ SigninUserManager.getUser(this).getId(),qmuiRadiusImageView);
        userName.setText(SigninUserManager.getUser(this).getName());
        userId.setText(SigninUserManager.getUser(this).getId());
        userEmail.setText(SigninUserManager.getUser(this).getEmail());
    }

    @OnClick(R.id.avatar)
    public void changeAvatar(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode != RESULT_OK) {
            Logger.i("canceled or other exception!");
            return;
        }

        if (requestCode == REQUEST_IMAGE_SELECT) {
            Logger.i("REQUEST_IMAGE_SELECT");
            Uri uri=data.getData();
            File file=uri2File(uri);

            showProgressDialog();
            mPresenter.changeAvatar(file);
        }
    }

    //此函数代码引用自http://blog.csdn.net/wangjintao1988/article/details/9233027
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    @OnClick(R.id.change_email)
    public void changeEmail(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setPlaceholder(getString(R.string.edit_email))
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
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
                .show();
    }

    @OnClick(R.id.change_password)
    public void changePwd(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        LinearLayout loginDialog= (LinearLayout) getLayoutInflater().inflate(R.layout.alertdialog_change_pwd,null);
        final EditText oldPwd=loginDialog.findViewById(R.id.old_pwd);
        final EditText newPwd=loginDialog.findViewById(R.id.new_pwd);
        final EditText pwdConfirm=loginDialog.findViewById(R.id.pwd_confirm);
        //设置内容区域为自定义View
        builder.setView(loginDialog);
        DialogInterface.OnClickListener onClick=new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showProgressDialog();
                mPresenter.changePassword(oldPwd.getText().toString(),newPwd.getText().toString(),pwdConfirm.getText().toString());
            }
        };
        builder.setPositiveButton(R.string.button_change,onClick);
        builder.setNegativeButton(R.string.button_cancel,null);

        //builder.setCancelable(false);
        AlertDialog dialog=builder.create();
        dialog.setTitle(getString(R.string.change_password));
        dialog.show();
    }

    @OnClick(R.id.logout)
    public void logout(){
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(getString(R.string.affirm_logout))
                .addAction(getString(R.string.button_cancel),new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getString(R.string.button_ok),new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        SigninUserManager.deleteUser(getApplicationContext());
                        finish();
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
        mPresenter.setUserInfo();
    }

    @Override
    public void setAvatarSuccessful(String message) {
        closeProgressDialog();
        Toast.makeText(this, getString(R.string.change_avatar_successful), Toast.LENGTH_SHORT).show();
        mPresenter.setUserInfo();
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

    @Override
    public void onUserInfoRefresh(User user) {
        SigninUserManager.setUser(this,user);
        refreshUI();
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
