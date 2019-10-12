package com.example.snapdrone08;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    public String lat, lng;
    public TextView textView1;
    public TextView logView;
    public TextView textView2;
    public TextView textView3;
    public TextView textView4;
    public TextView textView5;
    private Socket mSocket;

    float thr=0;
    float roll=0;
    float pitch=0;
    float yaw=0;
    JSONObject data_con = new JSONObject();
    {
        try {
            mSocket = IO.socket("https://api.teamhapco.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String id;
                    try {
                        id = data.getString("ID");
                    } catch (JSONException e) {
                        return;
                    }
                    // 메시지를 받으면 data에 담고,
                    // latitude와 longitude라는 키값으로 들어왔다는 가정으로 작성된 코드다.
                    // addMessage(latitude, longitude); 이런 식으로 코드를 실행시키면
                    //    addMessage 쪽으로 인자를 담아 보내니 화면에 노출하게 만들면 될 것이다.
                    textView1 = (TextView) findViewById(R.id.textView);
                    textView1.setText(id);
                    Log.d("test", "enterOnNewMessage");

                    JSONObject data8 = new JSONObject();
                    try {
                        data8.put("latitude", lat);
                        data8.put("longitude", lng);
                        mSocket.emit("init_client", data8);
                        Log.d("test","sendJsonData");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("test", "start");
        //mSocket.on(Socket.EVENT_CONNECT,onNewMessage);
       // mSocket.on("init_client", onNewMessage);
        //mSocket.on("drone_call", onNewMessage);
        mSocket.on("socket_disconnect", onNewMessage);
        mSocket.connect();


        // BtnOnClickListener의 객체 생성.
        BtnOnClickListener onClickListener = new BtnOnClickListener();

        // 각 Button의 이벤트 리스너로 onClickListener 지정.
        Button buttonRed = (Button) findViewById(R.id.button);
        buttonRed.setOnClickListener(onClickListener);
        Button buttonGreen = (Button) findViewById(R.id.button2);
        buttonGreen.setOnClickListener(onClickListener);
        Button buttonBlue = (Button) findViewById(R.id.button3);
        buttonBlue.setOnClickListener(onClickListener);
        Button buttonpic = (Button) findViewById(R.id.button4);
        buttonpic.setOnClickListener(onClickListener);
        Button thr_down = (Button) findViewById(R.id.button5);
        thr_down.setOnClickListener(onClickListener);
        Button thr_up = (Button) findViewById(R.id.button6);
        thr_up.setOnClickListener(onClickListener);
        Button roll_down = (Button) findViewById(R.id.button7);
        roll_down.setOnClickListener(onClickListener);
        Button roll_up = (Button) findViewById(R.id.button8);
        roll_up.setOnClickListener(onClickListener);
        Button pitch_down = (Button) findViewById(R.id.button9);
        pitch_down.setOnClickListener(onClickListener);
        Button pitch_up = (Button) findViewById(R.id.button10);
        pitch_up.setOnClickListener(onClickListener);
        Button yaw_down = (Button) findViewById(R.id.button11);
        yaw_down.setOnClickListener(onClickListener);
        Button yaw_up = (Button) findViewById(R.id.button12);
        yaw_up.setOnClickListener(onClickListener);
        Button hov = (Button) findViewById(R.id.button13);
        hov.setOnClickListener(onClickListener);

        logView = (TextView) findViewById(R.id.log);
        logView.setText("GPS 가 잡혀야 좌표가 구해짐");

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        // isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        //  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //  Log.d("Main", "isGPSEnabled="+ isGPSEnabled);
        //  Log.d("Main", "isNetworkEnabled="+ isNetworkEnabled);
        Log.d("test","first");
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());

                logView.setText("latitude: " + lat + ", longitude: " + lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                logView.setText("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                logView.setText("onProviderEnabled");
            }

            public void onProviderDisabled(String provider) {
                logView.setText("onProviderDisabled");
            }
        };
        Log.d("test","build before");
        // Register the listener with the Location Manager to receive location updates
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
                return;
            }
        }
        Log.d("test","mid");
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLongitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("test", "longtitude=" + lng + ", latitude=" + lat);
        }
        Log.d("test","haha");
    }



    class BtnOnClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.button :
                    textView1 = (TextView) findViewById(R.id.textView);
                    textView1.setText("init_client") ;

                   // String a = lat

                    JSONObject data = new JSONObject();
                    try {
                        data.put("latitude", lat);
                        data.put("longitude", lng);
                        mSocket.emit("init_client", data);
                        Log.d("test","sendJsonData");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    break ;



                case R.id.button2 :
                    textView1 = (TextView) findViewById(R.id.textView);
                    textView1.setText("drone_call") ;
                    JSONObject data2 = new JSONObject();
                    try {
                        data2.put("latitude", lat);
                        data2.put("longitude", lng);
                        Log.d("test","drone_call");
                        mSocket.emit("drone_call", data2,new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d("test", args[0].toString());
                            }
                        });

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    break ;
                case R.id.button3 :
                    textView1 = (TextView) findViewById(R.id.textView);
                    textView1.setText("RTH") ;
                    JSONObject data3 = new JSONObject();
                    try {
                        data3.put("latitude", lat);
                        data3.put("longitude", lng);
                        Log.d("test","drone_call");
                        mSocket.emit("return_to_home", data3, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d("test", args[0].toString());
                            }
                        });

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                        Log.d("test","return_to_home");

                    break ;
                case R.id.button4 :
                    textView1 = (TextView) findViewById(R.id.textView);
                    textView1.setText("take a picture") ;
                    JSONObject data4 = new JSONObject();
                    try {
                        data4.put("latitude", lat);
                        data4.put("longitude", lng);
                        Log.d("test","picture");
                        mSocket.emit("take_picture", data4, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d("test", args[0].toString());
                            }
                        });

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("test","take_picture");

                    break ;


                case R.id.button5 :
                    textView2 = (TextView) findViewById(R.id.textView2);

                    thr -= (float)0.1;
                    thr = (float) (Math.round(thr*100)/100.0);
                    textView2.setText(String.valueOf(thr)) ;


                    drone_control();
                    break;
                case R.id.button6 :
                    textView2 = (TextView) findViewById(R.id.textView2);

                    thr += (float)0.1;
                    thr = (float) (Math.round(thr*100)/100.0);
                    textView2.setText(String.valueOf(thr)) ;

                    drone_control();
                    break;
                case R.id.button7 :
                    textView3 = (TextView) findViewById(R.id.textView3);

                    roll -= (float)0.1;
                    roll = (float) (Math.round(roll*100)/100.0);
                    textView3.setText(String.valueOf(roll)) ;

                    drone_control();
                    break;
                case R.id.button8 :
                    textView3 = (TextView) findViewById(R.id.textView3);

                    roll += (float)0.1;
                    roll = (float) (Math.round(roll*100)/100.0);
                    textView3.setText(String.valueOf(roll)) ;

                    drone_control();
                    break;
                case R.id.button9 :
                    textView4 = (TextView) findViewById(R.id.textView4);

                    pitch -= (float)0.1;
                    pitch = (float) (Math.round(pitch*100)/100.0);
                    textView4.setText(String.valueOf(pitch)) ;

                    drone_control();
                    break;
                case R.id.button10 :
                    textView4 = (TextView) findViewById(R.id.textView4);

                    pitch += (float)0.1;
                    pitch = (float) (Math.round(pitch*100)/100.0);
                    textView4.setText(String.valueOf(pitch)) ;

                    drone_control();
                    break;
                case R.id.button11 :
                    textView5 = (TextView) findViewById(R.id.textView5);

                    yaw -= (float)0.1;
                    yaw = (float) (Math.round(yaw*100)/100.0);
                    textView5.setText(String.valueOf(yaw)) ;

                    drone_control();
                    break;
                case R.id.button12 :
                    textView5 = (TextView) findViewById(R.id.textView5);

                    yaw += (float)0.1;
                    yaw = (float) (Math.round(yaw*100)/100.0);
                    textView5.setText(String.valueOf(yaw)) ;

                    drone_control();
                    break;
                case R.id.button13 :
                    thr=0;
                    roll=0;
                    pitch=0;
                    yaw=0;
                    textView2 = (TextView) findViewById(R.id.textView2);
                    textView2.setText(String.valueOf(thr)) ;
                    textView3 = (TextView) findViewById(R.id.textView3);
                    textView3.setText(String.valueOf(roll)) ;
                    textView4 = (TextView) findViewById(R.id.textView4);
                    textView4.setText(String.valueOf(pitch)) ;
                    textView5 = (TextView) findViewById(R.id.textView5);
                    textView5.setText(String.valueOf(yaw)) ;
                    try {
                        data_con.put("throttle", thr);
                        data_con.put("roll", roll);
                        data_con.put("pitch", pitch);
                        data_con.put("yaw", yaw);
                        Log.d("test","tho_down");
                        mSocket.emit("drone_control", data_con, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d("test", args[0].toString());
                            }
                        });

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void drone_control(){
        try {
            data_con.put("throttle", thr);
            data_con.put("roll", roll);
            data_con.put("pitch", pitch);
            data_con.put("yaw", yaw);
            Log.d("test","drone_control");
            mSocket.emit("drone_control", data_con, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d("test", args[0].toString());
                }
            });

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }

}
