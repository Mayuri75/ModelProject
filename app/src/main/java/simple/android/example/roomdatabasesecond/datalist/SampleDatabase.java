package simple.android.example.roomdatabasesecond.datalist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

@Database(entities = {Cheese.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Cheese table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract CheeseDao cheese();

    /** The only instance */
    private static SampleDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized SampleDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), SampleDatabase.class, "ex")
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                SampleDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        if (cheese().count() == 0) {
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Cheese cheese = new Cheese();
                    for (int i = 0; i < Cheese.CHEESES.length; i++) {
                        cheese.name = Cheese.CHEESES[i];
                        cheese().insert(cheese);
                    }
                }
            });
        }
    }

}
