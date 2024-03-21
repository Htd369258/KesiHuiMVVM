package com.htd.mymvvm.base.binding

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * Created by goldze on 2017/6/16.
 */
object LineManagers {
    fun both(): LineManagerFactory {
        return object : LineManagerFactory {
            override fun create(recyclerView: RecyclerView): ItemDecoration {
                return DividerLine(recyclerView.context, DividerLine.LineDrawMode.BOTH)
            }
        }
    }

    fun horizontal(): LineManagerFactory {
        return object : LineManagerFactory {
            override fun create(recyclerView: RecyclerView): ItemDecoration {
                return DividerLine(recyclerView.context, DividerLine.LineDrawMode.HORIZONTAL)
            }
        }
    }

    fun vertical(): LineManagerFactory {
        return object : LineManagerFactory {
            override fun create(recyclerView: RecyclerView): ItemDecoration {
                return DividerLine(recyclerView.context, DividerLine.LineDrawMode.VERTICAL)
            }
        }
    }

    interface LineManagerFactory {
        fun create(recyclerView: RecyclerView): ItemDecoration
    }
}