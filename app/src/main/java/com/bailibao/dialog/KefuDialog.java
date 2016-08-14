package com.bailibao.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
 * Created by Administrator on 2016/4/22.
 */
public class KefuDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.kefu_dialog, container,false);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_kefu_cancle);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView login = (TextView) view.findViewById(R.id.dialog_kefu_ok);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//系统默认的action，用来打开默认的电话界面
//                intent.setAction(Intent.ACTION_CALL);
//                //需要拨打的号码
//                intent.setData(Uri.parse("tel:"+10086));
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"4000220101"));
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
}
