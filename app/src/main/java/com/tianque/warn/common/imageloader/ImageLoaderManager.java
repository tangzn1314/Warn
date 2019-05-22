package com.tianque.warn.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author ctrun on 2019/2/22
 */
public class ImageLoaderManager {

    /*public static void loadImageDefaultLogo(Context context, String imageUrl, ImageView imageView) {
        GlideApp.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.custom_default_image)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }*/

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .centerCrop();

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView, int placeholderResId) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderResId)
                .dontAnimate()
                .centerCrop();

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载头像
     */
    public static void loadHead(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = RequestOptions.circleCropTransform().dontAnimate();

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

}
