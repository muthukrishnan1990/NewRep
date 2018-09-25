package bss.bbs.com.teslaclone.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Anbu on 08-Dec-17.
 */

public class DBAdapter extends SQLiteOpenHelper{

    private static final String LOG = "DatabaseHelper";
    private static String DB_PATH = "";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FINDYOURCARAPP";
    SQLiteDatabase db1;
    Cursor cur;
    Context con;

    private static final String MYFAVORITES = "CREATE TABLE IF NOT EXISTS "
            + "myfavorites(VIN varchar,Dealer_ID varchar,Make varchar,Model varchar,image_url_pattern varchar,List_Price varchar,Mileage varchar,Year varchar,exterior varchar,interior varchar,Condition varchar,Location varchar)";
    public static final String TABLES = "myfavorites";
    /*private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todo_tags";*/

    public DBAdapter(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MYFAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLES);
        onCreate(db);
    }

    public DBAdapter open() throws SQLException {

        db1 = this.getWritableDatabase();
        return this;
    }
    public void close() {
        if (db1.isOpen()) {
            db1.close();
        }
    }
    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e("TAG", "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }
    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = con.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DATABASE_NAME;
        //Log.v("mPath", mPath);
        db1 = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //db1 = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return db1 != null;
    }

    /**********************************
     * SELECT
     **************************************************/

    public Cursor fetchQuery(String sql) {
        Cursor c = null;
        try {
            open();
            c = db1.rawQuery(sql, null);
        } catch (Exception e) {
            Log.e("error", e.getMessage() + "t");
            e.printStackTrace();
        }
        return c;
    }

    public Cursor fetchLike(String table, String where, String value) {
        Cursor c = null;
        try {
            open();
            c = db1.query(table, null, where + " LIKE '%" + value + "%'", null,
                    null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor fetchSingleRow(String table, String where, int value) {

        Cursor c = null;
        try {
            open();
            c = db1.query(table, null, where + "=?",
                    new String[]{String.valueOf(value)}, null, null, null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor fetchAllRow(String table) {

        Cursor c = null;
        try {
            open();
            c = db1.query(table, null, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;

    }

    /**********************************
     * INSERT
     **************************************************/

    public long insert(ContentValues cv, String table) {
        long sucess = 0;
        open();
        try {
            sucess = db1.insert(table, null, cv);
             Log.e("Table to Insert==>", table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return sucess;
    }

    public long insertWithOnConflict(ContentValues cv, String table) {
        long sucess = 0;
        open();
        try {
            sucess = db1.insertWithOnConflict(table, null, cv,
                    SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return sucess;
    }

    /**********************************
     * UPDATE
     **************************************************/

    public void updateQuery(String Query) {
        open();
        try {
            db1.execSQL(Query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public long update(String table, ContentValues cv, String con) {
        long success = 0;
        try {
            open();
            success = db1.update(table, cv, con, null);
            Log.e("Updated Table:", table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return success;

    }

    /**********************************
     * DELETE
     **************************************************/

    public void deleteQuery(String sql) {
        try {
            open();
            db1.execSQL(sql);
            Log.e("Delete", "Data deleted");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void delete(String table, String where, String[] args) {
        try {
            open();
            db1.delete(table, where, args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void deleteSingleRow(String table, String col, int value) {

        try {
            open();
            db1.delete(table, col + "=" + value, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void deleteAllRow(String table) {
        try {
            open();
            db1.delete(table, null, null);
            Log.e("Db Delete All Row", "Deleted table :" + table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**********************************
     * COUNT
     **************************************************/

    public int tableCount(String tablename) {
        Cursor c = null;
        int count = 0;
        try {
            open();
            c = db1.query(tablename, null, null, null, null, null, null);
            count = c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            close();
        }
        return count;
    }

/**********************************    END    **************************************************/

}
