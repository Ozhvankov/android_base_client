package com.test.test.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public static void attachFragment(AppCompatActivity context, Fragment fragment, int viewID) {
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        ft.add(viewID, fragment).commit();
    }

    public static void openNewFragment(AppCompatActivity context, Fragment fragment,
            boolean canAddBackStrace, int viewID) {
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        ft.replace(viewID, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (canAddBackStrace) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public static void openNewFragment(AppCompatActivity context,
            Fragment fragment, Bundle args, boolean canAddBackStrace, int viewID) {
        fragment.setArguments(args);
        openNewFragment(context, fragment, canAddBackStrace, viewID);
    }

    public static void popBackStack(AppCompatActivity context, int numBackStack) {
        FragmentManager manager = context.getSupportFragmentManager();
        int fragCount = manager.getBackStackEntryCount();
        for (int i = 0; i < fragCount - numBackStack; i++) {
            manager.popBackStack();
        }
    }
}
