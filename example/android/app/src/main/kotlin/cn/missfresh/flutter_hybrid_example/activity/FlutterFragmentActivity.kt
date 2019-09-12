package cn.missfresh.flutter_hybrid_example.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.missfresh.flutter_hybrid.FlutterHybridPlugin
import cn.missfresh.flutter_hybrid.containers.FHFlutterFragment
import cn.missfresh.flutter_hybrid.interfaces.IFlutterViewContainer
import cn.missfresh.flutter_hybrid_example.R

/**
 * Created by sjl
 * on 2019-09-01
 */
class FlutterFragmentActivity : AppCompatActivity() {

    private var mFragment: FHFlutterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.flutter_fragment)

        mFragment = FHFlutterFragment.instance("FlutterFragmentActivity Demo")

        mFragment?.let {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_stub, mFragment!!)
                    .commit()
        }
    }

    override fun onBackPressed() {
        FlutterHybridPlugin.instance.getContainerManager().onBackPressed(mFragment as IFlutterViewContainer)
    }
}