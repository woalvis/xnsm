package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_upload)
public class UploadActivity extends BaseActivity {
    public static final int RESULT_LOAD_IMAGE = 101;
    @ViewInject(R.id.upload_gv_pics)
    private GridView gvPics;
    @ViewInject(R.id.upload_bt_upload)
    private Button btUpload;
    @ViewInject(R.id.select_photos)
    private Button buSelect;


    @OnClick({R.id.upload_bt_upload, R.id.select_photos})
    private void widgetClick(View view) {
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

    }

    /**
     * 选择图片的按钮，应该是可以拍照或者去图片库里面找
     */
    private void selectPhotos() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
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
    }
}
