package com.courseapp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.courseapp.domain.model.Course
import com.courseapp.favorites.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavoritesAdapter(
            onRemove = { viewModel.removeFavorite(it) },
            onCardClick = { openCourseDetail(it) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collectLatest { list ->
                adapter.submitList(list)
            }
        }
    }

    private fun openCourseDetail(item: Course) {
        val bundle = bundleOf(
            "arg_title" to item.title,
            "arg_description" to item.description,
            "arg_price" to item.price,
            "arg_rate" to "${item.rate}",
            "arg_date" to item.publishDate,
            "arg_image_url" to item.imageUrl,
            "arg_is_favorite" to true
        )
        val actionId = resources.getIdentifier("action_favorites_to_detail", "id", requireContext().packageName)
        findNavController().navigate(actionId, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
