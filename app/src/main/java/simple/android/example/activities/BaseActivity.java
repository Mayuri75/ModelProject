package simple.android.example.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mahu.example.R;
import simple.android.example.model.MenuItems;
import simple.android.example.network.MyApplication;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();
    MyApplication application;

    String TAG_SIMILARGUESTFOUND = "SIMILAR GUEST FOUND";
    String TAG_SIMILARCOMPANYFOUND = "SIMILAR COMPANY FOUND";
    String TAG_SIMILARTRAVELAGENTFOUND = "SIMILAR TRAVEL AGENT FOUND";
    String TAG_SIMILARSOURCEFOUND = "SIMILAR SOURCE FOUND";
    String TAG_SIMILARBOOKERFOUND = "SIMILAR BOOKER FOUND";
    String TAG_SINGLEGUESTDETAILS = "SINGLE GUEST DETAILS";
    String TAG_SINGLECOMPANYDETAILS = "SINGLE COMPANY DETAILS";
    String TAG_SINGLESOURCEDETAILS = "SINGLE SOURCE DETAILS";
    String TAG_SINGLETADETAILS = "SINGLE TRAVEL AGENT DETAILS";
    String TAG_GUESTENTRYDETAILS = "GUEST ENTRY SCREEN";
    String TAG_SINGLEBOOKERDETAILS = "SINGLE BOOKER DETAILS";
    String TAG_BOOKERCREATED = "BOOKER CREATED DETAILS";
    String TAG_VIEW_RATE_CODE = "RATE CODE VIEW";
    String TAG_ROOMCOST = "CHANGE ROOM COST";
    String TAG_ALLOCATE_ROOM = "ALLOCATE_ROOM";
    public static String TAG_ROOMTRANSFER = "ROOM TRANSFER";
    public static String TAG_SPLITSHARER = "SPLIT SHARER";
    public static String TAG_LINKROOM = "LINK ROOM";
    public static String TAG_SPLITCHARGES = "SPLIT CHARGES";
    public static String TAG_QUICKBALANCE = "QUICK BALANCE";
    public static String TAG_TRANSFERCHARGES = "TRANSFER CHARGES";

    public static String FONT_ROBOTO_BOLD = "FONT_ROBOTO_BOLD";
    public static String FONT_ROBOTO_REGULAR = "FONT_ROBOTO_REGULAR";
    String FONT_ROBOTO_MEDIUM = "FONT_ROBOTO_MEDIUM";

    public static String OPERATIONTYPE_DEFAULT = "DEFAULT";
    public static String OPERATIONTYPE_UPDATE = "Update";
    public static String OPERATIONTYPE_INSERT = "Insert";
    public static String OPERATIONTYPE_DELETE = "Delete";


    /*final int REQUESTCODE_COMPANYCREATED=601;
    final int REQUESTCODE_TRAVELAGENTCREATED=602;
    final int REQUESTCODE_SOURCECREATED=603;
    final int REQUESTCODE_BOOKERCREATED=604;
    final int REQUESTCODE_GUESTCREATED=605;

    final int REQUESTCODE_COMPANYSELECTED=501;
    final int REQUESTCODE_TRAVELAGENTSELECTED=502;
    final int REQUESTCODE_SOURCESELECTED=503;
    final int REQUESTCODE_BOOKERSELECTED=504;
    final int REQUESTCODE_GUESTSELECTED=505;

    final int REQUESTCODE_CHANGEROOMCOST=701;
    final int REQUESTCODE_ROOMGUESTCOUNTCHANGED=702;*/
