package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException

abstract class BaseFragment<out T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding
        get() = _binding ?: throw IllegalStateException(
            "binding is only valid between onCreateView and onDestroyView"
        )

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerLiveData()
        initView()
        initData()
        initEvent()
    }

    open fun observerLiveData() {}
    open fun initView() {}
    open fun initData() {}
    open fun initEvent() {}

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}