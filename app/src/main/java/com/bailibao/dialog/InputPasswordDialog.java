package com.bailibao.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.data.ConfigsetData;
import com.bailibao.util.PreferencesUtils;

/**
 * Created by Administrator on 2016/4/16.
 */
public class InputPasswordDialog extends DialogFragment {

    private LoginInputListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.input_password_dialog, null);
        final EditText inputText = (EditText) view.findViewById(R.id.et_password);
        builder.setView(view);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_ok);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputText.getText().toString().trim();
                if (input == null || TextUtils.isEmpty(input)){
                    Toast.makeText(getContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                }else{
                    String realPassword = PreferencesUtils.getString(getContext(), ConfigsetData.CONFIG_KEY_LOGIN_PASSWORD);
                    if (realPassword.endsWith(input)){
                        if (mListener != null){
                            mListener.onLoginInputRight();
                            dismiss();
                        }
                    }else{
                        Toast.makeText(getContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        TextView tvCancle = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onLoginDismis();
                }
                dismiss();
            }
        });
        setCancelable(false);
        return builder.create();
    }

    public interface  LoginInputListener{
        //取消的回调
        void onLoginDismis();
        //密码输入正确后的回调
        void onLoginInputRight();
    }

    public void setLoginInputListener(LoginInputListener listener){
        mListener = listener;
    }
}
