package dev.herovitamin.hms.mlkitvsgooglevision.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.huawei.hms.mlsdk.face.MLFace
import com.huawei.hms.mlsdk.face.MLFaceShape
import dev.herovitamin.hms.mlkitvsgooglevision.camara.GraphicOverlay
import java.text.DecimalFormat
import java.util.*

class MLFaceGraphic(overlay: GraphicOverlay?, face: MLFace?) : GraphicOverlay.Graphic(overlay) {

    private val BOX_STROKE_WIDTH = 8.0f
    private val LINE_WIDTH = 5.0f

    private var overlay: GraphicOverlay? = null

    private var facePositionPaint: Paint? = null
    private var landmarkPaint: Paint? = null
    private var boxPaint: Paint? = null

    private var facePaint: Paint? = null
    private var eyePaint: Paint? = null
    private var eyebrowPaint: Paint? = null
    private var lipPaint: Paint? = null
    private var nosePaint: Paint? = null
    private var noseBasePaint: Paint? = null
    private var textPaint: Paint? = null
    private var probilityPaint: Paint? = null

    @Volatile
    private var mFace: MLFace? = null

    fun MLFaceGraphic(overlay: GraphicOverlay?, face: MLFace?) {
        mFace = face
        this.overlay = overlay
        val selectedColor = Color.WHITE
        facePositionPaint = Paint()
        facePositionPaint?.color = selectedColor
        textPaint = Paint()
        textPaint?.color = Color.WHITE
        textPaint?.textSize = 24f
        textPaint?.typeface = Typeface.DEFAULT
        probilityPaint = Paint()
        probilityPaint?.color = Color.WHITE
        probilityPaint?.textSize = 35f
        probilityPaint?.typeface = Typeface.DEFAULT
        landmarkPaint = Paint()
        landmarkPaint?.color = Color.RED
        landmarkPaint?.style = Paint.Style.FILL
        landmarkPaint?.strokeWidth = 10f
        boxPaint = Paint()
        boxPaint?.color = Color.WHITE
        boxPaint?.style = Paint.Style.STROKE
        boxPaint?.strokeWidth = BOX_STROKE_WIDTH
        facePaint = Paint()
        facePaint?.color = Color.parseColor("#ffcc66")
        facePaint?.style = Paint.Style.STROKE
        facePaint?.strokeWidth = LINE_WIDTH
        eyePaint = Paint()
        eyePaint?.color = Color.parseColor("#00ccff")
        eyePaint?.style = Paint.Style.STROKE
        eyePaint?.strokeWidth = LINE_WIDTH
        eyebrowPaint = Paint()
        eyebrowPaint?.color = Color.parseColor("#006666")
        eyebrowPaint?.style = Paint.Style.STROKE
        eyebrowPaint?.strokeWidth = LINE_WIDTH
        nosePaint = Paint()
        nosePaint?.color = Color.parseColor("#ffff00")
        nosePaint?.style = Paint.Style.STROKE
        nosePaint?.strokeWidth = LINE_WIDTH
        noseBasePaint = Paint()
        noseBasePaint?.color = Color.parseColor("#ff6699")
        noseBasePaint?.style = Paint.Style.STROKE
        noseBasePaint?.strokeWidth = LINE_WIDTH
        lipPaint = Paint()
        lipPaint?.color = Color.parseColor("#990000")
        lipPaint?.style = Paint.Style.STROKE
        lipPaint?.strokeWidth = LINE_WIDTH
    }

    fun sortHashMap(map: HashMap<String, Float>): List<String>? {
        val entey: Set<Map.Entry<String, Float>> =
            map.entries
        val list: List<Map.Entry<String, Float>> =
            ArrayList(entey)
        Collections.sort(
            list,
            object :
                Comparator<Map.Entry<String?, Float?>?> {
                override fun compare(
                    o1: Map.Entry<String?, Float?>?,
                    o2: Map.Entry<String?, Float?>?
                ): Int {
                    return if (o2?.value?.minus(o1?.value!!) ?: 0.0f >= 0) {
                        1
                    } else {
                        -1
                    }
                }

            })

        val emotions: MutableList<String> =
            ArrayList()
        for (i in 0..1) {
            emotions.add(list[i].key)
        }
        return emotions
    }

