package com.taxi.passenger.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.taxi.passenger.R;

import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.ProfileEditModel;
import com.taxi.passenger.utils.CircleTransform;
import com.taxi.passenger.utils.Common;

import com.taxi.passenger.utils.Url;
import com.taxi.passenger.utils.UtilityPermissionsCheck;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.taxi.passenger.utils.Common.showMKPanelError;
import static com.taxi.passenger.utils.Constants.REQUEST_CAMERA;

public class UserProfileActivity extends AppCompatActivity implements NetworkInterfaces.ProfileEditWebServiceInterface {

    public static final int SELECT_FILE = 200;
    private static final int MY_REQUEST_CODE = 300;
    private TextView mTvProfile;
    private TextView mTvName;
    private EditText mEtName;
    TextView mTvUserName;
    EditText mEtUserName;
    TextView mTvMobile;
    private RequestBody reqFile;
    EditText mEtMobile;
    TextView mTvEmail;
    EditText mEtEmail;
    RelativeLayout mRlBackArrow;
    CircleImageView mIvAddImage;
    TextView mTvDob;
    EditText mEtDob;
    Spinner spinnerGender;
    RelativeLayout mRlSave;
    TextView mTvSave;

    String genderString = "gender";

    Typeface OpenSans_Regular, OpenSans_Bold, Roboto_Regular, Roboto_Medium, Roboto_Bold;
    SharedPreferences userPref;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;

    Dialog OpenCameraDialog;


    File userImage;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    //Error Alert
    RelativeLayout rlMainView;
    TextView tvTitle;
    private String userChoosenTask = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setBackgroundDrawableResource(R.drawable.background);

