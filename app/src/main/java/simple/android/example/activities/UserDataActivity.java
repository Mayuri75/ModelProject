package simple.android.example.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahu.example.R;
import simple.android.example.activities.fragments.ActivitiesFragments;
import simple.android.example.activities.fragments.UserHabitsFragments;
import simple.android.example.activities.fragments.UsertTaskFragments;
import simple.android.example.utils.CustomTabLayout;

import java.util.ArrayList;
import java.util.List;

public class UserDataActivity extends AppCompatActivity {
    private int[] tabIcons = {
            R.drawable.group_white,
            R.drawable.health_white,
            R.drawable.health_white
    };


    int position;
    public ActivitiesFragments arrivalListFragment;
    public UserHabitsFragments departureListFragment;
    int CURRENT_POSIITON = 0;
    int mReservationNo = 0;
    public UsertTaskFragments searchReservationFragment;


  //  ActivityUserDataBinding binding;


    Bundle mArguments = null;
    Bundle mArguments1 = null;
    Bundle mArguments2 = null;


    public String reservationNumber;

    FragmentManager fragmentManager;
    ViewPager viewpager;
    CustomTabLayout tablayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_user_data);
        position = CURRENT_POSIITON;
        viewpager  = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tablayout);
        setupViewPager(viewpager);

        //setupTabIcons();
        tablayout.setupWithViewPager(viewpager);
       // for (int i = 0; i < tablayout.getTabCount(); i++) {

                tablayout.getTabAt(0).setIcon(R.drawable.health_white);
                tablayout.getTabAt(1).setIcon(R.drawable.group_white);
                tablayout.getTabAt(2).setIcon(R.drawable.group_white);
         //   tabLayout.getTabAt(i).getIcon().setTint(getResources().getColor(R.color.colorPrimary));



       // }
      //  tabLayout.getTabAt(selectedTabPosition).getIcon().setTint(colorId);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
              if (tab.getPosition()==0){
                  tablayout.getTabAt(0).setIcon(R.drawable.ic_child);
                  tablayout.getTabAt(1).setIcon(R.drawable.ic_edit);
                  tablayout.getTabAt(2).setIcon(R.drawable.ic_edit);
              }else   if (tab.getPosition()==1){
                  tablayout.getTabAt(0).setIcon(R.drawable.ic_edit);
                  tablayout.getTabAt(1).setIcon(R.drawable.ic_edit);
                  tablayout.getTabAt(2).setIcon(R.drawable.ic_child);
              }else   if (tab.getPosition()==2){
                  tablayout.getTabAt(0).setIcon(R.drawable.ic_edit);
                  tablayout.getTabAt(1).setIcon(R.drawable.ic_edit);
                  tablayout.getTabAt(2).setIcon(R.drawable.ic_child);
              }

           //   tab.setIcon(R.drawable.health_white);
            //  getSupportActionBar().setTitle(pageTitles[tab.getPosition()]);
              viewpager.setCurrentItem(tab.getPosition());
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {

          }

          @Override
          public void onTabReselected(TabLayout.Tab tab) {

          }
      });
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        arrivalListFragment = new ActivitiesFragments();
        departureListFragment = new UserHabitsFragments();
        searchReservationFragment = new UsertTaskFragments();
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(searchReservationFragment, "");
        adapter.addFrag(arrivalListFragment, "");
        adapter.addFrag(departureListFragment, "");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(CURRENT_POSIITON);


    }

}

