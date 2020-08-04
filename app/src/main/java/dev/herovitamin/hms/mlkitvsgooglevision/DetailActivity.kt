package dev.herovitamin.hms.mlkitvsgooglevision

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {

        val EMOTION_KEY = "EMOTION_KEY"
        val EMOTION_VALUE = "EMOTION_VALUE"
        val EXTRAS = "BUNDLE_EXTRAS"

        fun getProperIntent(
            context: Context,
            result: String?,
            fl: Float
        ): Intent {
            var bundle = Bundle()
            bundle.putString(EMOTION_KEY, result)
            bundle.putFloat(EMOTION_VALUE, fl)

            return Intent(context, DetailActivity.javaClass).putExtra(EXTRAS, bundle)
        }
    }

    var message : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bundle = intent.getBundleExtra(EXTRAS)
        if(bundle != null){
            message = "Face captured! Looks like it is expression is " +
                    "${bundle[EMOTION_KEY]}"
        }
    }

    override fun onResume() {
        super.onResume()
        content.text = message
    }
}