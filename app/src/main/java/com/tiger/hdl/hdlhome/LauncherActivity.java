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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiger.hdl.hdlhome.dummy.DummyItem;
import com.tiger.hdl.hdlhome.utils.FileUtils;
import com.tiger.hdl.hdlhome.utils.net.RxSocket;

import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    View recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
//        RxSocket.getInstance().setResultListener(new RxSocket.SocketListener<List<DummyItem>>() {
//            @Override
//            public void cancleListen() {
//
//            }
//
//            @Override
//            public void accept(List<DummyItem> dummyItems) throws Exception {
//                setupRecyclerView((RecyclerView) recyclerView, dummyItems);
//            }
//        });
        startService(new Intent(this, MyService.class));
        RxSocket.getInstance().setResultListener(new RxSocket.SocketListener() {
            @Override
            public void cancleListen() {

            }

            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<DummyItem> items) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 20);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(10, getResources().getColor(R.color.gray_666666)));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, items));
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
            RxSocket.getInstance().cancleAll(true);
            RxSocket.getInstance().connectSocket(path);
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
    protected void onDestroy() {
        super.onDestroy();
        RxSocket.getInstance().cancleAll(true);
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
