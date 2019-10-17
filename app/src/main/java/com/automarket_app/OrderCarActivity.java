package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.automarket_app.VO.CartVO;
import com.automarket_app.VO.OrderDetailVO;
import com.automarket_app.VO.OrderVO;
import com.automarket_app.VO.ProductVO;
import com.automarket_app.VO.UserVO;
import com.automarket_app.adapter.ProductAdapter;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderCarActivity extends AppCompatActivity  implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{
    private static final String LOG_TAG = "OrderCarActivity";

    private MapView mMapView;
    private TextView tv_location;
    private TextView tv_car;
    private TextView tv_msg;
    private ImageButton btnClose;
    private Button btnOrder,btnCancel;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};
    String tcp_server_ip="",tcp_server_port="";
    double user_lati=0.0,user_long=0.0;
    private LocationManager locationManager;
    private String carid="",login_userid="";
    private SharedPreferences appData;
    private List<OrderDetailVO> o_list;
    private String api_url;
    private List<CartVO> cartlist;
    private Integer cart_sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_car);
        mMapView = (MapView) findViewById(R.id.map_view);
        tv_location = (TextView) findViewById(R.id.tv_location);
        btnClose = (ImageButton)findViewById(R.id.btnClose);
        tv_car = (TextView)findViewById(R.id.tv_car);
        tv_msg = (TextView)findViewById(R.id.tv_msg);
        btnOrder = (Button)findViewById(R.id.btnOrder);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        //mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setCurrentLocationEventListener(this);

        api_url = Helper.getMetaData(this, "api_url");
        appData = getSharedPreferences("login_info", MODE_PRIVATE);
        login_userid= appData.getString("USERID","");

        Intent i_this = getIntent();
        cart_sum = (Integer)i_this.getExtras().get("cart_sum");
        String cart_prod_list = (String)i_this.getExtras().get("cart_prod_list");

        final ObjectMapper mapper = new ObjectMapper();
        try {
            cartlist = mapper.readValue(cart_prod_list, new TypeReference<List<CartVO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주문하기
                if(carid.equals("")) {
                    Toast.makeText(OrderCarActivity.this,"차량정보가 없습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    o_list = new ArrayList<OrderDetailVO>();
                    for(CartVO vo :cartlist){
                        o_list.add(new OrderDetailVO(vo.getProdid(),vo.getProdcnt()));
                    }
                    OrderVO vo = new OrderVO();
                    vo.setCarid(carid);
                    vo.setUserid(login_userid);
                    vo.setTotalprice(cart_sum);
                    vo.setReceiptaddr("");
                    vo.setReceiptlati(user_lati);
                    vo.setReceiptlong(user_long);
                    vo.setOrderdetail(o_list);

                    String json = mapper.writeValueAsString(vo);

                    Intent i = new Intent();
                    ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.OrderService");
                    i.setComponent(cname);
                    i.putExtra("order_vo",json);
                    i.putExtra("api_url",api_url);
                    startService(i);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation != null) {
            user_lati = lastKnownLocation.getLatitude();
            user_long = lastKnownLocation.getLongitude();
        }

        tcp_server_ip = Helper.getMetaData(OrderCarActivity.this, "tcp.server.ip");
        tcp_server_port = Helper.getMetaData(OrderCarActivity.this, "tcp.server.port");
        if(tcp_server_port.equals(null)) tcp_server_port="0";

        Intent i = new Intent();
        ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.TcpCarService");
        i.setComponent(cname);
        i.putExtra("tcp_server_ip",tcp_server_ip);
        i.putExtra("tcp_server_port",Integer.parseInt(tcp_server_port));
        i.putExtra("user_lati",user_lati);
        i.putExtra("user_long",user_long);
        startService(i);

    }

    private void checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(OrderCarActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(OrderCarActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(OrderCarActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(OrderCarActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(OrderCarActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderCarActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }
    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        System.out.println("================>onReverseGeoCoderFoundAddress:");
        System.out.println(s);
        //onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        String location = String.format("위도:%f, 경도:%f", mapPointGeo.latitude, mapPointGeo.longitude);
        tv_location.setText(location);
        user_lati = mapPointGeo.latitude;
        user_long = mapPointGeo.longitude;
        //Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("automarket_app","데이터가 Activity에 도달");
        if(intent.getAction()!=null && intent.getAction().equals("order")){
            String ordermap = intent.getExtras().getString("orderResultData");

            if(ordermap!=null && !ordermap.equals("")){

                //차량이동 요청
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.CarMoveService");
                i.setComponent(cname);
                i.putExtra("tcp_server_ip",tcp_server_ip);
                i.putExtra("tcp_server_port",Integer.parseInt(tcp_server_port));
                i.putExtra("carid",carid);
                startService(i);

                Toast.makeText(OrderCarActivity.this,"주문처리 되었습니다.",Toast.LENGTH_LONG).show();
                Intent i_order = new Intent(getApplicationContext(), InformationActivity.class);

                startActivity(i_order);

            }
            else{
                Toast.makeText(OrderCarActivity.this,"주문실패",Toast.LENGTH_LONG).show();

            }


        }else if(intent.getAction()!=null && intent.getAction().equals("car_move")){



        }else if(intent.getAction()!=null && intent.getAction().equals("car")){

            String carResult = intent.getExtras().getString("carResultData");

            if(carResult !=null && !carResult.equals("")){
                String[] arr_msg;
                arr_msg = carResult.split("/");
                carid = arr_msg[2];
                double car_lati = Double.parseDouble(arr_msg[3]);
                double car_long = Double.parseDouble(arr_msg[4]);

                tv_car.setText(carid);
                tv_msg.setText(String.format("[%s]차량을 찾았습니다.",carid));

                // 중심점 변경
                mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(car_lati, car_long), true);

                // 줌 레벨 변경
                mMapView.setZoomLevel(6, true);

                MapPOIItem customMarker = new MapPOIItem();
                customMarker.setItemName(String.format("차량ID : %s",carid));
                customMarker.setTag(1);
                customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(car_lati,car_long));
                customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                //customMarker.setCustomImageResourceId(R.drawable.truck); // 마커 이미지.
                customMarker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                mMapView.addPOIItem(customMarker);


            }else{
                tv_car.setText("");
                tv_msg.setText(String.format("대기중인 차량이 없습니다."));
            }
        }


    }
}
