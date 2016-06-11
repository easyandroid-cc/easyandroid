package cc.easyandroid.easydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cc.easyandroid.easydb.exception.SQLiteHelperException;

/**
 * @author cgpllx
 */
public final class SQLiteHelper extends SQLiteOpenHelper {

    private Builder builder;

    private SQLiteHelper(Builder builder) {
        super(builder.context, builder.databaseName, builder.factory, builder.databaseVersion);

        this.builder = builder;
    }

    /**
     * Database version.
     *
     * @return the version of database SQLite.
     */
    public int getDatabaseVersion() {
        return builder.databaseVersion;
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            if (builder.tables == null) {
                throw new SQLiteHelperException("The array of String tables can't be null!!");
            }
            builder.onCreateCallback.onCreate(db);
        } catch (SQLiteHelperException e) {
            Log.e(this.getClass().toString(), Log.getStackTraceString(e), e);
        }
    }

    /**
     * Called every time.
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (builder.tableNames == null) {
                throw new SQLiteHelperException("The array of String tableNames can't be null!!");
            }
            builder.onUpgradeCallback.onUpgrade(db, oldVersion, newVersion);
        } catch (SQLiteHelperException e) {
            Log.e(this.getClass().toString(), Log.getStackTraceString(e), e);
        }
    }

    /**
     * Build SQLiteHelper.
     *
     * @return The new SQLiteHelperBuilder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements SQLiteHelperBuilder, SQLiteHelperCallback, ConfigBuilder {

        private Context context;
        private String databaseName;
        private SQLiteDatabase.CursorFactory factory;
        private int databaseVersion;

        private String[] tables;
        private String[] tableNames;

        private OnCreateCallback onCreateCallback;
        private OnUpgradeCallback onUpgradeCallback;


        final static public String DROP = "DROP TABLE IF EXISTS ";

        private Builder() {
            this.context = null;
            this.databaseName = "cc.easyandroidsqlite";
            this.databaseVersion = 1;
            this.factory = null;
            this.onUpgradeCallback = this;
            this.onCreateCallback = this;

            //config
        }

        /**
         * Build the SQLiteHelper with SQLiteDatabase.CursorFactory factory.
         *
         * @param context         to use to open or create the database
         * @param databaseName    of the database file, or null for an in-memory database
         * @param factory         to use for creating cursor objects, or null for the default
         * @param databaseVersion number of the database (starting at 1); if the database is older,
         *                        {@link #onUpgrade} will be used to upgrade the database; if the database is
         *                        newer, {@link #onDowngrade} will be used to downgrade the database
         * @return new SQLiteHelper
         */
        @Override
        public SQLiteHelper build(Context context, String databaseName,
                                  SQLiteDatabase.CursorFactory factory, int databaseVersion) {
            this.context = context;
            this.databaseName = databaseName;
            this.factory = factory;
            this.databaseVersion = databaseVersion;
            return new SQLiteHelper(this);
        }

        /**
         * Build the SQLiteHelper with the minimum information, SQLiteDatabase.CursorFactory by default
         * is null.
         *
         * @param context         to use to open or create the database
         * @param databaseName    of the database file, or null for an in-memory database
         * @param databaseVersion number of the database (starting at 1); if the database is older,
         *                        {@link #onUpgrade} will be used to upgrade the database; if the database is
         *                        newer, {@link #onDowngrade} will be used to downgrade the database
         * @return new SQLiteHelper
         */
        @Override
        public SQLiteHelper build(Context context, String databaseName, int databaseVersion) {
            this.context = context;
            this.databaseName = databaseName;
            this.databaseVersion = databaseVersion;
            return new SQLiteHelper(this);
        }

        /**
         * Build the SQLiteHelper with the default values.
         * <p/>
         * The default values:
         * <p/>
         * -  Database name: com.sqlitedatabase
         * <p/>
         * -  Database version: 1
         * <p/>
         * If you wish increment the version, you must use the method {@link #version(int)}
         *
         * @param context to use to open or create the database
         * @return new SQLiteHelper
         */
        @Override
        public SQLiteHelper build(Context context) {
            this.context = context;
            return new SQLiteHelper(this);
        }

        /**
         * Set the create tables of database, this method is very important.
         * <p/>
         * Important:
         * <p/>
         * -  The tables in database must be sorted in order of creation,
         * to avoid problems with the foreign keys!.
         * <p/>
         * -  The array of tables cannot be null.
         *
         * @param tables array of tables
         * @return SQLiteHelperBuilder with new set tables
         */
        @Override
        public SQLiteHelperBuilder tables(String[] tables) {
            this.tables = tables;
            return this;
        }

        /**
         * Set the table names of database, this method is very important.
         * <p/>
         * Important:
         * <p/>
         * -  The table names in database must be sorted in opposite order
         * by the array of creation tables.
         * <p/>
         * -  The array of table names cannot be null.
         *
         * @param tableNames array of table names
         * @return SQLiteHelperBuilder with new set table names
         */
        @Override
        public SQLiteHelperBuilder tableNames(String[] tableNames) {
            this.tableNames = tableNames;
            return this;
        }

        /**
         * Set the version value of database.
         *
         * @param version number of the database (starting at 1); if the database is older,
         *                {@link #onUpgrade} will be used to upgrade the database; if the database is
         *                newer, {@link #onDowngrade} will be used to downgrade the database
         * @return SQLiteHelperBuilder with new set version
         */
        @Override
        public SQLiteHelperBuilder version(int version) {
            this.databaseVersion = version;
            return this;
        }

        /**
         * Set the name value of database.
         *
         * @param name of the database file, or null for an in-memory database
         * @return SQLiteHelperBuilder with new set name
         */
        @Override
        public SQLiteHelperBuilder name(String name) {
            this.databaseName = name;
            return this;
        }

        /**
         * Set the SQLiteDatabase.CursorFactory.
         *
         * @param factory to use for creating cursor objects
         * @return SQLiteHelperBuilder with new set factory
         */
        @Override
        public SQLiteHelperBuilder factory(SQLiteDatabase.CursorFactory factory) {
            this.factory = factory;
            return this;
        }

        /**
         * It is a default implementation of onCreate, only creates all tables.
         *
         * @param db The database.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String table : tables) {
                db.execSQL(table);
            }
        }

        /**
         * It is a default implementation of onUpgrade, only remove all tables and re-create the tables.
         *
         * @param db         The database.
         * @param oldVersion int Old version
         * @param newVersion int New version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (int i = tableNames.length - 1; i >= 0; i--) {
                db.execSQL(DROP + tableNames[i]);
            }

            onCreateCallback.onCreate(db);
        }

        /**
         * Begin the Builder configuration.
         *
         * @return ConfigBuilder
         */
        @Override
        public ConfigBuilder beginConfig() {
            return this;
        }


        /**
         * If you need change the default methods onCreate and onUpgrade.
         *
         * @param callback SQLiteHelperCallback interface contains onCreate and onUpgrade methods.
         * @return ConfigBuilder
         */
        @Override
        public ConfigBuilder helperCallback(SQLiteHelperCallback callback) {
            this.onCreateCallback = callback;
            this.onUpgradeCallback = callback;
            return this;
        }

        /**
         * If you need change only the method onCreate, use this method.
         *
         * @param onCreateCallback OnCreateCallback interface contains onCreate method.
         * @return ConfigBuilder
         */
        @Override
        public ConfigBuilder onCreateCallback(OnCreateCallback onCreateCallback) {
            this.onCreateCallback = onCreateCallback;
            return this;
        }

        /**
         * If you need change only the method onUpgrade, use this method.
         *
         * @param onUpgradeCallback OnUpgradeCallback interface contains onUpgrade method.
         * @return ConfigBuilder
         */
        @Override
        public ConfigBuilder onUpgradeCallback(OnUpgradeCallback onUpgradeCallback) {
            this.onUpgradeCallback = onUpgradeCallback;
            return this;
        }

        /**
         * End the configuration.
         *
         * @return SQLiteHelperBuilder
         */
        @Override
        public SQLiteHelperBuilder endConfig() {
            return this;
        }
    }

    public interface OnCreateCallback {
        void onCreate(SQLiteDatabase db);
    }

    public interface OnUpgradeCallback {
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    public interface SQLiteHelperCallback extends OnCreateCallback, OnUpgradeCallback {
    }

    public interface SQLiteBuilder {
        //with CursorFactory
        SQLiteHelper build(Context context, String databaseName,
                           SQLiteDatabase.CursorFactory factory, int databaseVersion);

        //without CursorFactory
        SQLiteHelper build(Context context, String databaseName, int databaseVersion);

        SQLiteHelper build(Context context);
    }

    public interface ConfigBuilder {
        ConfigBuilder helperCallback(SQLiteHelperCallback callback);

        ConfigBuilder onCreateCallback(OnCreateCallback onCreateCallback);

        ConfigBuilder onUpgradeCallback(OnUpgradeCallback onUpgradeCallback);

        SQLiteHelperBuilder endConfig();
    }

    //concrete build
    public interface SQLiteHelperBuilder extends SQLiteBuilder {
        ConfigBuilder beginConfig();

        SQLiteHelperBuilder tables(String[] tables);

        SQLiteHelperBuilder tableNames(String[] tableNames);

        SQLiteHelperBuilder version(int version);

        SQLiteHelperBuilder name(String name);

        SQLiteHelperBuilder factory(SQLiteDatabase.CursorFactory factory);
    }
}