package cn.atd3.code4a.net;

import android.content.Context;
import android.widget.Toast;

import cn.atd3.code4a.R;
import cn.atd3.proxy.exception.ProxyException;
import rx.Subscriber;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/28   8:19
 **/
public abstract class DisposeErrorSubscriber<T> extends Subscriber<T> {
    private static final int NAME_FORMAT_ERROR=-1;
    private static final int EMAIL_FORMAT_ERROR=-2;
    private static final int NAME_EXISTS_ERROR=-3;
    private static final int EMAIL_EXISTS_ERROR=-4;
    private static final int ACCOUNT_OR_PASSWORD_ERROR=-5;
    private static final int USER_FREEZED=-6;
    private static final int HUMAN_CODE_ERROR=-7;
    private static final int INVITE_CODE_ERROR=-8;

    private Context mContext;

    public DisposeErrorSubscriber(Context context){
        this.mContext=context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        String errorMessage;
        if(e instanceof ProxyException){
            ProxyException p=(ProxyException)e;
            int i=p.getCode();
            switch (i){
                case NAME_FORMAT_ERROR:
                    errorMessage=mContext.getString(R.string.name_format_error);
                    break;
                case EMAIL_FORMAT_ERROR:
                    errorMessage=mContext.getString(R.string.email_format_error);
                    break;
                case NAME_EXISTS_ERROR:
                    errorMessage=mContext.getString(R.string.name_exists_error);
                    break;
                case EMAIL_EXISTS_ERROR:
                    errorMessage=mContext.getString(R.string.email_exists_error);
                    break;
                case ACCOUNT_OR_PASSWORD_ERROR:
                    errorMessage=mContext.getString(R.string.account_or_password_error);
                    break;
                case USER_FREEZED:
                    errorMessage=mContext.getString(R.string.user_freezed);
                    break;
                case HUMAN_CODE_ERROR:
                    errorMessage=mContext.getString(R.string.human_code_error);
                    break;
                case INVITE_CODE_ERROR:
                    errorMessage=mContext.getString(R.string.invite_code_error);
                    break;
                default:
                    errorMessage=mContext.getString(R.string.unknown_error)+i+":"+p.getMessage();
            }
        }else{
            errorMessage=mContext.getString(R.string.unknown_error)+e.getMessage();
        }
        showErrorWithStatus(errorMessage);
        onThrowableError(e);
    }

    public abstract void onThrowableError(Throwable e);
    @Override
    public abstract void onNext(T t);

    public void showErrorWithStatus(String msg) {
        Toast.makeText(mContext, mContext.getString(R.string.error)+msg, Toast.LENGTH_SHORT).show();
    }
}
