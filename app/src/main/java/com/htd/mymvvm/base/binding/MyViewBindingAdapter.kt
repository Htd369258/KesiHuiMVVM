package com.htd.mymvvm.base.binding

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.htd.mymvvm.utils.ScreenUtil.dp2px
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.util.concurrent.TimeUnit

object MyViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(
        value = ["url", "placeholderRes", "topLeftRadius", "topRightRadius", "bottomLeftRadius", "bottomRightRadius", "asCircle", "borderSize", "borderColor", "isBlur", "blur", "radiusAll"],
        requireAll = false
    )
    fun setImageUri(
        imageView: ImageView,
        url: String?,
        placeholderRes: Int,
        topLeftRadius: Int,
        topRightRadius: Int,
        bottomLeftRadius: Int,
        bottomRightRadius: Int,
        asCircle: Boolean,
        borderSize: Int,
        @ColorInt borderColor: Int,
        isBlur: Boolean,
        blur: Int = 25,
        radiusAll: Int
    ) {
        if (!TextUtils.isEmpty(url)) {
            val options = RequestOptions()
            val mation = when {
                asCircle -> {
                    if (isBlur)
                        MultiTransformation(
                            BlurTransformation(blur),
                            CenterCrop(),
                            CropCircleWithBorderTransformation(
                                dp2px(
                                    imageView.context,
                                    borderSize.toFloat()
                                ), borderColor
                            )
                        )
                    else MultiTransformation(
                        CenterCrop(),
                        CropCircleWithBorderTransformation(
                            dp2px(
                                imageView.context,
                                borderSize.toFloat()
                            ), borderColor
                        )
                    )
                }

                radiusAll != 0 -> {
                    if (isBlur)
                        MultiTransformation(
                            BlurTransformation(blur),
                            CenterCrop(),
                            RoundedCornersTransformation(
                                dp2px(imageView.context, radiusAll.toFloat()),
                                0,
                                RoundedCornersTransformation.CornerType.ALL
                            )
                        )
                    else
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCornersTransformation(
                                dp2px(imageView.context, radiusAll.toFloat()),
                                0,
                                RoundedCornersTransformation.CornerType.ALL
                            )
                        )
                }

                else -> {
                    if (isBlur)
                        MultiTransformation(
                            BlurTransformation(blur),
                            CenterCrop(),
                            RoundedCornersTransformation(
                                dp2px(
                                    imageView.context,
                                    topLeftRadius.toFloat()
                                ), 0, RoundedCornersTransformation.CornerType.TOP_LEFT
                            ),
                            RoundedCornersTransformation(
                                dp2px(imageView.context, topRightRadius.toFloat()),
                                0, RoundedCornersTransformation.CornerType.TOP_RIGHT
                            ),
                            RoundedCornersTransformation(
                                dp2px(imageView.context, bottomLeftRadius.toFloat()),
                                0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT
                            ),
                            RoundedCornersTransformation(
                                dp2px(imageView.context, bottomRightRadius.toFloat()),
                                0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                            ),
                        )
                    else MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(
                            dp2px(
                                imageView.context,
                                topLeftRadius.toFloat()
                            ), 0, RoundedCornersTransformation.CornerType.TOP_LEFT
                        ),
                        RoundedCornersTransformation(
                            dp2px(imageView.context, topRightRadius.toFloat()),
                            0, RoundedCornersTransformation.CornerType.TOP_RIGHT
                        ),
                        RoundedCornersTransformation(
                            dp2px(imageView.context, bottomLeftRadius.toFloat()),
                            0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT
                        ),
                        RoundedCornersTransformation(
                            dp2px(imageView.context, bottomRightRadius.toFloat()),
                            0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                        )
                    )
                }
            }
            options.transform(mation)
                .placeholder(placeholderRes)
            Glide.with(imageView.context)
                .load(url)
                .apply(options)
                .into(imageView)
        }
    }

    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @BindingAdapter("android:drawableRight")
    fun setDrawableRight(view: TextView, resId: Int) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            view.resources.getDrawable(resId),
            null
        )
    }

    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(view: TextView, resId: Int) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
            view.resources.getDrawable(resId),
            null,
            null,
            null
        )
    }

    @BindingAdapter("android:drawableTop")
    fun setDrawableTop(view: TextView, resId: Int) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            view.resources.getDrawable(resId),
            null,
            null
        )
    }

    @BindingAdapter("android:drawableBottom")
    fun setDrawableBottom(view: TextView, resId: Int) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            null,
            view.resources.getDrawable(resId)
        )
    }

    //下拉刷新命令
    @JvmStatic
    @BindingAdapter("onRefreshCommand")
    fun onRefreshCommand(
        swipeRefreshLayout: SmartRefreshLayout,
        onRefreshCommand: BindingCommand<Boolean?>?
    ) {
        swipeRefreshLayout.setOnRefreshListener { onRefreshCommand?.execute() }
    }

    //上拉加载
    @JvmStatic
    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(
        swipeRefreshLayout: SmartRefreshLayout,
        onRefreshCommand: BindingCommand<Boolean?>?
    ) {
        swipeRefreshLayout.setOnLoadMoreListener { onRefreshCommand?.execute() }
    }

    // AdapterView
    @JvmStatic
    @BindingAdapter(value = ["items", "adapter", "layoutManager", "isRefresh"], requireAll = false)
    fun <T, VB : ViewDataBinding> setAdapter(
        recyclerView: RecyclerView,
        items: List<T>?,
        adapter: BaseQuickAdapter<T, BaseDataBindingHolder<VB>>,
        layoutManager: RecyclerView.LayoutManager?,
        isRefresh: Boolean
    ) {
        if (recyclerView.layoutManager == null)
            recyclerView.layoutManager = layoutManager
        if (adapter == null) return
        recyclerView.adapter = adapter
        if (items == null) return
        if (isRefresh) {
            adapter.setList(items)
        } else {
            adapter.addData(items)
        }
    }

    @BindingAdapter("lineManager")
    fun setLineManager(
        recyclerView: RecyclerView,
        lineManagerFactory: LineManagers.LineManagerFactory
    ) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView))
    }

    @BindingAdapter(
        value = ["onScrollChangeCommand", "onScrollStateChangedCommand"],
        requireAll = false
    )
    fun onScrollChangeCommand(
        recyclerView: RecyclerView,
        onScrollChangeCommand: BindingCommand<ScrollDataWrapper?>?,
        onScrollStateChangedCommand: BindingCommand<Int?>?
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var state = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollChangeCommand?.execute(
                    ScrollDataWrapper(
                        dx.toFloat(), dy.toFloat(), state
                    )
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state = newState
                onScrollStateChangedCommand?.execute(newState)
            }
        })
    }


    @BindingAdapter("itemAnimator")
    fun setItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator?) {
        recyclerView.itemAnimator = animator
    }

    class OnScrollListener(onLoadMoreCommand: BindingCommand<Int>) :
        RecyclerView.OnScrollListener() {
        private val methodInvoke = PublishSubject.create<Int>()
        private val onLoadMoreCommand: BindingCommand<Int>?
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.adapter!!.itemCount)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        init {
            this.onLoadMoreCommand = onLoadMoreCommand
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe { integer -> onLoadMoreCommand.execute(integer) }
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)
}