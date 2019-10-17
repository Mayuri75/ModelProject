package simple.android.example.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import simple.android.example.network.MyApplication;

public class CustomTabLayout extends TabLayout {
    public static String FONT_NARROW = "FONT_NARROW", FONT_NARROW_BOLD = "FONT_NARROW_BOLD";
    static MyApplication application;
    Typeface fontnarrow;

    public CustomTabLayout(Context context) {
        super(context);
        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    private void init() {
        application=new MyApplication();
       // fontnarrow = Typeface.createFromAsset(getContext().getAssets(), "font/Narrow.ttf");
      //  fontnarrow = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);


    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                //((TextView) tabViewChild).setTypeface(fontnarrow);
                  application.setTypeface(((TextView) tabViewChild),FONT_NARROW,getContext().getApplicationContext());
                ((TextView) tabViewChild).setTextSize(20);
            }
        }
    }
    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position,setSelected);
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(fontnarrow);
                ((TextView) tabViewChild).setTextSize(20);
               // application.setTypeface(((TextView) tabViewChild),FONT_NARROW,getContext().getApplicationContext());
            }
        }

    }
}