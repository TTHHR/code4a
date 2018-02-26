package cn.dxkite.common.ui.support;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 支持性图片获取器
 * 提供缓存图片
 * Created by DXkite on 2018/2/26 0026.
 */

public class CacheableImageGetter implements Html.ImageGetter {

    TextView textView;
    File output;

    public static final String TAG = "CacheableImageGetter";

    public CacheableImageGetter(TextView textView, File outputPath) {
        this.textView = textView;
        this.output = outputPath;
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.d(TAG, "loading image -> " + source);
        // 网络图片
        if (source.startsWith("http")) {
            File outputPath = new File(CacheableImageGetter.this.output.getAbsolutePath() + File.separator + source.hashCode());
            if (outputPath.exists()) {
                Log.d(TAG, "loading image from cache " + outputPath.getAbsolutePath());
                Drawable drawable = Drawable.createFromPath(outputPath.getAbsolutePath());
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            } else {
                Log.d(TAG, "download image for " + source);
                ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask();
                getterTask.execute(source, outputPath.getAbsolutePath());
            }
        }
        return new ImageHolderDrawable(textView);
    }

     class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                CacheableImageGetter.this.textView.setText(CacheableImageGetter.this.textView.getText());
            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            Log.d(TAG, "download image " + params[0]);
            return getDownloadImage(params[0], params[1]);
        }

        public Drawable getDownloadImage(String imageUrl, String path) {
            File outputPath = new File(path);
            if (!outputPath.getParentFile().exists()) {
                if (!outputPath.getParentFile().mkdirs()) {
                    Log.e(CacheableImageGetter.TAG, "can't mkdirs " + outputPath.getParentFile().getAbsolutePath());
                }
            }
            File file = new File(path);
            InputStream in = null;
            FileOutputStream out = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();
                connUrl.setConnectTimeout(5000);
                connUrl.setRequestMethod("GET");
                if (connUrl.getResponseCode() == 200) {
                    Log.d(CacheableImageGetter.TAG, "save " + imageUrl + " to " + path);
                    in = connUrl.getInputStream();
                    out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } else {
                    Log.i(TAG, "get image from url (" + connUrl.getResponseCode() + ") error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (outputPath.exists()) {
                Drawable drawable = Drawable.createFromPath(outputPath.getAbsolutePath());
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            } else {
                Log.e(CacheableImageGetter.TAG, "can't read " + path);
            }
            return null;
        }
    }

    class ImageHolderDrawable extends BitmapDrawable {

        TextView textView;
        int width,height;
        public ImageHolderDrawable(TextView textView) {
            this.textView = textView;
            width = 10;
            height = 10;
            setBounds(0, 0, width, height);
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint=new Paint();
            paint.setColor(Color.DKGRAY);
            canvas.drawRect(0,0,width,height,paint);
        }
    }
}
