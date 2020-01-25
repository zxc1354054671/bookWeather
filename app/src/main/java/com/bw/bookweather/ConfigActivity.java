package com.bw.bookweather;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bw.bookweather.util.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends BaseActivity implements View.OnClickListener {
    public static final int LEVEL0 = 0;
    public static final int LEVEL1 = 1;
    public static final int LEVEL2 = 2;
    private static final String TAG = "ConfigActivity   -";
    private int currentLevel;

    private TextView configTitleText;
    public Button configBackBt;
    public ListView configListView;
    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        configTitleText = findViewById(R.id.configTitleText);
        configBackBt = findViewById(R.id.configBackBt);
        configListView = findViewById(R.id.configListView);

        configBackBt.setOnClickListener(this);

        dataList.addAll(ConfigUtil.getConfigList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        configListView.setAdapter(adapter);

        configListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL0) {
                    String s = ConfigUtil.getConfigList().get(position);
                    if ("关于".equals(s)) {
                        configTitleText.setText("关于");
                        dataList.clear();

                        String aboutStr = "";
                        try {
//                            InputStream instream = new FileInputStream(new File("about.txt"));
                            InputStream instream = getApplicationContext().getResources().openRawResource(R.raw.about);
                            if (instream != null) {
                                InputStreamReader inputreader = new InputStreamReader(instream);
                                BufferedReader buffreader = new BufferedReader(inputreader);
                                String line;
                                //分行读取
                                while ((line = buffreader.readLine()) != null) {
                                    aboutStr += line + "\n";
                                }
                                instream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dataList.add(aboutStr);
                        adapter.notifyDataSetChanged();
                        configListView.setSelection(0);
                        currentLevel = LEVEL1;

                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.configBackBt:
//                Intent intent = new Intent();
//将想要传递的数据用putExtra封装在intent中
//                intent.putExtra(key, value);
//                setResult(RESULT_CANCELED，intent);
                if (currentLevel == LEVEL0) {
                    finish();
                } else if (currentLevel == LEVEL1) {
                    configTitleText.setText("设置");
                    dataList.clear();
                    dataList.addAll(ConfigUtil.getConfigList());
                    adapter.notifyDataSetChanged();
                    configListView.setSelection(0);
                    currentLevel = LEVEL0;

                }
                break;
        }

    }
}
