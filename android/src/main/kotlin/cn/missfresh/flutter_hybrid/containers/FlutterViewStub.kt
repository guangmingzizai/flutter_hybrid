package cn.missfresh.flutter_hybrid.containers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import cn.missfresh.flutter_hybrid.Logger
import cn.missfresh.flutter_hybrid.view.FHFlutterView
import io.flutter.view.FlutterView

/**
 * Created by sjl
 * on 2019-09-02
 */
class FlutterViewStub(context: Context, private val flutterView: FlutterView) : FrameLayout(context) {

    private var mCoverView: View? = null
    private var mBitmap: Bitmap? = null
    private var mSnapshot: ImageView
    private var mStub: FrameLayout = FrameLayout(context)
    private val mHandler: Handler = ViewStatusHandler(Looper.getMainLooper())

    init {
        mStub.setBackgroundColor(Color.WHITE)
        addView(mStub, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        mSnapshot = ImageView(context)
        mSnapshot.scaleType = ImageView.ScaleType.FIT_CENTER
        mSnapshot.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mCoverView = initFlutterCoverView()
        addView(mCoverView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /**
     * Called when Activity or Fragment onContainerAppear is called
     */
    fun onContainerAppear() {
        Logger.d("onContainerAppear")
        refresh()
        removeCover()
    }

    /**
     * Called when Activity or Fragment onContainerDisappear is called
     */
    fun onContainerDisappear() {

    }

    private fun refresh() {
        flutterView.requestFocus()
        flutterView.invalidate()
    }

    /**
     * Remove cover view
     */
    private fun removeCover() {
        mCoverView?.let {
            removeView(it)
        }
    }

    /**
     * Called when the Fragment's onPause is called, add a snapshot
     */
    fun snapshot() {
        if (mStub.childCount <= 0 || mSnapshot.parent != null) {
            return
        }

        val fhFlutterView = mStub.getChildAt(0) as FHFlutterView
        mBitmap = fhFlutterView?.bitmap
        mBitmap?.let {
            if (!it.isRecycled) {
                mSnapshot.setImageBitmap(it)
                addView(mSnapshot)
            }
        }
    }

    /**
     * Initialize the cover of the FlutterView
     */
    private fun initFlutterCoverView(): View {
        val initCover = View(context)
        initCover.setBackgroundColor(Color.WHITE)
        return initCover
    }

    /**
     * Called when Activity onPostResume or Fragment onResume is called
     */
    fun attachFlutterView(flutterView: FHFlutterView) {
        Logger.d("attachFlutterView")
        if (flutterView.parent !== mStub) {
            mHandler.removeMessages(ViewStatusHandler.MSG_DETACH)

            flutterView.parent?.let {
                (it as ViewGroup).removeView(flutterView)
            }

            mStub.addView(flutterView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    /**
     * Called when Activity onPause is called
     */
    fun detachFlutterView() {
        Logger.d("detachFlutterView")
        if (mStub?.childCount <= 0) {
            return
        }

        val fhFlutterView = mStub.getChildAt(0) as FHFlutterView

        fhFlutterView?.let {
            if (mSnapshot.parent == null) {
                mBitmap = it.bitmap
                mBitmap?.let { bitmap ->
                    if (!bitmap.isRecycled) {
                        mSnapshot.setImageBitmap(bitmap)
                        Logger.d("snapshot view")
                        addView(mSnapshot)
                    }
                }
            }

            val msg = Message()
            msg.what = ViewStatusHandler.MSG_DETACH
            msg.obj = Runnable {
                fhFlutterView?.parent?.let {
                    if (it == mStub) {
                        Logger.d("detachFlutterView")
                        mStub.removeView(fhFlutterView)
                    }
                }
            }
            mHandler.sendMessageDelayed(msg, 18)
        }
    }

    /**
     * Call this method to remove all child views from the
     * ViewGroup. And release bitmap
     */
    fun removeViews() {
        removeAllViews()
        mSnapshot?.setImageBitmap(null)
        recycleBitmap()
    }

    /**
     * Release bitmap
     */
    private fun recycleBitmap() {
        mBitmap?.let {
            if (!it.isRecycled) {
                it.recycle()
                mBitmap = null
            }
        }
    }

    class ViewStatusHandler internal constructor(looper: Looper) : Handler(looper) {

        companion object {
            const val MSG_DETACH = 180081
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.obj is Runnable) {
                (msg.obj as Runnable).run()
            }
        }
    }
}