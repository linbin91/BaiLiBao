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

/**
 * Created by Administrator on 2016/4/23.
 */
public class DeleteMeassgeDialog extends DialogFragment {

    private onClickDeleteCallBack mListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.delete_message_dialog, container,false);
        TextView cancel = (TextView) view.findViewById(R.id.delete_cancle);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final TextView delte = (TextView) view.findViewById(R.id.delete_ok);
        delte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    dismiss();
                    mListener.onDeleteAction();

                }
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        int width = SystemVal.sysWidth / 3 * 2;
//        int height = SystemVal.sysHeight / 8;
//        getDialog().getWindow().setLayout(width, height);
    }

    /*
     *点击删除的监听
     */
    public interface onClickDeleteCallBack{
        public void  onDeleteAction();
    }

    public void setListener(onClickDeleteCallBack listener ){
        mListener = listener;
    }
}
