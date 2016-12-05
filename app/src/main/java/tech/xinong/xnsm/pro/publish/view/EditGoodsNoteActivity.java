package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_edit_goos_note)
public class EditGoodsNoteActivity extends BaseActivity {
    @ViewInject(R.id.edit_goods_note_et)
    private EditText etGoodsNote;
    @ViewInject(R.id.edit_goods_note_submit)
    private Button btGoodsNote;

    @Override
    public void initWidget() {
        btGoodsNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = etGoodsNote.getText().toString().trim();
                if (!TextUtils.isEmpty(result)){
                    Intent intent = new Intent();
                    intent.putExtra("result",result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
}
