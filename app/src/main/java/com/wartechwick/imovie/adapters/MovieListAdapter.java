package com.wartechwick.imovie.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.wartechwick.imovie.R;
import com.wartechwick.imovie.app.Constants;
import com.wartechwick.imovie.model.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by penghaitao on 2016/9/19.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<Result> resultList;
    private Context mContext;
    private static IClickItem mIClickItem;
    private String mTabTitle;

    public MovieListAdapter(Context context, String tabTitle, List<Result> results) {
        resultList = results;
        mContext = context;
        mTabTitle = tabTitle;
    }

    public void setIClickItem(IClickItem IClickItem) {
        mIClickItem = IClickItem;
    }

    public interface IClickItem {

        void onClickItem(Result item, View view);
    }

    public void updateResult(List<Result> results) {
        resultList.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View itemView = inflater.inflate(R.layout.item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Result item = resultList.get(position);
        if ("UPCOMING".equals(mTabTitle)) {
            holder.mTitle.setText(item.getTitle()+" / "+item.getReleaseDate());
        } else {
            holder.mTitle.setText(item.getTitle());
        }
        Glide.with(mContext).load(Constants.IMAGE_W500_BASEURL + item.getPosterPath()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(holder.mImageView)
                {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim)
                    {
                        super.onResourceReady(bitmap, anim);

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener()
                        {
                            @Override
                            public void onGenerated(Palette palette)
                            {
                                holder.mTitleBackground.setBackgroundColor(palette.getVibrantColor(mContext
                                        .getResources().getColor(R.color.black_translucent_60)));
                            }
                        });
                    }
                });
//        Glide.with(mContext).load(Constants.IMAGE_W500_BASEURL + item.getPosterPath()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title)
        TextView mTitle;
        @BindView(R.id.item_image)
        ImageView mImageView;
        @BindView(R.id.title_background)
        View mTitleBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIClickItem.onClickItem(resultList.get(getAdapterPosition()), view);
                }
            });
        }
    }
}
