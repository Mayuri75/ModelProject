package simple.android.example.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mahu.example.R;
import simple.android.example.model.MenuItems;
import simple.android.example.model.MenuLevel1;
import simple.android.example.network.MyApplication;
import simple.android.example.roomdatabase.RoomDataBaseActivity;
import simple.android.example.roomdatabasesecond.RoomdbActivity;
import simple.android.example.utils.LocalSharedStorage;
import simple.android.example.utils.RealPathUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.LEFT;

public class MainActivity extends BaseActivity {
   // ActivityMainBinding binding;
    private final static int REQUEST_ID_MULTIPLE_CAMERA_PERMISSIONS = 1;
    String[] cameraPermissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    MenuAdapter mMenuAdapter;
    //MenuAdapterLevel1 mMenuAdapterLevel1;
    public DrawerLayout mDrawerLayout;
    LinearLayout mLeftDrawerList;
    ListView mDrawerLevel1List, mDrawerLevel2List;
    public ActionBarDrawerToggle mDrawerToggle;
    View listHeaderView;
    RelativeLayout logolayout;
    Toolbar mActionView;
    TextView tvActionbarTitle2;
    RelativeLayout rl_propertysearch, rl_propertyedit;
    ArrayList<MenuItems> mMenuItemsList = new ArrayList<>();
    ArrayList<MenuLevel1> mMenuLevel1List = new ArrayList<>();
    String USER_CAMERA = "1";
    String USER_GALLERY = "2";
    private boolean sentToSettings = false;
    private static String permissionAction = "";
    String userChoosenTask = "";
    int positionToUpdateImage = -1;
    String profilePath = "";
    Context context;
    LinearLayout  llprofile,UserData,llroomdb,llroomdb2,llcall,llwifi,llliveTrack,llbtmSheet,llclearCache;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
     //   binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setContentView(R.layout.activity_main);
        LocalSharedStorage sharedpref = new LocalSharedStorage(getApplicationContext());
        sharedpref.saveUserEmail("prashant.karamadi");
        llprofile = findViewById(R.id.llprofile);
        UserData = findViewById(R.id.UserData);
        llroomdb = findViewById(R.id.llroomdb);
        llroomdb2 = findViewById(R.id.llroomdb2);
        llcall = findViewById(R.id.llcall);
        llwifi = findViewById(R.id.llwifi);
        llliveTrack = findViewById(R.id.llliveTrack);
        llbtmSheet = findViewById(R.id.llbtmSheet);
        llclearCache = findViewById(R.id.llclearCache);
        llclearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((ActivityManager)context.getSystemService(ACTIVITY_SERVICE))
                                .clearApplicationUserData(); // note: it has a return value!
                    }*/
                    File dir = context.getCacheDir();
                   // deleteDir(dir);
                }catch (Exception e){
                    e.printStackTrace();
                }



                // use old hacky way, which can be removed
                    // once minSdkVersion goes above 19 in a few years.
                    /*try {
                        killProcessesAround(MainActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

            }
        });


       // getSupportActionBar().hide();
        mActionView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionView);
     llprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        llbtmSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BottomSheetActivity.class);
                startActivity(intent);
            }
        });
      UserData.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,UserDataActivity.class);
               startActivity(intent);
           }
       });
       llliveTrack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,LiveTrackActivity.class);
               startActivity(intent);
           }
       });
        llroomdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomDataBaseActivity.class);
                startActivity(intent);
            }
        });
   llroomdb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomdbActivity.class);
                startActivity(intent);
            }
        });
   llcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Call_And_messageActivity.class);
                startActivity(intent);
            }
        });
       llwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WifiActivity.class);
                startActivity(intent);
            }
        });



        getDocumentType();
       // mActionView.setVisibility(View.GONE);

//        setSupportActionBar(mActionView);
        mActionView.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fx_menu_arrow, null);
                mActionView.setNavigationIcon(d);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            int currentapiVersion = Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = this.getWindow();

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                // finally change the color
                // window.setStatusBarColor(getResources().getColor(R.color.daysLabelColor));
            } else {
                // do something for phones running an SDK before lollipop
            }
        }

        mMenuItemsList = getMenuData();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dash_drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawerLevel1List = (ListView) findViewById(R.id.drawer_level1);
        mDrawerLevel2List = (ListView) findViewById(R.id.drawer_level2);

        LayoutInflater inflater = getLayoutInflater();
        listHeaderView = inflater.inflate(R.layout.drawer_menu_header, null, false);
        logolayout = (RelativeLayout) listHeaderView.findViewById(R.id.logolayout);


        mDrawerLevel1List.addHeaderView(listHeaderView);


        mMenuAdapter = new MenuAdapter(this, mMenuItemsList);
        mMenuLevel1List = mMenuItemsList.get(0).getMenuLevel1List();
        //mMenuAdapterLevel1 = new MenuAdapterLevel1(this, mMenuLevel1List);
        mDrawerLayout.setDrawerShadow(R.mipmap.ic_launcher, GravityCompat.START);


        mDrawerLevel1List.setAdapter(mMenuAdapter);
        //  mDrawerLevel2List.setAdapter(mMenuAdapterLevel1);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
        // mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLevel1List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // mDrawerLayout.closeDrawer(mDrawerList);
                Log.i("TAG", "currTabPosition: " + position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 1) {
                            positionToUpdateImage = -1;
                            showMyCustomConfirmDialog();

                        }

                    }
                }, 250);
            }
        });




    }
    public class MenuAdapter extends BaseAdapter {
        private LayoutInflater lInflater;

        MyApplication subApplication;
        Context mContext;


        public MenuAdapter(Context context, List<MenuItems> customizedListView) {
            mContext = context;
            lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            subApplication = new MyApplication();
        }

        @Override
        public int getCount() {
            return mMenuItemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder listViewHolder;
            if (convertView == null) {
                listViewHolder = new ViewHolder();
                convertView = lInflater.inflate(R.layout.item_row, parent, false);

                listViewHolder.rowText = (TextView) convertView.findViewById(R.id.rowText);
                listViewHolder.rowIcon = (ImageView) convertView.findViewById(R.id.rowIcon);
                listViewHolder.rowArrow = (ImageView) convertView.findViewById(R.id.rowArrow);

                if ((position==0) || (position == 7)){

                    listViewHolder.rowArrow.setVisibility(View.GONE);

                } else
                    listViewHolder.rowArrow.setVisibility(View.VISIBLE);
                convertView.setTag(listViewHolder);

            } else {
                listViewHolder = (ViewHolder) convertView.getTag();
            }
            listViewHolder.rowText.setText(mMenuItemsList.get(position).getItemname());
            listViewHolder.rowIcon.setImageResource(mMenuItemsList.get(position).getItemDrawableId());


            return convertView;
        }

        class ViewHolder {

            ImageView rowIcon;
            ImageView rowArrow;
            TextView rowText;


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.i("TAG", "onOptionsItemSelected");

            mDrawerLayout.openDrawer(LEFT);



        }
        return super.onOptionsItemSelected(item);
    }
    public void getDocumentType() {
        BaseActivity.VolleyCallback callback = new BaseActivity.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();

                if (result != null && result.length() > 0) {
                    try {
                        Log.e("TAG", "roomBlock - result : \n" + result.toString());
                        JSONObject jsonData = new JSONObject(result);
                        String deviceStatus = jsonData.optString("Status", "");
                        if (deviceStatus != null && deviceStatus.length() > 0) {
                            if (deviceStatus.equalsIgnoreCase("Success")) {
                                Gson gson = new Gson();
                                JSONObject jsonResponse = new JSONObject(result);

                            }
                        }
                    } catch (JsonSyntaxException e) {
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                    }
                } else {
                }

            }

            @Override
            public void onError(String result) {
                try {
                    if (result != null) {
                        Log.d(TAG, "EnvironmentUrl.fetchDeviceURL() - result : " + result.toString());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
        volleyGetRequest("https://podtestcgsr0s9x4f.hana.ondemand.com/POD/rest/login", callback);
    }
    public void getApprove() {
        BaseActivity.VolleyCallback callback = new BaseActivity.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();

                if (result != null && result.length() > 0) {
                    try {
                        Log.e("TAG", "roomBlock - result : \n" + result.toString());
                        JSONObject jsonData = new JSONObject(result);
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        String deviceStatus = jsonData.optString("Status", "");
                        if (deviceStatus != null && deviceStatus.length() > 0) {
                            if (deviceStatus.equalsIgnoreCase("Success")) {
                                Gson gson = new Gson();
                                JSONObject jsonResponse = new JSONObject(result);

                            }
                        }
                    } catch (JsonSyntaxException e) {
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                    }
                } else {
                }

            }

            @Override
            public void onError(String result) {
                try {
                    if (result != null) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "EnvironmentUrl.fetchDeviceURL() - result : " + result.toString());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
        volleyGetRequest("https://podcgsr0s9x4f.hana.ondemand.com/POD/rest/login", callback);
    }
    public void showMyCustomConfirmDialog() {
        userChoosenTask = "";
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_photo_dialog);

        TextView textViewTitle = (TextView) dialog.findViewById(R.id.tvDialogTitle);
        textViewTitle.setTextColor(getResources().getColor(R.color.colorBlack));
        //application.setTypeface(textViewTitle, FONT_NARROW_BOLD, context);
        textViewTitle.setTextSize(20);

        TextView textViewTakepic = (TextView) dialog.findViewById(R.id.tvDialogContent);
        textViewTakepic.setText(getString(R.string.photo_option_camera));
        textViewTakepic.setTextColor(getResources().getColor(R.color.colorBlack));
        //application.setTypeface(textViewTakepic, FONT_NARROW, context);
        textViewTakepic.setTextSize(18);
        textViewTakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = getString(R.string.photo_option_camera);
                checkCameraPermission(USER_CAMERA);
                dialog.cancel();
            }
        });

        TextView textviewChoose = (TextView) dialog.findViewById(R.id.tvDialogContent1);
        textviewChoose.setText(getString(R.string.photo_option_gallery));
        textviewChoose.setTextColor(getResources().getColor(R.color.colorBlack));
        //application.setTypeface(textviewChoose, FONT_NARROW, context);
        textviewChoose.setTextSize(18);
        textviewChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = getString(R.string.photo_option_gallery);
                checkCameraPermission(USER_GALLERY);
                dialog.cancel();
            }
        });

        TextView negative_textview = (TextView) dialog.findViewById(R.id.tvDialogCancel);
        negative_textview.setTextColor(getResources().getColor(R.color.colorBlack));
        //application.setTypeface(negative_textview, FONT_NARROW_BOLD, context);
        negative_textview.setTextSize(20);
        negative_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    private void checkCameraPermission(String action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // To check user gone to phone setting page to enable or not
            sentToSettings = false;

            // hold user action whether camera or gallery
            permissionAction = action;

            // Check all permission, if all are granted
            if ((ActivityCompat.checkSelfPermission(this, cameraPermissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) &&
                    (ActivityCompat.checkSelfPermission(this, cameraPermissionsRequired[1]) == PackageManager.PERMISSION_GRANTED) &&
                    (ActivityCompat.checkSelfPermission(this, cameraPermissionsRequired[2]) == PackageManager.PERMISSION_GRANTED)) {
                if (action.equals(USER_CAMERA)) {
                    cameraIntent();
                } else if (action.equals(USER_GALLERY)) {
                    galleryIntent();
                }
            } else {
                requestPermissions(cameraPermissionsRequired, REQUEST_ID_MULTIPLE_CAMERA_PERMISSIONS);
            }
        } else {
            if (action.equals(USER_CAMERA)) {
                cameraIntent();
            } else if (action.equals(USER_GALLERY)) {
                galleryIntent();
            }
        }
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.label_Select_File)), 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == Activity.RESULT_OK) {
           /* String mDate = data.getStringExtra("customdate");
            String customDate = CommonFunctions.getCustomeMonthStringValue(mDate);*/
            /*if (!CommonFunctions.isEmpty(customDate)) {
                String[] cusValues = customDate.split("-");
                if (cusValues != null && cusValues.length == 3) {
                        *//*if (requestCode == 200) {
                            issuedDate = data.getStringExtra("customdate");
                            ;
                            ((TextView) mView.findViewById(R.id.tv_issuedate)).setText(cusValues[0]);
                            ((TextView) mView.findViewById(R.id.tv_issuemonth)).setText(cusValues[1]);
                            ((TextView) mView.findViewById(R.id.tv_issueyear)).setText(cusValues[2]);
                        } else*//* *//*if (requestCode == 300) {
                            expiryDate = data.getStringExtra("customdate");
                            ;
                            ((TextView) mView.findViewById(R.id.tv_expirydate)).setText(cusValues[0]);
                            ((TextView) mView.findViewById(R.id.tv_expirymonth)).setText(cusValues[1]);
                            ((TextView) mView.findViewById(R.id.tv_expiryyear)).setText(cusValues[2]);
                        } else if (requestCode == 400) {
                            dob = data.getStringExtra("customdate");
                            ;
                            ((TextView) mView.findViewById(R.id.tv_dobdate)).setText(cusValues[0]);
                            ((TextView) mView.findViewById(R.id.tv_dobMonth)).setText(cusValues[1]);
                            ((TextView) mView.findViewById(R.id.tv_dobYear)).setText(cusValues[2]);
                        } else if (requestCode == 500) {
                            anniversary = data.getStringExtra("customdate");
                            ;
                            ((TextView) mView.findViewById(R.id.tv_anniversarydate)).setText(cusValues[0]);
                            ((TextView) mView.findViewById(R.id.tv_anniversaryMonth)).setText(cusValues[1]);
                            ((TextView) mView.findViewById(R.id.tv_anniversaryYear)).setText(cusValues[2]);
                        }*//*
                }
            } else*/ if (requestCode == 1) {
                getImageGallery(data);
            } else if (requestCode == 2) {
                onCaptureImageResult(data);
            }
        }


    }
    void getImageGallery(Intent data) {

        if (data != null) {
            String realPath;
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            //  realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), data.getData());

            Log.i("TAG", "getImageGallery: " + realPath);
            Uri uriFromPath = Uri.fromFile(new File(realPath));
            Log.i("TAG", "uriFromPath: " + uriFromPath);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uriFromPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Log.i("TAG", "bitmap: " + bitmap);
            Log.d("TAG", "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
            Log.d("TAG", "URI Path:" + data.getData().getPath());
            Log.d("TAG", "Real Path: " + realPath);

            if (positionToUpdateImage == -1) {
               // binding.imgGuestProfile.setImageBitmap(bitmap);
                profilePath = realPath;
                /*lbl_profileImageAction.setText("Change Photo");
                imgProfileAction.setImageResource(R.drawable.ic_delete_white);*/
            } /*else {
                if (imagePos == 1) {
                    gDocType1.get(positionToUpdateImage).setDocFrontFilePath(realPath);
                } else if (imagePos == 2) {
                    gDocType1.get(positionToUpdateImage).setDocBackFilePath(realPath);
                }
                if (docTypeAdapter != null) {
                    docTypeAdapter.notifyDataSetChanged();
                }*/

        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = null;
        if (data != null && data.getExtras() != null) {
            thumbnail = (Bitmap) data.getExtras().get("data");
        }

        if (thumbnail != null) {
            Uri tempUri = getImageUri(this.getApplicationContext(), thumbnail);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String fPath = getRealPathFromURIForCamera(tempUri);
            if (positionToUpdateImage == -1) {
                profilePath = fPath;
                //binding.imgGuestProfile.setImageBitmap(thumbnail);
               /* lbl_profileImageAction.setText("Change Photo");
                imgProfileAction.setImageResource(R.drawable.ic_delete_white);*/
            } /*else {
                if (imagePos == 1) {
                    gDocType1.get(positionToUpdateImage).setDocFrontFilePath(fPath);
                } else if (imagePos == 2) {
                    gDocType1.get(positionToUpdateImage).setDocBackFilePath(fPath);
                }
                if (docTypeAdapter != null) {
                    docTypeAdapter.notifyDataSetChanged();
                }
            }*/
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURIForCamera(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



  /*  @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }}*/

    private static void killProcessesAround(Activity activity) throws PackageManager.NameNotFoundException {
        ActivityManager am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
        String myProcessPrefix = activity.getApplicationInfo().processName;
        String myProcessName = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0).processName;
        for (ActivityManager.RunningAppProcessInfo proc : am.getRunningAppProcesses()) {
            if (proc.processName.startsWith(myProcessPrefix) && !proc.processName.equals(myProcessName)) {
                android.os.Process.killProcess(proc.pid);
            }
        }
    }


    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        } }
    }
