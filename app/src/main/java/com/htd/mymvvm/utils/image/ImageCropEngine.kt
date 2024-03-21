package com.htd.mymvvm.utils.image

import android.content.Context
import com.luck.picture.lib.engine.CropEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.config.PictureMimeType
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import com.bumptech.glide.request.target.CustomTarget
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.utils.DateUtils
import java.io.File
import java.util.ArrayList

class ImageCropEngine(context: Context) : CropEngine {
    private val context: Context = context.applicationContext

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private val sandboxPath: String
        private get() {
            val externalFilesDir = context.getExternalFilesDir("")
            val customFile = File(externalFilesDir!!.absolutePath, "Sandbox")
            if (!customFile.exists()) {
                customFile.mkdirs()
            }
            return customFile.absolutePath + File.separator
        }

    override fun onStartCrop(
        fragment: Fragment, currentLocalMedia: LocalMedia,
        dataSource: ArrayList<LocalMedia>, requestCode: Int
    ) {
        val currentCropPath = currentLocalMedia.availablePath
        val inputUri: Uri
        inputUri = if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(
                currentCropPath
            )
        ) {
            Uri.parse(currentCropPath)
        } else {
            Uri.fromFile(File(currentCropPath))
        }
        val fileName = DateUtils.getCreateFileName("CROP_") + ".jpg"
        val destinationUri = Uri.fromFile(File(sandboxPath, fileName))
        val options = buildOptions()
        val dataCropSource = ArrayList<String>()
        for (i in dataSource.indices) {
            val media = dataSource[i]
            dataCropSource.add(media.availablePath)
        }
        val uCrop = UCrop.of(inputUri, destinationUri, dataCropSource)
        //options.setMultipleCropAspectRatio(buildAspectRatios(dataSource.size()));
        uCrop.withOptions(options)
        uCrop.setImageEngine(object : UCropImageEngine {
            override fun loadImage(context: Context, url: String, imageView: ImageView) {
                if (!ImageLoaderUtil.assertValidRequest(context)) {
                    return
                }
                Glide.with(context).load(url).into(imageView)
            }

            override fun loadImage(
                context: Context,
                url: Uri,
                maxWidth: Int,
                maxHeight: Int,
                call: UCropImageEngine.OnCallbackListener<Bitmap>
            ) {
                if (!ImageLoaderUtil.assertValidRequest(context)) {
                    return
                }
                Glide.with(context).asBitmap().override(maxWidth, maxHeight).load(url)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            if (call != null) {
                                call.onCall(resource)
                            }
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            if (call != null) {
                                call.onCall(null)
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        })
        uCrop.start(fragment.requireActivity(), fragment, requestCode)
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private fun buildOptions(): UCrop.Options {
        //        options.setHideBottomControls(!cb_hide.isChecked());
//        options.setFreeStyleCropEnabled(cb_styleCrop.isChecked());
//        options.setShowCropFrame(cb_showCropFrame.isChecked());
//        options.setShowCropGrid(cb_showCropGrid.isChecked());
//        options.setCircleDimmedLayer(cb_crop_circular.isChecked());
//        options.withAspectRatio(aspect_ratio_x, aspect_ratio_y);
//        options.setCropOutputPathDir(getSandboxPath());
//        options.isCropDragSmoothToCenter(false);
//        options.isUseCustomLoaderBitmap(cb_crop_use_bitmap.isChecked());
//        options.isForbidSkipMultipleCrop(false);
//        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//        options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.ps_color_white));
        return UCrop.Options()
    }

}