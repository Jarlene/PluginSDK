package com.baidu.music.plugin.context;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;

import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.utils.ApkUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginResource {

    public static final String TAG = PluginResource.class.getSimpleName();

    private Resources     res;// 获取的资源apk里面的res
    private String        apkPackageName;// 资源apk里面的包名
    private PluginContext mPluginContext;

    public PluginResource(PluginContext context, PluginItem item) {
        this.mPluginContext = context;
        this.res = context.getResources();
        this.apkPackageName = ApkUtils.getPackageInfo(context, item.getPluginPath()).packageName;

    }

    /**
     * 获取layout文件中的id号
     *
     * @param layoutName layout名
     */
    public int getResApkLayoutId(String layoutName) {
        LogUtil.d(TAG, "getResApkLayoutId");
        return res.getIdentifier(layoutName, "layout", apkPackageName);
    }

    /**
     * 获取布局layout文件
     *
     * @return view
     * @params layoutName
     */
    public View getResApkLayoutView(String layoutName) {
        LogUtil.d(TAG, "getResApkLayoutView");
        LayoutInflater inflater =
                (LayoutInflater) mPluginContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(res.getLayout(getResApkLayoutId(layoutName)), null);
    }

    /**
     * 获取控件view的id号
     *
     * @param widgetName 控件名
     */
    public int getResApkWidgetViewID(String widgetName) {
        LogUtil.d(TAG, "getResApkWidgetViewID");
        return res.getIdentifier(widgetName, "id", apkPackageName);
    }

    /**
     * 获取布局文件中的控件
     *
     * @return widgetView
     * @params layout, 资源apk中的布局(view)
     * @params widgetName 控件名称
     */
    public View getResApkWidgetView(View layout, String widgetName) {
        LogUtil.d(TAG, "getResApkWidgetView");
        return layout.findViewById(getResApkWidgetViewID(widgetName));
    }

    /**
     * 获取drawable文件的id
     *
     * @param imgName 图片名字
     */
    public int getDrawableId(String imgName) {
        LogUtil.d(TAG, "getDrawableId");
        return res.getIdentifier(imgName, "drawable", apkPackageName);
    }

    /**
     * 获取图片资源
     *
     * @param imgName
     * @return drawable
     */
    public Drawable getResApkDrawable(String imgName) {
        LogUtil.d(TAG, "getResApkDrawable");
        return res.getDrawable(getDrawableId(imgName));
    }

    /**
     * 获取string文件中的id号
     *
     * @param stringName 字符串在String文件中的名字
     */
    public int getResApkStringId(String stringName) {
        LogUtil.d(TAG, "getResApkStringId");
        return res.getIdentifier(stringName, "string", apkPackageName);
    }

    /**
     * 获取String字符串
     *
     * @param stringName
     * @return string
     */
    public String getResApkString(String stringName) {
        LogUtil.d(TAG, "getResApkString");
        return res.getString(getResApkStringId(stringName));
    }

    /**
     * 获取anim文件中的id号
     *
     * @param animationName
     */
    public int getResApkAnimId(String animationName) {
        LogUtil.d(TAG, "getResApkAnimId");
        return res.getIdentifier(animationName, "anim", apkPackageName);
    }

    /**
     * 获取anim文件 XmlPullParser
     *
     * @param animationName
     * @return XmlPullParser
     */
    public XmlPullParser getResApkAnimXml(String animationName) {
        LogUtil.d(TAG, "getResApkAnimXml");
        return res.getAnimation(getResApkAnimId(animationName));
    }

    /**
     * 获取动画anim
     *
     * @param context
     * @params animationName
     */
    public Animation getResApkAnim(Context context, String animationName) {
        LogUtil.d(TAG, "getResApkAnim");
        Animation animation = null;
        XmlPullParser parser = getResApkAnimXml(animationName);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        try {
            animation = createAnimationFromXml(context, parser, null, attrs);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return animation;
    }

    /**
     * 获取anim动画
     */
    private Animation createAnimationFromXml(Context c, XmlPullParser parser, AnimationSet parent,
            AttributeSet attrs) throws XmlPullParserException, IOException {
        LogUtil.d(TAG, "createAnimationFromXml");
        Animation anim = null;
        int type;
        int depth = parser.getDepth();
        while (((type =
                parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && type !=
                XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("set")) {
                anim = new AnimationSet(c, attrs);
                createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
            } else if (name.equals("alpha")) {
                anim = new AlphaAnimation(c, attrs);
            } else if (name.equals("scale")) {
                anim = new ScaleAnimation(c, attrs);
            } else if (name.equals("rotate")) {
                anim = new RotateAnimation(c, attrs);
            } else if (name.equals("translate")) {
                anim = new TranslateAnimation(c, attrs);
            } else {
                throw new RuntimeException("Unknown animation name: " + parser.getName());
            }
            if (parent != null) {
                parent.addAnimation(anim);
            }
        }
        return anim;
    }

    /**
     * 获取 color文件中的id号
     *
     * @param colorName
     */
    public int getResApkColorId(String colorName) {
        LogUtil.d(TAG, "getResApkColorId");
        return res.getIdentifier(colorName, "color", apkPackageName);
    }

    /**
     * 获取color 值
     *
     * @param colorName
     * @return int
     */

    public int getResApkColor(String colorName) {
        LogUtil.d(TAG, "getResApkColor");
        return res.getColor(getResApkColorId(colorName));
    }

    /**
     * 获取 dimens文件中的id号
     *
     * @param dimenName
     */
    public int getResApkDimensId(String dimenName) {
        LogUtil.d(TAG, "getResApkDimensId");
        return res.getIdentifier(dimenName, "dimen", apkPackageName);
    }

    /**
     * 获取dimens文件中值
     *
     * @param dimenName
     * @return float
     */
    public float getResApkDimens(String dimenName) {
        LogUtil.d(TAG, "getResApkDimens");
        return res.getDimension(getResApkDimensId(dimenName));
    }
}
