package dev.herovitamin.hms.mlkitvsgooglevision

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private val TAG = "LiveImageDetection"

    private val CAMERA_PERMISSION_CODE = 2
    var analyzer: MLFaceAnalyzer? = null
    private var mLensEngine: LensEngine? = null
    private var lensType = LensEngine.BACK_LENS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("CHECK ME!", "onCreate() called")

        setContentView(R.layout.activity_main)

        this.createFaceAnalyzer()
        val facingSwitch = findViewById<ToggleButton>(R.id.facingSwitch)
        facingSwitch.setOnCheckedChangeListener(this)
        // Checking Camera Permissions
        // Checking Camera Permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.createLensEngine()
        } else {
            requestCameraPermission()
        }

    }

    private fun createFaceAnalyzer(): MLFaceAnalyzer? {
        // todo step 2: add on-device face analyzer
        val setting = MLFaceAnalyzerSetting.Factory()
            .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES)
            .allowTracing()
            .create()
        analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting)

        // finish
        analyzer?.setTransactor(FaceAnalyzerTransactor(overlay))
        return analyzer
    }

    private fun createLensEngine() {
        val context = this.applicationContext
        // todo step 3: add on-device lens engine
        mLensEngine = LensEngine.Creator(context, analyzer)
            .setLensType(lensType)
            .applyDisplayDimension(1600, 1024)
            .applyFps(25.0f)
            .enableAutomaticFocus(true)
            .create()
        // finish
    }

    private fun requestCameraPermission() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                CAMERA_PERMISSION_CODE
            )
            return
        }
    }

    override fun onResume() {
        super.onResume()
        this.startLensEngine()
    }

    override fun onPause() {
        super.onPause()
        preview!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLensEngine != null) {
            mLensEngine?.release()
        }
        if (analyzer != null) {
            analyzer!!.destroy()
        }
    }

    private fun startLensEngine() {
        if (mLensEngine != null) {
            try {
                preview!!.start(mLensEngine, overlay)
            } catch (e: IOException) {
                Log.e(
                    TAG,
                    "Failed to start lens engine.",
                    e
                )
                mLensEngine?.release()
                mLensEngine = null
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (mLensEngine != null) {
            if (isChecked) {
                lensType = LensEngine.FRONT_LENS
            } else {
                lensType = LensEngine.BACK_LENS
            }
        }
        mLensEngine!!.close()
        createLensEngine()
        startLensEngine()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode != CAMERA_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createLensEngine()
            return
        }
    }

}