package com.robertas.ugithub.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.core.data.NetworkResult
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.ui.IOnItemClickListener
import com.robertas.ugithub.core.ui.UserListAdapter
import com.robertas.ugithub.databinding.FragmentFollowingListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FollowingListFragment : Fragment(), IOnItemClickListener<UserDomain> {
    private var _binding: FragmentFollowingListBinding? = null
    private val binding get() = _binding!!

    private val followingViewModel by viewModels<FollowingListViewModel>()

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFollowingList()
        loadFollowingList()
    }

    private fun loadFollowingList() {
        username?.let { followingViewModel.getFollowingList(it) }
    }

    private fun setupFollowingList() {
        val userListAdapter = UserListAdapter(isTouchableCard = false)
        userListAdapter.onItemClickListener = this
        binding.userList.adapter = userListAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            followingViewModel.getFollowingList(username.orEmpty()).collect { result ->
                when(result) {
                    is NetworkResult.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.userList.visibility = View.GONE
                    }

                    is NetworkResult.Success -> {
                        result.data.let { userListAdapter.submitList(it) }

                        binding.progressCircular.visibility = View.GONE
                        binding.userList.visibility = View.VISIBLE
                    }

                    is NetworkResult.Error -> {
                        result.message.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                        }
                        binding.progressCircular.visibility = View.GONE
                        binding.userList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(obj: UserDomain) {}

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            FollowingListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, username)
                }
            }

        private const val ARG_PARAM = "username"
    }
}