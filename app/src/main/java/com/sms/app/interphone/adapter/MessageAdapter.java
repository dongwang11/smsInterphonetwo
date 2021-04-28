package com.sms.app.interphone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.interphone.R;
import com.sms.app.interphone.entity.Mesage_entity;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.voiceutil.voice.SpeexInput;
import com.sms.app.interphone.view.CircleImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MessageAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Mesage_entity> mList = null;



    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    final int TYPE_4 = 3;




    private int[] leftVoiceBg = new int[] { R.mipmap.voice_1_left, R.mipmap.voice_2_left, R.mipmap.voice_3_left };
    private int[] rigthVoiceBg = new int[] { R.mipmap.voice_1_right, R.mipmap.voice_2_right, R.mipmap.voice_3_right };

    private long lastClickTime = 0;


    private SpeexInput player = null;

    private final int MIN_CLICK_DELAY_TIME = 500;   //延迟item的连续点击时间


    private int itemClick = 0;   //判断点击的Item是否是同一个

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1){

                ImageView img = (ImageView) msg.obj;

                if (msg.arg2 == 1) {

                    if(msg.sendingUid == 1){
                        img.setImageResource(leftVoiceBg[2]);
                    }else{
                        img.setImageResource(rigthVoiceBg[2]);
                    }

                } else {

                    img.setImageResource(msg.arg1);

                }

            }

        }
    };


    public MessageAdapter(Context context, List<Mesage_entity> mesage_entity_list) {
        this.mContext = context;
        this.mList = mesage_entity_list;
        this.inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 该方法返回多少个不同的布局
     */
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    /**
     * 根据position返回相应的Item
     */
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub

        try {

            if (mList.get(position).getMesg_type() > 1 && MyApplicatoin.evenUser != null && mList.get(position).getMesg_type() < 7) {

                if(mList.get(position).getUser_id() == MyApplicatoin.evenUser.getId()){

                    return TYPE_1;
                }else{

                    return TYPE_2;

                }

            }else{
                return TYPE_3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TYPE_4;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        try {

            ViewHolder holder = null;


            int type = getItemViewType(position);

            if (convertView == null) {
                //按当前所需的样式，确定new的布局

                convertView = inflater.inflate(R.layout.list_item_chat_right, parent, false);
                holder = new ViewHolder();

                holder.rightTvName = (TextView) convertView.findViewById(R.id.right_chat_name);
                holder.rightTvText = (TextView) convertView.findViewById(R.id.right_tv_chat_text);
                holder.rightTvTime = (TextView) convertView.findViewById(R.id.right_chat_time);

                holder.rightImg = (ImageView) convertView.findViewById(R.id.right_imageView_chat_audio);
                holder.rightIcin = (CircleImageView) convertView.findViewById(R.id.right_imageView3);

                holder.rightLayout = (RelativeLayout) convertView.findViewById(R.id.right_layout);

                holder.rightVoiceLayout = (RelativeLayout) convertView.findViewById(R.id.right_chat_voice_layout);



                holder.leftTvName = (TextView) convertView.findViewById(R.id.left_chat_name);
                holder.leftTvText = (TextView) convertView.findViewById(R.id.left_tv_chat_text);
                holder.leftTvTime = (TextView) convertView.findViewById(R.id.left_chat_time);

                holder.leftImg = (ImageView) convertView.findViewById(R.id.left_imageView_chat_audio);
                holder.leftIcin = (CircleImageView) convertView.findViewById(R.id.left_imageView3);

                holder.leftLayout = (RelativeLayout) convertView.findViewById(R.id.left_layout);

                holder.leftVoiceLayout = (RelativeLayout) convertView.findViewById(R.id.left_chat_voice_layout);



                holder.titleTv = (TextView) convertView.findViewById(R.id.chat_time);
                holder.titleTime = (TextView) convertView.findViewById(R.id.title_chat_time);
                holder.titleLayout = (RelativeLayout) convertView.findViewById(R.id.title_layout);

                holder.tv_left_time = (TextView) convertView.findViewById(R.id.tv_left_time);
                holder.tv_right_time = (TextView) convertView.findViewById(R.id.tv_right_time);

                convertView.setTag(holder);

            } else {
                //有convertView，按样式，取得不用的布局
                holder = (ViewHolder) convertView.getTag();
            }
            //设置资源

            byte types = mList.get(position).getMesg_type();
            final String text = mList.get(position).getContent();

            Date date = null;

            if(mList.get(position).getCreate_time() != null){
                date = new Date(mList.get(position).getCreate_time());
            }else{
                date = new Date();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

            switch (type) {

                case TYPE_1:

                    holder.titleLayout.setVisibility(View.GONE);
                    holder.leftLayout.setVisibility(View.GONE);
                    holder.rightLayout.setVisibility(View.VISIBLE);



                    holder.rightTvTime.setText(sdf.format(date));

                    if(mList.get(position).getUser_neme() != null){

                        holder.rightTvName.setText(mList.get(position).getUser_neme());

                    }else{

                        holder.rightTvName.setText(String.valueOf(mList.get(position).getUser_id()));

                    }

                    if (mList.get(position).getAvatar_url() != null) {

                        Bitmap bitmap = getLoacalBitmap(mList.get(position).getAvatar_url()); //从本地取图片(在cdcard中获取)

                        holder.rightIcin.setImageBitmap(bitmap);
                    }else{

                        holder.rightIcin.setImageResource(R.mipmap.icon_chat);

                    }

                    //判断数据类型

                    if (types == MsgResponse.Text) {

                        holder.rightVoiceLayout.setVisibility(View.GONE);
                        holder.rightTvText.setVisibility(View.VISIBLE);
                        holder.rightTvText.setText(text);

                    } else if (types == MsgResponse.Img) {


                    } else if (types == MsgResponse.Voice) {

                        holder.rightVoiceLayout.setVisibility(View.VISIBLE);
                        holder.rightTvText.setVisibility(View.GONE);

                        final ImageView img = holder.rightImg;

                        int t = getVoiceLength(mList.get(position).getContent_length());
                        if(t==0){
                            t=1;
                        }
                        holder.tv_right_time.setText(t+"\"");
                        holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long currentTime = Calendar.getInstance().getTimeInMillis();
                                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                                    lastClickTime = currentTime;



                                    boolean isClick = false;

                                    if(itemClick == position){
                                        isClick = true;
                                    }


                                    itemClick = position;

                                    if(player == null){

                                        player = new SpeexInput(mList.get(position).getUser_id(),mList.get(position).getContent(),img,handler);

                                        Thread thread = new Thread(player);
                                        thread.start();

                                    }else{

                                        if(isClick && player.isPaused()){

                                            player.setPaused(false);


                                        }else{


                                            if(player != null){

                                                if(player.isPaused()){
                                                    player.setPaused(false);
                                                }
                                            }



                                            player = new SpeexInput(mList.get(position).getUser_id(),mList.get(position).getContent(),img,handler);

                                            Thread thread = new Thread(player);
                                            thread.start();


                                        }

                                    }

                                }

                            }
                        });

                    }
                    break;

                case TYPE_2:

                    holder.titleLayout.setVisibility(View.GONE);
                    holder.leftLayout.setVisibility(View.VISIBLE);
                    holder.rightLayout.setVisibility(View.GONE);



                    holder.leftTvTime.setText(sdf.format(date));

                    if(mList.get(position).getUser_neme() != null){

                        holder.leftTvName.setText(mList.get(position).getUser_neme());

                    }else{

                        holder.leftTvName.setText(String.valueOf(mList.get(position).getUser_id()));

                    }

                    if (mList.get(position).getAvatar_url() != null) {

                        Bitmap bitmap = getLoacalBitmap(mList.get(position).getAvatar_url()); //从本地取图片(在cdcard中获取)

                        holder.leftIcin.setImageBitmap(bitmap);

                    }else{

                        holder.leftIcin.setImageResource(R.mipmap.icon_chat);

                    }

                    //判断数据类型

                    if (types == MsgResponse.Text) {

                        holder.leftVoiceLayout.setVisibility(View.GONE);
                        holder.leftTvText.setVisibility(View.VISIBLE);
                        holder.leftTvText.setText(text);

                    } else if (types == MsgResponse.Img) {


                    } else if (types == MsgResponse.Voice) {

                        holder.leftTvText.setVisibility(View.GONE);
                        holder.leftVoiceLayout.setVisibility(View.VISIBLE);

                        final ImageView img = holder.leftImg;

                        int t = getVoiceLength(mList.get(position).getContent_length());
                        if(t==0){
                            t=1;
                        }
                        holder.tv_left_time.setText(t+"\"");

                        holder.leftVoiceLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                long currentTime = Calendar.getInstance().getTimeInMillis();
                                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                                    lastClickTime = currentTime;



                                    boolean isClick = false;

                                    if(itemClick == position){
                                        isClick = true;
                                    }


                                    itemClick = position;

                                    if(player == null){

                                        player = new SpeexInput(mList.get(position).getUser_id(),mList.get(position).getContent(),img,handler);

                                        Thread thread = new Thread(player);
                                        thread.start();

                                    }else{

                                        if(isClick && player.isPaused()){

                                            player.setPaused(false);


                                        }else{


                                            if(player != null){

                                                if(player.isPaused()){
                                                    player.setPaused(false);
                                                }
                                            }


                                            player = new SpeexInput(mList.get(position).getUser_id(),mList.get(position).getContent(),img,handler);

                                            Thread thread = new Thread(player);
                                            thread.start();



                                        }

                                    }

                                }


                            }
                        });



                    }
                    break;

                case TYPE_3:


                    holder.titleLayout.setVisibility(View.VISIBLE);
                    holder.leftLayout.setVisibility(View.GONE);
                    holder.rightLayout.setVisibility(View.GONE);

                    if(mList.get(position).getUser_neme() != null){

                        if(types == MsgResponse.Join){

                            holder.titleTv.setText(mList.get(position).getUser_neme()+mContext.getResources().getString(R.string.chat_type_title_join));


                        }else if(types == MsgResponse.Exit){
                            holder.titleTv.setText(mList.get(position).getUser_neme()+mContext.getResources().getString(R.string.chat_type_title_exit));
                        }else if(types == MsgResponse.Leave){
                            holder.titleTv.setText(mList.get(position).getUser_neme()+mContext.getResources().getString(R.string.chat_type_title_leave));
                        }else if(types == MsgResponse.Into){
                            holder.titleTv.setText(mList.get(position).getUser_neme()+mContext.getResources().getString(R.string.chat_type_title_j));
                        }

                    }else{

                        if(types == MsgResponse.Join){

                            holder.titleTv.setText(mList.get(position).getUser_id()+mContext.getResources().getString(R.string.chat_type_title_join));

                        }else if(types == MsgResponse.Exit){
                            holder.titleTv.setText(mList.get(position).getUser_id()+mContext.getResources().getString(R.string.chat_type_title_exit));
                        }else if(types == MsgResponse.Leave){
                            holder.titleTv.setText(mList.get(position).getUser_id()+mContext.getResources().getString(R.string.chat_type_title_leave));
                        }else if(types == MsgResponse.Into){
                            holder.titleTv.setText(mList.get(position).getUser_id()+mContext.getResources().getString(R.string.chat_type_title_j));
                        }
                    }

                    holder.titleTime.setText(sdf.format(date));

                    break;

                default:
                    break;
            }
        } catch (Exception e) {

            ExceptionUtil.handleException(e);

        }

        return convertView;

    }

    private class ViewHolder {
        TextView rightTvName,rightTvText,rightTvTime,leftTvName,leftTvText,leftTvTime,titleTv,titleTime,tv_left_time,tv_right_time;
        ImageView rightImg,leftImg;
        CircleImageView rightIcin,leftIcin;
        RelativeLayout rightLayout,rightVoiceLayout,leftLayout,leftVoiceLayout,titleLayout;

    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }


    private int getVoiceLength(int content_length) {

        double a = (content_length * 1.333) / 1000;

        //    LogUtil.i(GlobalConsts.TAG,"ak47:"+(content_length / 1.33) * 0.001);

        return (int)a;

    }

}
