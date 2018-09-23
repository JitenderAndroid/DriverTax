package com.taxi.passenger.SignUpMvp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taxi.passenger.R;
import com.taxi.passenger.SignUpMvp.Presenter.SignUpPresenterImp;
import com.taxi.passenger.SignUpMvp.View.SignUpView;
import com.taxi.passenger.activity.HomeActivity;
import com.taxi.passenger.gpsLocation.GPSTracker;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.CabDetail;
import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;
import com.taxi.passenger.utils.CircleTransform;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Url;
import com.taxi.passenger.utils.UtilityPermissionsCheck;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.taxi.passenger.R.id.edit_email;
import static com.taxi.passenger.R.id.edit_name;

import static com.taxi.passenger.utils.Common.showMKPanelError;
import static com.taxi.passenger.utils.Constants.REQUEST_CAMERA;

@ReportsCrashes(
        mailTo = "abhilasha@yopmail.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash)
public class SignUpActivity extends AppCompatActivity implements NetworkInterfaces.RegisterWebServiceInterface,SignUpView {
    private EditText mEtUserName;
    private EditText mEtName;
    private EditText mEtMobile;
    public static final int SELECT_FILE = 200;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private RelativeLayout mRlSignUp;
    private CircleImageView mIvAddImage;
    private EditText edit_date_of_birth;
    private Spinner spinner_gender;
    private RelativeLayout layout_show_hide;
    private TextView txt_hide_show;
    public int isSelectPhoto = 0;
    String  token;
    private RelativeLayout layout_info_panel;
    private TextView subtitle, txt_sign_up_logo, txt_signup;
    private ScrollView profile_scrollview;
    private Typeface OpenSans_Regular, regularRoboto, Roboto_Bold;
    private Dialog loader;
    private RotateLoading cusRotateLoading;
    private String facebook_id = "";
    private String twitter_id = "";

    private String facebook_email = "";
    private String facebook_name = "";
    private String genderString = "gender";
    private Dialog OpenCameraDialog;
    private Uri mCapturedImageURI;
    private File userImage;
    private SharedPreferences userPref;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private double PickupLongtude;
    private double PickupLatitude;
    private boolean passwordShowHide = false;
    private Pattern letter;
    private Pattern digit = Pattern.compile("[0-9]");
    //    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
//    boolean newFocuValidation = false;
    //Error Alert
    private RelativeLayout rlMainView;
    private TextView tvTitle;

    private SignUpPresenterImp mSignUpPresenterImp;
    private String userChoosenTask = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ACRA.init(getApplication());
        mSignUpPresenterImp= new SignUpPresenterImp(this);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        Log.d("Locale", "Locale Language = " + Locale.getDefault().getLanguage());
        if (Locale.getDefault().getLanguage().equals("en")) {
            letter = Pattern.compile("[a-zA-z]");
            Log.d("Locale", "Locale Language one= " + Locale.getDefault().getLanguage());
        } else {
            letter = Pattern.compile("[^x00-x7F]");
        }
        loader = new Dialog(SignUpActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        loader.setContentView(R.layout.custom_progress_dialog);
        loader.setCancelable(false);
        initViews();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        GPSTracker gpsTracker = new GPSTracker(SignUpActivity.this);
        PickupLatitude = gpsTracker.getLatitude();
        PickupLongtude = gpsTracker.getLongitude();
        userPref = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
        initTypeFace();
        setTypeFace();
        facebook_id = getIntent().getStringExtra("facebook_id");
        facebook_email = getIntent().getStringExtra("facebook_email");
        facebook_name = getIntent().getStringExtra("facebook_name");
        twitter_id = getIntent().getStringExtra("twitter_id");
        mEtName.setText(facebook_name);
        mEtEmail.setText(facebook_email);
        mEtName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        EdittextActonListner(mEtName, "name");
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    mEtName.removeTextChangedListener(this);
                    // change the text

                    String text = s.toString();
                    String upperString = "";
                    if (s.length() != 0)
                        upperString = text.substring(0, 1).toUpperCase() + text.substring(1, text.length());

                    Log.d("upperString", "upperString = " + upperString);
                    mEtName.setText(upperString);
                    // enable it again
                    mEtName.addTextChangedListener(this);
                    mEtName.setSelection(mEtName.getText().length());
                }
            }
        });

        EdittextActonListner(mEtUserName, "username");

        EdittextActonListner(mEtMobile, "mobile");

        EdittextActonListner(mEtEmail, "email");


        mEtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("hasFocus", "hasFocus password= " + hasFocus);
                if (hasFocus) {

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(mEtPassword, InputMethodManager.SHOW_IMPLICIT);
//                        }
//                    }, 500);

                    subtitle.setText("");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LayoutEnimation(R.color.dialog_hit_color, getResources().getString(R.string.password_valid));
                        }
                    }, 100);

                }
            }
        });
        EdittextActonListner(mEtPassword, "password");

        EdittextActonListner(mEtConfirmPassword, "confirm_password");

        mRlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isvalid = ValidationRegister();
                if (isvalid) {
                    if (Common.isNetworkAvailable(SignUpActivity.this)) {
                        //postSignup();

                        RequestBody facebook,twitterId;
                        //File userFile = new File(userImage.getPath());
                        String isDevice = "1";
                        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), mEtName.getText().toString());
                        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), mEtUserName.getText().toString());
                        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), mEtPassword.getText().toString());
                        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mEtMobile.getText().toString());
                        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), edit_date_of_birth.getText().toString());
                        RequestBody isdevice = RequestBody.create(MediaType.parse("text/plain"), "" + 1);
                        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), mEtEmail.getText().toString());
                        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), spinner_gender.getSelectedItem().toString());
                      /*  File file = new File(mCapturedImageURI.getPath());*/
                        RequestBody device_token= RequestBody.create(MediaType.parse("text/plain"), token);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image*//*"),userImage);
                        if (facebook_id != null && !facebook_id.equals("")) {
                            facebook = RequestBody.create(MediaType.parse("text/plain"), facebook_id);
                        }
                        //entityBuilder.addTextBody("facebook_id", facebook_id);
                        else{
                            facebook = RequestBody.create(MediaType.parse("text/plain"),"");
                        }


                        if (twitter_id != null && !twitter_id.equals("")) {
                            twitterId = RequestBody.create(MediaType.parse("text/plain"), twitter_id);
                        }
                        else {
                            twitterId = RequestBody.create(MediaType.parse("text/plain"), "");
                        }


                        mSignUpPresenterImp.getSignUpCredentials( name,
                                email,
                                userName,
                                mobile,
                                password ,
                                isdevice ,
                                device_token,
                                facebook,
                                twitterId,
                                dob,
                                gender,
                                reqFile
                        );
                        // new SighUpUserHttp().execute();
                    } else {
                        Common.showInternetInfo(SignUpActivity.this, "");
                    }
                }

            }


        });
        Log.d("facebook_id", "facebook_id = " + facebook_id);
        if (facebook_id != null && !facebook_id.equals("")) {
            String facebookImage = Url.FacebookImgUrl + facebook_id + "/picture?type=large";
            Log.d("facebookImage", "facebookImage = " + facebookImage);
            Picasso.with(SignUpActivity.this)
                    .load(facebookImage)
                    .placeholder(R.drawable.avatar_placeholder)
                    .resize(200, 200)
                    .transform(new CircleTransform())
                    .into(mIvAddImage);
        }

        mIvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenCameraDialog = new Dialog(SignUpActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                OpenCameraDialog.setContentView(R.layout.camera_dialog_layout);
                OpenCameraDialog.show();

                RelativeLayout layout_open_camera = OpenCameraDialog.findViewById(R.id.layout_open_camera);
                layout_open_camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                /*        boolean result = UtilityPermissionsCheck.checkPermission(SignUpActivity.this);
                        boolean result1 = UtilityPermissionsCheck.checkcheckPermissionCamera(SignUpActivity.this);*/


                        if (UtilityPermissionsCheck.checkPermission(SignUpActivity.this) && UtilityPermissionsCheck.checkcheckPermissionCamera(SignUpActivity.this)&&UtilityPermissionsCheck.checkPermissionWriteExternal(SignUpActivity.this)) {
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

                        if (UtilityPermissionsCheck.checkPermission(SignUpActivity.this)&&UtilityPermissionsCheck.checkPermissionWriteExternal(SignUpActivity.this)) {
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

                // OpenCameraDialog.show();
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
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                edit_date_of_birth.setText(sdf.format(myCalendar.getTime()));
                spinner_gender.performClick();
            }

        };

        edit_date_of_birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_date_of_birth.getWindowToken(), 0);
                    DatePickerDialog dpd = new DatePickerDialog(SignUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    dpd.getDatePicker().setMaxDate((long) (System.currentTimeMillis()- (1000 * 60 * 60 * 24 * 365.50 * 15)));
                    Calendar minCal = Calendar.getInstance();
                    minCal.add(Calendar.YEAR, -100);
                    long hundredYearsAgo = minCal.getTimeInMillis();
                    dpd.getDatePicker().setMinDate(hundredYearsAgo);
                    dpd.show();
                }
            }
        });


        edit_date_of_birth.setInputType(InputType.TYPE_NULL);
        edit_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_date_of_birth.getWindowToken(), 0);
                DatePickerDialog dpd = new DatePickerDialog(SignUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                dpd.show();
            }
        });

        List<String> list = new ArrayList<String>();

        list.add("Male");
        list.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.gender_spinner_layout);
        spinner_gender.setAdapter(dataAdapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) parent.getChildAt(0)).setTextSize(16);
                    ((TextView) parent.getChildAt(0)).setTypeface(OpenSans_Regular);
                    genderString = parent.getItemAtPosition(position).toString();

                    Log.d("genderString", "genderString = " + genderString);
