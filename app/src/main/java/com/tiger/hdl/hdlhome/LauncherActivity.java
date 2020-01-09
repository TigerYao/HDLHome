package com.tiger.hdl.hdlhome;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tiger.hdl.hdlhome.dummy.DeskInfo;
import com.tiger.hdl.hdlhome.dummy.DummyItem;
import com.tiger.hdl.hdlhome.utils.DisplayUtil;
import com.tiger.hdl.hdlhome.utils.FileUtils;
import com.tiger.hdl.hdlhome.utils.net.SocketClientUtil;
import com.tiger.hdl.hdlhome.view.LoadingView;

import java.util.List;


public class LauncherActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SimpleItemRecyclerViewAdapter mAdapter;
    String mConfigPath;
    LoadingView mLoadingView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        showDialog();
        recyclerView = findViewById(R.id.item_list);
        DisplayUtil.computeWidth(this);
        SocketClientUtil.getInstance().setCtx(this);
//        SocketClientUtil.getInstance().openConfig(EnvironmentCompat.getStorageState(Environment.getRootDirectory()));
        setupRecyclerView(recyclerView, null);
        SocketClientUtil.getInstance().setClientListener(new SocketClientUtil.OnMsgListener() {
            @Override
            public void onReceived(DeskInfo deskInfo) {
                setupRecyclerView(recyclerView, deskInfo.data);
            }

            @Override
            public void onConnected() {
                Toast.makeText(getBaseContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
                hideLoading();
            }

            @Override
            public void onDisconnect() {
                showDialog();
                SocketClientUtil.getInstance().openConfig(mConfigPath);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SocketClientUtil.getInstance().isConnectd()) {
            mConfigPath = /*Environment.getExternalStorageDirectory().getPath()+"/config.txt";//*/("file:///android_asset/config.txt");
            Log.i("LauncherActivity", mConfigPath);
            SocketClientUtil.getInstance().openConfig(mConfigPath);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<DummyItem> items) {
        if(mAdapter == null) {
            mAdapter = new SimpleItemRecyclerViewAdapter(this, items);
//            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(20, VERTICAL);
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager(this, true);
            recyclerView.setLayoutManager(flowLayoutManager);
//            recyclerView.addItemDecoration(new GridDividerItemDecoration((int)DisplayUtil.dp2px(1, this), Color.parseColor("#c3c3c3")));
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
            mConfigPath = FileUtils.getPath(this, data.getData());
            SocketClientUtil.getInstance().openConfig(mConfigPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == 1001){
                showLoading();
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

    public void showLoading(){
        if (mLoadingView == null) {
            mLoadingView = new LoadingView(this);
            mLoadingView.setCanceledOnTouchOutside(false);
        }
        mLoadingView.show(); // 显示
    }

    public void hideLoading(){
        if(mLoadingView != null && mLoadingView.isShowing())
            mLoadingView.dismiss();
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
