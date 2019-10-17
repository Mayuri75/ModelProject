package simple.android.example.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mahu.example.R;
import simple.android.example.model.MenuItems;
import simple.android.example.model.MenuLevel1;
import simple.android.example.network.MyApplication;
import simple.android.example.utils.RealPathUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.Gravity.LEFT;

public class ProfileActivity extends BaseActivity {
 //   ActivityProfileBinding binding;
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
    int width;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        mActionView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionView);
       /* binding.rlTakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionToUpdateImage = -1;
                showMyCustomConfirmDialog();
            }
        });*/
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabel();
            }
        };

      /*  binding.tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                //Dont'n want future date
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });
        binding.tvdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(ProfileActivity.this);
            }
        });
        binding.tvdate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDatePicker();
            }
        });
        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertTimePicker();
            }
        });
        binding.time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.time2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        String barcode_content="Z123456";
        Bitmap bm = null;
        try {
            bm = encodeAsBitmap(barcode_content);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if(bm != null) {
            binding.ivBarCode.setImageBitmap(bm);
        }*/
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


                        }

                    }
                }, 250);
            }
        });
        getDocumentType();

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
                Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();

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
                        //Log.d(TAG, "EnvironmentUrl.fetchDeviceURL() - result : " + result.toString());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
        volleyGetRequest("https://profile-data-services-p.cfapps.eu10.hana.ondemand.com/lookup/country", callback);
    }
    public void getApprove() {
        BaseActivity.VolleyCallback callback = new BaseActivity.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

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
                        Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "EnvironmentUrl.fetchDeviceURL() - result : " + result.toString());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
        volleyGetRequest("https://userregistration.cfapps.eu10.hana.ondemand.com/user/contact/pending", callback);
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
              //  binding.imgGuestProfile.setImageBitmap(bitmap);
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
               // binding.imgGuestProfile.setImageBitmap(thumbnail);
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
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.CODE_128, width, width, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Log.e("Tag","BitmapData"+bitmap);

        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String sendFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        SimpleDateFormat sdf1 = new SimpleDateFormat(sendFormat, Locale.US);

        //binding.tvdate.setText(sdf.format(myCalendar.getTime()));
        String sendDate = sdf1.format(myCalendar.getTime());
    }

    public void datePicker( Context context) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDate.set(year, monthOfYear, dayOfMonth);
             //   binding.tvdate.setText(dateFormatter.format(newDate.getTime()));

            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    public void alertDatePicker() {

        /*
         * Inflate the XML view. activity_main is in res/layout/date_picker.xml
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker, null, false);

        // the time picker on the alert dialog, this is how to get the value
        final DatePicker myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);

        // so that the calendar view won't appear
        myDatePicker.setCalendarViewShown(false);

        // the alert dialog
        new AlertDialog.Builder(ProfileActivity.this).setView(view)
                .setTitle("Set Date")
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        /*
                         * In the docs of the calendar class, January = 0, so we
                         * have to add 1 for getting correct month.
                         * http://goo.gl/9ywsj
                         */
                        int month = myDatePicker.getMonth() + 1;
                        int day = myDatePicker.getDayOfMonth();
                        int year = myDatePicker.getYear();

                        ToastShort(ProfileActivity.this,month + "/" + day + "/" + year);



                        dialog.cancel();

                    }

                }).show();
    }
    /*
     * Show AlertDialog with time picker.
     */
    public void alertTimePicker() {

        /*
         * Inflate the XML view. activity_main is in res/layout/time_picker.xml
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker, null, false);

        // the time picker on the alert dialog, this is how to get the value
        final TimePicker myTimePicker = (TimePicker) view
                .findViewById(R.id.myTimePicker);

        /*
         * To remove option for AM/PM, add the following line:
         *
         * operatingHoursTimePicker.setIs24HourView(true);
         */

        // the alert dialog
        new AlertDialog.Builder(ProfileActivity.this).setView(view)
                .setTitle("Set Time")
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        String currentHourText = myTimePicker.getCurrentHour()
                                .toString();

                        String currentMinuteText = myTimePicker
                                .getCurrentMinute().toString();

                        // We cannot get AM/PM value since the returning value
                        // will always be in 24-hour format.
                        ToastShort(ProfileActivity.this,currentHourText + ":" + currentMinuteText);



                        dialog.cancel();

                    }

                }).show();
    }

}
