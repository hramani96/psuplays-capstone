package com.example.psuplays;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Admin.class}, version = 1, exportSchema = false)
public abstract class AdminDatabase extends RoomDatabase {

    public abstract adminsDAO AdminDAO();

    private static AdminDatabase INSTANCE;

    public static AdminDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AdminDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AdminDatabase.class, "admin_database")
                            .addCallback(createAdminDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createAdminDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            createAdminTable();
        }
    };

    private static void createAdminTable() {
    /*
        for (int i = 0; i < DefaultContent.TITLE.length; i++) {
            insert(new Admin(0, DefaultContent.TITLE[i], DefaultContent.SETUP[i], DefaultContent.PUNCHLINE[i], false));
        }

     */
    }


    public static void insert(Admin admin) {
        new AsyncTask<Admin, Void, Void> () {
            protected Void doInBackground(Admin... admins) {
                INSTANCE.AdminDAO().insert(admins);
                return null;
            }
        }.execute(admin);
    }

    public static void delete(int adminId) {
        new AsyncTask<Integer, Void, Void> () {
            protected Void doInBackground(Integer... ids) {
                INSTANCE.AdminDAO().delete(ids[0]);
                return null;
            }
        }.execute(adminId);
    }


    public static void update(Admin admin) {
        new AsyncTask<Admin, Void, Void> () {
            protected Void doInBackground(Admin... admins) {
                INSTANCE.AdminDAO().update(admins);
                return null;
            }
        }.execute(admin);
    }
}
