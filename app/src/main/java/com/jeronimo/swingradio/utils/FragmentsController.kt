package com.jeronimo.swingradio.utils

import android.app.Fragment
import android.app.FragmentManager

/**
 * Created by gleb on 23.12.15.
 */
class FragmentsController {
    private var mFragmentManager: FragmentManager? = null
    private var mContainerID: Int = 0
    private object Holder { val INSTANCE = FragmentsController() }

    fun init(fragmentManager: FragmentManager) {
        mFragmentManager = fragmentManager
    }

    fun setContainer(id: Int) {
        mContainerID = id
    }

    fun addFragmentToContainer(fragment: Fragment, container: Int) {
        val fragmentTransaction = mFragmentManager!!.beginTransaction()
        fragmentTransaction.add(container, fragment, getTagForFragment(fragment))
        fragmentTransaction.addToBackStack(MAIN_STACK)

        fragmentTransaction.commit()

    }

    fun backInStack() {
        mFragmentManager!!.popBackStack(MAIN_STACK, 0)
    }

    companion object {

        val MAIN_STACK = "main"
        val instance : FragmentsController by lazy {Holder.INSTANCE}

        fun getTagForFragment(fragment: Fragment): String {
            return fragment.javaClass.name
        }
    }


}
