package cc.easyandroid.simple;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import cc.easyandroid.easycore.EasyCall;
import cc.easyandroid.easyhttp.core.progress.ProgressListener;
import cc.easyandroid.easymvp.call.OkHttpDownLoadEasyCall;
import cc.easyandroid.easymvp.presenter.EasyWorkPresenter;
import cc.easyandroid.easymvp.view.ISimpleCallView;

public class DownLoadActivity extends Activity implements ISimpleCallView<OkHttpDownLoadEasyCall.DownLoadResult>, ProgressListener {
    EasyWorkPresenter<OkHttpDownLoadEasyCall.DownLoadResult> presenter = new EasyWorkPresenter<>();
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        presenter.attachView(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.execute();
            }
        });
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    public EasyCall<OkHttpDownLoadEasyCall.DownLoadResult> onCreateCall(int presenterId, Bundle bundle) {
//        String url = "http://www.apk.anzhi.com/data2/apk/201604/23/09c8ab2bc3bd4d7a1bdb2ede136be2d5_00726300.apk";
        String url = "http://www.apk.anzhi.com/data3/apk/201507/17/com.yybackup_62449500.apk?1213115";
//        String url = "http://www.anzhi.com";
//        String url = "http://hk.qfang.com/qfang-api/mobile/common/query/getVerifyCodeImg?1" + System.currentTimeMillis();
        File file = new File(Environment.getExternalStorageDirectory(), "xxx4.apk");
        return HttpUtils.creatGetDownLoadCall(url, null, file, this);
    }

    @Override
    public void onStart(int presenterId) {
        System.out.println("bytesRead  onStart");
    }

    @Override
    public void onCompleted(int presenterId) {
        System.out.println("bytesRead  onCompleted");
    }

    @Override
    public void onError(int presenterId, Throwable e) {
        System.out.println("bytesRead  onError" + e.getMessage());

    }

    @Override
    public void deliverResult(int presenterId, OkHttpDownLoadEasyCall.DownLoadResult results) {

    }

    @Override
    public void update(final long bytesRead, final long contentLength, final boolean done) {//子线程中执行
        if (Looper.myLooper() != Looper.getMainLooper()) {
            System.out.println("bytesRead  111");
        } else {
            System.out.println("bytesRead  22222");
        }
        System.out.println("bytesRead=" + bytesRead + " contentLength=" + contentLength + " done=" + done);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText("bytesRead=" + bytesRead + " contentLength=" + contentLength + " done=" + done);
            }
        });
    }
}
