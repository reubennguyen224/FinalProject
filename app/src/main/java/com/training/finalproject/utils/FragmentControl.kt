package com.training.finalproject.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.training.finalproject.R

inline fun FragmentManager.transaction(
    func: FragmentTransaction.() -> FragmentTransaction
) {
    beginTransaction().func().setCustomAnimations(R.anim.enter_to_left, R.anim.back_to_left)
        .commit()
}

fun Fragment.replaceFragment(fragment: Fragment, container: Int) {
    parentFragmentManager.transaction { replace(container, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, container: Int) {
    supportFragmentManager.transaction { replace(container, fragment) }
}

fun AppCompatActivity.popBackStackInclusive() {
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

fun Fragment.popBackStackInclusive() {
    if (parentFragmentManager.backStackEntryCount > 0)
        parentFragmentManager.popBackStack(
            parentFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment, container: Int, addToStack: Boolean
) {
    supportFragmentManager.transaction {
        if (addToStack) {
            replace(
                container, fragment, fragment::class.java.simpleName
            ).addToBackStack(fragment::class.java.simpleName)
        } else
            replace(container, fragment, fragment::class.java.simpleName)
    }
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    container: Int,
    addToStack: Boolean,
    callToActivity: Boolean
) {
    if (callToActivity)
        activity?.supportFragmentManager?.transaction {
            if (addToStack) {
                replace(
                    container, fragment, fragment::class.java.simpleName
                ).addToBackStack(fragment::class.java.simpleName)
            } else
                replace(container, fragment, fragment::class.java.simpleName)
        }
    else replaceFragment(fragment, container, addToStack)
}

fun Fragment.replaceFragment(
    fragment: Fragment, container: Int, addToStack: Boolean
) {
    parentFragmentManager.transaction {
        if (addToStack) {
            replace(
                container, fragment, fragment::class.java.simpleName
            ).addToBackStack(fragment::class.java.simpleName)
        } else
            replace(container, fragment, fragment::class.java.simpleName)
    }
}

fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fm = supportFragmentManager
    var tag = ""
    if (fm.backStackEntryCount > 0) {
        tag = fm.getBackStackEntryAt(fm.backStackEntryCount - 1).javaClass.simpleName
    }
    return fm.findFragmentByTag(tag)
}

fun Fragment.getCurrentFragment(): Fragment? {
    val fm = parentFragmentManager
    var tag = ""
    if (fm.backStackEntryCount > 0) {
        tag = fm.getBackStackEntryAt(fm.backStackEntryCount - 1).javaClass.simpleName
    }
    return fm.findFragmentByTag(tag)
}

fun Fragment.findFragment(tag: String): Fragment? {
    val fm = parentFragmentManager
    return fm.findFragmentByTag(tag)
}

