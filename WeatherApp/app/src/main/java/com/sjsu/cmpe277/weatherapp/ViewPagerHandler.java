package com.sjsu.cmpe277.weatherapp;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Mak on 11/3/17.
 */

public class ViewPagerHandler {

    private MainPageAdapter pagerAdapter = null;
    private static final String LOG_TAG = "ViewPagerHandler";
    ViewPager pager;


    public ViewPagerHandler(MainPageAdapter pagerAdapter, ViewPager pager) {
        this.pagerAdapter = pagerAdapter;
        this.pager = pager;
    }

    public void addView (View newPage)
    {
        Log.e(LOG_TAG,"ADDING VIEW");
        int pageIndex = pagerAdapter.addView (newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem (pageIndex, true);
    }

    public void removeView (View defunctPage)
    {
        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem (pageIndex);
    }

    public View getCurrentPage ()
    {
        return pagerAdapter.getView (pager.getCurrentItem());
    }

    public void setCurrentPage (View pageToShow)
    {
        Log.e(LOG_TAG,"Setting Currentpage ==>" +pagerAdapter.getItemPosition (pageToShow));
        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
    }

}
