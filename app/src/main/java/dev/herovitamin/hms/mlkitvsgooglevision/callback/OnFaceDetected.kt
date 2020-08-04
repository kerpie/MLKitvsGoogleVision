package dev.herovitamin.hms.mlkitvsgooglevision.callback

import android.util.SparseArray
import com.huawei.hms.mlsdk.face.MLFace

interface OnFaceDetected {

    fun onSuccess(analyseList: SparseArray<MLFace?>)

}