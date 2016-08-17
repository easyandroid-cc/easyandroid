package cc.easyandroid.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cc.easyandroid.easydb.abs.DataAccesObject;
import cc.easyandroid.simple.core.SimpleSqlite;
import cc.easyandroid.simple.pojo.Tab;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView listview;
    private String[] str_name = new String[]{"得到String 结果", "Gson1得到java对象", "Gson2得到java对象", "下载"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_name));
        listview.setOnItemClickListener(this);
        SimpleSqlite simpleSqlite = new SimpleSqlite(this);
        DataAccesObject<Tab> dataAccesObject = simpleSqlite.getDao("a1");
        try {
            dataAccesObject.insert(new Tab());
            Tab tab1 = dataAccesObject.findById("d");
            Tab tab2 = dataAccesObject.findById("f");
            System.out.println("easyandroid tab1=" + tab1.getXxx());
            System.out.println("easyandroid tab2=" + tab2);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                startGson2Activity();
                break;
            case 3:
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

    private void startGson2Activity() {
        startActivity(new Intent(this, Gson2Activity.class));
    }

    private void startStringActivity() {
        startActivity(new Intent(this, StringActivity.class));
    }
}
