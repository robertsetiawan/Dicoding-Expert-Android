package com.robertas.ugithub.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.adapters.UserListAdapter
import com.robertas.ugithub.databinding.FragmentFollowerListBinding
import com.robertas.ugithub.abstractions.IOnItemClickListener
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.FollowerListViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM = "username"

@AndroidEntryPoint
class FollowerListFragment : Fragment(), IOnItemClickListener<UserDomain> {

    private var _binding: FragmentFollowerListBinding? = null

    private val binding get() = _binding!!

    private val followerViewModel by viewModels<FollowerListViewModel>()

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
        _binding = FragmentFollowerListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFollowerList()

        loadFollowerList()
    }

    private fun loadFollowerList() {
        username?.let { followerViewModel.getFollowerList(it) }
    }

    private fun setupFollowerList() {
        val userListAdapter = UserListAdapter(isTouchableCard = false)

        userListAdapter.onItemClickListener = this

        binding.userList.adapter = userListAdapter

        val followerListObserver: Observer<NetworkResult<List<UserDomain>>> = Observer { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE

                    binding.userList.visibility = View.GONE
                }

                is NetworkResult.Loaded -> {
                    result.data?.let { userListAdapter.submitList(it) }

                    binding.progressCircular.visibility = View.GONE

                    binding.userList.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    result.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                    binding.progressCircular.visibility = View.GONE

                    binding.userList.visibility = View.VISIBLE
                }
            }
        }

        followerViewModel.requestGetFollowerList.observe(viewLifecycleOwner, followerListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onClick(obj: UserDomain) {}

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            FollowerListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, username)
                }
            }
    }
}