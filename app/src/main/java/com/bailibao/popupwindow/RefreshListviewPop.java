package com.bailibao.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.bailibao.R;
import com.bailibao.bean.InvestProductBean;
import com.bailibao.util.ScreenUtil;
import com.bailibao.view.myadapter.CommonAdapter;
import com.bailibao.view.myadapter.ViewHolder;
import com.bailibao.view.pullview.PullToRefreshListView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RefreshListviewPop extends PopupWindow {

    private Context mContext;
    private List<InvestProductBean.ProductItem> mListContent;
    private View mView;
    private PullToRefreshListView mListView;
    private ListAdapter mAdapter;

    private int mSelect;
    private OnPopClickListener mListener;

    public RefreshListviewPop(Context context, List<InvestProductBean.ProductItem> mList, int select , OnPopClickListener listener) {
        mContext = context;
        mListContent = mList;
        mSelect = select;
        mListener = listener;
        initPopWindow();
    }

    private void initPopWindow() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popwindon_view, null);

        setContentView(mView);
        //设置宽高
        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        int itemHeight = ScreenUtil.dip2px(mContext, 51);
        final int height = itemHeight * mListContent.size();
        setHeight(height);
        //窗口可点击
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        //设置点击消失
        setOutsideTouchable(true);
        mListView = (PullToRefreshListView) mView.findViewById(R.id.lv_pop);
//        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView.getRefreshableView().setAdapter(mAdapter = new CommonAdapter<InvestProductBean.ProductItem>(mContext, mListContent, R.layout.group_item_view) {
            int index = 0;
            @Override
            public void convert(ViewHolder helper, InvestProductBean.ProductItem item) {

                if (mSelect == item.productId){
                    helper.setImageResource(R.id.group_item_img, R.mipmap.withdrawal_select_bk);
                }

                helper.setText(R.id.groupItem,item.name);
                index++;

            }
        });

        mListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    mListener.selectItem(mListContent.get(position).productId);
                }
                dismiss();

            }
        });

        if (mListener != null){
            mListener.showPop();
        }
    }

    public interface OnPopClickListener{
        public void selectItem(int product);
        public void dismiss();
        public void showPop();
    }

    public void setListener(OnPopClickListener listener){
        mListener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener != null){
            mListener.dismiss();
        }
    }
}