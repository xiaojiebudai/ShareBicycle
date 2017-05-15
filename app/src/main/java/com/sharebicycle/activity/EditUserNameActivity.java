package com.sharebicycle.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.sharebicycle.www.R;


/***
 * Description:修改用户名 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-27
 ***/
public class EditUserNameActivity extends FatherActivity implements
        OnClickListener {
    public static final String NAME = "key_name";
    private EditText et_name;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_edit_username;
    }

    @Override
    protected void initValues() {

        initDefautHead(R.string.username, true);
        initTextHeadRigth("保存", new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_name.getText().toString().trim();
                if (!TextUtils.isEmpty(s)) {
                    Intent intent = new Intent();
                    intent.putExtra(NAME, s);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setHint(getIntent().getStringExtra(NAME));

    }

    @Override
    protected void doOperate() {

    }

}
