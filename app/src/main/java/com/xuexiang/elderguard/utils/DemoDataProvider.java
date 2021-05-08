package com.xuexiang.elderguard.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.gson.reflect.TypeToken;
import com.xuexiang.elderguard.R;
import com.xuexiang.elderguard.adapter.entity.NewInfo;
import com.xuexiang.elderguard.entity.EgUser;
import com.xuexiang.elderguard.entity.EgVisit;
import com.xuexiang.elderguard.fragment.relation.eleme.ElemeGroupedItem;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResourceUtils;

import java.util.ArrayList;
import java.util.List;


public class DemoDataProvider {
    @MemoryCache
    public static List<ElemeGroupedItem> getElemeGroupItems() {
        return JsonUtil.fromJson(ResourceUtils.readStringFromAssert("eleme.json"), new TypeToken<List<ElemeGroupedItem>>() {
        }.getType());
    }

    public static String[] titles = new String[]{
            "如今老龄化不断加剧",
            "养老问题必须重视",
            "老人的安全伙伴",
    };

    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://striveyadong.com/wp-content/uploads/2021/05/3.png",
            "http://striveyadong.com/wp-content/uploads/2021/05/2.png",
            "http://striveyadong.com/wp-content/uploads/2021/05/1.png"

    };

    @MemoryCache
    public static List<BannerItem> getBannerList() {
        List<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            item.title = titles[i];

            list.add(item);
        }
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getDemoNewInfos() {
        List<NewInfo> list = new ArrayList<>();
        list.add(new NewInfo("源码", "Android源码分析--Android系统启动")
                .setSummary("其实Android系统的启动最主要的内容无非是init、Zygote、SystemServer这三个进程的启动，他们一起构成的铁三角是Android系统的基础。")
                .setDetailUrl("https://juejin.im/post/5c6fc0cdf265da2dda694f05")
                .setImageUrl("https://user-gold-cdn.xitu.io/2019/2/22/16914891cd8a950a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));

        list.add(new NewInfo("Android UI", "XUI 一个简洁而优雅的Android原生UI框架，解放你的双手")
                .setSummary("涵盖绝大部分的UI组件：TextView、Button、EditText、ImageView、Spinner、Picker、Dialog、PopupWindow、ProgressBar、LoadingView、StateLayout、FlowLayout、Switch、Actionbar、TabBar、Banner、GuideView、BadgeView、MarqueeView、WebView、SearchView等一系列的组件和丰富多彩的样式主题。\n")
                .setDetailUrl("https://juejin.im/post/5c3ed1dae51d4543805ea48d")
                .setImageUrl("https://user-gold-cdn.xitu.io/2019/1/16/1685563ae5456408?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));

        list.add(new NewInfo("面试", "写给即将面试的你")
                .setSummary("最近由于公司业务发展，需要招聘技术方面的人才，由于我在技术方面比较熟悉，技术面的任务就交给我了。今天我要分享的就和面试有关，主要包含技术面的流程、经验和建议，避免大家在今后的面试过程中走一些弯路，帮助即将需要跳槽面试的人。")
                .setDetailUrl("https://juejin.im/post/5ca4df966fb9a05e4e58320c")
                .setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554629219186&di=6cdab5cfceaae1f7e6d78dbe79104c9f&imgtype=0&src=http%3A%2F%2Fimg.qinxue365.com%2Fuploads%2Fallimg%2F1902%2F4158-1Z22FZ64E00.jpg"));

        list.add(new NewInfo("Android", "XUpdate 一个轻量级、高可用性的Android版本更新框架")
                .setSummary("XUpdate 一个轻量级、高可用性的Android版本更新框架。本框架借鉴了AppUpdate中的部分思想和UI界面，将版本更新中的各部分环节抽离出来，形成了如下几个部分：")
                .setDetailUrl("https://juejin.im/post/5b480b79e51d45190905ef44")
                .setImageUrl("https://user-gold-cdn.xitu.io/2018/7/13/16492d9b7877dc21?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));

        list.add(new NewInfo("Android/HTTP", "XHttp2 一个功能强悍的网络请求库，使用RxJava2 + Retrofit2 + OKHttp进行组装")
                .setSummary("一个功能强悍的网络请求库，使用RxJava2 + Retrofit2 + OKHttp组合进行封装。还不赶紧点击使用说明文档，体验一下吧！")
                .setDetailUrl("https://juejin.im/post/5b6b9b49e51d4576b828978d")
                .setImageUrl("https://user-gold-cdn.xitu.io/2018/8/9/1651c568a7e30e02?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));
        return list;
    }



    private static List<AdapterItem> getGridItems(Context context, int titleArrayId, int iconArrayId) {
        List<AdapterItem> list = new ArrayList<>();
        String[] titles = ResUtils.getStringArray(titleArrayId);
        Drawable[] icons = ResUtils.getDrawableArray(context, iconArrayId);
        for (int i = 0; i < titles.length; i++) {
            list.add(new AdapterItem(titles[i], icons[i]));
        }
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getEmptyNewInfo() {
        List<NewInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new NewInfo());
        }
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<EgVisit> getEmptyVisitInfo() {
        List<EgVisit> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new EgVisit());
        }
        return list;
    }

    public static String getBaseImgUrl() {
        String baseUrl = XHttp.getBaseUrl().trim();
        if (baseUrl.endsWith("/")) {
            return baseUrl + "file/downloadFile/";
        } else {
            return baseUrl + "/file/downloadFile/";
        }

    }

    public static String getVisitImgUrl(EgVisit egVisit) {
        return getBaseImgUrl() + egVisit.getImage();
    }

    public static String getUserImgUrl(EgUser egUser) {
        return getBaseImgUrl() + egUser.getImage();
    }

    public static String getVisitImgUrlWithoutBaseUrl(EgVisit egVisit) {
        return "/file/downloadFile/" + egVisit.getImage();
    }

}
