package com.sjsu.cmpe277.weatherapp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.io.Serializable;

/**
 * Created by Mak on 11/5/17.
 */

public class ViewPagerExtension extends ViewPager implements Serializable {
    public ViewPagerExtension(Context context) {
        super(context);
    }

    public ViewPagerExtension(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
