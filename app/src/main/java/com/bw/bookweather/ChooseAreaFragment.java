package com.bw.bookweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.bookweather.db.City;
import com.bw.bookweather.db.County;
import com.bw.bookweather.db.Province;
import com.bw.bookweather.util.HttpUtil;
import com.bw.bookweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zxc on 2020/1/11.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("", "onCreateView:0 ");
        Log.d("", "onCreateView:0 ");
        Log.d("", "onCreateView:0 ");
        Log.d("", "onCreateView:0 ");
        Log.d("", "onCreateView:0 ");
        Log.d("", "onCreateView:0 ");
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.titleText);
        backButton = view.findViewById(R.id.backButton);
        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        Log.d("", "onCreateView:1 ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onActivityCreated: backButton"+currentLevel);
                Log.d(TAG, "onActivityCreated: backButton"+currentLevel);
                Log.d(TAG, "onActivityCreated: backButton"+currentLevel);
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
//        DataSupport.deleteAll(Province.class,"");
        queryProvinces();
        Log.d(TAG, "onActivityCreated: 1");
    }

    private static final String TAG = "ChooseAreaFragment";

    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String url = "http://guolin.tech/api/china";
            queryFromServer(url, "province");
        }
    }

    private void queryCities() {
        titleText.setText(selectedProvince.getName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("parentid=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            String code = selectedProvince.getCode();
            String url = "http://guolin.tech/api/china/" + code;
            queryFromServer(url, "city");
        }
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("parentid=?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String code = selectedProvince.getCode();
            String code1 = selectedCity.getCode();
            String url = "http://guolin.tech/api/china/" + code + "/" + code1;
            queryFromServer(url, "county");
        }
    }

    private void queryFromServer(String url, final String type) {
        showProgressDialog();
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String address = response.body().string();
                boolean result = false;
                if ("province".equals(type))
                    result = Utility.handlePronvinceResponse(address);
                if ("city".equals(type))
                    result = Utility.handleCityResponse(address, String.valueOf(selectedProvince.getId()));
                if ("county".equals(type))
                    result = Utility.handleCountyResponse(address, String.valueOf(selectedCity.getId()));
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                                queryProvinces();
                            if ("city".equals(type))
                                queryCities();
                            if ("county".equals(type))
                                queryCounties();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
