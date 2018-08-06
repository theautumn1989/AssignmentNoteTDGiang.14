package com.example.tomato.assignmentnotetdgiang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.example.tomato.assignmentnotetdgiang.R;
import com.example.tomato.assignmentnotetdgiang.adapter.RecyclerMainAdapter;
import com.example.tomato.assignmentnotetdgiang.database.DBManager;
import com.example.tomato.assignmentnotetdgiang.model.Note;
import com.example.tomato.assignmentnotetdgiang.myInterface.OnCallBack;
import com.example.tomato.assignmentnotetdgiang.utils.AlarmReceiver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnCallBack, View.OnClickListener {

    public static final String LIST_POSITION = "1989";

    RecyclerView rvNote;
    ImageButton ibtnAdd;

    ArrayList<Note> arrNote;
    RecyclerMainAdapter adapterNote;
    private AlarmReceiver alarmReceiver;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initAdapter();
        initEvent();

        alarmReceiver = new AlarmReceiver();
        db = new DBManager(this);

        updateData();

        // printKeyHash(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        arrNote = (ArrayList<Note>) db.getAllData();
        adapterNote = new RecyclerMainAdapter(arrNote, getApplicationContext(), this);
        rvNote.setAdapter(adapterNote);

    }

    private void initEvent() {
        ibtnAdd.setOnClickListener(this);
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvNote.setLayoutManager(gridLayoutManager);

        rvNote.setHasFixedSize(true);

        arrNote = new ArrayList<>();
        adapterNote = new RecyclerMainAdapter(arrNote, getApplicationContext(), this);
        rvNote.setAdapter(adapterNote);
    }

    private void initView() {
        rvNote = findViewById(R.id.rv_note);
        ibtnAdd = findViewById(R.id.ibtn_add);
    }

    // my interface
    @Override
    public void onItemClicked(int position, boolean isLongClick) {
        if (isLongClick) {    // longClick

        } else {
            Note note = arrNote.get(position);
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, note.getID());
            intent.putExtra(LIST_POSITION, position);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_add:
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    // ketHash facebook
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                //  Log.e("Key Hash=", key);
                Log.d("keyHash", key);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }


}
