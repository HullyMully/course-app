package com.courseapp.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.courseapp.account.databinding.FragmentAccountBinding
import com.courseapp.domain.model.Course
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModel()
    private lateinit var adapter: ProfileCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProfileCourseAdapter { openCourseDetail(it) }
        binding.coursesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.coursesRecycler.adapter = adapter

        binding.supportRow.setOnClickListener {
            Toast.makeText(requireContext(), R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
        binding.settingsRow.setOnClickListener {
            Toast.makeText(requireContext(), R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
        binding.logoutRow.setOnClickListener {
            Toast.makeText(requireContext(), R.string.logged_out, Toast.LENGTH_SHORT).show()
            val loginClass = Class.forName("com.courseapp.login.LoginActivity")
            val intent = Intent(requireContext(), loginClass)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courses.collectLatest { list ->
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
        val actionId = resources.getIdentifier("action_account_to_detail", "id", requireContext().packageName)
        findNavController().navigate(actionId, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
