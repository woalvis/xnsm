package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.FileUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_upload)
public class UploadActivity extends BaseActivity implements ImageLoader.DownloadSuccessListener{
    public static final int RESULT_LOAD_IMAGE = 101;

    @ViewInject(R.id.upload_bt_upload)
    private Button btUpload;
    @ViewInject(R.id.select_photos)
    private Button buSelect;
    @ViewInject(R.id.show_image)
    private ImageView showIm;
    private Bitmap tempBitmap;
    private Uri photoUri;

    private String orderId;

    private static final int REQ_SELECT_PIC = 0x1001;


    @Override
    public void initData() {
        orderId = getIntent().getStringExtra("orderId");
    }

    @OnClick({R.id.upload_bt_upload, R.id.select_photos})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.upload_bt_upload:
                upLoad();
                break;
            case R.id.select_photos:
                selectPhotos();
                break;
        }
    }

    private void upLoad() {
//        String imageUrl = String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,"Y0","b6bcecaf9d704f078f3d168d6c11e3c0");
//        //String imageUrl = "http://img3.imgtn.bdimg.com/it/u=214931719,1608091472&fm=21&gp=0.jpg";
//        ImageLoader.getInstance().setLis(this).displayImage(imageUrl,showIm);

        File file = FileUtil.bitmapToFile(mContext,tempBitmap,"uploadFile.jpg");
        showProgress("正在上传");
        XinongHttpCommend.getInstance(mContext).uploadBuyOrderTopay(
                orderId,
                file,
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        cancelProgress();
                        if (info.equals(HttpConstant.OK)){
                            T.showShort(mContext,result);
                            UploadActivity.this.finish();
                        }
                    }
                }
        );



    }

    /**
     * 选择图片的按钮，应该是可以拍照或者去图片库里面找
     */
    private void selectPhotos() {
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, RESULT_LOAD_IMAGE);

        Intent intent = new Intent(this, SelectPhotoTheWayActivity.class);

        //startActivity(intent);
        startActivityForResult(intent,REQ_SELECT_PIC);

        overridePendingTransition(R.animator.acitivity_entry, R.animator.activity_exit);

    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        startActivityForResult(intent, 1);
    }


    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.RESULT_LOAD_IMAGE) {
            if (data != null) {
                Uri selectImg = data.getData();
                String[] filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectImg, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imagePath = cursor.getString(columnIndex);
//                cursor.close();
//                uploadFile = new File(imagePath);
//                uploadImg.setText("");
//                uploadImg.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(imagePath)));
            }
        }


        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_SELECT_PIC:
                    Bitmap bitmap = data.getParcelableExtra("bitmap");
                    showIm.setImageBitmap(bitmap);
                    tempBitmap = bitmap;
                    break;
            }
        }
    }

    @Override
    public void onDownloadSuccess(final Bitmap bitmap,final ImageView imageView) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });

    }
}