    override fun draw(canvas: Canvas?) {
        if (mFace == null) {
            return
        }
        val start = 350f
        var x = start
        val width = 500f
        var y: Float = overlay?.height?.minus(300.0f) ?: 0.0f
        val emotions =
            HashMap<String, Float>()
        emotions["Smiling"] = mFace!!.emotions.smilingProbability
        emotions["Neutral"] = mFace!!.emotions.neutralProbability
        emotions["Angry"] = mFace!!.emotions.angryProbability
        emotions["Fear"] = mFace!!.emotions.fearProbability
        emotions["Sad"] = mFace!!.emotions.sadProbability
        emotions["Disgust"] = mFace!!.emotions.disgustProbability
        emotions["Surprise"] = mFace!!.emotions.surpriseProbability
        val result = sortHashMap(emotions)
        val decimalFormat = DecimalFormat("0.000")
        // Draw the facial feature value.
        canvas?.drawText(
            "Left eye: " + decimalFormat.format(
                mFace!!.features.leftEyeOpenProbability.toDouble()
            ), x, y, probilityPaint!!
        )
        x = x + width
        canvas?.drawText(
            "Right eye: " + decimalFormat.format(
                mFace!!.features.rightEyeOpenProbability.toDouble()
            ), x, y, probilityPaint!!
        )
        y = y - 40.0f
        x = start
        canvas?.drawText(
            "Moustache Probability: " + decimalFormat.format(
                mFace!!.features.moustacheProbability.toDouble()
            ), x, y, probilityPaint!!
        )
        x = x + width
        canvas?.drawText(
            "Glass Probability: " + decimalFormat.format(
                mFace!!.features.sunGlassProbability.toDouble()
            ), x, y, probilityPaint!!
        )
        y = y - 40.0f
        x = start
        canvas?.drawText(
            "Hat Probability: " + decimalFormat.format(
                mFace!!.features.hatProbability.toDouble()
            ), x, y, probilityPaint!!
        )
        x = x + width
        canvas?.drawText("Age: " + mFace!!.features.age, x, y, probilityPaint!!)
        y = y - 40.0f
        x = start
        val sex =
            if (mFace!!.features.sexProbability > 0.5f) "Female" else "Male"
        canvas?.drawText("Gender: $sex", x, y, probilityPaint!!)
        x = x + width
        canvas?.drawText(
            "EulerAngleY: " + decimalFormat.format(
                mFace!!.rotationAngleY.toDouble()
            ), x, y, probilityPaint!!
        )
        y = y - 40.0f
        x = start
        canvas?.drawText(
            "EulerAngleZ: " + decimalFormat.format(
                mFace!!.rotationAngleZ.toDouble()
            ), x, y, probilityPaint!!
        )
        x = x + width
        canvas?.drawText(
            "EulerAngleX: " + decimalFormat.format(
                mFace!!.rotationAngleX.toDouble()
            ), x, y, probilityPaint!!
        )
        y = y - 40.0f
        x = start
        result?.get(0)?.let { canvas?.drawText(it, x, y, probilityPaint!!) }

        // Draw a face contour.
        if (mFace!!.faceShapeList != null) {
            for (faceShape in mFace!!.faceShapeList) {
                if (faceShape == null) {
                    continue
                }
                val points = faceShape.points
                for (i in points.indices) {
                    val point = points[i]
                    canvas?.drawPoint(
                        translateX(point!!.x.toFloat()),
                        translateY(point.y.toFloat()),
                        boxPaint!!
                    )
                    if (i != points.size - 1) {
                        val next = points[i + 1]
                        if (point != null && point.x != null && point.y != null) {
                            if (i % 3 == 0) {
                                canvas?.drawText(
                                    (i + 1).toString(),
                                    translateX(point.x.toFloat()),
                                    translateY(point.y.toFloat()),
                                    textPaint!!
                                )
                            }
                            canvas?.drawLines(
                                floatArrayOf(
                                    translateX(point.x.toFloat()),
                                    translateY(point.y.toFloat()),
                                    translateX(next.x.toFloat()),
                                    translateY(next.y.toFloat())
                                ), getPaint(faceShape)!!
                            )
                        }
                    }
                }
            }
        }
        // Face Key Points
        for (keyPoint in mFace!!.faceKeyPoints) {
            if (keyPoint != null) {
                val point = keyPoint.point
                canvas?.drawCircle(
                    translateX(point.x),
                    translateY(point.y),
                    10f, landmarkPaint!!
                )
            }
        }
    }

    private fun getPaint(faceShape: MLFaceShape): Paint? {
        return when (faceShape.faceShapeType) {
            MLFaceShape.TYPE_LEFT_EYE, MLFaceShape.TYPE_RIGHT_EYE -> eyePaint
            MLFaceShape.TYPE_BOTTOM_OF_LEFT_EYEBROW, MLFaceShape.TYPE_BOTTOM_OF_RIGHT_EYEBROW, MLFaceShape.TYPE_TOP_OF_LEFT_EYEBROW, MLFaceShape.TYPE_TOP_OF_RIGHT_EYEBROW -> eyebrowPaint
            MLFaceShape.TYPE_BOTTOM_OF_LOWER_LIP, MLFaceShape.TYPE_TOP_OF_LOWER_LIP, MLFaceShape.TYPE_BOTTOM_OF_UPPER_LIP, MLFaceShape.TYPE_TOP_OF_UPPER_LIP -> lipPaint
            MLFaceShape.TYPE_BOTTOM_OF_NOSE -> noseBasePaint
            MLFaceShape.TYPE_BRIDGE_OF_NOSE -> nosePaint
            else -> facePaint
        }
    }

}