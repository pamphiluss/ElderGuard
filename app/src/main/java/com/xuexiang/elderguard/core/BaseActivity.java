
package com.xuexiang.elderguard.core;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xrouter.facade.service.SerializationService;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.slideback.SlideBack;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BaseActivity extends XPageActivity {

    Unbinder mUnbinder;

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * 是否支持侧滑返回
     */
    public static final String KEY_SUPPORT_SLIDE_BACK = "key_support_slide_back";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStatusBarStyle();
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);

        registerSlideBack();
    }

    /**
     * 初始化状态栏的样式
     */
    protected void initStatusBarStyle() {

    }

    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }


    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return openPage(clazz, false);
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    public String serializeObject(Object object) {
        return XRouter.getInstance().navigation(SerializationService.class).object2Json(object);
    }

    @Override
    protected void onRelease() {
        mUnbinder.unbind();
        unregisterSlideBack();
        super.onRelease();
    }

    /**
     * 注册侧滑回调
     */
    protected void registerSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.with(this)
                    .haveScroll(true)
                    .edgeMode(ResUtils.isRtl() ? SlideBack.EDGE_RIGHT : SlideBack.EDGE_LEFT)
                    .callBack(this::popPage)
                    .register();
        }
    }

    /**
     * 注销侧滑回调
     */
    protected void unregisterSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.unregister(this);
        }
    }

    /**
     * @return 是否支持侧滑返回
     */
    protected boolean isSupportSlideBack() {
        CoreSwitchBean page = getIntent().getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN);
        return page == null || page.getBundle() == null || page.getBundle().getBoolean(KEY_SUPPORT_SLIDE_BACK, true);
    }

}
