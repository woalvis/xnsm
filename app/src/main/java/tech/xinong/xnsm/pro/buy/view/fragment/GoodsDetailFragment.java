package tech.xinong.xnsm.pro.buy.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ListingDetailsDTO;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by xiao on 2017/11/23.
 */

public class GoodsDetailFragment extends Fragment{
    private ListingDetailsDTO data;
    private static final String ARG_PARAM1 = "data";
    private TextView tv_goods_note;
    private ListView lv_imgs;
    private TextView tv_create_time;
    private String urlTemp;
    public GoodsDetailFragment(){}
    public static GoodsDetailFragment newInstance(ListingDetailsDTO param1) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (ListingDetailsDTO) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail,container,false);
        tv_goods_note = view.findViewById(R.id.tv_goods_note);
        lv_imgs = view.findViewById(R.id.lv_imgs);
        tv_create_time =  view.findViewById(R.id.tv_create_time);
        tv_goods_note.setText(data.getNotes());
        List<ListingDocDTO> docs = new ArrayList<>(data.getListingDocs());
        lv_imgs.setAdapter(new CommonAdapter<ListingDocDTO>(getActivity(),
                R.layout.item_img_show,docs) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final ListingDocDTO item) {
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.img);
                simpleDraweeView.setImageURI(ImageUtil.getImgUrl(item.getDocName()));
                viewHolder.setOnClickListener(R.id.img, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        urlTemp = ImageUtil.getImgUrl(item.getDocName());
                        showImg(urlTemp);
                    }
                });
                //Glide.with(getActivity()).load(ImageUtil.getImgUrl(item.getDocName())).into(simpleDraweeView);
            }
        });
        tv_create_time.setText(data.getCreateTime());
        return view;
    }




    public void showImg(String url){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                return;
            }else {
                showImage(url);
            }
            return;
        }else{
            showImage(url);
        }


    }

    private void showImage(String url){
        OkGo.get(ImageUtil.getImgUrl(url)).execute(new FileCallback() {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(getActivity());
                rxDialogScaleView.setImagePath(file.getAbsolutePath());
                rxDialogScaleView.show();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==getActivity().RESULT_OK){
            if (requestCode==222){
                showImage(urlTemp);
            }
        }
    }
}
