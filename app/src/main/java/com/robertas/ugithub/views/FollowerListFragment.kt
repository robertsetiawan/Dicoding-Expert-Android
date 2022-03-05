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
import com.robertas.ugithub.interfaces.IOnItemClickListener
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.FollowerListViewModel


private const val ARG_PARAM = "username"

class FollowerListFragment : Fragment(), IOnItemClickListener<User> {

    private lateinit var binding: FragmentFollowerListBinding

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
        // Inflate the layout for this fragment
        binding = FragmentFollowerListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFollowerList()

        loadFollowerList()
    }

    private fun loadFollowerList() {
        username?.also { followerViewModel.getFollowerList(it) }
    }

    private fun setupFollowerList() {
        val userListAdapter = UserListAdapter(isTouchableCard = false)

        userListAdapter.onItemClickListener = this

        binding.userList.adapter = userListAdapter

        val followerListObserver: Observer<NetworkResult<List<User>>> = Observer { result ->
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

    override fun onClick(obj: User) {}

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