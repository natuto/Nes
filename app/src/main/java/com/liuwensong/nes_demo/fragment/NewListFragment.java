package com.liuwensong.nes_demo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liuwensong.nes_demo.NewsActivity;
import com.liuwensong.nes_demo.R;
import com.liuwensong.nes_demo.adapter.NewListAdapter;
import com.liuwensong.nes_demo.bean.NewsBean;
import com.liuwensong.nes_demo.utils.FileUtils;
import com.liuwensong.nes_demo.utils.HttpUtils;
import com.liuwensong.nes_demo.utils.JsonUtils;
import com.liuwensong.nes_demo.utils.Urls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by song on 2017/5/13.
 */

public class NewListFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{
    int pageIndex = 0;
    private String url;
    private RecyclerView recycle_news;
    private SwipeRefreshLayout sr_refresh;
    private Context context;
    private int count = 0;
    private ArrayList<NewsBean> newsBeen;
   private NewListAdapter newListAdapter;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_recycle, null);
        initView(view); context = getContext();
        layoutManager = new LinearLayoutManager(context);

        newListAdapter = new NewListAdapter(context);
        newListAdapter.setOnItemClickListener(onItemClickListener);
        recycle_news.setLayoutManager(layoutManager);
        recycle_news.setAdapter(newListAdapter);
        sr_refresh.setColorSchemeResources(R.color.primary, R.color.primary_dark,
                R.color.primary_light, R.color.accent);
        sr_refresh.setOnRefreshListener(this);
        recycle_news.addOnScrollListener(onScrollListener);
        if (context != null) {
            Log.i("11","1111111111111111");
            File cacheFile = FileUtils.getDisCacheDir(context, "NewsBean" + url);
            if (cacheFile.exists()) {
                try {
                    Log.i("22","22222222222222222222");
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheFile));
                    ArrayList<NewsBean> list = (ArrayList<NewsBean>) ois.readObject();
                    newListAdapter.setData(list);
                    Log.i(TAG, list.size() + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        new DownloadTask().execute(getUrl());
        return view;
    }
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //SCROLL_STATE_IDLE
            //The RecyclerView is not currently scrolling.
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == newListAdapter.getItemCount()
                    && newListAdapter.isShowFooter()) {
                //加载更多新闻
                pageIndex += Urls.PAZE_SIZE;
                new UpdateTask().execute(getUrl());
            }

        }
    };
    public void setKeyword(String keyword) {
        this.url = keyword;

        if (count != 0) {
            new DownloadTask().execute(getUrl());
        }
    }
    private NewListAdapter.OnItemClickListener onItemClickListener = new NewListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), NewsActivity.class);
            intent.putExtra("url", newsBeen.get(position).getUrl());

            startActivity(intent);
        }
    };
    private String getUrl() {
        StringBuilder sb = new StringBuilder();
        switch (url) {
            //头条
            case Urls.TOP_ID:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            //NBA
            case Urls.NBA_ID:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            //汽车
            case Urls.CAR_ID:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            //笑话
            case Urls.JOKE_ID:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }

        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
    class UpdateTask extends AsyncTask<String, Integer, ArrayList<NewsBean>> {

        private ArrayList<NewsBean> updateNewsList;

        @Override
        protected ArrayList<NewsBean> doInBackground(String... params) {
            try {
                String infoUrl = params[0];
                HttpUtils.getJsonString(infoUrl, new HttpUtils.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        updateNewsList = JsonUtils.readJsonNewsBean(response, url);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

                if (updateNewsList.size() <= 0) {
                    newListAdapter.isShowFooter(false);
                }


                for (NewsBean i : updateNewsList) {
                    newsBeen.add(i);
                }

                return updateNewsList;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsBean> updateNewsList) {
            if (updateNewsList == null) {
                Toast.makeText(getContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
            } else {
//                newsListAdapter.isShowFooter(false);
                newListAdapter.setData(NewListFragment.this.newsBeen);
            }
        }
    }



    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        pageIndex = 0;

        if (newsBeen != null) {
            newsBeen.clear();
        }

        new DownloadTask().execute(getUrl());
    }


    class DownloadTask extends AsyncTask <String ,Integer,ArrayList<NewsBean>>{
             private ObjectOutputStream oos;
      @Override
      protected ArrayList<NewsBean> doInBackground(String... strings) {
          String infoUrl =strings[0];
          HttpUtils.getJsonString(infoUrl, new HttpUtils.HttpCallbackListener() {
                    @Override
              public void onFinish(String response) {
                  newsBeen = JsonUtils.readJsonNewsBean(response,url);
                  if (count == 0){
                      if (context != null) {
                          File cachFile = FileUtils.getDisCacheDir(context,"NewsBean"+url);
                          try {
                              oos = new ObjectOutputStream(new FileOutputStream(cachFile));

                              oos.writeObject(newsBeen);

                              count++;
                          } catch (IOException e) {
                              e.printStackTrace();
                          }finally {
                              if (oos != null){
                                  try {
                                      oos.close();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                              }
                          }
                      }
                  }
              }

              @Override
              public void onError(Exception e) {
                  e.printStackTrace();

              }
          });
          return newsBeen;
      }

        @Override
        protected void onPostExecute(ArrayList<NewsBean> newsBeen) {
             if (newsBeen ==null){
                 return;
             }else {
                NewListFragment.this.newsBeen =newsBeen;
                 newListAdapter.setData(newsBeen);
//                recycle_news.setLayoutManager(layoutManager);
//                recycle_news.setAdapter(newsListAdapter);
                 newListAdapter.notifyDataSetChanged();
             } sr_refresh.setRefreshing(false);
        }
    }
    private void initView(View view) {
        recycle_news = (RecyclerView) view.findViewById(R.id.rl_view);
        sr_refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    }
}
