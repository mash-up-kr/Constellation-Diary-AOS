package com.mashup.telltostar.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.util.ConstellationUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_horoscope.*
import timber.log.Timber


class HoroscopeDialog : DialogFragment() {

    private val horoscopeRepository = Injection.provideHoroscopeRepo()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.dialog_horoscope, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            val horoscopeId = getInt(ARGUMENT_HOROSCOPE_ID, -1)
            if (horoscopeId > 0) {
                loadHoroscope(horoscopeId)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private fun loadHoroscope(horoscopeId: Int) {
        horoscopeRepository.get(horoscopeId)
            .subscribe({

                ivHoroscopeIcon.setImageResource(
                    ConstellationUtil.getIconBlack(
                        requireContext().resources,
                        it.constellation
                    )
                )
                tvHoroscopeDate.text =
                    ConstellationUtil.getDate(requireContext().resources, it.constellation)

                tvHoroscopeName.text = it.constellation
                tvHoroscopeContents.text = it.content
                tvHoroscopeContentsDate.text = it.date
            }) {
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }

    }

    companion object {

        const val ARGUMENT_HOROSCOPE_ID = "horoscope_id"

        fun newInstance(horoscopeId: Int) = HoroscopeDialog().apply {
            arguments = bundleOf(Pair(ARGUMENT_HOROSCOPE_ID, horoscopeId))
        }
    }
}