package com.sms.app.interphone.util.msnutil;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *Created by Administrator on 2017/1/13.
 *
 * 监听输入框，只允许输入 字母及 数字
 *
 */

public class SearchWather implements TextWatcher {

    //监听改变的文本框
    private EditText editText;

    /**
     * 构造函数
     */
    public SearchWather(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence ss, int start, int before, int count) {
        String editable = editText.getText().toString();
        String str = stringFilter(editable.toString());
        if (!editable.equals(str)) {
            editText.setText(str);
            //设置新的光标所在位置
            editText.setSelection(str.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^0-9a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
