package com.sejin.hyucafeteria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.CafeteriaIdName
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
        // Inflate the layout for this fragment
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

        initObservers()
    }

    private fun initObservers() {
        mainViewModel.apply {
            cafeteriaIdNames.observe(viewLifecycleOwner) {
                it.forEach { idName -> addToRadioButtonToGroup(idName) }
                setRadioGroup()

            }
            currentCafeteriaIdName.observe(viewLifecycleOwner) {
                mainViewModel.setCurrentPageInfo()
            }
            currentPageInfo.observe(viewLifecycleOwner) {
                val text = it.mealList.map { meal -> meal.getInfo() }.toString()
                requireContext().toast(text)
            }
        }

    }

    private fun setRadioGroup() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioBtn: CafeteriaRadioButton = radioGroup.findViewById(checkedId)
            mainViewModel.currentCafeteriaIdName.value = radioBtn.idName
        }
        if (radioGroup.checkedRadioButtonId == -1) checkRadioButton(0)
    }

    private fun checkRadioButton(index: Int) {
        radioGroup.check(radioGroup.getChildAt(0).id)
        // info 바꾸라고 설정
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