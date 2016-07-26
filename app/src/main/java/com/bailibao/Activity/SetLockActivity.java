package com.bailibao.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bailibao.R;
import com.bailibao.base.BaseActivity;
import com.bailibao.util.LockUtil;
import com.bailibao.view.CustomLockView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 *
 * 设置手势的
 */
public class SetLockActivity extends Activity implements View.OnClickListener{

    private ImageView iva,ivb,ivc,ivd,ive,ivf,ivg,ivh,ivi;
    private List<ImageView> list=new ArrayList<ImageView>();
    private TextView tvWarn;
    private int times=0;
    private int[] mIndexs=null;
    private CustomLockView mLockView;

    private ImageView mClose;
    private TextView mTvReset;
    private boolean isCanReset = false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SetLockActivity.this;
        findView();
        initData();
        setListener();

    }

    protected void initData() {

    }

    protected void setListener() {
        mLockView.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
            @Override
            public void onComplete(int[] indexs) {
                mIndexs=indexs;
                //显示次数
                if(times==0){
                    for(int i=0;i<indexs.length;i++){
                        list.get(indexs[i]).setImageDrawable(getResources().getDrawable(R.mipmap.gesturecirlebrownsmall));
                    }
                    tvWarn.setText("再次绘制解锁图案");
                    tvWarn.setTextColor(getResources().getColor(R.color.white));
                    times++;
                    isCanReset = true;
                    mTvReset.setText("重新设置手势");
                }else if(times==1){
                    //将密码设置在本地
                    BaseActivity.isJustSetGestureLock = true;
                    LockUtil.setPwdToDisk(mContext.getApplicationContext(), mIndexs);
                    LockUtil.setPwdStatus(mContext.getApplicationContext(), true);
                    Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError() {
                tvWarn.setText("与上一次绘制不一致，请重新绘制");
                tvWarn.setTextColor(getResources().getColor(R.color.red));
            }
        });

        mClose.setOnClickListener(this);
        mTvReset.setOnClickListener(this);
    }



    protected void findView() {
        setContentView(R.layout.activity_setlock);
        //初始化9个小圆
        iva=(ImageView)findViewById(R.id.iva);
        ivb=(ImageView)findViewById(R.id.ivb);
        ivc=(ImageView)findViewById(R.id.ivc);
        ivd=(ImageView)findViewById(R.id.ivd);
        ive=(ImageView)findViewById(R.id.ive);
        ivf=(ImageView)findViewById(R.id.ivf);
        ivg=(ImageView)findViewById(R.id.ivg);
        ivh=(ImageView)findViewById(R.id.ivh);
        ivi=(ImageView)findViewById(R.id.ivi);
        list.add(iva);
        list.add(ivb);
        list.add(ivc);
        list.add(ivd);
        list.add(ive);
        list.add(ivf);
        list.add(ivg);
        list.add(ivh);
        list.add(ivi);

        tvWarn = (TextView) findViewById(R.id.tvWarn);
        mLockView = (CustomLockView) findViewById(R.id.cl);
        mClose = (ImageView) findViewById(R.id.title_left);
        mTvReset = (TextView) findViewById(R.id.lock_warn);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.lock_warn:
                if (isCanReset){
                    reSetLockView();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 重新设置密码锁
     *
     */
    private void reSetLockView() {
        tvWarn.setText("绘制解锁密码");
        mTvReset.setText("绘制手势密码时，请防止他人未经授权查看");
        times = 0;
        for (int i = 0; i < list.size(); i++){
            list.get(i).setImageDrawable(getResources().getDrawable(R.mipmap.unselected));;
        }
        mLockView.setStatus(0);
        mLockView.clearCurrent();
    }
}
