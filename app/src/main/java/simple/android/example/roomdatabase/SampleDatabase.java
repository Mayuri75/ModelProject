package simple.android.example.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import simple.android.example.roomdatabase.data.DaoAccess;
import simple.android.example.roomdatabase.data.University;

@Database(entities = {University.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
