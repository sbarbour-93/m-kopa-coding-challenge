package com.scottbarbour.devicelocktimer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scottbarbour.devicelocktimer.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountdownTimerFragment : Fragment() {

    private val viewModel: CountdownTimerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_countdown_timer, container, false)
    }

}