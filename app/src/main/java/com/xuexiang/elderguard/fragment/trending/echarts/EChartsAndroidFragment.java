package com.xuexiang.elderguard.fragment.trending.echarts;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.core.webview.BaseWebViewFragment;
import com.xuexiang.elderguard.utils.Utils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;


@Page(anim = CoreAnim.none)
public class EChartsAndroidFragment extends BaseWebViewFragment {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private ChartInterface mChartInterface;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_echarts_android;
    }

    @Override
    protected void initViews() {
        mAgentWeb = Utils.createAgentWeb(this, flContainer, "file:///android_asset/chart/src/template.html");

        mAgentWeb.getJsInterfaceHolder().addJavaObject("Android", mChartInterface = new ChartInterface());

    }

    @SingleClick
    @OnClick({R.id.btn_bar_chart, R.id.btn_line_chart, R.id.btn_pie_chart, R.id.btn_hot_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bar_chart:
                initBarChart();
                break;
            case R.id.btn_line_chart:
                initLineChart();
                break;
            case R.id.btn_pie_chart:
                initPieChart();
                break;
            case R.id.btn_hot_chart:
                initHotChart();
                break;
            default:
                break;
        }
    }

    private void initBarChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeBarChartOptions());
    }

    private void initLineChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLineChartOptions());
    }

    private void initPieChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makePieChartOptions());
    }

    private void initHotChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLinesChartOptions());

    }


    /**
     * 注入到JS里的对象接口
     */
    public static class ChartInterface {

        @JavascriptInterface
        public String makeBarChartOptions() {
            GsonOption option = new GsonOption();
            option.setLegend(new Legend().data("上周", "本周"));
            option.tooltip().trigger(Trigger.item).formatter("{b} : {c}");
            option.xAxis(new CategoryAxis().data("访问总量", "陌生人", "熟客", "男性", "关系", "女性"));
            option.yAxis();

            Bar bar = new Bar("上周");
            Bar bar1 = new Bar("本周");
            bar.data(5, 2, 3, 2, 2, 1);
            bar1.data(7, 2, 5, 2, 4, 3);
            option.series(bar, bar1);

            return option.toString();
        }

        @JavascriptInterface
        public String makeLineChartOptions() {
            GsonOption option = new GsonOption();
            option.toolbox().show(false);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis).formatter("{a} : {c}个");
            option.legend().data("总数", "熟客", "生客");

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.axisLabel().formatter("{value} 次");
            option.yAxis(valueAxis);

            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.axisLabel().formatter("{value} 月");
            categoryAxis.boundaryGap(false);
            categoryAxis.data(1, 2, 3, 4, 5);
            option.xAxis(categoryAxis);


            Line line1 = new Line("总数").smooth(true).data(0, 0, 0, 22, 8);
            Line line2 = new Line("熟客").smooth(true).data(0, 0, 0, 13, 5);
            Line line3 = new Line("生客").smooth(true).data(0, 0, 0, 9, 3);
            option.series(line1, line2, line3);
            return option.toString();
        }

        @JavascriptInterface
        public String makePieChartOptions() {
            GsonOption option = new GsonOption();
            option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
            option.legend().data("生客", "熟客");

            Pie pie = new Pie("访问类别").data(
                    new PieData("熟客", 5),
                    new PieData("生客", 3)
            ).center("50%", "45%").radius("50%");
            pie.label().normal().show(true).formatter("{b}{c}({d}%)");
            option.series(pie);
            return option.toString();
        }

        @JavascriptInterface
        public String makeLinesChartOptions() {
            GsonOption option = new GsonOption();
            option.toolbox().show(false);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis).formatter("{a} : {c}个");
            option.legend().data("总数", "熟客", "生客");

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.axisLabel().formatter("{value} 次");
            option.yAxis(valueAxis);

            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.axisLabel().formatter("{value}号");
            categoryAxis.boundaryGap(false);
            categoryAxis.data(1, 2, 3, 4, 5, 6, 7, 8, 9);
            option.xAxis(categoryAxis);


            Line line1 = new Line("总数").smooth(true).data(2, 0, 0, 1, 3, 0, 0, 0, 3);
            Line line2 = new Line("熟客").smooth(true).data(2, 0, 0, 0, 2, 0, 0, 0, 3);
            Line line3 = new Line("生客").smooth(true).data(0, 0, 0, 1, 1, 0, 0, 0, 0);
            option.series(line1, line2, line3);
            return option.toString();
        }
    }

}
