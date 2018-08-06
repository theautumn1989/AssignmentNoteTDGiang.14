package com.example.tomato.assignmentnotetdgiang.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.tomato.assignmentnotetdgiang.R;
import com.example.tomato.assignmentnotetdgiang.database.DBManager;
import com.example.tomato.assignmentnotetdgiang.model.Note;
import com.example.tomato.assignmentnotetdgiang.utils.AlarmReceiver;
import com.facebook.CallbackManager;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity implements
        View.OnClickListener {

    final int REQUEST_CODE_GALLERY = 999;
    final int REQUEST_CODE_CAMERA = 888;

    Boolean status, statusImage;
    Spinner spnDate, spnTime;
    ImageView ivNote = null;
    TextView tvTime, tvDate, tvTimeNow;
    EditText edtTitle, edtContent;
    ImageButton ibtnAlarm, ibtnDate, ibtnTime, ibtnClearImage, ibtnClearDatetime;
    LinearLayout llCreateNote, llDate, llTime;
    RelativeLayout rlImage;
    Calendar calendar;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mTime, mDate, mTimeNow;
    String mColor = "#F5F5F5";

    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        initView();
        initEvent();
        initData();
    }

    private void initData() {
        status = false;
        statusImage = false;
        calendar = Calendar.getInstance();
        db = new DBManager(this);
        mCalendar = Calendar.getInstance();
        loadData(status);
        loadClear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        mTimeNow = simpleDateFormat.format(calendar.getTime());
        tvTimeNow.setText(mTimeNow.toString());
    }

    private void loadData(Boolean status) {
        llTime.setVisibility(View.GONE);
        llDate.setVisibility(View.GONE);
        if (status) {
            spinnerDate();
            spinnerTime();
            llDate.setVisibility(View.VISIBLE);
            llTime.setVisibility(View.VISIBLE);
        }
    }

    private void loadClear() {
        rlImage.setVisibility(View.GONE);
        if (statusImage) {
            rlImage.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        ibtnAlarm.setOnClickListener(this);
        ibtnTime.setOnClickListener(this);
        ibtnDate.setOnClickListener(this);
        ibtnClearImage.setOnClickListener(this);
        ibtnClearDatetime.setOnClickListener(this);
    }

    private void initView() {
        edtContent = findViewById(R.id.edt_content);
        edtTitle = findViewById(R.id.edt_title);
        ibtnAlarm = findViewById(R.id.ibtn_alarm);
        ibtnDate = findViewById(R.id.ibtn_date);
        ibtnTime = findViewById(R.id.ibtn_time);
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        tvTimeNow = findViewById(R.id.tv_time_now);
        spnDate = findViewById(R.id.spn_Date);
        spnTime = findViewById(R.id.spn_time);
        llDate = findViewById(R.id.ll_date);
        llTime = findViewById(R.id.ll_time);
        ibtnClearImage = findViewById(R.id.ibtn_clear_image);
        rlImage = findViewById(R.id.rl_image);
        ibtnClearDatetime = findViewById(R.id.ibtn_clear_datetime);

        ivNote = findViewById(R.id.iv_note);

        llCreateNote = findViewById(R.id.ll_create_note);

        getSupportActionBar().setTitle(R.string.title_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != edtTitle.getLayout() && edtTitle.getLayout().getLineCount() > 1) {
                    edtTitle.getText().delete(edtTitle.getText().length() - 1, edtTitle.getText().length());
                }
            }
        });
        // max 3 lines editetxt
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != edtContent.getLayout() && edtContent.getLayout().getLineCount() > 3) {
                    edtContent.getText().delete(edtContent.getText().length() - 1, edtContent.getText().length());
                }
            }
        });
    }

    // On clicking Time picker
    private void setTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute);

                mHour = hourOfDay;
                mMinute = minute;

                mTime = simpleDateFormat.format(calendar.getTime());
                tvTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);
        tpd.show();
    }

    // On clicking Date picker
    private void setDate() {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DATE);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                mDay = dayOfMonth;
                mMonth = month;
                mYear = year;

                mDate = simpleDateFormat.format(calendar.getTime());
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, y, m, d);
        dpd.show();
    }

    public void saveNote() {
        Calendar calendarCheck = Calendar.getInstance();

        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        byte[] image = null;
        if (ivNote != null) {
            if (ivNote.getDrawable() != null) {       // kiểm tra đã chọn ảnh chưa
                image = imageViewToByte(ivNote);
            }
        }
        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, R.string.please_input_title_and_content, Toast.LENGTH_SHORT).show();
        } else {
            // if date time != null --> set Alarm
            if (mDate != null && mTime != null) {
                if (mCalendar.getTimeInMillis() - calendarCheck.getTimeInMillis() < 0) {
                    Toast.makeText(this, R.string.please_select_time, Toast.LENGTH_SHORT).show();
                } else {
                    Note note = new Note(title, content, mDate, mTime, image, mColor, mTimeNow);
                    int ID = db.addNote(note);

                    // Create a new notification
                    new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);

                    // Create toast to confirm new reminder
                    Toast.makeText(getApplicationContext(), R.string.saved,
                            Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            } else {
                // no alarm
                Note note = new Note(title, content, mDate, mTime, image, mColor, mTimeNow);
                db.insertData(note);
                onBackPressed();
            }
        }
    }

    private void dialogColor() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.title_color);
        dialog.setContentView(R.layout.dialog_color_custom);

        ImageButton ibtnWhite = dialog.findViewById(R.id.ibtn_color_white);
        ImageButton ibtnBlue = dialog.findViewById(R.id.ibtn_color_blue);
        ImageButton ibtnYellow = dialog.findViewById(R.id.ibtn_color_yellow);
        ImageButton ibtnOrange = dialog.findViewById(R.id.ibtn_color_orange);

        ibtnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColor = getString(R.string.color_white);
                llCreateNote.setBackgroundColor(Color.parseColor(getString(R.string.color_white)));
                dialog.dismiss();
            }
        });

        ibtnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColor = getString(R.string.color_blue);
                llCreateNote.setBackgroundColor(Color.parseColor(getString(R.string.color_blue)));
                dialog.dismiss();
            }
        });
        ibtnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColor = getString(R.string.color_yellow);
                llCreateNote.setBackgroundColor(Color.parseColor(getString(R.string.color_yellow)));
                dialog.dismiss();
            }
        });
        ibtnOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColor = getString(R.string.color_orange);
                llCreateNote.setBackgroundColor(Color.parseColor(getString(R.string.color_orange)));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void dialogImage() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.dialog_title_image);
        dialog.setContentView(R.layout.dialog_image_custom);

        LinearLayout llChoseCamera = dialog.findViewById(R.id.ll_chose_camera);
        LinearLayout llChosePicture = dialog.findViewById(R.id.ll_chose_photo);


        llChoseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateNoteActivity.this, R.string.camera, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                dialog.dismiss();
            }
        });
        llChosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateNoteActivity.this, R.string.picture, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(
                        CreateNoteActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void spinnerTime() {
        ArrayList<String> spnTimeList = new ArrayList<>();
        spnTimeList.add("1 minute");
        spnTimeList.add("9:00");
        spnTimeList.add("12:00");
        spnTimeList.add("18:00");
        spnTimeList.add("Other ...");
        spnTimeList.add("Time");

        final ArrayAdapter<String> spnAdapterTime = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spnTimeList
        );
        spnAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTime.setAdapter(spnAdapterTime);

        spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> view, View view1, int i, long l) {
                spnSetTime(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {
            }
        });

    }

    public void spnSetTime(int index) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendarDateSpn = Calendar.getInstance();
        int hour = calendarDateSpn.get(Calendar.HOUR_OF_DAY);
        int minute = calendarDateSpn.get(Calendar.MINUTE);

        mHour = hour;
        mMinute = minute;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        calendar1.set(0, 0, 0, hour, minute);

        mTime = simpleDateFormat.format(calendar1.getTime());
        tvTime.setText(simpleDateFormat.format(calendar1.getTime()));

        switch (index) {
            case 0:
                mMinute = minute + 1;
                calendar1.set(0, 0, 0, hour, minute + 1);
                mTime = simpleDateFormat.format(calendar1.getTime());
                tvTime.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 1:
                hour = 9;
                mHour = hour;
                calendar1.set(0, 0, 0, hour, minute);
                mTime = simpleDateFormat.format(calendar1.getTime());
                tvTime.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 2:
                hour = 12;
                mHour = hour;
                calendar1.set(0, 0, 0, hour, minute);
                mTime = simpleDateFormat.format(calendar1.getTime());
                tvTime.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 3:
                hour = 18;
                mHour = hour;
                calendar1.set(0, 0, 0, hour, minute);
                mTime = simpleDateFormat.format(calendar1.getTime());
                tvTime.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 4:
                setTime();
                break;
            default:
                break;
        }
    }

    private void spinnerDate() {
        ArrayList<String> spnDateList = new ArrayList<>();
        spnDateList.add("Today");
        spnDateList.add("Tomorrow");
        spnDateList.add("Other ...");
        spnDateList.add("Date");

        final ArrayAdapter<String> spnAdapterDate = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spnDateList
        );
        spnAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(spnAdapterDate);

        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> view, View view1, int i, long l) {
                spnSetDate(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {
            }
        });
    }

    public void spnSetDate(int index) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendarDateSpn = Calendar.getInstance();
        int d = calendarDateSpn.get(Calendar.DATE);
        int m = calendarDateSpn.get(Calendar.MONTH);
        int y = calendarDateSpn.get(Calendar.YEAR);

        mDay = d;
        mMonth = m;
        mYear = y;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        tvDate.setText(simpleDateFormat.format(calendarDateSpn.getTime()));
        switch (index) {
            case 0:
                mDay = d;
                calendar1.set(y, m, d);
                mDate = simpleDateFormat.format(calendar1.getTime());
                tvDate.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 1:
                d += 1;
                mDay = d;
                calendar1.set(y, m, d);
                mDate = simpleDateFormat.format(calendar1.getTime());
                tvDate.setText(simpleDateFormat.format(calendar1.getTime()));
                break;
            case 2:
                setDate();
                break;
            default:
                break;
        }
    }

    private void clearImage() {
        statusImage = false;
        loadClear();
        ivNote = null;
    }

    private void clearDatetime() {
        status = false;
        loadData(status);
        mDate = null;
        mTime = null;
    }

    public static byte[] imageViewToByte(ImageView image) {

        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivNote.setImageBitmap(bitmap);
                statusImage = true;
                loadClear();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivNote.setImageBitmap(bitmap);
                statusImage = true;
                loadClear();
            } catch (Exception e) {
                e.toString();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), R.string.permissions, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                Toast.makeText(getApplicationContext(), R.string.permissions, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save_note_menu:
                saveNote();
                return true;
            case R.id.image_menu:
                dialogImage();
                return true;
            case R.id.color_menu:
                dialogColor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_alarm:
                status = true;
                loadData(status);
                break;
            case R.id.ibtn_clear_image:
                clearImage();
                break;
            case R.id.ibtn_clear_datetime:
                clearDatetime();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
