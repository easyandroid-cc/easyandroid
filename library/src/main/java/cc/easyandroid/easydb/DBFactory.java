package cc.easyandroid.easydb;

public class DBFactory {
    public static SQLiteDelegate getxxx() {
//         SQLiteHelper.builder().
        SQLiteDelegate sqLiteDelegate = new SQLiteDelegate(null,null,null);
        return sqLiteDelegate;
    }
}
