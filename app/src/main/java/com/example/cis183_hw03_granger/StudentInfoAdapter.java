package com.example.cis183_hw03_granger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentInfoAdapter extends BaseAdapter
{
    Context context;
    ArrayList<StudentInfo> students;

    public StudentInfoAdapter(Context c, ArrayList<StudentInfo> s)
    {
        context = c;
        students = s;
    }

    @Override
    public int getCount()
    {
        return students.size();
    }
    @Override
    public Object getItem(int position)
    {
        return students.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.studentcell, null);
        }

        TextView tv_j_fname = convertView.findViewById(R.id.tv_v_studCell_fname);
        //TextView tv_j_lname = convertView.findViewById(R.id.tv_v_studCell_lname);
        TextView tv_j_id = convertView.findViewById(R.id.tv_v_studCell_studentId);
        //TextView tv_j_email = convertView.findViewById(R.id.tv_v_studCell_email);
        //TextView tv_j_age = convertView.findViewById(R.id.tv_v_studCell_age);
        //TextView tv_j_gpa = convertView.findViewById(R.id.tv_v_studCell_gpa);
        //TextView tv_j_major = convertView.findViewById(R.id.tv_v_studCell_major);

        StudentInfo student = students.get(position);

            tv_j_fname.setText("" + student.getFname() + " " + student.getLname());
            //tv_j_lname.setText(student.getLname());
            tv_j_id.setText("" + student.getId());
            //tv_j_email.setText("Email: " + student.getEmail());
            //tv_j_age.setText("Age: " + student.getAge());
            //tv_j_gpa.setText("GPA: " + student.getGpa());
            //tv_j_major.setText("Major: " + student.getMajor());



        return convertView;

    }









}
