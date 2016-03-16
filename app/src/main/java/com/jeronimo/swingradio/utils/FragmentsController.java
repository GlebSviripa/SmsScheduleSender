package com.jeronimo.swingradio.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by gleb on 23.12.15.
 */
public class FragmentsController {

    public static final String MAIN_STACK = "main";
    private static  FragmentsController instance;
    private FragmentManager mFragmentManager;
    private int mContainerID;


    public static FragmentsController getInstance()
    {
        if(instance == null)
        {
            instance = new FragmentsController();
        }
        return  instance;
    }

    public void init(FragmentManager fragmentManager)
    {
        mFragmentManager = fragmentManager;
    }

    public void setContainer(int id)
    {
        mContainerID = id;
    }

    public void addFragmentToContainer(Fragment fragment, int container)
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment, getTagForFragment(fragment));
        fragmentTransaction.addToBackStack(MAIN_STACK);

        fragmentTransaction.commit();

    }

    public void backInStack()
    {
        mFragmentManager.popBackStack(MAIN_STACK, 0);
    }

    public static String getTagForFragment(Fragment fragment)
    {
        return fragment.getClass().getName();
    }


}
