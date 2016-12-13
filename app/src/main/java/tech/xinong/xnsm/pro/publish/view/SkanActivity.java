package tech.xinong.xnsm.pro.publish.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.views.MyImageView;

public class SkanActivity extends Activity {
	private GridView mGridView;
	private ArrayList<String> lists;
	private LayoutInflater inflate;
	private int viewWidth=0,viewHeight=0;
	private MyAdapter adapter;
	private ArrayList<String> strs=new ArrayList<String>();
	private LinearLayout navigationLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);
		lists=(ArrayList<String>) getIntent().getExtras().get("data");
		initView();
	}
	private void initView() {
		initNavigation();
		mGridView=(GridView) findViewById(R.id.child_grid);
		inflate=LayoutInflater.from(SkanActivity.this);
		adapter=new MyAdapter();
		mGridView.setAdapter(adapter);
	}

	private void initNavigation() {
		navigationLayout = (LinearLayout) findViewById(R.id.show_image_navigation);
		ImageView leftImage = (ImageView) navigationLayout.findViewById(R.id.im_left);
		leftImage.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		Intent intent=new Intent();
		intent.putExtra("data", strs);
		setResult(200,intent);
		finish();
		super.onBackPressed();
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return lists.size();
		}
		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String path=lists.get(position);
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflate.inflate(R.layout.item_select_photo_grid_child, null);
				viewHolder.image=(MyImageView) convertView.findViewById(R.id.child_image);
				viewHolder.check=(CheckBox) convertView.findViewById(R.id.child_checkbox);
				convertView.setTag(viewHolder);
				viewHolder.image.setOnMeasureListener(new MyImageView.OnMeasureListener() {
					@Override
					public void onMeasureSize(int width, int height) {
						viewWidth=width;
						viewHeight=height;
					}
				});
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
				viewHolder.image.setImageResource(R.mipmap.friends_sends_pictures_no);
			}
			viewHolder.image.setTag(path);
			Bitmap bitmap= ImageLoader.getInstance().loadImage(path, viewWidth, viewHeight, new ImageLoader.OnCallBackListener() {
				@Override
				public void setOnCallBackListener(Bitmap bitmap, String url) {
					ImageView image=(ImageView) mGridView.findViewWithTag(url);
					if(image!=null&&bitmap!=null){
						image.setImageBitmap(bitmap);
					}
				}
			});
			if(bitmap!=null){
				viewHolder.image.setImageBitmap(bitmap);
			}else{				
				viewHolder.image.setImageResource(R.mipmap.friends_sends_pictures_no);
			}
			viewHolder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						strs.add(path);
					}else{
						if(strs.contains(path))
							strs.remove(path);
					}
				}
			});
			
			return convertView;
			
		}		
	}
	private static class ViewHolder{
		public MyImageView image;
		public CheckBox check;
	}
	
	
}
