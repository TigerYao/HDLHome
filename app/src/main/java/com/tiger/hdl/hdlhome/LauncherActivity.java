package com.tiger.hdl.hdlhome;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiger.hdl.hdlhome.dummy.DeskInfo;
import com.tiger.hdl.hdlhome.dummy.DummyItem;
import com.tiger.hdl.hdlhome.utils.DisplayUtil;
import com.tiger.hdl.hdlhome.utils.FileUtils;
import com.tiger.hdl.hdlhome.utils.net.SocketClientUtil;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class LauncherActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SimpleItemRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        recyclerView = findViewById(R.id.item_list);
//        DisplayUtil.getMacAddress();
//        assert recyclerView != null;
        SocketClientUtil.getInstance().setCtx(this);
        SocketClientUtil.getInstance().openConfig("file:///android_asset/config.txt");
        SocketClientUtil.getInstance().setClientListener(new SocketClientUtil.OnMsgListener() {
            @Override
            public void onReceived(DeskInfo deskInfo) {
                setupRecyclerView(recyclerView, deskInfo.data);
            }

            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnect() {

            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<DummyItem> items) {
        if(mAdapter == null) {
            mAdapter = new SimpleItemRecyclerViewAdapter(this, items);
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(20, VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new GridDividerItemDecoration(10, getResources().getColor(R.color.gray_666666)));
            recyclerView.setAdapter(mAdapter);
        }else
            mAdapter.setValues(items);
    }


    @Override
    public void onBackPressed() {
        showDialog();
    }

    AlertDialog mSettingDialog;

    private void showDialog() {
        if (mSettingDialog == null)
            mSettingDialog = new AlertDialog.Builder(this, R.style.CustomDialog).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.settings)), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            if(checkPermission()) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(intent, 1);
                            }
                            break;
                        case 1:
                            Intent intents = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intents);
                            break;
                    }
                }
            }).setTitle("设置").create();
        mSettingDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String path = FileUtils.getPath(this, data.getData());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == 1001){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SocketClientUtil.getInstance().disConnect();
        super.onDestroy();
    }

    private boolean checkPermission() {
        int checkSelfPermissionWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermissionWrite == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        return false;
    }
}
