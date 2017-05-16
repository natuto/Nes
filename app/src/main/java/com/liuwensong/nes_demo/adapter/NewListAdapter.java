package com.liuwensong.nes_demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liuwensong.nes_demo.R;
import com.liuwensong.nes_demo.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by song on 2017/5/14.
 */

public class NewListAdapter  extends RecyclerView.Adapter{
    private static final int TYPE_ITEM = 0;
    //脚布局
    private static final int TYPE_FOOTER = 1;

    public final String TAG = "NewsListAdapter";
    private ArrayList<NewsBean> newsList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    //是否显示脚布局
    private boolean showFooter = true;
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item,parent,false);
           ItemViewHolder viewHolder =new ItemViewHolder(root);
            return  viewHolder;
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_listview_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);

        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder vh = (ItemViewHolder) holder;
            vh.tv_title.setText(newsList.get(position).getTitle());

            vh.tv_desc.setText(newsList.get(position).getDigest());

            //加载图片
            Glide.with(context).load(newsList.get(position).getImgsrc())
                    .error(R.drawable.ic_image_loadfail).crossFade().into(vh.iv_image);
        }


    }


    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!showFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class  ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_title;
        private TextView tv_desc;
        private ImageView iv_image;

        public ItemViewHolder(View root) {
            super(root);

            iv_image = (ImageView) root.findViewById(R.id.iv_image);
            tv_title = (TextView) root.findViewById(R.id.tv_title);
            tv_desc = (TextView) root.findViewById(R.id.tv_desc);

            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, this.getLayoutPosition());
            }

        }
    }
    public void setData(ArrayList<NewsBean> newsList) {
        this.newsList = newsList;
        this.notifyDataSetChanged();
    }

    public NewListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        int ishow = showFooter ? 1 : 0;
        if (newsList == null) {
            return ishow;
        }
        return newsList.size() + ishow;

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void isShowFooter(boolean showFooter) {
        this.showFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.showFooter;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
