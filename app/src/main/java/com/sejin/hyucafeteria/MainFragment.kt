package com.sejin.hyucafeteria

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.view.isGone
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.sejin.hyucafeteria.adapters.MealAdapter
import com.sejin.hyucafeteria.data.CafeteriaIdName
import com.sejin.hyucafeteria.data.PageInfo
import com.sejin.hyucafeteria.data.defaultPageInfo
import com.sejin.hyucafeteria.databinding.FragmentMainBinding
import com.sejin.hyucafeteria.utilities.*

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var rcv: RecyclerView
    private lateinit var lottieAnimation: LottieAnimationView
    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    private var alreadyCheckedOnInit: Boolean = false

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
        lottieAnimation = binding.aniLoad

        initViews()
        initObservers()
    }

    private fun initViews() {
        radioGroup.setOnCheckedChangeListener { group, checkedId -> // 라디오버튼 체크시 현재 식당정보를 업데이트합니다.
            val radioBtn: CafeteriaRadioButton = radioGroup.findViewById(checkedId)
            mainViewModel.currentCafeteriaIndex = radioGroup.indexOfChild(radioBtn)
            if (alreadyCheckedOnInit) {
                updateCurrentPage(radioBtn.idName) // 처음에는 작동 안하게 해야함. ㅁㄴㅇㄹ
                pageInfoAnimationStart()
            }
        }
    }

    private fun updateCurrentPage(idName: CafeteriaIdName) {
        rcv.adapter = null
        mainViewModel.currentCafeteriaIdName.value = idName
        mainViewModel.updateCurrentPageInfo()
    }

    private fun initObservers() {
        observeCafeteriaIdNames()
        observeCurrentPageInfo()
        observeCafeteriaLoadingError()
        observeNetworkError()
    }

    private fun observeCafeteriaIdNames() { // 최초 앱 진입하거나 상태변경시 실행
        mainViewModel.idNameList.observe(viewLifecycleOwner) {
            it.forEach { idName -> addToRadioButtonToGroup(idName) }
            if (needRadioButtonInit()) {
                radioGroup.check(radioGroup.getChildAt(0).id)
                alreadyCheckedOnInit = true
            } else {
                radioGroup.check(radioGroup.getChildAt(mainViewModel.currentCafeteriaIndex).id)
            }
        }
    }

    private fun needRadioButtonInit() =
        radioGroup.isNotEmpty() && radioGroup.checkedRadioButtonId == -1 && mainViewModel.currentCafeteriaIndex == -1

    private fun observeCurrentPageInfo() {
        mainViewModel.currentPageInfo.observe(viewLifecycleOwner) { pageInfo ->
            var list = pageInfo.mealList
            if (list.isEmpty()) {
                onPageInfoEmpty()
                return@observe
            }

            if (pageInfo.mealList.last().title.contains("공통"))
                list = pageInfo.mealList.toMutableList().apply { removeLast() }

            rcv.adapter =
                MealAdapter(mainViewModel.currentPageInfo.value ?: defaultPageInfo).apply {
                    submitList(list)
                }
            pageInfoAnimationEnd()
        }
    }

    private fun observeCafeteriaLoadingError() {
        mainViewModel.repeated404ErrorEvent.observe(viewLifecycleOwner) { message ->

            requireContext().toast(message)
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().finish()
            }, 2000)
        }
    }

    private fun observeNetworkError() {
        mainViewModel.isNetWorkError.observe(viewLifecycleOwner) { message ->
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

    fun onBeforeDateClicked() {
        mainViewModel.updateDateToBefore()
        rcv.adapter = null
        pageInfoAnimationStart()
    }

    fun onNextDateClicked() {
        mainViewModel.updateDateToNext()
        rcv.adapter = null
        pageInfoAnimationStart()
    }

    private fun addToRadioButtonToGroup(idName: CafeteriaIdName) {
        val radioButton = CafeteriaRadioButton(requireContext(), idName)
        radioGroup.addView(radioButton)
        radioButton.setMargins(dp(requireContext(), 16))
    }

    private fun onPageInfoEmpty() {
        binding.aniLoad.pauseAnimation()
        binding.aniLoad.isGone = true
        binding.textNone.isVisible = true
    }

    private fun pageInfoAnimationEnd() {
        binding.aniLoad.pauseAnimation()
        binding.aniLoad.isGone = true
    }

    private fun pageInfoAnimationStart() {
        binding.aniLoad.playAnimation()
        binding.aniLoad.isVisible = true
        binding.textNone.isGone = true
    }
}