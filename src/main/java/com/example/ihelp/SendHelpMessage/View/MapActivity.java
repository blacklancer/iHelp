package com.example.ihelp.SendHelpMessage.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.R;
import com.example.ihelp.SendHelpMessage.Model.LocationInfo;
import com.example.ihelp.SendHelpMessage.Presenter.SendHelpHttpUtil;
import com.example.ihelp.SendHelpMessage.Presenter.SendLocationHttpUtil;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 朱继涛 on 2019/11/11.
 * 地图展示界面
 */

public class MapActivity extends Fragment{
    private static final int SEND_SUCCESS=1;
    private static final int SEND_FAILD=0;
    private static final int SEND_ADDRESS=2;

    private AMap aMap;
    private MapView mapView;
    private Spinner titleSpinner;
    private Spinner levelSpinner;
    private Button button1;
    private Button button2;
    private EditText editText;
    private Context mContext;
    //声明AMapLocationClient对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明蓝点
    private MyLocationStyle myLocationStyle = null;

    private SendLocationHttpUtil sendLocationHttpUtil;
    private SendHelpHttpUtil sendHelpHttpUtil;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_activity, container,false);

        //初始化地图
        mapView = (MapView) view.findViewById(R.id.map);
        if(aMap == null){
            aMap = mapView.getMap();
        }

        titleSpinner = (Spinner) view.findViewById(R.id.title);
        levelSpinner = (Spinner) view.findViewById(R.id.level);
        editText = (EditText) view.findViewById(R.id.demand);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得本地信息
                SharedPreferences pref = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
                String Name = pref.getString("name", "");
                String Phone = pref.getString("phone","");
                pref = getActivity().getSharedPreferences("locationInfo",MODE_PRIVATE);
                float latitude = pref.getFloat("lat",0);
                float longitude = pref.getFloat("lng",0);
                String address = pref.getString("address","");

                //获得UI界面信息
                String Title = (String) titleSpinner.getSelectedItem();
                String Level = (String) levelSpinner.getSelectedItem();
                int level = Integer.parseInt(Level);
                String Content = editText.getText().toString();
                HelpMessage message = new HelpMessage(Name, Phone, Title, Content, latitude, longitude, level, address);

                //发送
                sendHelpHttpUtil = new SendHelpHttpUtil(message);
                sendHelpHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if(response!=null){
                            int id = Integer.parseInt(response);
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                            editor.putInt("id",id);
                            editor.apply();
                            Message message = new Message();
                            message.what = SEND_SUCCESS;
                            handler.sendMessage(message);
                        }
                        else{
                            Message message = new Message();
                            message.what = SEND_FAILD;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MyHelpMessage.class);
                SharedPreferences pref = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
                int id = pref.getInt("id", 0);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initLocation();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 解析location获得地址描述信息
     */
    public String getAddress(String address){
        int start = 0,end = 0;
        int count = 0;
        char[] add = address.toCharArray();
        for(int i=0; i<add.length; i++){
            if(add[i]=='#'){
                count++;
                if(count==7){
                    start = i+9;
                }
                else if(count==8){
                    end = i;
                    break;
                }
            }
        }
        address = address.substring(start,end);
        return address;
    }


    /**
     * 初始化定位
     */
    private void initLocation(){
        if(myLocationStyle == null){
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.showMyLocation(false);
        }
        myLocationStyle.interval(2000);//连续定位时间间隔
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);

        aMap.setMyLocationStyle(myLocationStyle);


        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if(location != null){

                    //获得本地手机号
                    SharedPreferences pref = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
                    String Phone = pref.getString("phone","");
                    //创建LocationInfo对象
                    LocationInfo locationInfo = new LocationInfo(Phone,(float)location.getLatitude(),(float)location.getLongitude());
                    //发送

                    //将地理位置存在本地
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("locationInfo", MODE_PRIVATE).edit();
                    editor.putFloat("lat",(float)location.getLatitude());
                    editor.putFloat("lng",(float)location.getLongitude());
                    editor.putString("address",getAddress(location.toString()));
                    editor.apply();

                    sendLocationHttpUtil = new SendLocationHttpUtil(locationInfo);
                    sendLocationHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });



                }
            }
        });
        aMap.setMyLocationEnabled(true);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SEND_SUCCESS:
                    //在这里可以进行UI操作
                    Toast.makeText(getContext(), "发布求助成功！", Toast.LENGTH_SHORT).show();
                    break;
                case SEND_FAILD:
                    Toast.makeText(getContext(), "发布求助失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
