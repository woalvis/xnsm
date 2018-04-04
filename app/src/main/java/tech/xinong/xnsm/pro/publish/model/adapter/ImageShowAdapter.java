package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by xiao on 2018/1/12.
 */

public class ImageShowAdapter extends BaseAdapter {

    private ArrayList<String> photoList;//照片库里选来的
    private List<ListingDocDTO> result;//修改本来有的
    private List<ListingDocDTO> updateDocs;
    private List<Object> allList;
    private Context context;
    private View.OnClickListener listener;

    public void setDelListener(OnDelListener delListener) {
        this.delListener = delListener;
    }

    private OnDelListener delListener;


    public ImageShowAdapter(Context context){
        this.context = context;
        photoList = new ArrayList<>();
        result = new ArrayList<>();
        allList = new ArrayList<>();
        updateDocs = new ArrayList<>();
    }



    public interface OnDelListener{
        void onDel(int position);
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
        resetAllList();
        notifyDataSetChanged();
    }
    public void setResultList(List<ListingDocDTO> result) {
        this.result = result;
        resetAllList();
        notifyDataSetChanged();
    }

    public List<ListingDocDTO> getResult() {
        return result;
    }


    public void setResult(List<ListingDocDTO> result) {
        this.result = result;
    }

    public ArrayList<String> getPhotoList() {
        return photoList;
    }


    public void setListener(View.OnClickListener listener){
        this.listener = listener;
    }

    private void resetAllList() {
        allList.clear();
        allList.addAll(result);
        allList.addAll(photoList);
    }



    @Override
    public int getCount() {
        return allList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==allList.size()){
            return null;
        }
        return allList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_img,null);
        SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.img);
        ImageView imgDel = convertView.findViewById(R.id.img_del);

        if (getItem(position)==null){
            simpleDraweeView.setImageResource(R.mipmap.gridview_addpic);
            simpleDraweeView.setOnClickListener(listener);
            imgDel.setVisibility(View.GONE);
        }else if ( getItem(position) instanceof String){

            simpleDraweeView.setImageURI("file://"+getItem(position));
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(context);
                    rxDialogScaleView.setImageUri(Uri.parse("file://"+getItem(position)));
                    rxDialogScaleView.show();
                }
            });
            imgDel.setVisibility(View.VISIBLE);
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allList.remove(getItem(position));
                    photoList.remove(position);
                    if (delListener!=null)
                        delListener.onDel(position);
                    notifyDataSetChanged();
                }
            });

        }else{
            imgDel.setVisibility(View.VISIBLE);
            simpleDraweeView.setImageURI(ImageUtil.getImgUrl(((ListingDocDTO)getItem(position)).getDocName()));
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImg(((ListingDocDTO)getItem(position)).getDocName());
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDocs.add((ListingDocDTO) getItem(position));
                    allList.remove(getItem(position));
                    result.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }


    public List<ListingDocDTO> getUpdateDocs(){
        for (ListingDocDTO listingDocDTO : updateDocs){
            listingDocDTO.setDocName("");
        }
        return updateDocs;
    }


    private Bitmap adjustImage(String absolutePath,Bitmap bm,ImageView imageView) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;


        int screenWidth = DeviceInfoUtil.getScreenWidth();
        int screenHeight = DeviceInfoUtil.getScreenHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm= BitmapFactory.decodeFile(absolutePath, opt);
        return bm;
    }


    public void showImg(String url){

        OkGo.get(ImageUtil.getImgUrl(url)).execute(new FileCallback() {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(context);
                rxDialogScaleView.setImagePath(file.getAbsolutePath());
                rxDialogScaleView.show();
            }
        });


    }



}
