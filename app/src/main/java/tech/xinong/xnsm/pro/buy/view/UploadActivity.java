package tech.xinong.xnsm.pro.buy.view;

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
    @ViewInject(R.id.upload_gv_pics)
    private GridView gvPics;
    @ViewInject(R.id.upload_bt_upload)
    private Button btUpload;
    @ViewInject(R.id.select_photos)
    private Button buSelect;


    @OnClick({R.id.upload_bt_upload,R.id.select_photos})
    private void widgetClick(View view){
        switch (view.getId()){
            case R.id.upload_bt_upload:
                break;
            case R.id.select_photos:
                break;
        }
    }

}
