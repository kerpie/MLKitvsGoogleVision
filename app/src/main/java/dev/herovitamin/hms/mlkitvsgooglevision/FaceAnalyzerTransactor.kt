package dev.herovitamin.hms.mlkitvsgooglevision

import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.common.MLAnalyzer.MLTransactor
import com.huawei.hms.mlsdk.face.MLFace
import dev.herovitamin.hms.mlkitvsgooglevision.callback.OnFaceDetected
import dev.herovitamin.hms.mlkitvsgooglevision.camera.GraphicOverlay
import dev.herovitamin.hms.mlkitvsgooglevision.ui.MLFaceGraphic

class FaceAnalyzerTransactor: MLTransactor<MLFace>{

    private var mGraphicOverlay: GraphicOverlay? = null
    private val callback : OnFaceDetected

    constructor(ocrGraphicOverlay: GraphicOverlay?, callback: OnFaceDetected) {
        mGraphicOverlay = ocrGraphicOverlay
        this.callback = callback
    }

    override fun transactResult(result: MLAnalyzer.Result<MLFace?>) {
        mGraphicOverlay?.clear()
        val faceSparseArray = result.analyseList
        for (i in 0 until faceSparseArray.size()) {
            val graphic = MLFaceGraphic(mGraphicOverlay, faceSparseArray.valueAt(i))
            mGraphicOverlay?.add(graphic)
        }
        callback.onSuccess(faceSparseArray)
    }

    override fun destroy() {
        mGraphicOverlay?.clear()
    }



}