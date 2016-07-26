package com.bailibao.view.myadapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bailibao.R;
import com.bailibao.bean.AdBean;

import java.util.List;

/**
 * Created by lin on 2016/4/10.
 */
public class AdImageAdapter extends  BasePagerAdapter<AdBean.AdItem> {
    public AdImageAdapter(Context context, List<AdBean.AdItem> list) {
        super(context, list);
    }

    /**
     * // 返回很大的值使得getView中的position不断增大来实现循环。
     * @return
     */
    @Override
    public int getCount() {
        if (mList != null && !mList.isEmpty()){
            if ( mList.size() == 1)
                return 1;
            return Integer.MAX_VALUE; // 返回很大的值使得getView中的position不断增大来实现循环。
        }
       return  0;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (ctx == null || mInflater == null || mList == null){
            return null;
        }else{
            final int curPos = position % mList.size();
            if (mList.get(curPos).logo != null){
                View convertView = mInflater.inflate(R.layout.ad_item,container,false);
                ImageView img = (ImageView) convertView.findViewById(R.id.ad_imageView1);
                container.addView(convertView);
//                ImageLoader.getInstance().displayImage(mList.get(curPos).logo ,img,options);
//
//                img.setOnClickListener(new AdOnClickListener(mList.get(curPos).act));
                if (curPos == 0){
                    img.setImageResource(R.mipmap.test1);
                }else if (curPos == 1){
                    img.setImageResource(R.mipmap.test2);
                }else {
                    img.setImageResource(R.mipmap.test3);
                }
                return  convertView;
            }
            return  null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 点击图片后的响应
     */
    class AdOnClickListener implements View.OnClickListener {
        String url = null;
        AdOnClickListener(String url) {
            this.url = url;
        }
        @Override
        public void onClick(View v) {
//          Intent in = new Intent(mContext, TopicDetailActivity.class);
            if (url != null){
                Intent in = null;
                in.putExtra("URL", url);
                ctx.startActivity(in);
            }
        }
    }
}
