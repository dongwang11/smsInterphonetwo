package com.sms.app.interphone.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Avatar;
import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.frequentlyutil.Constants;
import com.sms.app.interphone.util.frequentlyutil.getPhotoFromPhotoAlbum;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.view.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ModificationMessageActivity extends BaseActivity implements View.OnClickListener{

    private ImagePicker imagePicker = new ImagePicker();
    private  EditText sms_main_user_name,ed_sex,ed_site;
    private LinearLayout ll_save,ll_icon;
    private ImageView iv_back;
    CircleImageView sms_main_icon;
    private TextView ed_mail;

   //头像
    private final int IMAGE_REQUEST_CODE = 0;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int RESULT_REQUEST_CODE = 2;
    private final int PHOTOGRAPH_REQUEST = 3;
    private String imgPath = null;
    private String path = null;
    private Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificationmessage);

        // 设置标题
        imagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);

        setView();
        setUserData();
    }

    private void setView() {

        sms_main_user_name = (EditText) findViewById(R.id.sms_main_user_name);
        ed_sex= (EditText) findViewById(R.id.ed_sex);
        ed_mail= (TextView) findViewById(R.id.ed_mail);
        ed_site= (EditText) findViewById(R.id.ed_site);
        sms_main_icon = (CircleImageView)findViewById(R.id.sms_main_icon);
        ll_save= (LinearLayout) findViewById(R.id.ll_save);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_icon= (LinearLayout) findViewById(R.id.ll_icon);

        ll_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_icon.setOnClickListener(this);


        sms_main_user_name.setText(MyApplicatoin.evenUser.getName());
        ed_sex.setText(MyApplicatoin.evenUser.getSex()+"");
        ed_mail.setText(MyApplicatoin.evenUser.getE_mail()+"");

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ll_save:
                 MyApplicatoin.evenUser.setName(sms_main_user_name.getText().toString());
                 finish();
                break;
            case R.id.iv_back:
                 finish();
                break;
            case R.id.ll_icon:
                if(MyApplicatoin.evenUser == null||MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//
                    Tools.showInfo(ModificationMessageActivity.this,getResources().getString(R.string.sms_dialog_login_title));
                    return;
                }

                try {
                    if(MyApplicatoin.evenUser != null){

                        if(hasPermission(Manifest.permission.CAMERA)){


                            // 判断存储卡是否可以用，可用进行存储

                            chooseIcon(ModificationMessageActivity.this);

                        }else{
                            requstPermission(GlobalConsts.CAMERA,Manifest.permission.CAMERA);
                        }

                    }
                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }
                break;
            default:
                break;
        }
    }




    private void setUserData() {//设置头像

        if(MyApplicatoin.evenUser != null){
            if(MyApplicatoin.evenUser.getId().equals(Constants.USERIDLONG)){//设置默认MyApplicatoin.evenUser，设置登录
                sms_main_icon.setImageResource(R.mipmap.icon_chat_01);
                return;
            }
            try {

                if(MyApplicatoin.evenUser.getAvatar_url() != null){
                    Bitmap bitmap =getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //从本地取图片(在cdcard中获取)
                    sms_main_icon.setImageBitmap(bitmap);
                }else{
                    sms_main_icon.setImageResource(R.mipmap.icon_chat_01);
                }
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }

        }else{
            sms_main_icon.setImageResource(R.mipmap.icon_chat_01);

        }

    }

     //加载本地图片
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }

    /**
     *
     * 为子类提供一个权限检查方法
     *
     * */
    public boolean hasPermission(String... permissions){

        for(String permission : permissions){

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){

                return false;
            }
        }
        return true;
    }

    public void requstPermission(int code, String... permissions){

        ActivityCompat.requestPermissions(this, permissions, code);

    }


    /**
     * 选择头像来源
     */
    private void chooseIcon(final Context context) {
        if(Build.VERSION.SDK_INT >=26){
            startChooser();
            return;
        }
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.sms_main_hite_icon))
                .setPositiveButton(getResources().getString(R.string.sms_main_hite_icon_photo_album),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();

                                String[] PERMISSIONS = {
                                        "android.permission.WRITE_EXTERNAL_STORAGE",
                                        "android.permission.READ_EXTERNAL_STORAGE"
                                };

                                if(hasPermission(PERMISSIONS)){


                                    // 判断存储卡是否可以用，可用进行存储

                                    pickPhoto();

                                }else{
                                    requstPermission(GlobalConsts.WRITE_EXTERNAL_STORAGE,PERMISSIONS);
                                }

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.sms_main_hite_icon_photo_camera),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                takePhoto();
                            }
                        }).create();
        dialog.show();
    }


    private void startChooser() {//android8.0后剪切图片

        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override public void onCropImage(Uri imageUri) {
                // draweeView.setImageURI(imageUri);
                //  draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
                try {
                    getImageToView(new File(new URI(imageUri.toString())));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(150, 150)
                        // 宽高比
                        .setAspectRatio(2, 2);
            }

            // 用户拒绝授权回调
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });
    }


    /**
     *
     * 定位权限
     *
     * @param grantResults*/
    public void doLocation(int[] grantResults, final String hite) {


        if (grantResults.length > 0) {

            if(grantResults[0] == -1){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = new AlertDialog.Builder(ModificationMessageActivity.this)
                                .setTitle(getResources().getString(R.string.sms_dialog_hite))

                                .setMessage(hite+getResources().getString(R.string.sms_dialog_hite_permission))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.sms_dialog_hite_permission_up),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();

                                            }
                                        })
                                .setNegativeButton(getResources().getString(R.string.sms_dialog_hite_permission_ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                                Intent intent = new Intent();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                if (Build.VERSION.SDK_INT >= 9) {
                                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                } else if (Build.VERSION.SDK_INT <= 8) {
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                                    intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                                }
                                                startActivity(intent);

                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                            }
                                        }).create();
                        dialog.show();
                    }
                });


            }
        }

    }

    /**
     *
     * 用户选择打开回调
     *
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       // LogUtil.i(TAG,"onRequestPermissionsResult:"+grantResults[0]);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);//android 8.0后设置
        switch (requestCode){

            case GlobalConsts.CAMERA:

               // LogUtil.i(TAG,"相机:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.camera));

                }else{

                   // LogUtil.i(TAG,"获取相机权限成功");
                    chooseIcon(ModificationMessageActivity.this);

                }

                break;
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {

        String state = Environment.getExternalStorageState();

        if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26) {//版本大于等于26设置头像
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");

            // 设置裁剪

            startActivityForResult(intent, RESULT_REQUEST_CODE);//RESULT_REQUEST_CODE    PHOTO_ALBUL
        }else{//版本小于26设置头像
            if (state.equals(Environment.MEDIA_MOUNTED)) {

                Intent intentTakePic = new Intent();
                intentTakePic.setAction(Intent.ACTION_PICK);
                intentTakePic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentTakePic, IMAGE_REQUEST_CODE);
            }


        }
    }



    /**
     * 拍照获取图片
     */
    private void takePhoto() {


        try {
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {

                imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

                path = imgPath+String.valueOf(System.currentTimeMillis()) + ".jpg";

                File imgFile = new File(path);

                if (!imgFile.exists()) {
                    imgFile.getParentFile().mkdirs();
                }

                if(Build.VERSION.SDK_INT <26){
                    if (Build.VERSION.SDK_INT < 24) {
                        // 从文件中创建uri
                        imgUri = Uri.fromFile(imgFile);
                    } else {
                        //兼容android7.0 使用共享文件的形式
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA, imgFile.getAbsolutePath());
                        imgUri = this.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    }


                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                }else{
                    startChooser();
                }

            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

    }

    /**
     * 保存裁剪之后的图片数据
     *

     */
    private void getImageToView(File data) {

        final Bitmap bitmap = getLoacaBitmap(data); //从本地取图片(在cdcard中获取)

        if(bitmap != null){

            saveBitmap(bitmap);
        }

    }


    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param file
     * @return
     */
    public Bitmap getLoacaBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }


    public void saveBitmap(final Bitmap mBitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageData = byteArrayOutputStream.toByteArray();

        final Avatar avatar = new Avatar();

        avatar.setUser_id(MyApplicatoin.evenUser.getId());
        avatar.setData(imageData);
        Tools.showProgressDialog(ModificationMessageActivity.this,getResources().getString(R.string.sms_main_hite_please_later));

        sms_request.dorequest(ModificationMessageActivity.this, sms_request.SET, avatar, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {
                if(opcode == sms_request.SET){

                    Tools.closeProgressDialog();
                    Avatar avatars = (Avatar) obj;
                   // LogUtil.i(TAG,"respone:Avatar:"+avatar.getData().length);


                    try {
                        String icon_path = Tools.writeToSdcard(ModificationMessageActivity.this, "user", avatar.getUser_id().toString(),avatar.getData());

                       // LogUtil.i(TAG,"icon_path:"+icon_path);

                        MyApplicatoin.evenUser.setAvatar_url(icon_path);

                        final Bitmap bitmap = getLoacalBitmap(MyApplicatoin.evenUser.getAvatar_url()); //从本地取图片(在cdcard中获取)

                        if(bitmap != null){

                            sms_main_icon.setImageBitmap(bitmap);

                           // user_name.setText(MyApplicatoin.evenUser.getName());

                        }

                    } catch (IOException e) {
                        ExceptionUtil.handleException(e);
                    }

                }

            }
        });

    }

    private File mOutputFile;

    /**
     * 裁剪图片方法实现
     *
     * @param file
     */
    public void startPhotoZoom(File file) {

        try {

            if (Build.VERSION.SDK_INT < 26) {//版本小于26设置头像

                Intent intent = new Intent("com.android.camera.action.CROP");

                mOutputFile = new File(generateImgePath());

                File parentFile = mOutputFile.getParentFile();

                //LogUtil.i(TAG,"IMGpath:"+file.getAbsolutePath());

                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                if(Build.VERSION.SDK_INT < 24){
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                }else{
                    intent.setDataAndType(getImageContentUri(this, file), "image/*");
                }

                // 设置裁剪
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 2);
                intent.putExtra("aspectY", 2);
                // outputX outputY 是裁剪图片宽高

                //服务器头像数据存储最大60K 所以头像上传的数据限定在60K以内
                intent.putExtra("outputX", 150);
                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", true);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                startActivityForResult(intent, RESULT_REQUEST_CODE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        //LogUtil.i(TAG,"onActivityResult:requestCode:"+requestCode+",resultCode:"+resultCode);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);//android 8.0后设置
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case IMAGE_REQUEST_CODE :

                    Uri sourceUri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.managedQuery(sourceUri, proj, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String imgPaths = cursor.getString(columnIndex);
                    File mInputFile = new File(imgPaths);

                    startPhotoZoom(mInputFile);
                    break;
                case CAMERA_REQUEST_CODE :
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {


                        if(path != null){

                            try {
                                File file = new File(path);

                                startPhotoZoom(file);
                            } catch (Exception e) {
                                ExceptionUtil.handleException(e);
                            }

                        }else{

                            //LogUtil.i(TAG,"图片文件为 null");
                        }

                    } else {

                        Tools.showInfo(ModificationMessageActivity.this,getResources().getString(R.string.sms_main_icon_hite_toast));

                    }
                    break;
                case RESULT_REQUEST_CODE : // 图片缩放完成后
                   // LogUtil.i(TAG,"图片缩放完成后:"+mOutputFile);
                    if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26) {//版本大于等于26时文件路径不同，获取路径，设置文件
                        mOutputFile = new File(getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData()));
                    }
                    if (data != null && mOutputFile != null) {
                        getImageToView(mOutputFile);
                    }
                    break;

                default:
                    break;
            }
        }

    }


    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    private String generateImgePath() {
        return this.getExternalStoragePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //  return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";//测试用
    }
    /**
     * 获取SD下的应用目录
     */
    private String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        String ROOT_DIR = "Android/data/" + this.getPackageName();
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }


    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    private Uri getImageContentUri(Context context, File imageFile) {

        try {
            String filePath = imageFile.getAbsolutePath();

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
