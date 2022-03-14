package com.robertas.ugithub.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.R
import com.robertas.ugithub.abstractions.IOnItemClickListener
import com.robertas.ugithub.adapters.UserListAdapter
import com.robertas.ugithub.databinding.FragmentFavouriteListBinding
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.FavouriteListViewModel
import com.robertas.ugithub.viewmodels.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteListFragment : Fragment() {
    private var _binding: FragmentFavouriteListBinding? = null

    private val binding get() = _binding!!

    private val favouriteListViewModel by viewModels<FavouriteListViewModel>()

    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupToolbarNavigation()

        setupNavigation()

        setupFavouriteList()
    }

    private fun setupNavigation() {

        val navigationObserver: Observer<NetworkResult<UserDomain>> = Observer { result ->
            when (result) {

                is NetworkResult.Loading -> {}

                is NetworkResult.Loaded -> {

                    result.data?.let {
                        val detailUserDirection =
                            FavouriteListFragmentDirections.actionFavouriteListFragmentToUserDetailFragment(
                                it
                            )

                        navController.navigate(detailUserDirection)
                    }

                    binding.progressCircular.visibility = View.GONE

                    userDetailViewModel.doneNavigatingToDetailFragment()
                }

                is NetworkResult.Error -> {
                    result.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }

                    binding.progressCircular.visibility = View.GONE

                    userDetailViewModel.doneNavigatingToDetailFragment()
                }
            }

        }

        userDetailViewModel.requestGetDetailUser.observe(viewLifecycleOwner, navigationObserver)
    }

    private fun setupToolbarNavigation() {

        binding.toolbarFragment.apply {
            inflateMenu(R.menu.favourite_menu)

            setupWithNavController(navController)

            setNavigationOnClickListener {
                navController.navigateUp()
            }

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        val alertBuilder = AlertDialog.Builder(requireContext())

                        alertBuilder.apply {
                            setTitle(getString(R.string.delete_confirmation))

                            setMessage(getString(R.string.delete_all_favourites))

                            setPositiveButton(getString(R.string.yes)) { _, _ ->

                                favouriteListViewModel.deleteFavouriteList()

                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.favourites_cleared),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                            }
                            setNegativeButton(getString(R.string.cancel)) { _, _ -> }

                            show()
                        }

                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupFavouriteList() {
        val userListAdapter = UserListAdapter(isTouchableCard = true)

        userListAdapter.onItemClickListener = object : IOnItemClickListener<UserDomain> {
            override fun onClick(obj: UserDomain) {
                binding.progressCircular.visibility = View.VISIBLE

                userDetailViewModel.getDetailUser(obj.login)
            }
        }

        binding.userList.adapter = userListAdapter

        val favouriteListObserver: Observer<List<UserDomain>> =
            Observer { list ->

                if (list.isEmpty()) {

                    binding.apply {
                        emptyLayout.visibility = View.VISIBLE

                        userList.visibility = View.GONE
                    }

                } else {
                    userListAdapter.submitList(list)

                    binding.apply {
                        emptyLayout.visibility = View.GONE

                        userList.visibility = View.VISIBLE
                    }
                }

            }

        favouriteListViewModel.favouriteList.observe(viewLifecycleOwner, favouriteListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}