package com.jidouauto.refule;

import android.Manifest;
import android.car.hardware.CarPropertyConfig;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.car.hardware.property.ICarProperty;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.car.Car;
import android.support.car.CarConnectionCallback;
import android.support.car.CarInfoManager;
import android.support.car.CarInfoManagerEmbedded;
import android.support.car.CarNotConnectedException;
import android.support.car.CarServiceLoaderEmbedded;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elektrobit.aed.interfaces.mbbservice.AccessTokenServiceManager;

import java.util.ArrayList;
import java.util.List;

import vendor.elektrobit.hardware.automotive.vehicle.V1_0.VehicleProperty;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wws";


    private AccessTokenServiceManager mAccessTokenServiceManager;
    private CarInfoManager mCarManager;
    private ICarProperty mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccessTokenServiceManager = new AccessTokenServiceManager(this, new AccessTokenServiceManager.IAccessTokenCallbackListener() {
            @Override
            public void onServiceConnected() {
                Log.d(TAG, "onServiceConnected: ");
            }

            @Override
            public void onAccessTokenReceived(String s) {
                Log.d(TAG, "onAccessTokenReceived: " + s);
                Toast.makeText(MainActivity.this, "Token : " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBaseUrlReceived(String s) {
                Log.d(TAG, "onAccessTokenReceived: " + s);
                Toast.makeText(MainActivity.this, "BaseUrl : " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorReceived(String s) {
                Log.d(TAG, "onAccessTokenReceived: " + s);
                Toast.makeText(MainActivity.this, "error : " + s, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.showToken).setOnClickListener(v -> {
            mAccessTokenServiceManager.requestAccessToken();
        });
        findViewById(R.id.bindservice).setOnClickListener(v -> {
            mAccessTokenServiceManager.connectService();
        });
        findViewById(R.id.showurl).setOnClickListener(v -> mAccessTokenServiceManager.requestBaseUrl());
//        findViewById(R.id.showInfo2).setOnClickListener(v -> mAccessTokenServiceManager.connectService());
        findViewById(R.id.showInfo).setOnClickListener(v -> Car.createCar(this, new CarConnectionCallback() {
            @Override
            public void onConnected(Car car) {
                Log.d(TAG, "onConnected: ");
                try {
                    /*mCarManager = car.getCarManager(CarInfoManager.class);
                    String model = mCarManager.getModel();
                    String modelYear = mCarManager.getModelYear();
                    String VehicleId = mCarManager.getVehicleId();
                    String HeadunitModel = mCarManager.getHeadunitModel();
                    String HeadunitManufacturer = mCarManager.getHeadunitManufacturer();
                    String HeadunitSoftwareBuild = mCarManager.getHeadunitSoftwareBuild();
                    String HeadunitSoftwareVersion = mCarManager.getHeadunitSoftwareVersion();
                    String Manufacturer = mCarManager.getManufacturer();
                    int DriverPosition = mCarManager.getDriverPosition();
//                    float EvBatteryCapacity = mCarManager.getEvBatteryCapacity();
//                    float FuelCapacity = mCarManager.getFuelCapacity();
//                    int[] EvConnectorTypes = mCarManager.getEvConnectorTypes();
//                    int[] FuelTypes = mCarManager.getFuelTypes();
                    String info = "car info: " + "model----" + model + "\n"
                            + "modelYear----" + modelYear + "\n"
                            + "VehicleId----" + VehicleId + "\n"
                            + "HeadunitModel----" + HeadunitModel + "\n"
                            + "HeadunitManufacturer----" + HeadunitManufacturer + "\n"
                            + "HeadunitSoftwareBuild----" + HeadunitSoftwareBuild + "\n"
                            + "HeadunitSoftwareVersion----" + HeadunitSoftwareVersion + "\n"
                            + "Manufacturer----" + Manufacturer + "\n"
                            + "DriverPosition----" + DriverPosition + "\n"
//                            + "EvBatteryCapacity----" + EvBatteryCapacity + "\n"
//                            + "FuelCapacity----" + FuelCapacity + "\n"
//                            + "EvConnectorTypes----" + EvConnectorTypes + "\n"
                            *//*+ "FuelTypes----" + FuelTypes + "\n"*//*;
                    Log.d(TAG, "car info: " + info);*/

                    final CarPropertyManager property = (CarPropertyManager) car.getCarManager("property");
                    final List<CarPropertyConfig> propertyList = property.getPropertyList();
                    StringBuilder info = new StringBuilder();
                    for (CarPropertyConfig config : propertyList){
                        CarPropertyValue value = property.getProperty(config.getPropertyId(),0);
//                        if(config.getPropertyType().equals(Integer.class.getName())){
//                            property.getProperty(Class.forName(config.getPropertyType().getName()), config.getPropertyId(), 0);
//                        }
//
//                        config.getPropertyType();

                        info.append(value.toString() + "\n");
                    }
                    Log.d(TAG, "car info: " + info.toString());
                    ((TextView) findViewById(R.id.viewInfo)).setText(info.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(Car car) {
                Log.d(TAG, "onDisconnected: ");
            }
        }).connect());

        findViewById(R.id.showInfo2).setOnClickListener(v -> {
            try {
                String countryCode = getCountryCode();
                String model = getModel();
                String modelYear = getModelYear();
                String make = getMake();
                String nightMode = getNightMode();
                String info = "car info: " + "model----" + model + "\n"
                        + "modelYear----" + modelYear + "\n"
                        + "make----" + make + "\n"
                        + "nightMode----" + nightMode + "\n"
                        + "countryCode----" + countryCode;
                Log.d(TAG, "car info2: " + info);
                ((TextView) findViewById(R.id.viewInfo)).setText(info);
            } catch (android.car.CarNotConnectedException e) {
                e.printStackTrace();
            }
        });

        connectService();

        requestActivityPremission();

    }

    void connectService() {
        android.car.Car.createCar(
                MainActivity.this,
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.d(TAG, "onServiceConnected: " + name.getClassName());
                        mService = ICarProperty.Stub.asInterface(service);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }

        ).connect();
    }

    /**
     * 请求权限
     *
     * @return
     */
    private boolean requestActivityPremission() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, "android.car.permission.CAR_ENERGY") != PackageManager.PERMISSION_GRANTED) {
            permissions.add("android.car.permission.CAR_ENERGY");
        }
        if (ContextCompat.checkSelfPermission(this, "android.car.permission.CAR_SPEED") != PackageManager.PERMISSION_GRANTED) {
            permissions.add("android.car.permission.CAR_SPEED");
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Access permission denied");
                        return;
                    }
                    connectService();
                }
            }
        }
    }

    public String getModelYear() throws android.car.CarNotConnectedException {
        CarPropertyValue<String> carProp = this.getProperty(String.class, VehicleProperty.INFO_MODEL_YEAR, 0);
        return carProp != null ? (String) carProp.getValue() : null;
    }

    public String getModel() throws android.car.CarNotConnectedException {
        CarPropertyValue<String> carProp = this.getProperty(String.class, VehicleProperty.INFO_MODEL, 0);
        return carProp != null ? (String) carProp.getValue() : null;
    }

    public String getMake() throws android.car.CarNotConnectedException {
        CarPropertyValue<String> carProp = this.getProperty(String.class, VehicleProperty.INFO_MAKE, 0);
        return carProp != null ? (String) carProp.getValue() : null;
    }

    public String getCountryCode() throws android.car.CarNotConnectedException {
        CarPropertyValue<String> carProp = this.getProperty(String.class, VehicleProperty.INFO_COUNTRY_CODE, 0);
        return carProp != null ? (String) carProp.getValue() : null;
    }

    public String getNightMode() throws android.car.CarNotConnectedException {
        CarPropertyValue<Object> carProp = null;
        try {
            carProp = this.getProperty(VehicleProperty.NIGHT_MODE, 0);
        } catch (CarNotConnectedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " boolean getNightMode: " + (carProp == null ? "" : carProp.toString()));
        CarPropertyValue<Object> carProp2 = this.getProperty(Object.class, VehicleProperty.NIGHT_MODE, 0);
        Log.d(TAG, " Object getNightMode: " + (carProp2 == null ? "" : carProp2.toString()));
        return carProp != null ? carProp.toString() : "";
    }

    private <E> CarPropertyValue<E> getProperty(Class<E> clazz, int propId, int area) throws android.car.CarNotConnectedException {
        try {
            CarPropertyValue<E> propVal = this.mService.getProperty(propId, area);
            if (propVal != null && propVal.getValue() != null) {
                Class<?> actualClass = propVal.getValue().getClass();
                if (actualClass != clazz) {
                    throw new IllegalArgumentException("Invalid property type. Expected: " + clazz + ", but was: " + actualClass);
                }
            } else {
                Log.d(TAG, "getProperty: propVal == null");
            }

            if (propVal == null) {
                Log.d(TAG, "getProperty: propVal == null");
            }
            return propVal;
        } catch (RemoteException var6) {
            Log.e(TAG, "getProperty failed with " + var6.toString() + ", propId: 0x" + Integer.toHexString(propId) + ", area: 0x" + Integer.toHexString(area), var6);
            throw new android.car.CarNotConnectedException(var6);
        } catch (IllegalArgumentException var7) {
            Log.e(TAG, "getProperty failed with " + var7.toString() + ", propId: 0x" + Integer.toHexString(propId) + ", area: 0x" + Integer.toHexString(area), var7);
            return null;
        }
    }

    public <E> CarPropertyValue<E> getProperty(int paramInt1, int paramInt2)
            throws CarNotConnectedException
    {
        try
        {
            CarPropertyValue localCarPropertyValue = this.mService.getProperty(paramInt1, paramInt2);
            return localCarPropertyValue;
        }
        catch (RemoteException localRemoteException)
        {
            String str = this.TAG;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("getProperty failed with ");
            localStringBuilder.append(localRemoteException.toString());
            localStringBuilder.append(", propId: 0x");
            localStringBuilder.append(Integer.toHexString(paramInt1));
            localStringBuilder.append(", area: 0x");
            localStringBuilder.append(Integer.toHexString(paramInt2));
            Log.e(str, localStringBuilder.toString(), localRemoteException);
            throw new CarNotConnectedException(localRemoteException);
        }
    }
}
