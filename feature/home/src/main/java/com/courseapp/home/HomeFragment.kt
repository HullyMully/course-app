package com.courseapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.courseapp.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var adapter: CourseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CourseListAdapter(
            onFavoriteClick = { viewModel.toggleFavorite(it) },
            onCardClick = { openCourseDetail(it) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.sortChip.setOnClickListener { viewModel.toggleSort() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                adapter.submitList(state.courses)
                binding.progress.visibility = if (state.loading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun openCourseDetail(item: CourseItem) {
        val bundle = bundleOf(
            CourseDetailFragment.ARG_TITLE to item.dto.title,
            CourseDetailFragment.ARG_DESCRIPTION to item.dto.text,
            CourseDetailFragment.ARG_PRICE to item.dto.price,
            CourseDetailFragment.ARG_RATE to "${item.dto.rate}",
            CourseDetailFragment.ARG_DATE to item.dto.publishDate,
            CourseDetailFragment.ARG_IMAGE_URL to item.dto.imageUrl,
            CourseDetailFragment.ARG_IS_FAVORITE to item.isFavorite
        )
        findNavController().navigate(
            resId = resources.getIdentifier("action_home_to_detail", "id", requireContext().packageName),
            args = bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
