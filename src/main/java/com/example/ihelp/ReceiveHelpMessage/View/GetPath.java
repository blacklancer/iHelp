package com.example.ihelp.ReceiveHelpMessage.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.example.ihelp.CommonClass.HelpMessage;
import com.example.ihelp.CommonClass.HttpCallbackListener;
import com.example.ihelp.R;
import com.example.ihelp.ReceiveHelpMessage.Presenter.GoToHelpHttpUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

/**
 * Created by 朱继涛 2019/11/14
 * 点击求助信息后获取路线
 */

public class GetPath extends AppCompatActivity implements RouteSearch.OnRouteSearchListener{

    private static final int GOTO_SUCCESS = 1;
    private static final int GOTO_FAILD = 0;

    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    //声明AMapLocationClient对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明蓝点
    public MyLocationStyle myLocationStyle = null;
    //定位
    public LatLng locationLatLng = null;
    //声明一个UiSettings对象
    public UiSettings mUiSettings;
    //当前定位
    private LatLng latlng = null;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint;//起点
    private LatLonPoint mEndPoint;//终点，116.481288,39.995576
    private final int ROUTE_TYPE_WALK = 3;
    private Polyline polyline;//画线
    private List<LatLng> route = new ArrayList<LatLng>();//路线

    private GoToHelpHttpUtil goToHelpHttpUtil;

    private Button button;

    private int id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_path);
        mContext = this.getApplicationContext();

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        mEndPoint = new LatLonPoint(bundle.getFloat("lat"),bundle.getFloat("lng"));
        SharedPreferences pref = getSharedPreferences("locationInfo", MODE_PRIVATE);
        mStartPoint = new LatLonPoint(pref.getFloat("lat",0),pref.getFloat("lng",0));


        init();
        initLocation();
        initInteraction();
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WALK_DEFAULT);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHelpHttpUtil = new GoToHelpHttpUtil(id, 1);
                goToHelpHttpUtil.sendRequestWithOkHttp(new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        if(response.equals("ok")){
                            message.what = GOTO_SUCCESS;
                        }
                        else{
                            message.what = GOTO_FAILD;
                        }
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()), 19));
        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     * 初始化定位
     */
    private void initLocation(){
        if(myLocationStyle == null){
            myLocationStyle = new MyLocationStyle();
        }
        myLocationStyle.interval(2000);//连续定位时间间隔
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.showMyLocation(true);

        aMap.setMyLocationStyle(myLocationStyle);


        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if(location != null){
                    locationLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    latlng = locationLatLng;
                    mStartPoint = new LatLonPoint(latlng.latitude,latlng.longitude);
                }
            }
        });
        aMap.setMyLocationEnabled(true);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
                    for(int i =0;i<walkPath.getSteps().size();i++){
                        WalkStep walkStep = walkPath.getSteps().get(i);
                        List<LatLonPoint> points=walkStep.getPolyline();
                        for(int j=0;j<points.size();j++){
                            route.add(new LatLng(points.get(j).getLatitude(),points.get(j).getLongitude()));
                        }
                    }
                    aMap.addPolyline(new PolylineOptions().addAll(route).width(25).color(Color.argb(255,75,155,238)));
                }
            }
        }
    }

    /**
     * 地图交互
     */
    private void initInteraction(){
        mUiSettings = aMap.getUiSettings();

        //缩放按钮
        mUiSettings.setZoomControlsEnabled(true);
        //mUiSettings.setZoomPosition();

        //指南针
        mUiSettings.setCompassEnabled(true);

        //比例尺
        mUiSettings.setScaleControlsEnabled(true);

        //定位按钮
        mUiSettings.setMyLocationButtonEnabled(true);

    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GOTO_SUCCESS:
                    Toast.makeText(getContext(), "前往救助成功！", Toast.LENGTH_SHORT).show();
                    break;
                case GOTO_FAILD:
                    Toast.makeText(getContext(), "前往救助失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }
}
