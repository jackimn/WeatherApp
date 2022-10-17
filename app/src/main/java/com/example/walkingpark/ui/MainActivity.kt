package com.example.walkingpark.ui

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.walkingpark.R
import com.example.walkingpark.constants.Common
import com.example.walkingpark.data.model.entity.LocationEntity
import com.example.walkingpark.data.model.entity.LocationObject
import com.example.walkingpark.databinding.ActivityMainBinding
import com.example.walkingpark.ui.receiver.LocationReceiver
import com.example.walkingpark.ui.service.LocationService
import com.example.walkingpark.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity(
) {
    private var binding: ActivityMainBinding? = null
    val viewModel by viewModels<MainViewModel>()

    private lateinit var locationReceiver: LocationReceiver
    private var locationService: LocationService? = null
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>

    /**
     * 1. LifeCycle Service 의
     * 2. 퍼미션 요청
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setBottomMenuButtons()         // 하단 버튼 설정
        locationReceiver = LocationReceiver()

        val filter = IntentFilter().apply {
            addAction(Common.REQUEST_LOCATION_INIT)
            addAction(Common.REQUEST_LOCATION_UPDATE_START)
            addAction(Common.REQUEST_LOCATION_UPDATE_CANCEL)
        }
        registerReceiver(locationReceiver, filter)
        // 퍼미션 요청 핸들링. (onActivityResult 대체)
        locationPermissionLauncher = handlePermissionByInitLauncher()
        startPermissionRequest()
    }

    /**
     * 퍼미션 요청을 위한 퍼미션 런처
     */
    private fun handlePermissionByInitLauncher() = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // viewModel.loadingIndicator = LoadingIndicator(this, "RestApi 데이터 읽어오는중....")
        // 퍼미션이 허용되었으므로 서비스 실행
        if (getPermissionCheckedToBoolean()) {
            Intent(this, LocationService::class.java)
                .putExtra("intent-filter", "aaaa").apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startForegroundService(this)
                    else
                        startService(this)
                }
        }
        // 퍼미션 비 허용 -> 앱 종료
        else {
            Toast.makeText(this, "퍼미션을 허용해야 앱 이용이 가능합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * 퍼미션 런치를 통한 퍼미션 요청 메서드
     */
    // 퍼미션 요청 수행!!
    private fun startPermissionRequest() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    
    /**
     * onCreate 수행 이후 수행되어, LifeCycle 서비스 등록
     */
    override fun onStart() {
        super.onStart()
        Intent(this, LocationService::class.java).apply {
            startService(this)
            bindService(
                this,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    /**
     *  Lifecycle Service: 몇몇 컴포넌트가 서비스의 소멸된 라이프사이클 스테이지를 구독하게 함으로써, 자동 소멸,
     *  리스너 해제, 메모리 확보 등의 작업을 수행하게 함.
     *  LifecycleScope 에 접근하여 서비스가 중지되면 자동으로 취소되는 코루틴을 시작하는 데 사용할 수 있음
     *  ViewModel 및 LiveData 는 라이프사이클 사용 중심으로 설계되어 이론적으로 LifecycleService 와 함께 사용할
     *  수 있지만, UI 용으로 사용하기에는 비효율적
     *
     *  onServiceConnected: 라이프사이클 서비스가 연결되었을 때 수행되는 콜백
     *  onServiceDisconnected: 라이프사이클 서비스가 끊어졌을 때 수행되는 콜백
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            // LocationService 의 Flowable 을 ViewModel 로 전달.
            locationService?.let {
                viewModel.locationObservable.value = it.getLocationFlowable()
                viewModel.locationObservableHandler()
                //viewModel.userLocation.postValue(getLocationFromService(it))
            }
        }
        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    /**
     * 프래그먼트 트랜잭션 교체를 위한 하단 메뉴 버튼 설정
     */
    private fun setBottomMenuButtons() {
        // 홈프래그먼트를 기본프래그먼트로 설정
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, HomeFragment()).commit()

        binding!!.buttonHome.setOnClickListener {
            val transaction1 = supportFragmentManager.beginTransaction()
            transaction1.replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

        binding!!.buttonMaps.setOnClickListener {
            val transaction2 = supportFragmentManager.beginTransaction()
            transaction2.replace(R.id.fragmentContainer, MapsFragment()).commit()
        }

        binding!!.buttonSettings.setOnClickListener {
            val transaction3 = supportFragmentManager.beginTransaction()
            transaction3.replace(R.id.fragmentContainer, SettingsFragment()).commit()
        }
    }

    override fun onDestroy() {
        Log.e("onDestroy()0", "destroy");
        super.onDestroy()
        binding = null
        // 위치 업데이트 콜백 해지.
        // viewModel.cancelUpdateLocation(viewModel.locationCallback)
        // 포그라운드 서비스 정지.
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
        unregisterReceiver(locationReceiver)
    }

    // 퍼미션이 허용되어 Intent 를 통하여 서비스를 실행할 지 아니면, 앱을 종료할지 체크
    // 서비스는 액티비티에서 실행해야 하므로 이후 로직은 액티비티에서 수행.
    private fun getPermissionCheckedToBoolean(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }
}