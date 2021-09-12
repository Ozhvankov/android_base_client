package com.test.test.Activities.Outbound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.filelogger.FL;
import com.fxn.stash.Stash;
import com.test.test.Models.TaskListModel;
import com.test.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private TextView mOutboundNumberTxt, mTaskNameTxt;
    private ListView mListView;
    private List<TaskListModel> mTaskListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mListView = findViewById(R.id.task_list);
        mOutboundNumberTxt = findViewById(R.id.num_text);
        mTaskNameTxt = findViewById(R.id.task_text);

        final Intent intent = getIntent();
        if (intent != null) {
            final String name = intent.getStringExtra("name");
            final String code = intent.getStringExtra("code");
            mTaskListModels = new ArrayList<>();
            mOutboundNumberTxt.setText(intent.getStringExtra("outbound"));
            mTaskNameTxt.setText(intent.getStringExtra("name"));
            try {
                JSONArray array = new JSONArray(intent.getStringExtra("data"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    mTaskListModels.add(new TaskListModel(o.getString("id"),
                            o.getString("Initial_PRINTED_LPN"), o.getString("Location")));
                }
                Comparator<? super TaskListModel> c = new Comparator<TaskListModel>() {
                    @Override
                    public int compare(TaskListModel taskListModel1, TaskListModel taskListModel2) {
                        return taskListModel1.Location.compareTo(taskListModel2.Location);
                    }
                };
                Collections.sort(mTaskListModels, c);
                ArrayAdapter<TaskListModel> adapter = new ArrayAdapter<>(TaskActivity.this,
                        android.R.layout.simple_list_item_1, mTaskListModels);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (name != null) {
                            Intent startIntent = new Intent(TaskActivity.this, OubboundRefillOrStagingActivity.class);
                            startIntent.putExtra("id", mTaskListModels.get(i).id);
                            startIntent.putExtra("lpn", mTaskListModels.get(i).initialLPN);
                            startIntent.putExtra("code", code);
                            startIntent.putExtra("name", name);
                            startActivityForResult(startIntent, 100);
                        }
                    }
                });
                if(mTaskListModels.size() == 1)
                {
                    Intent startIntent = new Intent(TaskActivity.this, OubboundRefillOrStagingActivity.class);
                    startIntent.putExtra("id", mTaskListModels.get(0).id);
                    startIntent.putExtra("lpn", mTaskListModels.get(0).initialLPN);
                    startIntent.putExtra("code", code);
                    startIntent.putExtra("name", name);
                    startActivityForResult(startIntent, 100);
                }
            } catch (JSONException e) {
                if(Stash.getBoolean("logger")) {
                    FL.d("Error parse task: " + e.toString());
                }
                Toast.makeText(TaskActivity.this, "Error parse task: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 100) {
            setResult(100);
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
