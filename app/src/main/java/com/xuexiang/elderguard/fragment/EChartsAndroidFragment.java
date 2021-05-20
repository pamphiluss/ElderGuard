package com.xuexiang.elderguard.fragment;

import android.annotation.SuppressLint;
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
import com.xuexiang.elderguard.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.elderguard.core.webview.BaseWebViewFragment;
import com.xuexiang.elderguard.manager.TokenManager;
import com.xuexiang.elderguard.utils.Utils;
import com.xuexiang.elderguard.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.utils.TypeUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;


@Page(anim = CoreAnim.none)
public class EChartsAndroidFragment extends BaseWebViewFragment {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private List<Integer> lastWeek;
    private List<Integer> thisWeek;
    private List<Integer> AcqYear;
    private List<Integer> StrangerYear;
    private int thisMonthAcqAll;
    private int thisMonthStrAll;
    private List<Integer> thisMonthAcq;
    private List<Integer> thisMonthstr;
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
        getLastWeekDate();
        getThisWeekDate();
        GetAcqYear();
        GetStrYear();
        GetthisMonthAcqAll();
        GetthisMonthStrAll();
        GetthisMonthAcq();
        GetthisMonthStr();
        mAgentWeb = Utils.createAgentWeb(this, flContainer, "file:///android_asset/chart/src/template.html");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("Android", mChartInterface = new ChartInterface());

    }

    @SingleClick
    @OnClick({R.id.btn_bar_chart, R.id.btn_line_chart, R.id.btn_pie_chart, R.id.btn_hot_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bar_chart:

                initBarChart(lastWeek, thisWeek);
                break;
            case R.id.btn_line_chart:

                initLineChart(AcqYear, StrangerYear);
                break;
            case R.id.btn_pie_chart:

                initPieChart(thisMonthAcqAll, thisMonthStrAll);
                break;
            case R.id.btn_hot_chart:

                initHotChart(thisMonthAcq, thisMonthstr);
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void getLastWeekDate() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getLastWeekDate")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    lastWeek = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getThisWeekDate() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getThisWeekDate")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    thisWeek = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetAcqYear() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getAcqYear")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    AcqYear = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetStrYear() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getStrangerYear")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    StrangerYear = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetthisMonthAcqAll() {
        Observable<Integer> observable = XHttp.post("/chart/GetthisMonthAcqAll")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(Integer.class);
        observable.subscribeWith(new TipRequestSubscriber<Integer>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Integer response) {
                if (response != null) {
                    thisMonthAcqAll = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetthisMonthStrAll() {
        Observable<Integer> observable = XHttp.post("/chart/GetthisMonthStrAll")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(Integer.class);
        observable.subscribeWith(new TipRequestSubscriber<Integer>() {
            @SuppressLint("CheckResult")
            @Override
            protected void onSuccess(Integer response) {
                if (response != null) {
                    thisMonthStrAll = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetthisMonthAcq() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getthisMonthAcq")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {

            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    thisMonthAcq = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void GetthisMonthStr() {
        Observable<List<Integer>> observable = XHttp.post("/chart/getthisMonthStr")
                .params("userId", TokenManager.getInstance().getLoginUser().getId())
                .syncRequest(false)
                .onMainThread(true)
                .execute(TypeUtils.getListType(Integer.class));
        observable.subscribeWith(new TipRequestSubscriber<List<Integer>>() {

            @Override
            protected void onSuccess(List<Integer> response) {
                if (response != null) {
                    thisMonthstr = response;
                    XToastUtils.info("删除成功");

                } else {
                    XToastUtils.info("删除失败");
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onError(ApiException e) {
                XToastUtils.info("访问错误");
            }
        });
    }

    private void initBarChart(List<Integer> lastWeek, List<Integer> thisWeek) {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeBarChartOptions(lastWeek, thisWeek));
    }

    private void initLineChart(List<Integer> AcqYear, List<Integer> StrangerYear) {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLineChartOptions(AcqYear, StrangerYear));
    }

    private void initPieChart(int thisMonthAcqAll, int thisMonthStrAll) {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makePieChartOptions(thisMonthAcqAll, thisMonthStrAll));
    }

    private void initHotChart(List<Integer> thisMonthAcq, List<Integer> thisMonthStr) {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLinesChartOptions(thisMonthAcq, thisMonthStr));

    }


    /**
     * 注入到JS里的对象接口
     */
    public static class ChartInterface {

        @JavascriptInterface
        public String makeBarChartOptions(List<Integer> lastWeek, List<Integer> thisWeek) {
            GsonOption option = new GsonOption();
            option.setLegend(new Legend().data("上周", "本周"));
            option.tooltip().trigger(Trigger.item).formatter("{b} : {c}");
            option.xAxis(new CategoryAxis().data("访问总量", "陌生人", "熟客", "男性", "女性"));
            option.yAxis();

            Bar bar = new Bar("上周");
            Bar bar1 = new Bar("本周");
            for (Integer integer : lastWeek) {
                bar.data().add(integer);
            }
            for (Integer integer : thisWeek) {
                bar1.data().add(integer);
            }
            option.series(bar, bar1);

            return option.toString();
        }

        @JavascriptInterface
        public String makeLineChartOptions(List<Integer> AcqYear, List<Integer> StrangerYear) {
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


            Line line1 = new Line("总数").smooth(true);
            Line line2 = new Line("熟客").smooth(true);
            Line line3 = new Line("生客").smooth(true);
            for (Integer integer : AcqYear) {
                line2.data().add(integer);
            }
            for (Integer integer : StrangerYear) {
                line3.data().add(integer);
            }
            for (int i = 0; i < AcqYear.size(); i++) {
                line1.data().add(AcqYear.get(i) + StrangerYear.get(i));
            }

            option.series(line1, line2, line3);
            return option.toString();
        }

        @JavascriptInterface
        public String makePieChartOptions(int thisMonthAcqAll, int thisMonthStrAll) {
            GsonOption option = new GsonOption();
            option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
            option.legend().data("生客", "熟客");

            Pie pie = new Pie("访问类别").data(
                    new PieData("熟客", thisMonthAcqAll),
                    new PieData("生客", thisMonthStrAll)
            ).center("50%", "45%").radius("50%");
            pie.label().normal().show(true).formatter("{b}{c}({d}%)");
            option.series(pie);
            return option.toString();
        }

        @JavascriptInterface
        public String makeLinesChartOptions(List<Integer> thisMonthAcq, List<Integer> thisMonthStr) {
            GsonOption option = new GsonOption();
            option.toolbox().show(false);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis).formatter("{b}号: {c}个");
            option.legend().data("总数", "熟客", "生客");

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.axisLabel().formatter("{value} 次");
            option.yAxis(valueAxis);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
            Date date = new Date(System.currentTimeMillis());
            int s = Integer.parseInt(formatter.format(date));
            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.axisLabel().formatter("{value}号");
            categoryAxis.boundaryGap(false);
            categoryAxis.data(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
            option.xAxis(categoryAxis);


            Line line1 = new Line("总数").smooth(true);
            Line line2 = new Line("熟客").smooth(true);
            Line line3 = new Line("生客").smooth(true);
            for (Integer integer : thisMonthAcq) {
                line2.data().add(integer);
            }
            for (Integer integer : thisMonthStr) {
                line3.data().add(integer);
            }

            for (int i = 0; i < thisMonthAcq.size(); i++) {
                line1.data().add(thisMonthAcq.get(i) + thisMonthStr.get(i));
            }
            option.series(line1, line2, line3);
            return option.toString();
        }
    }

}
