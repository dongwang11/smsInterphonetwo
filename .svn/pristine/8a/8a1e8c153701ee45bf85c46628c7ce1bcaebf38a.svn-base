package com.sms.app.interphone.util.frequentlyutil;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *Created by Administrator on 2017/1/13.
 *
 * 监听输入框，只允许输入 字母及 数字
 *
 */

public class SearchWatherChatRoom implements TextWatcher {

    //监听改变的文本框
    private EditText editText;
    String imputtxt;
    String str;

    /**
     * 构造函数
     */
    public SearchWatherChatRoom(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence ss, int start, int before, int count) {
        String editable = editText.getText().toString();
         str = stringFilter(editable.toString());
        if (!editable.equals(str)) {
            editText.setText(str);
            //设置新的光标所在位置
            editText.setSelection(str.length());
        }
        imputtxt = editText.getText().toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(str)){
            String limtxt = getLimitSubstring(str);
            if (!TextUtils.isEmpty(limtxt)){
                if (!limtxt.equals(imputtxt)){
                    editText.setText(limtxt);
                    editText.setSelection(limtxt.length());
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";     //    只允许字母和数字   "[^0-9a-zA-Z]"       只能中文，英文和数字 "[^a-zA-Z0-9\u4E00-\u9FA5]"
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }



    //限制字符长度
    private String getLimitSubstring(String inputStr) {
        int orignLen = inputStr.length();
        int resultLen = 0;
        String temp = null;
        for (int i = 0; i < orignLen; i++) {
            temp = inputStr.substring(i, i + 1);
            try {
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (resultLen > 18) {
                return inputStr.substring(0, i);
            }
        }
        return inputStr;
    }
}
