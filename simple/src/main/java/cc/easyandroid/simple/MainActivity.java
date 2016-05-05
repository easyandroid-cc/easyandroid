package cc.easyandroid.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView listview;
    private String[] str_name = new String[]{"得到String 结果", "得到java对象", "下载"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_name));
        listview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startStringActivity();
                break;
            case 1:
                startGsonActivity();
                break;
            case 2:
                downLoadActivity();
                break;
        }
    }

    private void downLoadActivity() {
        startActivity(new Intent(this, DownLoadActivity.class));
    }

    private void startGsonActivity() {
        startActivity(new Intent(this, GsonActivity.class));
    }

    private void startStringActivity() {
        startActivity(new Intent(this, StringActivity.class));
    }
}