//                if (!genderString.equals("Gender"))
//                    mEtPassword.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        layout_show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowHide) {
                    txt_hide_show.setText(getResources().getString(R.string.show));
                    mEtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordShowHide = false;
                } else {
                    mEtPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                    txt_hide_show.setText(getResources().getString(R.string.hide));
                    passwordShowHide = true;
                }
                mEtPassword.setSelection(mEtPassword.getText().length());

            }
        });

        profile_scrollview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlMainView.getVisibility() == View.VISIBLE) {
                    if (!isFinishing()) {
                        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -100);
                        slideUp.setDuration(10);
                        slideUp.setFillAfter(true);
                        rlMainView.startAnimation(slideUp);
                        slideUp.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                rlMainView.setVisibility(View.GONE);
                            }
                        });

                    }
                }
            }
        });

        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtName);
        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtUserName);
        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtPassword);
        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtConfirmPassword);
        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtMobile);
        Common.ValidationGone(SignUpActivity.this, rlMainView, mEtEmail);
        Common.ValidationGone(SignUpActivity.this, rlMainView,edit_date_of_birth);

    }


    public void cameraImageIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);}

    private void galleryImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void setTypeFace() {
        mEtName.setTypeface(OpenSans_Regular);
        mEtUserName.setTypeface(OpenSans_Regular);
        mEtMobile.setTypeface(OpenSans_Regular);
        mEtEmail.setTypeface(OpenSans_Regular);
        mEtPassword.setTypeface(OpenSans_Regular);
        mEtConfirmPassword.setTypeface(OpenSans_Regular);
        edit_date_of_birth.setTypeface(OpenSans_Regular);
        subtitle.setTypeface(OpenSans_Regular);
        txt_sign_up_logo.setTypeface(Roboto_Bold);
        txt_signup.setTypeface(Roboto_Bold);
    }

    private void initTypeFace() {
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    private void initViews() {
        FirebaseApp.initializeApp(this);
        token= FirebaseInstanceId.getInstance().getToken();
        Log.d("SignUp",token);
        cusRotateLoading = loader.findViewById(R.id.rotateloading_register);
        profile_scrollview =  findViewById(R.id.profile_scrollview);
        mEtUserName = findViewById(R.id.edit_username);
        mEtUserName.setNextFocusDownId(R.id.edit_name);
        mEtName = findViewById(edit_name);
        mEtName.setNextFocusDownId(R.id.edit_mobile);
        mEtMobile = findViewById(R.id.edit_mobile);
        mEtEmail = findViewById(edit_email);
        mEtEmail.setNextFocusDownId(R.id.edit_password);
        mEtPassword =  findViewById(R.id.edit_password);
        mEtConfirmPassword = findViewById(R.id.edit_com_password);
        mRlSignUp = findViewById(R.id.layout_signup);
        mIvAddImage = findViewById(R.id.img_add_image);
        edit_date_of_birth = findViewById(R.id.edit_date_of_birth);
        spinner_gender =findViewById(R.id.spinner_gender);
        layout_show_hide =  findViewById(R.id.layout_show_hide);
        txt_hide_show =findViewById(R.id.txt_hide_show);
        layout_info_panel = findViewById(R.id.layout_info_panel);
        subtitle = findViewById(R.id.subtitle);
        txt_sign_up_logo =  findViewById(R.id.txt_sign_up_logo);
        txt_signup =  findViewById(R.id.txt_signup);

        //Error Alert
        rlMainView = findViewById(R.id.rlMainView);
        tvTitle =  findViewById(R.id.tvTitle);


    }



    public void EdittextActonListner(final EditText editText, final String EdtTxtName) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isvalid = ValidationRegister();
                    if (isvalid) {
                        if (Common.isNetworkAvailable(SignUpActivity.this)) {


                        } else {
                            Common.showInternetInfo(SignUpActivity.this, "");
                        }
                    }
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Log.d("Done", "hasFocus Done = " + editText.getText().length() + "==" + EdtTxtName);
                    layout_info_panel.setVisibility(View.GONE);
                    boolean isValidNext = ValidationNextRegister(EdtTxtName);

                    if (!isValidNext) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }



    @Override
    public void onSuccess(RegisterDetailModel registerDetailModel) {
        String status = registerDetailModel.getStatus();
        String message = registerDetailModel.getMessage();
        try {
            if (status.equalsIgnoreCase("success")) {

                Log.d("Result >>>", "Result One" + registerDetailModel);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

                List<CabDetail> CabDetAry = registerDetailModel.getCabDetails();


                SharedPreferences.Editor id = userPref.edit();
                id.putString("id", registerDetailModel.getUserDetail().get(0).getId());
                id.commit();

                SharedPreferences.Editor name = userPref.edit();
                name.putString("name", registerDetailModel.getUserDetail().get(0).getName());
                name.commit();

                SharedPreferences.Editor passwordPre = userPref.edit();
                passwordPre.putString("password", registerDetailModel.getUserDetail().get(0).getPassword());
                passwordPre.commit();

                SharedPreferences.Editor username = userPref.edit();
                username.putString("username", registerDetailModel.getUserDetail().get(0).getUsername());
                username.commit();

                SharedPreferences.Editor mobile = userPref.edit();
                mobile.putString("mobile", registerDetailModel.getUserDetail().get(0).getMobile());
                mobile.commit();

                SharedPreferences.Editor email = userPref.edit();
                email.putString("email", registerDetailModel.getUserDetail().get(0).getEmail());
                email.commit();

                SharedPreferences.Editor isLogin = userPref.edit();
                isLogin.putString("isLogin", "1");
                isLogin.commit();

                SharedPreferences.Editor userImage = userPref.edit();
                userImage.putString("userImage", registerDetailModel.getUserDetail().get(0).getImage());
                userImage.commit();

                SharedPreferences.Editor dob = userPref.edit();
                dob.putString("date_of_birth", registerDetailModel.getUserDetail().get(0).getDob());
                dob.commit();

                SharedPreferences.Editor gender = userPref.edit();
                gender.putString("gender", registerDetailModel.getUserDetail().get(0).getGender());
                gender.commit();

                if (!registerDetailModel.getUserDetail().get(0).getFacebookId().equalsIgnoreCase("")) {
                    SharedPreferences.Editor facebook_id = userPref.edit();
                    facebook_id.putString("facebook_id", registerDetailModel.getUserDetail().get(0).getFacebookId());
                    facebook_id.commit();
                }

                if (!registerDetailModel.getUserDetail().get(0).getTwitterId().equals("")) {
                    SharedPreferences.Editor twitter_id = userPref.edit();
                    twitter_id.putString("twitter_id", registerDetailModel.getUserDetail().get(0).getTwitterId());
                    twitter_id.commit();
                }

                loader.cancel();
                Intent hi = new Intent(SignUpActivity.this, HomeActivity.class);
                hi.putExtra("pickupLatitude", PickupLatitude);
                hi.putExtra("pickupLongtude", PickupLongtude);
                startActivity(hi);
                finish();


            } else if (status.equalsIgnoreCase("failed")) {
                loader.cancel();
                Common.showLoginRegisterMkError(SignUpActivity.this,  registerDetailModel.getMessage());
               // Toast.makeText(this, "something happen wrong  " + message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("SIGN_UP_URL", "SIGN_UP_URL error = " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {

        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgress() {

        loader.show();
        cusRotateLoading.start();

    }

    @Override
    public void hideProgress() {
        loader.cancel();
        cusRotateLoading.stop();

    }

    @Override
    public void onSignUp(RegisterDetailModel registerDetailModel) {

        Gson gson = new Gson();
        String stringCabDetail = gson.toJson(registerDetailModel.getCabDetails());

        String stringUserdetail=gson.toJson(registerDetailModel.getUserDetail());
        String stringTimeDetail=gson.toJson(registerDetailModel.getTimeDetail());
        String stringCountryDetail=gson.toJson(registerDetailModel.getCountryDetail());





        Log.d("LoginActivityCabDetail",stringCabDetail);

        SharedPreferences.Editor userDetail = userPref.edit();
        userDetail.putString("userDetail", stringUserdetail);
        userDetail.commit();


        SharedPreferences.Editor countryDetail = userPref.edit();
        countryDetail.putString("countryDetail", stringCountryDetail);
        countryDetail.commit();

        SharedPreferences.Editor timeDetail = userPref.edit();
        timeDetail.putString("timeDetail", stringTimeDetail);
        timeDetail.commit();

        SharedPreferences.Editor name1 = userPref.edit();
        name1.putString("cabDetail", stringCabDetail);
        name1.commit();
        Log.d("SIGN_UP_URL", "SIGN_UP_URL result= " + registerDetailModel.getStatus());
        boolean isStatus = Common.ShowHttpErrorMessage(SignUpActivity.this,registerDetailModel.getMessage());
        if (isStatus) {
            try {
                // JSONObject resObj = new JSONObject(new String(result));
                // Log.d("SIGN_UP_URL", "SIGN_UP_URL two= " + resObj);
                if (registerDetailModel.getStatus().equals("success")) {

                    JSONArray CabDetAry = new JSONArray(stringCabDetail);

                    List <CabDetail>  cabDetails = new ArrayList<>();
                    //Common.CabDetail = CabDetAry;
                    // Common.CabDetail =  registerDetailModel.getCabDetails();

                        /*set Start Currency*/

                    // JSONArray currencyArray = new JSONArray(resObj.getString("country_detail"));

                /*    JSONArray currencyArray = new JSONArray(registerDetailModel.getCountryDetail());
                    for (int ci = 0; ci < currencyArray.length(); ci++) {
                        JSONObject startEndTimeObj = currencyArray.getJSONObject(ci);
                     *//*   Common.Currency = startEndTimeObj.getString("currency");
                        Common.Country = startEndTimeObj.getString("country");*//*
                    }


                        *//*set Start And End Time*//*
                    //JSONArray startEndTimeArray = new JSONArray(resObj.getString("time_detail"));
                    JSONArray startEndTimeArray = new JSONArray(registerDetailModel.getTimeDetail());
                    for (int si = 0; si < startEndTimeArray.length(); si++) {
                        JSONObject startEndTimeObj = startEndTimeArray.getJSONObject(si);
                 *//*       Common.StartDayTime = startEndTimeObj.getString("day_start_time");
                        Common.EndDayTime = startEndTimeObj.getString("day_end_time");*//*
                    }*/

                        /*passenger Detail*/
                    JSONArray userDetilArray = new JSONArray(registerDetailModel.getUserDetail());
                    // JSONObject userDetilObj = userDetilArray.getJSONObject(0);

                    SharedPreferences.Editor id = userPref.edit();
                    id.putString("id", registerDetailModel.getUserDetail().get(0).getId());
                    id.commit();

                    SharedPreferences.Editor name = userPref.edit();
                    name.putString("name",  registerDetailModel.getUserDetail().get(0).getName());
                    name.commit();

                    SharedPreferences.Editor passwordPre = userPref.edit();
                    passwordPre.putString("password", mEtPassword.getText().toString());
                    passwordPre.commit();

                    SharedPreferences.Editor username = userPref.edit();
                    username.putString("username",  registerDetailModel.getUserDetail().get(0).getUsername());
                    username.commit();

                    SharedPreferences.Editor mobile = userPref.edit();
                    mobile.putString("mobile",  registerDetailModel.getUserDetail().get(0).getMobile());
                    mobile.commit();

                    SharedPreferences.Editor email = userPref.edit();
                    email.putString("email",  registerDetailModel.getUserDetail().get(0).getEmail());
                    email.commit();

                    SharedPreferences.Editor isLogin = userPref.edit();
                    isLogin.putString("isLogin", "1");
                    isLogin.commit();

                    SharedPreferences.Editor userImage = userPref.edit();
                    userImage.putString("userImage",  registerDetailModel.getUserDetail().get(0).getImage());
                    userImage.commit();

                    SharedPreferences.Editor dob = userPref.edit();
                    dob.putString("date_of_birth", registerDetailModel.getUserDetail().get(0).getDob());
                    Log.d("date_of_birth", registerDetailModel.getUserDetail().get(0).getDob());
                    dob.commit();

                    SharedPreferences.Editor gender = userPref.edit();
                    gender.putString("gender", genderString);
                    gender.commit();

                    if (registerDetailModel.getUserDetail().get(0).getFacebookId().equals("")) {
                        SharedPreferences.Editor facebook_id = userPref.edit();
                        facebook_id.putString("facebook_id",registerDetailModel.getUserDetail().get(0).getFacebookId());
                        facebook_id.commit();
                    }

                    if (!registerDetailModel.getUserDetail().get(0).getTwitterId().equals("")) {
                        SharedPreferences.Editor twitter_id = userPref.edit();
                        twitter_id.putString("twitter_id",registerDetailModel.getUserDetail().get(0).getTwitterId());
                        twitter_id.commit();
                    }

                    //   progressDialog.cancel();
                    cusRotateLoading.stop();

                    //Common.showMkSucess(SignUpActivity.this, resObj.getString("message"),"no");

                    loader.cancel();
                    Intent hi = new Intent(SignUpActivity.this, HomeActivity.class);
                    hi.putExtra("pickupLatitude", PickupLatitude);
                    hi.putExtra("pickupLongtude", PickupLongtude);
                    startActivity(hi);
                    finish();


//                        RequestParams loginParams = new RequestParams();
//                        loginParams.put("password", edit_password.getText().toString());
//                        loginParams.put("email", edit_email.getText().toString());
//
//                        Common.LoginCall(SignUpActivity.this, loginParams, progressDialog, cusRotateLoading, edit_password.getText().toString());

                } else if (registerDetailModel.getStatus().equals("failed")) {
                    Log.d("SIGN_UP_URL", "SIGN_UP_URL status = " +registerDetailModel.getStatus());
                    // progressDialog.cancel();

                    Common.showLoginRegisterMkError(SignUpActivity.this, registerDetailModel.getStatus());

                }
            } catch (JSONException e) {
                Log.d("SIGN_UP_URL", "SIGN_UP_URL error = " + e.getMessage());
                e.printStackTrace();
            }
        }

    }







    public boolean ValidationNextRegister(String EditTextString) {

        Log.d("Passwors", "Password = " + PasswordValidaton(mEtPassword.getText().toString()));
        if (EditTextString.equals("name")) {
            if (mEtName.getText().toString().trim().length() == 0) {
                //layout_info_panel.setVisibility(View.GONE);
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_name), rlMainView, tvTitle, regularRoboto);
                mEtName.requestFocus();
                return false;
            } else if (mEtName.getText().toString().trim().length() < 4) {
                //layout_info_panel.setVisibility(View.GONE);
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_min_name), rlMainView, tvTitle, regularRoboto);
                mEtName.requestFocus();
                return false;
            }
        } else if (EditTextString.equals("username")) {
            if (mEtUserName.getText().toString().trim().length() == 0) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_username), rlMainView, tvTitle, regularRoboto);
                mEtUserName.requestFocus();
                return false;
            } else if (mEtUserName.getText().toString().trim().length() < 4) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_min_isername), rlMainView, tvTitle, regularRoboto);
                mEtUserName.requestFocus();
                return false;
            }
        } else if (EditTextString.equals("password")) {
            if (!PasswordValidaton(mEtPassword.getText().toString())) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_valid), rlMainView, tvTitle, regularRoboto);
                mEtPassword.requestFocus();
                return false;
            } else if (mEtPassword.getText().toString().trim().length() == 0) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_password), rlMainView, tvTitle, regularRoboto);
                mEtPassword.requestFocus();
                return false;
            } else if (mEtPassword.getText().toString().trim().length() < 6 || mEtPassword.getText().toString().trim().length() > 32) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_length), rlMainView, tvTitle, regularRoboto);
                mEtPassword.requestFocus();
                return false;
            }
        } else if (EditTextString.equals("confirm_password")) {
            if (mEtConfirmPassword.getText().toString().trim().length() == 0) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_confirm_password), rlMainView, tvTitle, regularRoboto);
                mEtConfirmPassword.requestFocus();
                return false;
            } else if (!mEtPassword.getText().toString().equals(mEtConfirmPassword.getText().toString())) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_confirm), rlMainView, tvTitle, regularRoboto);
                mEtConfirmPassword.requestFocus();
                return false;
            }
        } else if (EditTextString.equals("mobile")) {
            if (mEtMobile.getText().toString().trim().length() == 0) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_mobile), rlMainView, tvTitle, regularRoboto);
                mEtMobile.requestFocus();
                return false;
            }
        } else if (EditTextString.equals("email")) {
            if (mEtEmail.getText().toString().trim().length() == 0) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_email), rlMainView, tvTitle, regularRoboto);
                mEtEmail.requestFocus();
                return false;
            } else if (mEtEmail.getText().toString().trim().length() != 0 && !isValidEmail(mEtEmail.getText().toString().trim())) {
                showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_valid_email), rlMainView, tvTitle, regularRoboto);
                mEtEmail.requestFocus();
                return false;
            }
        }
        return true;
    }






    public boolean ValidationRegister() {


        Log.d("Passwors", "Password = " + PasswordValidaton(mEtPassword.getText().toString()));

        if (isSelectPhoto == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_image), rlMainView, tvTitle, regularRoboto);
            mIvAddImage.requestFocus();
            return false;
        } else if (mEtName.getText().toString().trim().length() == 0) {
            //layout_info_panel.setVisibility(View.GONE);
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_name), rlMainView, tvTitle, regularRoboto);
            mEtName.requestFocus();
            return false;
        } else if (mEtName.getText().toString().trim().length() < 4) {
            //layout_info_panel.setVisibility(View.GONE);
            LayoutEnimation(R.color.dialog_error_color, getResources().getString(R.string.please_min_name));
            mEtName.requestFocus();
            return false;
        } else if (mEtUserName.getText().toString().trim().length() == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_username), rlMainView, tvTitle, regularRoboto);
            mEtUserName.requestFocus();
            return false;
        } else if (mEtUserName.getText().toString().trim().length() < 4) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_min_isername), rlMainView, tvTitle, regularRoboto);
            mEtUserName.requestFocus();
            return false;
        } else if (mEtPassword.getText().toString().trim().length() == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_password), rlMainView, tvTitle, regularRoboto);
            mEtPassword.requestFocus();
            return false;
        } else if (!PasswordValidaton(mEtPassword.getText().toString())) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_valid), rlMainView, tvTitle, regularRoboto);
            mEtPassword.requestFocus();
            return false;
        } else if (mEtPassword.getText().toString().trim().length() < 6 || mEtPassword.getText().toString().trim().length() > 32) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_length), rlMainView, tvTitle, regularRoboto);
            mEtPassword.requestFocus();
            return false;
        } else if (mEtConfirmPassword.getText().toString().trim().length() == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_confirm_password), rlMainView, tvTitle, regularRoboto);
            mEtConfirmPassword.requestFocus();
            return false;
        } else if (!mEtPassword.getText().toString().equals(mEtConfirmPassword.getText().toString())) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.password_confirm), rlMainView, tvTitle, regularRoboto);
            mEtConfirmPassword.requestFocus();
            return false;
        } else if (mEtMobile.getText().toString().length() != 10) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.mobile_length), rlMainView, tvTitle, regularRoboto);
            return false;
        } else if (mEtMobile.getText().toString().trim().length() == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_mobile), rlMainView, tvTitle, regularRoboto);
            mEtMobile.requestFocus();
            return false;
        } else if (mEtEmail.getText().toString().trim().length() == 0) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_email), rlMainView, tvTitle, regularRoboto);
            mEtEmail.requestFocus();
            return false;
        } else if (mEtEmail.getText().toString().trim().length() != 0 && !isValidEmail(mEtEmail.getText().toString().trim())) {
            showMKPanelError(SignUpActivity.this, getResources().getString(R.string.please_enter_valid_email), rlMainView, tvTitle, regularRoboto);
            mEtEmail.requestFocus();
            return false;
        }

        return true;
    }

    public final boolean PasswordValidaton(String password) {
        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        //Matcher hasSpecial = special.matcher(password);

        return hasLetter.find() && hasDigit.find();
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
        isSelectPhoto = 1;
          userImage =savebitmap(thumbnail);
    }

    private File savebitmap(Bitmap bp) {
        isSelectPhoto = 1;
        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Navori/";
        File destination=new File(destinationPath);
        if(!destination.exists()){
            destination.mkdirs();
        }
        String filename="Snap_" + System.currentTimeMillis() + "_.jpg";
        File myImage = new File(destinationPath+filename);
        try {
            FileOutputStream fos = new FileOutputStream(myImage);
            bp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
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
        userImage=savebitmap(bm);
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void LayoutEnimation(int color, String message) {

        layout_info_panel.setBackgroundResource(color);
        if ((layout_info_panel.getVisibility() == View.GONE)) {
            layout_info_panel.setVisibility(View.VISIBLE);
        }
        subtitle.setText(message);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_map);
        layout_info_panel.startAnimation(slideUpAnimation);

        slideUpAnimation.setFillAfter(true);
        slideUpAnimation.setDuration(2000);

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout_info_panel.setAlpha(1);
                if ((layout_info_panel.getVisibility() == View.GONE)) {
                    layout_info_panel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.cancel();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout_info_panel.setAlpha(0);
                    }
                }, 1000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        mEtUserName = null;
        mEtName = null;
        mEtMobile = null;
        mEtEmail = null;
        mEtPassword = null;
        mEtConfirmPassword = null;
        mRlSignUp = null;
        mIvAddImage = null;
        edit_date_of_birth = null;
        spinner_gender = null;
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
}