        //Error Alert
        rlMainView =findViewById(R.id.rlMainView);
        tvTitle = findViewById(R.id.tvTitle);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int) getResources().getDimension(R.dimen.height_50), 0, 0);
        rlMainView.setLayoutParams(params);

        mTvProfile = findViewById(R.id.txt_profile);
        mTvName = findViewById(R.id.txt_name);
        mEtName = findViewById(R.id.edt_name);
        mTvUserName = findViewById(R.id.txt_user_name);
        mEtUserName = findViewById(R.id.edt_user_name);
        mTvMobile = findViewById(R.id.txt_user_mobile);
        mEtMobile = findViewById(R.id.edt_mobile);
        mTvEmail = findViewById(R.id.txt_user_email);
        mEtEmail = findViewById(R.id.edt_email);

        mRlBackArrow = findViewById(R.id.layout_back_arrow);
        mIvAddImage = findViewById(R.id.img_add_image);
        mTvDob = findViewById(R.id.txt_date_of_birth);
        mEtDob = findViewById(R.id.edt_date_of_birth_val);
        spinnerGender = findViewById(R.id.spinner_gender);
        mRlSave = findViewById(R.id.layout_save);
        mTvSave = findViewById(R.id.txt_save);

        ProgressDialog = new Dialog(UserProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);

        userPref = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);

        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        Roboto_Regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        mTvProfile.setTypeface(OpenSans_Bold);
        mTvSave.setTypeface(Roboto_Regular);

        mTvName.setTypeface(Roboto_Regular);
        mEtName.setTypeface(Roboto_Regular);
        mTvUserName.setTypeface(Roboto_Regular);
        mEtUserName.setTypeface(Roboto_Regular);
        mTvMobile.setTypeface(Roboto_Regular);
        mEtMobile.setTypeface(Roboto_Regular);
        mTvEmail.setTypeface(Roboto_Regular);

        mEtDob.setTypeface(Roboto_Regular);
        mTvDob.setTypeface(Roboto_Regular);

        mEtEmail.setTypeface(Roboto_Regular);

        mEtUserName.setText(userPref.getString("username", ""));
        mEtName.setText(userPref.getString("name", ""));
        mEtMobile.setText(userPref.getString("mobile", ""));
        mEtEmail.setText(userPref.getString("email", ""));
        mEtDob.setText(userPref.getString("date_of_birth", ""));

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String facebook_id = userPref.getString("facebook_id", "");
        Log.d("facebook_id", "facebook_id = " + facebook_id);
        //noinspection ConstantConditions
        if (facebook_id != null && !facebook_id.equals("") && userPref.getString("userImage", "").equals("")) {
            String facebookImage = Url.FacebookImgUrl + facebook_id + "/picture?type=large";
            Log.d("facebookImage", "facebookImage = " + facebookImage);
            Picasso.with(UserProfileActivity.this)
                    .load(facebookImage)
                    .placeholder(R.drawable.avatar_placeholder)
                    .resize(200, 200)
                    .transform(new CircleTransform())
                    .into(mIvAddImage);
        } else {
            Picasso.with(UserProfileActivity.this)
                    .load(Uri.parse(Url.USER_IMAGE_URL + userPref.getString("userImage", "")))
                    .placeholder(R.drawable.mail_defoult)
                    .transform(new CircleTransform())
                    .into(mIvAddImage);
        }

        mRlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtName.getText().toString().trim().length() == 0) {
                    showMKPanelError(UserProfileActivity.this, getResources().getString(R.string.please_enter_name), rlMainView, tvTitle, Roboto_Regular);
                    mEtName.requestFocus();
                    return;
                } else if (mEtUserName.getText().toString().trim().length() == 0) {
                    showMKPanelError(UserProfileActivity.this, getResources().getString(R.string.please_enter_username), rlMainView, tvTitle, Roboto_Regular);
                    mEtUserName.requestFocus();
                    return;
                } else if (mEtMobile.getText().toString().trim().length() == 0) {
                    showMKPanelError(UserProfileActivity.this, getResources().getString(R.string.please_enter_mobile), rlMainView, tvTitle, Roboto_Regular);
                    mEtMobile.requestFocus();
                    return;
                } else if (mEtEmail.getText().toString().trim().length() == 0) {
                    showMKPanelError(UserProfileActivity.this, getResources().getString(R.string.please_enter_email), rlMainView, tvTitle, Roboto_Regular);
                    mEtEmail.requestFocus();
                    return;
                } else if (mEtEmail.getText().toString().trim().length() != 0 && !isValidEmail(mEtEmail.getText().toString().trim())) {
                    showMKPanelError(UserProfileActivity.this, getResources().getString(R.string.please_enter_valid_email), rlMainView, tvTitle, Roboto_Regular);
                    mEtEmail.requestFocus();
                    return;
                }


                ProgressDialog.show();
                cusRotateLoading.start();


                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), mEtName.getText().toString());
                RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), mEtUserName.getText().toString());
                RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), userPref.getString("id", ""));
                RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mEtMobile.getText().toString());
                RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), mEtDob.getText().toString());
                RequestBody isdevice = RequestBody.create(MediaType.parse("text/plain"), "" + 1);
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), mEtEmail.getText().toString());
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), genderString);


                if (userImage != null) {

                    reqFile = RequestBody.create(MediaType.parse("image/*"), userImage);

                    WebService.getInstance().callProfileEdit(name, email, userName, mobile, uid, isdevice, dob, gender, reqFile, UserProfileActivity.this);
                } else {
                    reqFile = RequestBody.create(MediaType.parse("text/plain"), userPref.getString("image", ""));
                    WebService.getInstance().callProfileEditWithoutImage(name, email, userName, mobile, uid, isdevice, dob, gender, reqFile, UserProfileActivity.this);
                }
            }
        });

        mRlBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenCameraDialog = new Dialog(UserProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                OpenCameraDialog.setContentView(R.layout.camera_dialog_layout);
                OpenCameraDialog.show();

                RelativeLayout layout_open_camera = OpenCameraDialog.findViewById(R.id.layout_open_camera);
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        boolean result = UtilityPermissionsCheck.checkPermission(UserProfileActivity.this);
                        boolean result2 = UtilityPermissionsCheck.checkPermissionWriteExternal(UserProfileActivity.this);
                        boolean result1 = UtilityPermissionsCheck.checkcheckPermissionCamera(UserProfileActivity.this);


                        if (result && result1 && result2) {
                            OpenCameraDialog.cancel();
                            userChoosenTask = "Take Photo";
                            cameraImageIntent();
                        }

                    }
                });

                RelativeLayout layout_open_gallery = OpenCameraDialog.findViewById(R.id.layout_open_gallery);

                layout_open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UtilityPermissionsCheck.checkPermission(UserProfileActivity.this) && UtilityPermissionsCheck.checkPermissionWriteExternal(UserProfileActivity.this)) {
                            OpenCameraDialog.cancel();
                            userChoosenTask = "Choose from Library";
                            galleryImageIntent();
                        }


                    }
                });

                RelativeLayout layout_open_cancel = OpenCameraDialog.findViewById(R.id.layout_open_cancel);
                layout_open_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenCameraDialog.cancel();
                    }
                });


            }
        });

        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.gender_spinner_layout);
        spinnerGender.setAdapter(dataAdapter);
        for (int si = 0; si < list.size(); si++) {
            if (userPref.getString("gender", "").equals(list.get(si))) {
                spinnerGender.setSelection(si);
            }
        }
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //noinspection deprecation
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                ((TextView) parent.getChildAt(0)).setTextSize(16);
                ((TextView) parent.getChildAt(0)).setTypeface(OpenSans_Regular);
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.RIGHT);
                genderString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myCalendar = Calendar.getInstance();


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                Date d = null;
                try {
                    d = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -15);
                if (minAdultAge.before(userAge)) {
                    mEtDob.setText(userPref.getString("date_of_birth", ""));
                    Common.showMkError(UserProfileActivity.this, getString(R.string.dob_age_validation));

                } else {
                    mEtDob.setText(sdf.format(d));


                }


            }
        };

        mEtDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        mEtDob.setInputType(InputType.TYPE_NULL);
        mEtDob.requestFocus();

        mEtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(UserProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 15)));


                Calendar minCal = Calendar.getInstance();
                minCal.add(Calendar.YEAR, -100);
                long hundredYearsAgo = minCal.getTimeInMillis();
                dpd.getDatePicker().setMinDate(hundredYearsAgo);

                dpd.show();
            }
        });

        Common.ValidationGone(UserProfileActivity.this, rlMainView, mEtName);
        Common.ValidationGone(UserProfileActivity.this, rlMainView, mEtUserName);
        Common.ValidationGone(UserProfileActivity.this, rlMainView, mEtMobile);
        Common.ValidationGone(UserProfileActivity.this, rlMainView, mEtEmail);
        Common.ValidationGone(UserProfileActivity.this, rlMainView, mEtDob);

    }

    private void galleryImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void cameraImageIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTvProfile = null;
        mTvUserName = null;
        mEtUserName = null;
        mTvMobile = null;
        mEtMobile = null;
        mTvEmail = null;
        mEtEmail = null;
        mRlSave = null;
        mTvSave = null;
        mRlBackArrow = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
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
        mIvAddImage.setImageBitmap(thumbnail);

        userImage = savebitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIvAddImage.setImageBitmap(bm);
        userImage = savebitmap(bm);
    }


    private File savebitmap(Bitmap bp) {
        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Navori/";
        File destination = new File(destinationPath);
        if (!destination.exists()) {
            destination.mkdirs();
        }
        String filename = "Snap_" + System.currentTimeMillis() + "_.jpg";
        File myImage = new File(destinationPath + filename);
        try {
            FileOutputStream fos = new FileOutputStream(myImage);
            bp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case UtilityPermissionsCheck.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (userChoosenTask.equals("Take Photo"))
                        cameraImageIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryImageIntent();
                } else {
                    //code for deny
                }
                break;
            case UtilityPermissionsCheck.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (userChoosenTask.equals("Take Photo"))
                        cameraImageIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryImageIntent();
                } else {
                    //code for deny
                }
                break;


            case UtilityPermissionsCheck.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (userChoosenTask.equals("Take Photo"))
                        cameraImageIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryImageIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onSuccess(ProfileEditModel profileEditModel) {
        ProgressDialog.cancel();
        cusRotateLoading.stop();
        SharedPreferences.Editor id = userPref.edit();
        id.putString("id", profileEditModel.getUserDetail().get(0).getId());
        id.commit();

        SharedPreferences.Editor name = userPref.edit();
        name.putString("name", profileEditModel.getUserDetail().get(0).getName());
        name.commit();

        SharedPreferences.Editor username = userPref.edit();
        username.putString("username", profileEditModel.getUserDetail().get(0).getUsername());
        username.commit();

        SharedPreferences.Editor mobile = userPref.edit();
        mobile.putString("mobile", profileEditModel.getUserDetail().get(0).getMobile());
        mobile.commit();

        SharedPreferences.Editor email = userPref.edit();
        email.putString("email", profileEditModel.getUserDetail().get(0).getEmail());
        email.commit();

        SharedPreferences.Editor isLogin = userPref.edit();
        isLogin.putString("isLogin", "1");
        isLogin.commit();

        SharedPreferences.Editor userImage = userPref.edit();
        userImage.putString("userImage", profileEditModel.getUserDetail().get(0).getImage());
        userImage.commit();

        SharedPreferences.Editor dob = userPref.edit();
        Log.d("dateUserProfile", profileEditModel.getUserDetail().get(0).getDob());
        dob.putString("date_of_birth", profileEditModel.getUserDetail().get(0).getDob());
        dob.commit();

        SharedPreferences.Editor gender = userPref.edit();
        gender.putString("gender", profileEditModel.getUserDetail().get(0).getGender());
        gender.commit();
        Common.showMkSucess(UserProfileActivity.this, getResources().getString(R.string.profile_update_sucess), "yes");
        Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure() {
        ProgressDialog.cancel();
        cusRotateLoading.stop();
        Common.showMkSucess(UserProfileActivity.this, getResources().getString(R.string.error_msg_forgotPass), "yes");
    }
}
