package com.example.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private final TaskDbHelper dbHelper;
    public TaskAdapter(Context context, List<Task> tasks, TaskDbHelper dbHelper) {
        super(context, 0, tasks);
        this.dbHelper = dbHelper;
    }

    // ListViewの各行（アイテム）を表示するときに呼ばれるメソッド
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.task_item, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView taskTitle = convertView.findViewById(R.id.taskTitle);

        if (task != null) {
            taskTitle.setText(task.getTitle());

            // チェック状態に応じて取り消し線の有無を設定
            if (task.isCompleted()) {
                taskTitle.setPaintFlags(taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                taskTitle.setPaintFlags(taskTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
            // リスナーの多重登録を防ぐために一度外す
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(task.isCompleted());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                // DBに状態を保存
                dbHelper.updateTaskCompleted(task.getId(), isChecked);

                // チェックの変化に応じて取り消し線を切り替え
                if (isChecked) {
                    taskTitle.setPaintFlags(taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    taskTitle.setPaintFlags(taskTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });
        }

        return convertView;
    }
}

