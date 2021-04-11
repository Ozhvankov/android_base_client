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

import java.util.ArrayList;
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
            final String action = intent.getStringExtra("action");
            mTaskListModels = new ArrayList<>();
            mOutboundNumberTxt.setText(intent.getStringExtra("outbound"));
            mTaskNameTxt.setText(intent.getStringExtra("action"));
            try {
                JSONArray array = new JSONArray(intent.getStringExtra("data"));
                for (int i = 0; i < array.length(); i++) {
                    mTaskListModels.add(new TaskListModel(array.getJSONObject(i).getString("id"),
                            array.getJSONObject(i).getString("Initial_PRINTED_LPN")));
                }
                ArrayAdapter<TaskListModel> adapter = new ArrayAdapter<>(TaskActivity.this,
                        android.R.layout.simple_list_item_1, mTaskListModels);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (action != null) {
                            Intent startIntent = new Intent(TaskActivity.this, OubboundRefillOrStagingActivity.class);
                            startIntent.putExtra("id", mTaskListModels.get(i).id);
                            startIntent.putExtra("lpn", mTaskListModels.get(i).initialLPN);
                            startIntent.putExtra("action", action);
                            startActivityForResult(startIntent, 100);
                        }
                    }
                });
                if(mTaskListModels.size() == 1)
                {
                    Intent startIntent = new Intent(TaskActivity.this, OubboundRefillOrStagingActivity.class);
                    startIntent.putExtra("id", mTaskListModels.get(0).id);
                    startIntent.putExtra("lpn", mTaskListModels.get(0).initialLPN);
                    startIntent.putExtra("action", action);
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
