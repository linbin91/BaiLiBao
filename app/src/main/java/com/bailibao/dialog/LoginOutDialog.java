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
 * Created by Administrator on 2016/4/29.
 */
public class LoginOutDialog extends DialogFragment {
    public OnClickCallBack mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_loginout, container,false);
        TextView cancel = (TextView) view.findViewById(R.id.login_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView login = (TextView) view.findViewById(R.id.login_dialog_ok);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (mListener != null){
                    mListener.loginout();
                }
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = SystemVal.sysWidth / 3 * 2;
        int height = SystemVal.sysHeight / 4;
        getDialog().getWindow().setLayout(width, height);
    }

    public interface OnClickCallBack{
        public void loginout();
    }

    public void setListener(OnClickCallBack listener){
        mListener = listener;
    }
}
