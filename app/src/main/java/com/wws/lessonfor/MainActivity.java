package com.wws.lessonfor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wws.lessonfor.utils.PhotoUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    private static final int CODE_CAMERA_REQUEST = 3;
    private static final int CODE_GALLERY_REQUEST = 4;
    private static final int CODE_RESULT_REQUEST = 5;

    private final File mImageFile = new File(Environment.getExternalStorageDirectory().getPath(),"/photo.jpg");
    private final File mCropFile = new File(Environment.getExternalStorageDirectory().getPath(),"/crop_photo.jpg");
    private Uri mImageUri;
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);

        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyDialog() //弹出违规提示对话框
                .penaltyLog() //在Logcat 中打印违规异常信息
                .penaltyFlashScreen() //API等级11
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects() //API等级11
                .penaltyLog()
                .penaltyDeath()
                .build());*/
    }

    private void autoObtainCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //记住禁止询问
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                Toast.makeText(this, "你已经拒绝过一次！", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERMISSION_REQUEST_CODE);
        }else{
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(this, "设备没有SDCard!", Toast.LENGTH_SHORT).show();
            }else{
                //拍照
                mImageUri = Uri.fromFile(mImageFile);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    mImageUri = FileProvider.getUriForFile(this,"com.wws.lessonfor.fileprovider",mImageFile);
                }
                PhotoUtil.takePicture(this,mImageUri, CODE_CAMERA_REQUEST);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CODE_CAMERA_REQUEST:
                    //启动裁剪
                    mCropImageUri = Uri.fromFile(mCropFile);
                    PhotoUtil.cropImageUri(this,mImageUri,mCropImageUri,1,1,200,200,CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST:
                    mCropImageUri = Uri.fromFile(mCropFile);
                    mImageUri = Uri.parse(PhotoUtil.getPath(this, data.getData()));
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        mImageUri = FileProvider.getUriForFile(this,"com.wws.lessonfor.fileprovider",new File(mImageUri.getPath()));
                    }
                    PhotoUtil.cropImageUri(this,mImageUri,mCropImageUri,1,1,200,200,CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    ImageView view = findViewById(R.id.imageview);
                    view.setImageBitmap(PhotoUtil.getBitmapFromUri(mCropImageUri,this));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED){
                    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        Toast.makeText(this, "没有SD卡！", Toast.LENGTH_SHORT).show();
                    }else{
                        mImageUri = Uri.fromFile(mImageFile);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            mImageUri = FileProvider.getUriForFile(this,"com.wws.lessonfor.fileprovider",mImageFile);
                        }
                        PhotoUtil.takePicture(this,mImageUri,CODE_CAMERA_REQUEST);
                    }
                }else{
                    Toast.makeText(this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    PhotoUtil.openPic(this,CODE_GALLERY_REQUEST);
                }else{
                    Toast.makeText(this, "请打开SDcard权限!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button1){
            //from 相册 裁剪
            autoObtainCameraPermission();
        }else if(id == R.id.button2){
            //from camera crop
            PhotoUtil.openPic(this,CODE_GALLERY_REQUEST);
        }
    }
}
