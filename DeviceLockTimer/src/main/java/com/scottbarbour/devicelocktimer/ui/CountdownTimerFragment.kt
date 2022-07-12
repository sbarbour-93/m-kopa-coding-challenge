package com.scottbarbour.devicelocktimer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.scottbarbour.devicelocktimer.R
import com.scottbarbour.devicelocktimer.di.timerModules
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin

class CountdownTimerFragment : Fragment() {

    private val viewModel: CountdownTimerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_countdown_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.timeRemainingUntilDeviceLocks.observe(viewLifecycleOwner) {
            activity!!.findViewById<TextView>(R.id.message).text = it
        }

        viewModel.setupTimer()
    }
}