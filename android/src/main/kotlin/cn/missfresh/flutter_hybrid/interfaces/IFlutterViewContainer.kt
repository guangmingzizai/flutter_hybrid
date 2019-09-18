package cn.missfresh.flutter_hybrid.interfaces

import android.app.Activity
import cn.missfresh.flutter_hybrid.view.FHFlutterView

/**
 * Created by sjl
 * on 2019-09-02
 */
interface IFlutterViewContainer {

    /**
     * Return the container name.
     */
    fun getContainerName(): String

    /**
     * Return the container params.
     */
    fun getContainerParams(): Map<String, Any>

    /**
     * Return the FHFlutterView.
     */
    fun getFHFlutterView(): FHFlutterView

    /**
     * Return current Activity.
     */
    fun getCurrActivity(): Activity

    /**
     * Called when the page is displayed.
     */
    fun onContainerAppear()

    /**
     * Called when the page is not visible.
     */
    fun onContainerDisappear()

    /**
     * Called when the page is destroyed，to call the finish method of the activity.
     */
    fun destroyContainerView()

    /**
     * Check to see whether this container is in the process of finishing.
     */
    fun isFinishing(): Boolean
}