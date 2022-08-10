package com.robertas.ugithub.favorite

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.ui.IOnItemClickListener
import com.robertas.ugithub.core.ui.UserListAdapter
import com.robertas.ugithub.di.FavoriteModuleDependency
import com.robertas.ugithub.favorite.databinding.FragmentFavoriteListBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject


class FavoriteListFragment : Fragment() {
    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val favoriteListViewModel by viewModels<FavoriteListViewModel>{ factory }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFavoriteComponent.builder()
            .context(requireContext())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireActivity().applicationContext,
                    FavoriteModuleDependency::class.java
                )
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupToolbarNavigation()
        setupFavouriteList()
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
                                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                                    favoriteListViewModel.deleteFavouriteList()
                                }
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
        val userListAdapter = UserListAdapter(isTouchableCard = false)
        userListAdapter.onItemClickListener = object : IOnItemClickListener<UserDomain> {
            override fun onClick(obj: UserDomain) { return }
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
        favoriteListViewModel.favouriteList.observe(viewLifecycleOwner, favouriteListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}