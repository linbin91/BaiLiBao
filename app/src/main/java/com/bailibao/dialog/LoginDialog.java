package com.bailibao.dialog;


import android.content.Intent;
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

import com.bailibao.Activity.LoginActivity;
import com.bailibao.R;
import com.bailibao.data.SystemVal;

/**
 * Created by Administrator on 2016/4/13.
 */
public class LoginDialog extends DialogFragment {

    //用于传给登入界面
    private int mActionKey = 0;
    private OnClickCallBack mListener;
    private int mId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.login_dialog, container,false);
        TextView cancel = (TextView) view.findViewById(R.id.login_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null){
                    mListener.doCancelAction();
                }
            }
        });

        TextView login = (TextView) view.findViewById(R.id.login_dialog_ok);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.putExtra("donext",mActionKey);
                intent.putExtra("id",mId);
                startActivity(intent);

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

    public void setData(int data){
        mActionKey = data;
    }


    public void setProductId(int id){
        mId = id;
    }


    public interface OnClickCallBack{
        public void doCancelAction();
    }

    public void setListener(OnClickCallBack listener){
        mListener = listener;
    }
}
