package simple.android.example.network;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import net.danlew.android.joda.JodaTimeAndroid;

import okhttp3.OkHttpClient;


public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    public static String FONT_ROBOTO_BOLD = "FONT_ROBOTO_BOLD";
    public static String FONT_ROBOTO_REGULAR = "FONT_ROBOTO_REGULAR";
    public static String FONT_ROBOTO_MEDIUM="FONT_ROBOTO_MEDIUM";

    private static Typeface fontRobotoBold, fontRobotoRegular,fontRobotoMedium;

    public static MyApplication mInstance;

    public static Gson gson = new Gson();

    /**
     * Testing purpose
     */
    private int myPort;

    private boolean isConnectionListenerRunning = false;


    @Override
    public void onCreate() {


        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mInstance = this;
        JodaTimeAndroid.init(this);
        Log.i("TAG","****************************"+mInstance);
        //Fabric.with(this, new Crashlytics());



    }





    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue(OkHttpClient interceptor) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader(OkHttpClient interceptor) {
        getRequestQueue(interceptor);
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

  /*  public <T> void addToRequestQueue(Request<T> req, String tag,OkHttpClient interceptor) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(interceptor).add(req);
    }*/

    public <T> void addToRequestQueue(Request<T> req, String tag_json_obj, OkHttpClient interceptor) {
        req.setTag(TAG);
        getRequestQueue(interceptor).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setTypeface(TextView textView, String typeface, Context ctx) {
        if (textView != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                textView.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                textView.setTypeface(getFontRobotoRegular(ctx));
            }
            else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                textView.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }


    public void setTypeface(TextInputLayout textInputLayout, String typeface, Context ctx) {
        if (textInputLayout != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                textInputLayout.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                textInputLayout.setTypeface(getFontRobotoRegular(ctx));
            }  else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                textInputLayout.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }

    public void setTypeface(AutoCompleteTextView autoCompleteTextView, String typeface, Context ctx) {
        if (autoCompleteTextView != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                autoCompleteTextView.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                autoCompleteTextView.setTypeface(getFontRobotoRegular(ctx));
            }
            else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                autoCompleteTextView.setTypeface(getFontRobotoMedium(ctx));
            }
        }
    }

    public void setTypeface(Button button, String typeface, Context ctx) {
        if (button != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                button.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                button.setTypeface(getFontRobotoRegular(ctx));
            }else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                button.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }

    public void setTypeface(CheckBox checkBox, String typeface, Context ctx) {
        if (checkBox != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                checkBox.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                checkBox.setTypeface(getFontRobotoRegular(ctx));
            }else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                checkBox.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }

    public void setTypeface(RadioButton radioButton, String typeface, Context ctx) {
        if (radioButton != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                radioButton.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                radioButton.setTypeface(getFontRobotoRegular(ctx));
            }else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                radioButton.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }

    public void setTypeface(EditText editText, String typeface, Context ctx) {
        if (editText != null) {
            if (typeface.equals(FONT_ROBOTO_BOLD)) {
                editText.setTypeface(getFontRobotoBold(ctx));
            } else if (typeface.equals(FONT_ROBOTO_REGULAR)) {
                editText.setTypeface(getFontRobotoRegular(ctx));
            }else if (typeface.equals(FONT_ROBOTO_MEDIUM)) {
                editText.setTypeface(getFontRobotoMedium(ctx));
            }

        }
    }

    public Typeface getFontRobotoBold(Context ctx) {
        if (fontRobotoBold == null) {
            fontRobotoBold = Typeface.createFromAsset(ctx.getAssets(), "font/Roboto-Bold.ttf");
        }
        return this.fontRobotoBold;
    }

    public Typeface getFontRobotoRegular(Context ctx) {
        if (fontRobotoRegular == null) {
            fontRobotoRegular = Typeface.createFromAsset(ctx.getAssets(), "font/Roboto-Regular.ttf");
        }
        return this.fontRobotoRegular;
    }

    public Typeface getFontRobotoMedium(Context ctx) {
        if (fontRobotoMedium == null) {
            fontRobotoMedium = Typeface.createFromAsset(ctx.getAssets(), "font/Roboto-Medium.ttf");
        }
        return this.fontRobotoMedium;
    }




    /**
     * Testing
     */




    public boolean isConnListenerRunning() {
        return isConnectionListenerRunning;
    }

    public int getPort(){
        return myPort;
    }

    public void setConnectivityListener(InternetConnectivityReceiver.ConnectivityReceiverListener
                                                listener) {
        InternetConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
