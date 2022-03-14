package com.robertas.ugithub.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.robertas.ugithub.R
import com.robertas.ugithub.adapters.UserListAdapter
import com.robertas.ugithub.databinding.FragmentUserListBinding
import com.robertas.ugithub.abstractions.IOnItemClickListener
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.UserDetailViewModel
import com.robertas.ugithub.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentUserListBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val userViewModel by viewModels<UserViewModel>()

    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupNavigation()

        setupUserList()

        setupToolbarSearch()
    }

    private fun setupNavigation() {
        binding.toolbarFragment.setupWithNavController(navController)

        binding.toolbarFragment.setNavigationOnClickListener { navController.navigateUp() }

        val navigationObserver: Observer<NetworkResult<UserDomain>> = Observer { result ->
            when (result) {

                is NetworkResult.Loading -> {}

                is NetworkResult.Loaded -> {

                    result.data?.let {
                        val directionsToDetailUser =
                            UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(it)

                        navController.navigate(directionsToDetailUser)
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

    private fun setupToolbarSearch() {

        binding.toolbarFragment.apply {
            inflateMenu(R.menu.home_menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.setting -> {
                        val settingsDirection =
                            UserListFragmentDirections.actionUserListFragmentToSettingsFragment()

                        navController.navigate(settingsDirection)

                        true
                    }

                    R.id.favorite -> {
                        val favouriteListDirection =
                            UserListFragmentDirections.actionUserListFragmentToFavouriteListFragment()

                        navController.navigate(favouriteListDirection)

                        true
                    }

                    else -> false
                }
            }
        }

        val search = binding.toolbarFragment.menu.findItem(R.id.appSearchBar)

        val searchView = search.actionView as SearchView

        searchView.queryHint = getString(R.string.github_username)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newQuery: String?): Boolean {

                newQuery?.let {
                    userViewModel.getFilteredUserList(it)
                }

                return true
            }

            override fun onQueryTextChange(newQuery: String?): Boolean = false
        })

        searchView.setOnCloseListener {
            userViewModel.getFilteredUserList(UserViewModel.DEFAULT_KEYWORD)

            false
        }
    }

    private fun setupUserList() {
        val userListAdapter = UserListAdapter(isTouchableCard = true)

        binding.swipeRefresh.setOnRefreshListener(this)

        userListAdapter.onItemClickListener = object : IOnItemClickListener<UserDomain> {

            override fun onClick(obj: UserDomain) {
                userDetailViewModel.getDetailUser(username = obj.login)

                binding.progressCircular.visibility = View.VISIBLE
            }

        }

        val listUserObserver: Observer<NetworkResult<List<UserDomain>>> = Observer { result ->
            when (result) {
                is NetworkResult.Loading -> binding.swipeRefresh.isRefreshing = true

                is NetworkResult.Loaded -> {
                    result.data?.let {
                        if (it.isEmpty()) {

                            switchRefreshAndList(isEmptyList = true)

                        } else {
                            switchRefreshAndList(isEmptyList = false)

                            userListAdapter.submitList(it)
                        }
                    }
                }

                is NetworkResult.Error -> {
                    result.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }

                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        userViewModel.requestGetUserList.observe(viewLifecycleOwner, listUserObserver)

        binding.userList.adapter = userListAdapter
    }

    private fun switchRefreshAndList(isEmptyList: Boolean) {

        if (isEmptyList) {

            binding.apply {
                swipeRefresh.isRefreshing = false

                userList.visibility = View.GONE

                emptyLayout.visibility = View.VISIBLE
            }

        } else {

            binding.apply {
                swipeRefresh.isRefreshing = false

                userList.visibility = View.VISIBLE

                emptyLayout.visibility = View.GONE
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onRefresh() {
        userViewModel.getFilteredUserList(UserViewModel.DEFAULT_KEYWORD)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyBoard()

        _binding = null
    }
}