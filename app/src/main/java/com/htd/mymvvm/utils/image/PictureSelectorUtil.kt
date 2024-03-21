package com.htd.mymvvm.utils.image

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import com.htd.mymvvm.R
import com.luck.picture.lib.app.PictureAppMaster
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.MediaUtils

object PictureSelectorUtil {

    val TAG = "PictureSelectorManager"

    fun getImage(context: Context, callBack: OnResultCallbackListener<LocalMedia>) {
        var imgs = ArrayList<LocalMedia>()
//        var  selectorStyle= PictureSelectorStyle();
//        val whiteTitleBarStyle = TitleBarStyle()
//        whiteTitleBarStyle.titleBackgroundColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_white)
//        whiteTitleBarStyle.titleDrawableRightResource = R.drawable.ic_orange_arrow_down
//        whiteTitleBarStyle.titleLeftBackResource = R.drawable.ps_ic_black_back
//        whiteTitleBarStyle.titleTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_black)
//        whiteTitleBarStyle.titleCancelTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_53575e)
//
//        val whiteBottomNavBarStyle = BottomNavBarStyle()
//        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
//        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_53575e)
//
//        whiteBottomNavBarStyle.bottomPreviewNormalTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_9b)
//        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_fa632d)
//        whiteBottomNavBarStyle.isCompleteCountTips = false
//        whiteBottomNavBarStyle.bottomEditorTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_53575e)
//        whiteBottomNavBarStyle.bottomOriginalTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_53575e)
//
//        val selectMainStyle = SelectMainStyle()
//        selectMainStyle.statusBarColor = ContextCompat.getColor(context!!, R.color.ps_color_white)
//        selectMainStyle.isDarkStatusBarBlack = true
//        selectMainStyle.selectNormalTextColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_9b)
//        selectMainStyle.selectTextColor = ContextCompat.getColor(context!!, R.color.ps_color_fa632d)
//        selectMainStyle.selectText = context.getString(R.string.ps_done_front_num)
//        selectMainStyle.mainListBackgroundColor =
//            ContextCompat.getColor(context!!, R.color.ps_color_white)
//
//        selectMainStyle.previewSelectBackground=R.drawable.picture_checkbox_num_selector
//        selectorStyle.titleBarStyle = whiteTitleBarStyle
//        selectorStyle.bottomBarStyle = whiteBottomNavBarStyle
//        selectorStyle.selectMainStyle = selectMainStyle

        // 进入相册
        var model = PictureSelector.create(context)
            .openGallery(SelectMimeType.ofImage())
//            .setSelectorUIStyle(selectorStyle)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCropEngine(ImageCropEngine(context))
//            .setCompressEngine(getCompressEngine())
//            .setSandboxFileEngine(com.luck.pictureselector.MeSandboxFileEngine())
//            .setCameraInterceptListener(getCustomCameraEvent())
//            .setSelectLimitTipsListener(com.luck.pictureselector.MeOnSelectLimitTipsListener())
//            .setEditMediaInterceptListener(getCustomEditMediaEvent()) //.setExtendLoaderEngine(getExtendLoaderEngine())
//            .setInjectLayoutResourceListener(getInjectLayoutResource())
//            .setSelectionMode(if (cb_choose_mode.isChecked()) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
//            .setLanguage(language)
//            .setOutputCameraDir(if (chooseMode == SelectMimeType.ofAudio()) getSandboxAudioOutputPath() else getSandboxCameraOutputPath())
//            .setOutputAudioDir(if (chooseMode == SelectMimeType.ofAudio()) getSandboxAudioOutputPath() else getSandboxCameraOutputPath())
//            .setQuerySandboxDir(if (chooseMode == SelectMimeType.ofAudio()) getSandboxAudioOutputPath() else getSandboxCameraOutputPath())
//            .isDisplayTimeAxis(cb_time_axis.isChecked())
//            .isOnlyObtainSandboxDir(cb_only_dir.isChecked())
//            .isPageStrategy(cbPage.isChecked())
//            .isOriginalControl(cb_original.isChecked())
//            .isDisplayCamera(cb_isCamera.isChecked())
//            .isOpenClickSound(cb_voice.isChecked()) //.setOutputCameraImageFileName("luck.jpeg")
            //.setOutputCameraVideoFileName("luck.mp4")
//            .isWithSelectVideoImage(cb_WithImageVideo.isChecked())
//            .isPreviewFullScreenMode(cb_preview_full.isChecked())
//            .isPreviewZoomEffect(cb_preview_scale.isChecked())
//            .isPreviewImage(cb_preview_img.isChecked())
//            .isPreviewVideo(cb_preview_video.isChecked())
//            .isPreviewAudio(cb_preview_audio.isChecked()) //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
//            .isMaxSelectEnabledMask(cbEnabledMask.isChecked())
//            .isDirectReturnSingle(cb_single_back.isChecked())
            .setMaxSelectNum(1)
//            .setRecyclerAnimationMode(animationMode)
            .isGif(true)
            .setSelectedData(imgs)
        model.forResult(callBack)
    }


    /**
     * 处理选择结果
     *
     * @param result
     */
    private fun analyticalSelectResults(result: java.util.ArrayList<LocalMedia>) {
        for (media in result) {
            if (media.width == 0 || media.height == 0) {
                if (PictureMimeType.isHasImage(media.mimeType)) {
                    val imageExtraInfo = MediaUtils.getImageSize(media.path)
                    media.width = imageExtraInfo.width
                    media.height = imageExtraInfo.height
                } else if (PictureMimeType.isHasVideo(media.mimeType)) {
                    val videoExtraInfo = MediaUtils.getVideoSize(
                        PictureAppMaster.getInstance().appContext,
                        media.path
                    )
                    media.width = videoExtraInfo.width
                    media.height = videoExtraInfo.height
                }
            }
            Log.i(TAG, "文件名: " + media.fileName)
            Log.i(TAG, "是否压缩:" + media.isCompressed)
            Log.i(TAG, "压缩:" + media.compressPath)
            Log.i(TAG, "原图:" + media.path)
            Log.i(TAG, "绝对路径:" + media.realPath)
            Log.i(TAG, "是否裁剪:" + media.isCut)
            Log.i(TAG, "裁剪:" + media.cutPath)
            Log.i(TAG, "是否开启原图:" + media.isOriginal)
            Log.i(TAG, "原图路径:" + media.originalPath)
            Log.i(TAG, "沙盒路径:" + media.sandboxPath)
            Log.i(TAG, "原始宽高: " + media.width + "x" + media.height)
            Log.i(TAG, "裁剪宽高: " + media.cropImageWidth + "x" + media.cropImageHeight)
            Log.i(TAG, "文件大小: " + media.size)
        }

    }
}