//

    /* public DrawerLayout mDrawerLayout;
     LinearLayout mLeftDrawerList;
     ListView mDrawerLevel1List, mDrawerLevel2List;
     public ActionBarDrawerToggle mDrawerToggle;
     View listHeaderView;
     RelativeLayout logolayout;
     Toolbar mActionView;
     ArrayList<MenuItems> mMenuItemsList = new ArrayList<>();
     ArrayList<MenuLevel1> mMenuLevel1List = new ArrayList<>();
 */
    ArrayList<MenuItems> mMenuItemsList = new ArrayList<>();

    private HashMap<String, Object> firebaseDefaultMap;
    private static boolean popupAlreadyShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



      /*  OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());*/
    }





    public void volleyGetStringRequest(final String mUrl, final VolleyCallback callback)
    {
        if(!isNetworkConnected()){

            // ToastShort(this,getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            //RequestQueue queue = Volley.newRequestQueue(this);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                Log.d(TAG, "response : " + response.toString());
                                callback.onSuccess(response.toString());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (error != null) {
                        try {
                            Log.e(TAG, "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e(TAG, "VolleyError  : " + error);
                    callback.onError(error.toString());
                }
            });

            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);
            Log.i(TAG, "volleyGetRequest - getRequest : " + stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyGetRequest - Exception : " + e.getMessage());
        }
    }

    public void volleyGetRequest(final String mUrl, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            //   ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            //RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d(TAG, "response : " + response.toString());
                        //CommonFunctions.printLog("i", "response", response.toString());
                        callback.onSuccess(response.toString());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e(TAG, "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "UDAwMDA1NzpGb3JkZW1vQDI=");
                    //headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());
                    Log.i("TAG", "Headers: " + headers.toString());
                    return headers;
                }
            };


            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));

            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            getRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(getRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);
            Log.i(TAG, "volleyGetRequest - getRequest : " + getRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyGetRequest - Exception : " + e.getMessage());
        }
    }


    public void volleyPostRequest(final String mUrl, final String jsonParams, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            //ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            //RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, mUrl,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                Log.d("VOLLEY", "\nResponse :\n\n " + response.toString());
                                // CommonFunctions.printLog("d", "\nResponse :\n\n ", response.toString());
                                callback.onSuccess(response.toString());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                    Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                    Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                    Log.d("VOLLEY", "\nResponse :\n\n " + error.networkResponse.statusCode);
                                    Log.e("TAG", "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode + "\n mUrl: " + mUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    //for token
                    // headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());
                    Log.i("TAG", "Headers: " + headers.toString());
                    return headers;
                }
            };

            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);

            Log.d(TAG, " - postRequest : " + postRequest);
            Log.d("param", jsonParams);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " - Exception : " + e.getMessage());
            Log.d("param", jsonParams);
        }

    }

    public void volleyPutRequest(final String mUrl, final String jsonParams, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            //   ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            Log.d("json body ", jsonParams);
            String tag_json_obj = "json_obj_req";
            //RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, mUrl,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                Log.d("VOLLEY", "\nResponse :\n\n " + response.toString());
                                //CommonFunctions.printLog("d", "\nResponse :\n\n ", response.toString());
                                callback.onSuccess(response.toString());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                    Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                    Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                    Log.d("VOLLEY", "\nResponse :\n\n " + error.networkResponse.statusCode);
                                    Log.e("TAG", "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode + "\n mUrl: " + mUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    // headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());
                    Log.i("TAG", "Headers: " + headers.toString());

                    return headers;
                }
            };
            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);

            Log.d(TAG, "volleyPostRequest - postRequest : " + postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyPostRequest - Exception : " + e.getMessage());
        }

    }

    public void VolleyPutRequest1(String mUrl) {
        if (!isNetworkConnected()) {
            //ToastShort(this, getResources().getString(R.string.no_internet));
            return;
        }
        StringRequest putRequest = new StringRequest(Request.Method.PUT, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Departure", response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Departure", error.getMessage());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "");
                params.put("domain", "");

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(putRequest);
    }

    public void VolleyDeleteRequest(final String mUrl, final String jsonParams, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            //ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.DELETE, mUrl,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("VOLLEY", "\n\n\n*******************************************************************");
                                Log.i("VOLLEY", "\nURL: " + mUrl);
                                Log.i("VOLLEY", "\njsonParams: " + jsonParams);
                                Log.d("VOLLEY", "\nGuestIDProffDetailsResponse : " + response.toString());
                                //CommonFunctions.printLog("d", "\nResponse :\n\n ", response.toString());
                                callback.onSuccess(response.toString());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e("TAG", "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode + "\n mUrl: " + mUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    //headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());
                    Log.i("TAG", "Headers: " + headers.toString());

                    return headers;
                }
            };
            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);

            Log.d(TAG, "volleyPostRequest - postRequest : " + postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyPostRequest - Exception : " + e.getMessage());
        }
    }


    public interface VolleyCallback {
        void onSuccess(String result) throws JSONException;

        void onError(String result);
    }

    public static void ToastLong(Context ctx, String msg) {
        MyApplication application = new MyApplication();
        try {
            Log.i("CRASH", ">>> toast long >> baseactivity");
            if (ctx != null)
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ToastShort(Context ctx, String msg) {
        MyApplication application = new MyApplication();
        try {
            Log.i("CRASH", ">>> toast short >> baseactivity");
            if (ctx != null)
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volleyGetRequest(final String mUrl, final String jsonParams, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            // ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest GetRequest = new JsonObjectRequest(Request.Method.GET, mUrl,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                Log.d("VOLLEY", "\nResponse :\n\n " + response.toString());
                                //CommonFunctions.printLog("d", "\nResponse :\n\n ", response.toString());
                                callback.onSuccess(response.toString());
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e("VOLLEY", "\n\n\n*******************************************************************");
                                    Log.i("VOLLEY", "\nURL:\n " + mUrl);
                                    Log.i("VOLLEY", "\njsonParams: \n" + jsonParams);
                                    Log.d("VOLLEY", "\nResponse :\n\n " + error.networkResponse.statusCode);
                                    Log.e("TAG", "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode + "\n mUrl: " + mUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    //headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());

                    return headers;
                }
            };
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            GetRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(GetRequest, tag_json_obj,getInterceptor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleygetRequest_JSON(final String mUrl, String jsondata, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            // ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            JSONObject data = null;
            //RequestQueue queue = Volley.newRequestQueue(this);
            try {
                data = new JSONObject(jsondata);
            } catch (Exception j) {
                j.printStackTrace();
            }
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, mUrl, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d(TAG, "response : " + response.toString());
                        callback.onSuccess(response.toString());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e(TAG, "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    //headers.put("Authorization", "Bearer " + sharedStorage.getAccessToken());
                    Log.i("TAG", "Headers: " + headers.toString());
                    return headers;
                }
            };


            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            getRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(getRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);
            Log.i(TAG, "volleyGetRequest - getRequest : " + getRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyGetRequest - Exception : " + e.getMessage());
        }
    }

    public void volleygetRequest_Print(final String mUrl, String jsondata, final VolleyCallback callback) {
        if (!isNetworkConnected()) {
            //ToastShort(this, getResources().getString(R.string.no_internet));
            callback.onError("No internet");
            return;
        }
        try {
            String tag_json_obj = "json_obj_req";
            JSONObject data = null;
            //RequestQueue queue = Volley.newRequestQueue(this);
            try {
                data = new JSONObject(jsondata);
            } catch (Exception j) {
                j.printStackTrace();
            }
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, mUrl, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d(TAG, "response : " + response.toString());
                        callback.onSuccess(response.toString());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Get response code here
                            error.printStackTrace();
                            if (error != null) {
                                try {
                                    Log.e(TAG, "VolleyError  - networkResponse.statusCode : " + error.networkResponse.statusCode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e(TAG, "VolleyError  : " + error);
                            callback.onError(error.toString());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + Base64.NO_WRAP);
                    //headers.put("Authorization", "bearer " + sharedStorage.getAccessToken());
                    return headers;
                }
            };


            //DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
            //queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //queue.start();

            // clear all volley caches.
            //queue.add(new ClearCacheRequest(cache, null));
            int socketTimeout = 30000;//50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            getRequest.setRetryPolicy(policy);
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(getRequest, tag_json_obj,getInterceptor());
            //queue.add(postRequest);
            Log.i(TAG, "volleyGetRequest - getRequest : " + getRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "volleyGetRequest - Exception : " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("TAG", "onBackPressed - baseActivity");
        finish();
    }



    @SuppressLint("MissingPermission")
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public ArrayList<MenuItems> getMenuData() {

        MenuItems menuItem0 = new MenuItems();
        menuItem0.setItemname(getResources().getString(R.string.camera));
        menuItem0.setItemDrawableId(R.drawable.ic_mail);
        mMenuItemsList.add(menuItem0);


        MenuItems menuItem1 = new MenuItems();
        menuItem1.setItemname(getResources().getString(R.string.profile));
        menuItem1.setItemDrawableId(R.drawable.ic_child);
        mMenuItemsList.add(menuItem1);


        MenuItems menuItem2 = new MenuItems();
        menuItem2.setItemname(getResources().getString(R.string.edit));
        menuItem2.setItemDrawableId(R.drawable.ic_edit);
        mMenuItemsList.add(menuItem2);



        return mMenuItemsList;
    }


    private static OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                            okhttp3.Request original = chain.request();
                            okhttp3.Request request = getRequestBuilder(original)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .readTimeout(60 * 5, TimeUnit.SECONDS)
                    .connectTimeout(60 * 5, TimeUnit.SECONDS)
                    .build();


    private static OkHttpClient authClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                            okhttp3.Request original = chain.request();
                            okhttp3.Request request = getRequestBuilder(original)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .readTimeout(60 * 5, TimeUnit.SECONDS)
                    .connectTimeout(60 * 5, TimeUnit.SECONDS)
                    .build();

    private static OkHttpClient getInterceptor() {
        return okHttpClient;
    }
    private static okhttp3.Request.Builder getRequestBuilder(okhttp3.Request request) {
        return request.newBuilder();
    }
}