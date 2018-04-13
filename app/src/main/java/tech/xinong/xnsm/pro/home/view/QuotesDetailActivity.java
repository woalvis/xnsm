package tech.xinong.xnsm.pro.home.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.home.model.PriceDetailModel;
import tech.xinong.xnsm.pro.home.model.PriceModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.HorizontalScrollMenu;
import tech.xinong.xnsm.views.entity.BaseAdapter;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

@ContentView(R.layout.activity_quotes_detail)
public class QuotesDetailActivity extends BaseActivity {

    @ViewInject(R.id.line_chart)
    private LineChartView lineChart;
    @ViewInject(R.id.hsm_container)
    private HorizontalScrollMenu hsm_container;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;
    @ViewInject(R.id.rl_show)
    private RelativeLayout rl_show;
    @ViewInject(R.id.tv_no_data)
    private TextView tv_no_data;
    @ViewInject(R.id.tv_compare)
    private TextView tv_compare;
    @ViewInject(R.id.tv_present)
    private TextView tv_present;
    @ViewInject(R.id.im_state)
    private ImageView im_state;
    private List<String> dates = new ArrayList<>();//X轴的标注
    private List<Float> prices = new ArrayList<>();//图表的数据
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisValues = new ArrayList<>();
    private String specId;
    private String cityId;
    private List<PriceDetailModel> priceDetailModels;
    private PriceModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @ViewInject(R.id.tv_today_price)
    private TextView tv_today_price;
    private List<SpecificationConfigDTO> spes;
    private int currentPosition;

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {

        specId = intent.getStringExtra("specId");
        cityId = intent.getStringExtra("cityId");
        model = (PriceModel) intent.getSerializableExtra("model");
        setToolBarTitle(model.getProvince()+model.getCity()+model.getSpecName()+"的行情");

        XinongHttpCommend.getInstance(mContext).getSpecByProductId(model.getProductId(),
                new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                spes = JSON.parseArray(result,SpecificationConfigDTO.class);
                List<String> arr = new ArrayList<>();
                for (int i=0;i<spes.size();i++){
                    if (specId.equals(spes.get(i).getId())){
                        arr.add(0,spes.get(i).getName());
                        spes.add(0,spes.get(i));
                        spes.remove(i+1);
                    }else {
                        arr.add(spes.get(i).getName());
                    }


                }
                hsm_container.setSwiped(false);
                hsm_container.setAdapter(new MenuAdapter(arr),currentPosition);
                //updateChart(specId);
            }
        });


    }


    private void updateChart(SpecificationConfigDTO spec){

        setToolBarTitle(model.getProvince()+model.getCity()+spec.getName()+"的行情");

        XinongHttpCommend.getInstance(mContext).priceSpceOrCity(spec.getId(), cityId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                tv_empty.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                priceDetailModels = JSON.parseArray(result,PriceDetailModel.class);
                if (priceDetailModels.size()>0){
                    dates.clear();
                    prices.clear();
                    for (PriceDetailModel model : priceDetailModels){
                        dates.add(model.getRecordingDate().substring(5,model.getRecordingDate().length()));
                        prices.add(model.getCurrentAveragePrice().setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    }
                    PriceDetailModel cuPriceDetailModel = priceDetailModels.get(priceDetailModels.size()-1);
                    if (cuPriceDetailModel.getRecordingDate().equals(
                            sdf.format(new Date(System.currentTimeMillis()-1000*60*60*24)))){//如果相等说明有当前价格
                        tv_today_price.setText(cuPriceDetailModel.getCurrentAveragePrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+
                                cuPriceDetailModel.getUom());
                        PriceDetailModel cuPriceDetailModel2 = priceDetailModels.get(priceDetailModels.size()-2);
                        if (cuPriceDetailModel2!=null){
                            if (cuPriceDetailModel2.getRecordingDate().equals(
                                    sdf.format(new Date(System.currentTimeMillis()-1000*60*60*24*2)))){
                                BigDecimal today = cuPriceDetailModel.getCurrentAveragePrice();
                                BigDecimal yesToday = cuPriceDetailModel2.getCurrentAveragePrice();
                                BigDecimal compare = today.subtract(yesToday);
                                tv_no_data.setVisibility(View.GONE);
                                tv_present.setVisibility(View.VISIBLE);
                                rl_show.setVisibility(View.VISIBLE);
                                if (compare.compareTo(new BigDecimal(0))>0){
                                    tv_compare.setText("上涨"+compare.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+
                                            cuPriceDetailModel.getUom());
                                    im_state.setImageResource(R.drawable.arrow_up);
                                }else {
                                    tv_compare.setText("下跌"+compare.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+
                                            cuPriceDetailModel.getUom());
                                    im_state.setImageResource(R.drawable.arrow_down);
                                }
                                String present = compare.divide(today,2,ROUND_HALF_DOWN).multiply(new BigDecimal(100)).
                                        setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%";
                                tv_present.setText(present);
                            }
                        }

                    }else {
                        tv_today_price.setText("暂无");
                        rl_show.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }


                    getAxisLables();//获取x轴的标注
                    getAxisPoints();//获取坐标点
                    initLineChart();//初始化
                }else {
                    tv_empty.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.GONE);
                    tv_today_price.setText("暂无");
                    rl_show.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.GREEN).setCubic(false);  //折线的颜色
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(true);//是否填充曲线的面积
//      line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        //axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.GREEN);  //设置字体颜色
        axisX.setName(model.getCity()+spes.get(currentPosition).getName()+"均价走势");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(3);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//      data.setAxisXTop(axisX);  //x 轴在顶部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
        axisY.setName("价格");//y轴标注
        axisY.setTextSize(7);//设置字体大小


        data.setAxisYLeft(axisY);  //Y轴设置在左边
//      data.setAxisYRight(axisY);  //y轴设置在右边




        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        final Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = 0;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
    }


    /**
     * X 轴的显示
     */
    private void getAxisLables(){
        mAxisValues.clear();
        for (int i = 0; i < dates.size(); i++) {
            mAxisValues.add(new AxisValue(i).setLabel(dates.get(i)));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        mPointValues.clear();
        for (int i = 0; i < prices.size(); i++) {
            mPointValues.add(new PointValue(i, prices.get(i)));
        }
    }


    class MenuAdapter extends BaseAdapter
    {

        public MenuAdapter(List<String> names){
            this.names = names;
        }
        private List<String> names;

        @Override
        public List<String> getMenuItems()
        {
            // TODO Auto-generated method stub
            return names;
        }

        @Override
        public List<View> getContentViews()
        {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (String str : names)
            {
                View v = LayoutInflater.from(QuotesDetailActivity.this).inflate(
                        R.layout.content_view, null);
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus)
        {
            // TODO Auto-generated method stub
//            Toast.makeText(QuotesDetailActivity.this,
//                    "内容页：" + (position + 1) + " 访问状态：" + visitStatus,
//                    Toast.LENGTH_SHORT).show();
            currentPosition = position;
            updateChart(spes.get(position));

        }

    }

}
