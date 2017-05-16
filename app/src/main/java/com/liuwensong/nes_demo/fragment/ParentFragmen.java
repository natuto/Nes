package com.liuwensong.nes_demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuwensong.nes_demo.R;
import com.liuwensong.nes_demo.adapter.mPagerAdaper;
import com.liuwensong.nes_demo.utils.Urls;

import java.util.ArrayList;

/**
 * Created by song on 2017/5/12.
 */

public class ParentFragmen extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragent_new,null);
        initView(view);
        initDate();
        viewPager.setAdapter(new mPagerAdaper(getChildFragmentManager(),fragmentList,titleList));
        tabLayout.setupWithViewPager(viewPager);

        return  view;
    }

    public void initDate(){
        titleList = new ArrayList<>();
        titleList.add("头条");
        titleList.add("NBA");
        titleList.add("汽车");
        titleList.add("笑话");
        NewListFragment hotNewsList = new NewListFragment();
        hotNewsList.setKeyword(Urls.TOP_ID);

        NewListFragment sportNewsList = new NewListFragment();
        sportNewsList.setKeyword(Urls.NBA_ID);

        NewListFragment carNewsList = new NewListFragment();
        carNewsList.setKeyword(Urls.CAR_ID);

        NewListFragment jokeNewsList = new NewListFragment();
        jokeNewsList.setKeyword(Urls.JOKE_ID);


        fragmentList.add(hotNewsList);
        fragmentList.add(sportNewsList);
        fragmentList.add(carNewsList);
        fragmentList.add(jokeNewsList);

    }
    public  void initView(View v){
        viewPager = (ViewPager)v.findViewById(R.id.vp_content);
        tabLayout = (TabLayout) v.findViewById(R.id.tb_title);
    }
}
