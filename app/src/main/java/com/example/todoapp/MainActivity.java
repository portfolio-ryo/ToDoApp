package com.example.todoapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText taskInput;
    private TaskAdapter taskAdapter;
    private TaskDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ツールバーの設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //UI部品の取得
        taskInput = findViewById(R.id.taskInput);
        ListView taskListView = findViewById(R.id.taskListView);

        dbHelper = new TaskDbHelper(this);

        // 最初の読み込み
        taskAdapter = new TaskAdapter(this, dbHelper.getAllTasks(), dbHelper);
        taskListView.setAdapter(taskAdapter);

        // 各処理の設定
        setupAddTaskHandler();
        setupTaskDeleteHandler();

    }

    //オプションメニューの生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //オプションメニュー項目が選択されたときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dl_all_del_title))
                    .setMessage(getString(R.string.dl_all_del_message))
                    .setPositiveButton(getString(R.string.positive_bt), (dialog, which) -> {
                        // OKボタン押下時の処理
                        deleteAllTasks();
                    })
                    .setNegativeButton(getString(R.string.negative_bt), null) // キャンセルは何もしない
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTasks() {
        // DBからすべて削除
        dbHelper.deleteAllTasks();

        // リストを空にして更新
        taskAdapter.clear();
        taskAdapter.notifyDataSetChanged();
    }

    private void reloadTaskList() {
        taskAdapter.clear();
        List<Task> updatedTasks = dbHelper.getAllTasks(); //DBから再取得
        taskAdapter.addAll(updatedTasks);                 //新しいデータをセット
        taskAdapter.notifyDataSetChanged();               //表示を更新
    }

    private void setupAddTaskHandler() {
        Button addButton = findViewById(R.id.addButton);
        // タスク追加処理
        addButton.setOnClickListener(v -> {
            String title = taskInput.getText().toString().trim();
            if (!title.isEmpty()) {
                dbHelper.insertTask(title);            // DBに追加
                reloadTaskList();                      // リスト更新
                taskInput.setText("");                 // 入力欄クリア
            }
        });
    }
    private void setupTaskDeleteHandler() {
        ListView taskListView = findViewById(R.id.taskListView);
        // タスク長押しで削除
        taskListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Task task = taskAdapter.getItem(position); //対象タスク取得
            if (task != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.dl_del_title))
                        .setMessage(getString(R.string.dl_del_message))
                        .setPositiveButton((getString(R.string.positive_bt)), (dialog, which) -> {
                            dbHelper.deleteTask(task.getId());  // DBから削除
                            reloadTaskList();                   // リスト更新
                        })
                        .setNegativeButton((getString(R.string.negative_bt)), null)
                        .show();
            }
            return true;
        });
    }
}
