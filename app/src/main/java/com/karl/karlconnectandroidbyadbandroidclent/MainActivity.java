package com.karl.karlconnectandroidbyadbandroidclent;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karl.karlconnectandroidbyadbandroidclent.adapter.UserAdapter;
import com.karl.karlconnectandroidbyadbandroidclent.dbmodel.UserModel;
import com.karl.karlconnectandroidbyadbandroidclent.utils.FileUtils;
import com.karl.karlconnectandroidbyadbandroidclent.utils.PathUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //请求状态码
    private static final int REQUEST_PERMISSION_CODE = 1;

    private static String packageName = "com.karl.karlconnectandroidbyadbandroidclent";

    private Toolbar toolbar;
    private ListView lv_users;
    private ArrayList<UserModel> userModels;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_users = findViewById(R.id.lv_users);
        userModels = new ArrayList<>();
        adapter = new UserAdapter(userModels);
        lv_users.setAdapter(adapter);

        refreshData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_user:
                startActivity(new Intent(this,UserAddActivity.class));
                break;
            case R.id.menu_adb_update:

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    }else{
                        if (copyDbToPublicDirectory()) {
                            Toast.makeText(this, "Adb上传数据准备就绪", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Adb上传数据准备失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }

        return true;
    }

    private void refreshData() {
        List<UserModel> allUsers = LitePal.findAll(UserModel.class);
        if (allUsers.size() > 0) {
            userModels.clear();
            userModels.addAll(allUsers);
            adapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (copyDbToPublicDirectory()) {
                        Toast.makeText(this, "Adb上传数据准备就绪", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Adb上传数据准备失败", Toast.LENGTH_SHORT).show();
                    };
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * 拷贝数据库db文件到外部的public文件夹内
     */
    public boolean copyDbToPublicDirectory() {
        boolean success = false;
        try {
            //获取db文件
            String dbDirPath = "/data/data/" + packageName
                    + "/databases/";

            File dir = new File(dbDirPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (file.getName().endsWith("db")) {


                            //拷贝文件方式
                            Log.i("hf", "获取到数据库文件" + file.getName());
                            //拷贝这个文件到外部共享文档文件夹下
                            File documentDir = Environment.getExternalStorageDirectory();
                            Log.i("hf", "获取到SDka" + documentDir.getAbsolutePath());
                            String saveDir = documentDir.getAbsolutePath() + "/" + packageName + "/" + "dbfiles";
                            File f_saveDir = new File(saveDir);
                            if (!f_saveDir.exists()) {
                                f_saveDir.mkdirs();
                            }
                            String saveFile = saveDir + "/" + file.getName();
                            Log.i("hf", "拷贝文件的路径:" + saveFile);
                            File f_saveFile = new File(saveFile);
                            if (!f_saveFile.exists()) {
                                f_saveFile.createNewFile();
                            }
                            //拷贝文件
                            Log.i("hf", "开始拷贝文件");
                            FileUtils.copyFileUsingFileChannels(file, f_saveFile);
                            Log.i("hf", "拷贝文件成功");
                        }
                    }
                    success = true;
                }

            }
        } catch (Exception ex) {
            Log.e("hf", ex.getMessage());
        }
        return success;
    }
}
