package com.sejin.hyucafeteria

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.adapters.MealAdapter
import com.sejin.hyucafeteria.data.CafeteriaIdName
import com.sejin.hyucafeteria.data.PageInfo
import com.sejin.hyucafeteria.databinding.FragmentMainBinding
import com.sejin.hyucafeteria.utilities.*

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var rcv: RecyclerView
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = mainViewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        radioGroup = binding.cafeteriaRadioGroup
        rcv = binding.rcv

        initViews()
        initObservers()
    }

    private fun initViews() {
        radioGroup.setOnCheckedChangeListener { group, checkedId -> // 라디오버튼 체크시 현재 식당정보를 업데이트합니다.
            val radioBtn: CafeteriaRadioButton = radioGroup.findViewById(checkedId)
            updateCurrentPage(radioBtn.idName)
        }
    }

    private fun updateCurrentPage(idName: CafeteriaIdName) {
        mainViewModel.currentCafeteriaIdName.value = idName
        mainViewModel.setCurrentPageInfo()
    }

    private fun initObservers() {
        observeCafeteriaIdNames()
        observeCurrentPageInfo()
        observeCafeteriaLoadingError()
    }

    private fun observeCafeteriaIdNames() {
        mainViewModel.cafeteriaIdNames.observe(viewLifecycleOwner) {
            it.forEach { idName -> addToRadioButtonToGroup(idName) }
            if (radioGroup.isNotEmpty() && radioGroup.checkedRadioButtonId == -1)
                checkRadioButton(0)
        }
    }

    private fun observeCurrentPageInfo() {
        mainViewModel.currentPageInfo.observe(viewLifecycleOwner) { pageInfo ->
            rcv.adapter = MealAdapter().apply {
                submitList(pageInfo.mealList)
            }
            logPageInfo(pageInfo)
        }
    }

    private fun observeCafeteriaLoadingError() {
        mainViewModel.errorEvent.observe(viewLifecycleOwner) { message ->
            requireContext().toast(message)
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().finish()
            }, 3000)

        }
    }

    private fun logPageInfo(pageInfo: PageInfo) {
        var cafeteriaInfo = pageInfo.cafeteria
        var urlDate = pageInfo.urlDate
        var mealList = pageInfo.mealList

        logger("$cafeteriaInfo, $urlDate")
        for (meal in mealList) {
            logger("\n" + meal.toString())
        }
    }

    private fun checkRadioButton(index: Int) {
        radioGroup.check(radioGroup.getChildAt(0).id)
    }

    fun onBeforeDateClicked() {
        mainViewModel.updateDateToBefore()
    }

    fun onNextDateClicked() {
        mainViewModel.updateDateToNext()
    }

    private fun addToRadioButtonToGroup(idName: CafeteriaIdName) {
        val radioButton = CafeteriaRadioButton(requireContext(), idName)
        radioGroup.addView(radioButton)
        radioButton.setMargins(left = 20)
    }
}