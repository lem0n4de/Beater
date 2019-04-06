package com.lem0n.beater.client

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.R
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionFailed
import com.lem0n.common.EventBus.onConnectionSuccessful
import com.lem0n.common.EventBus.onRetryConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.home_page_fragment.*
import org.koin.android.ext.android.inject
import timber.log.Timber

@SuppressLint("CheckResult")
class HomePageFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance() = HomePageFragment()
    }

    private val viewModel : HomePageViewModel by inject()
    private val bus : IEventBus by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retry_connection.setOnClickListener {
            bus.publish(onRetryConnection())
        }
        Timber.d("Set on click listener for retry button.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")


        // Events
        val conFailDis = bus.listen(onConnectionFailed::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {}
            .subscribe {
                Timber.d("Connection failed.")
                connection_status.text = getString(R.string.on_connection_failed)
            }
        val conSuccessDis = bus.listen(onConnectionSuccessful::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {}
            .subscribe {
                Timber.d("Connection successfull.")
                connection_status.text = getString(R.string.on_connection_successful)
            }
        compositeDisposable.addAll(conFailDis, conSuccessDis)
    }

    override fun onResume() {
        super.onResume()
        val ac = activity as AppCompatActivity
        ac.supportActionBar!!.title = getString(R.string.home_page_title)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
