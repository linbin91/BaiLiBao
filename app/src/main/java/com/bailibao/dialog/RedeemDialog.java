package com.bailibao.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bailibao.R;
import com.bailibao.data.SystemVal;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RedeemDialog extends DialogFragment {

    private OnContinueClickListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_redeem, container,false);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancle);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView login = (TextView) view.findViewById(R.id.dialog_ok);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    dismiss();
                    mListener.doContinueAction();
                }
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = SystemVal.sysWidth / 3 * 2;
        int height = SystemVal.sysHeight / 3;
        getDialog().getWindow().setLayout(width, height);
    }



    /**
     * 点击继续的监听
     */
    public interface OnContinueClickListener{
        public void  doContinueAction();
    }



    public void  setListener(OnContinueClickListener listener){
        this.mListener = listener;
    }
}
