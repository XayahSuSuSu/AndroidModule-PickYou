package com.xayah.materialyoufileexplorer.util

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.xayah.materialyoufileexplorer.ExplorerActivity.Companion.rootAccess
import com.xayah.materialyoufileexplorer.ExplorerViewModel

class PathUtil {
    companion object {
        const val STORAGE_EMULATED_0 = "/storage/emulated/0"
        const val STORAGE_EMULATED_0_ANDROID = "/storage/emulated/0/Android"
        const val STORAGE_EMULATED_0_ANDROID_DATA = "/storage/emulated/0/Android/data"
        const val STORAGE_EMULATED_0_ANDROID_OBB = "/storage/emulated/0/Android/obb"

        fun onBack(model: ExplorerViewModel, activity: AppCompatActivity) {
            val path = model.getPath()
            if (!rootAccess && path == STORAGE_EMULATED_0 || path == "") {
                activity.finish()
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                    model.documentFileList.removeLastOrNull()
                }
                model.removePath()
            }
        }

        fun handleSpecialPath(
            path: String,
            onOutEvent: (path: String) -> Unit = {},
            onAndroidEvent: (path: String) -> Unit = {},
            onAndroidDataEvent: (path: String) -> Unit = {},
            onAndroidObbEvent: (path: String) -> Unit = {},
            onElseEvent: (path: String) -> Unit = {},
        ) {
            if (!path.contains(STORAGE_EMULATED_0)) {
                onOutEvent(path)
            } else {
                var mPath = path
                if (path.contains(STORAGE_EMULATED_0_ANDROID_DATA)) {
                    mPath = STORAGE_EMULATED_0_ANDROID_DATA
                } else if (path.contains(STORAGE_EMULATED_0_ANDROID_OBB)) {
                    mPath = STORAGE_EMULATED_0_ANDROID_OBB
                }
                when (mPath) {
                    STORAGE_EMULATED_0_ANDROID -> {
                        onAndroidEvent(path)
                    }
                    STORAGE_EMULATED_0_ANDROID_DATA -> {
                        onAndroidDataEvent(path)
                    }
                    STORAGE_EMULATED_0_ANDROID_OBB -> {
                        onAndroidObbEvent(path)
                    }
                    else -> {
                        onElseEvent(path)
                    }
                }
            }
        }
    }
}