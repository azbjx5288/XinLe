package com.xinle.lottery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xinle.lottery.R;
import com.xinle.lottery.app.BaseFragment;
import com.xinle.lottery.data.Register;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ACE-PC on 2016/5/2.
 */
public class RegisterSetting extends BaseFragment {
    private static final String TAG = RegisterSetting.class.getSimpleName();

    @BindView(R.id.reg_edituser)
    EditText regEdituser;
    @BindView(R.id.proxy)
    RadioButton proxy;
    @BindView(R.id.user)
    RadioButton user;
    @BindView(R.id.user_type)
    RadioGroup userType;
    @BindView(R.id.reg_editpass)
    EditText regEditpass;
    @BindView(R.id.reg_surepass)
    EditText regSurepass;

    private Register registerData;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateView(inflater, container, "注册下级", R.layout.register_setting,true,true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user.setChecked(true);
        userType.setOnCheckedChangeListener((group, checkedId) ->{
            switch(group.getCheckedRadioButtonId()){
                case R.id.proxy:
                    registerData.setType(1);
                    break;
                case R.id.user:
                    registerData.setType(2);
                    break;
            }
        });
    }

    private void init(){
        registerData=new Register();
    }

    @OnClick({R.id.rebates_settingbut})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rebates_settingbut:
               if (verification()) {
                    registerData.setUsername(regEdituser.getText().toString());
                    registerData.setPassword(regEditpass.getText().toString());
                    registerData.setPassword2(regSurepass.getText().toString());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("reg",registerData);
                    launchFragment(LowerRebateSetting.class,bundle);
                }
                break;
        }
    }

    private boolean verification(){
        if (TextUtils.isEmpty(regEdituser.getText().toString())) {
            Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(regEditpass.getText().toString())) {
            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_LONG).show();
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        if (!regEditpass.getText().toString().matches(regex)){
            Toast.makeText(getActivity(), "密码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(regSurepass.getText().toString())) {
            Toast.makeText(getContext(), "请输入确认密码", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!regEditpass.getText().toString().equals(regSurepass.getText().toString())) {
            Toast.makeText(getContext(), "密码与确认密码不相同", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        getActivity().finish();
        super.onDestroyView();
    }
}
