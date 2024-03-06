package com.school.loglife.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.school.loglife.Diaries.Diary;
import com.school.loglife.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends ArrayAdapter<Diary> {

    private ArrayList<Diary> diaries;
    private Context mcontext;
    Locale loc = new Locale("en", "US");
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);

    public DiaryAdapter(@NonNull Context context, int resource, @NonNull List<Diary> diaryList) {
        super(context, resource, diaryList);
        this.mcontext = context;
    }

    public void setDiaries(ArrayList<Diary> diaries) {
        this.diaries = diaries;
        this.notifyDataSetChanged();
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();//自动获取对应list集合的对象的名字
        Date date = getItem(position).getCreatedAt();
        dateFormat.format(date);//获得每个user对象的date属性
        LayoutInflater inflater = LayoutInflater.from(mcontext);//初始化列表渲染器
        View itemView = inflater.inflate(R.layout.item, parent, false);//确定渲染格式，todolist为自己定义的xml文件，里面写了每一行内容的外观式
        TextView des = (TextView) itemView.findViewById(R.id.list_item_description); //获得这个xml文件里面的组件，用于显示每一行的内容
        TextView time = (TextView) itemView.findViewById(R.id.list_item_creationDate);
        des.setText(name);//把获得的user对象的属性放到组件中
        time.setText(dateFormat.format(date));
        return itemView;
    }

}
