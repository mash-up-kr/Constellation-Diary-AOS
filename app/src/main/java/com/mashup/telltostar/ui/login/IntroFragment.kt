package com.mashup.telltostar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.view.*

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initToolbarTitle()

        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    private fun initToolbarTitle() {
        (activity as LoginActivity).toolbarLogin.toolbarTextView.text = getString(R.string.title_intro)
    }
}
