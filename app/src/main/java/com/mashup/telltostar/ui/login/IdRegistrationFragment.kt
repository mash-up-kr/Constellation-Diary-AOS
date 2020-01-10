package com.mashup.telltostar.ui.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer

import com.mashup.telltostar.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_id_registration.view.*
import java.util.concurrent.TimeUnit

class IdRegistrationFragment : Fragment() {

    private lateinit var mRootView: View
    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_id_registration, container, false)

        mRootView = rootView

        setListeners()
        setViewModelObserver()

        return rootView
    }

    private fun setListeners() {
        mRootView.passwordVisibilityImageView.setOnClickListener {
            performVisibilityImageViewClick()
        }
        mRootView.passwordInvisibilityImageView.setOnClickListener {
            performInVisibilityImageViewClick()
        }
        mRootView.passwordConfirmVisibilityImageView.setOnClickListener {
            performConfirmVisibilityImageViewClick()
        }
        mRootView.passwordConfirmInvisibilityImageView.setOnClickListener {
            performConfirmInvisibilityImageViewClick()
        }
    }

    private fun performVisibilityImageViewClick() {
        IdRegistrationViewModel.requestPasswordInvisible()
        mRootView.passwordVisibilityImageView.visibility = View.GONE
        mRootView.passwordInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performInVisibilityImageViewClick() {
        IdRegistrationViewModel.requestPasswordVisible()
        mRootView.passwordVisibilityImageView.visibility = View.VISIBLE
        mRootView.passwordInvisibilityImageView.visibility = View.GONE
        moveEditTextCursorEnd(mRootView.passwordEditText)
    }

    private fun performConfirmVisibilityImageViewClick() {
        IdRegistrationViewModel.requestConfirmPasswordInvisible()
        mRootView.passwordConfirmVisibilityImageView.visibility = View.GONE
        mRootView.passwordConfirmInvisibilityImageView.visibility = View.VISIBLE
        moveEditTextCursorEnd(mRootView.passwordConfirmEditText)
    }

    private fun performConfirmInvisibilityImageViewClick() {
        IdRegistrationViewModel.requestConfirmPasswordVisible()
        mRootView.passwordConfirmVisibilityImageView.visibility = View.VISIBLE
        mRootView.passwordConfirmInvisibilityImageView.visibility = View.GONE
        moveEditTextCursorEnd(mRootView.passwordConfirmEditText)
    }

    private fun moveEditTextCursorEnd(editText: EditText) {
        mCompositeDisposable.add(
            Single.just(1)
                .subscribeOn(Schedulers.io())
                .delay(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    editText.setSelection(editText.text.length)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun setViewModelObserver() {
        IdRegistrationViewModel.isPasswordVisible.observe(this, Observer {
            mRootView.passwordEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
        })
        IdRegistrationViewModel.isConfirmPasswordVisible.observe(this, Observer {
            mRootView.passwordConfirmEditText.transformationMethod =
                if (it) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
        })
    }

    override fun onDestroyView() {
        mCompositeDisposable.clear()

        super.onDestroyView()
    }
}
