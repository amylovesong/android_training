package com.sun.training.animation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.training.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSlidePageFragment extends Fragment {
    private int[] bgResIds = {R.color.color_00aaff, R.color.actionbar_text, R.color.actionbar_background,
            R.color.color_33aa77, R.color.color_ffaaaa};

    private static final String TAG = ScreenSlidePageFragment.class.getSimpleName();
    private int pageIndex;

    private ViewGroup rootView;

    public ScreenSlidePageFragment() {
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView" + " " + pageIndex + " " + this);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        initRootView();
        return rootView;
    }

    private void initRootView() {
        TextView txvPageIndex = (TextView) rootView.findViewById(R.id.page_index);
        txvPageIndex.setText("Page " + (pageIndex + 1) + ": ");
        rootView.setBackgroundResource(bgResIds[pageIndex]);
    }
}
