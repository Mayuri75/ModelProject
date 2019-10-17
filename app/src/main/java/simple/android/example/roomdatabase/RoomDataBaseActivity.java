package simple.android.example.roomdatabase;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahu.example.R;
import simple.android.example.roomdatabase.data.College;
import simple.android.example.roomdatabase.data.University;

public class RoomDataBaseActivity extends AppCompatActivity {

    private static final int LOADER_CHEESES = 1;
    SampleDatabase sampleDatabase;
    //private CheeseAdapter mCheeseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_data_base);

        sampleDatabase = Room.databaseBuilder(getApplicationContext(),
                SampleDatabase.class, "sample-db").build();
        new DatabaseAsync().execute();

    }


    private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Perform pre-adding operation here.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Let's add some dummy data to the database.
            University university = new University();
            university.setName("MyUniversity");

            College college = new College();
            college.setId(1);
            college.setName("MyCollege");

            university.setCollege(college);

            //Now access all the methods defined in DaoAccess with sampleDatabase object
            sampleDatabase.daoAccess().insertOnlySingleRecord(university);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //To after addition operation here.
        }
    }

}