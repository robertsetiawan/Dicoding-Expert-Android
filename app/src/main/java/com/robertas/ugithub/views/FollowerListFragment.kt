package com.robertas.ugithub.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.R
import com.robertas.ugithub.adapters.UserListAdapter
import com.robertas.ugithub.databinding.FragmentFollowerListBinding
import com.robertas.ugithub.interfaces.IOnItemClickListener
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.UserViewModel


class FollowerListFragment : Fragment(), IOnItemClickListener<User> {

    private lateinit var binding: FragmentFollowerListBinding

    private val userViewModel by activityViewModels<UserViewModel>()

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
    }

    private fun setupFollowerList() {
        val userListAdapter = UserListAdapter()

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

        userViewModel.requestGetFollowerList.observe(viewLifecycleOwner, followerListObserver)
    }

    override fun onClick(obj: User) {}
}