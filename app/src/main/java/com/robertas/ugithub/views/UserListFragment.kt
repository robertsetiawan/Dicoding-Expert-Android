package com.robertas.ugithub.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.robertas.ugithub.interfaces.IOnItemClickListener
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.viewmodels.UserViewModel

class UserListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentUserListBinding

    private lateinit var navController: NavController

    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserListBinding.inflate(inflater, container, false)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()

        setupUserList()

        setupToolbarSearch()
    }

    private fun setupNavigation() {
        binding.toolbarFragment.setupWithNavController(navController)

        binding.toolbarFragment.setNavigationOnClickListener { navController.navigateUp() }

        val navigationObserver: Observer<NetworkResult<User>> = Observer { result ->
            when (result) {

                is NetworkResult.Loading -> {}

                is NetworkResult.Loaded -> {

                    result.data?.let {
                        val directionsToDetailUser =
                            UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(it)

                        navController.navigate(directionsToDetailUser)
                    }

                    binding.progressCircular.visibility = View.GONE

                    userViewModel.doneNavigatingToDetailFragment()
                }

                is NetworkResult.Error -> {
                    result.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }

                    binding.progressCircular.visibility = View.GONE

                    userViewModel.doneNavigatingToDetailFragment()
                }
            }

        }

        userViewModel.requestGetDetailUser.observe(viewLifecycleOwner, navigationObserver)
    }

    private fun setupToolbarSearch() {

        binding.toolbarFragment.inflateMenu(R.menu.search_menu)

        val search = binding.toolbarFragment.menu.findItem(R.id.appSearchBar)

        val searchView = search.actionView as SearchView

        searchView.queryHint = "Github Username"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newQuery: String?): Boolean {

                userViewModel.setQuerySearch(newQuery)

                return true
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                newQuery?.let {
                    if (it.isBlank() || it.isEmpty()) {
                        userViewModel.doneSearching()

                        userViewModel.getFilteredUserList(
                            UserViewModel.DEFAULT_KEYWORD
                        )
                    }
                }
                return true
            }
        })

        val searchObserver: Observer<String?> = Observer { query ->
            query?.let {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    userViewModel.getFilteredUserList(it)
                }
            }
        }

        userViewModel.querySearch.observe(viewLifecycleOwner, searchObserver)
    }

    private fun setupUserList() {
        val userListAdapter = UserListAdapter(isTouchableCard = true)

        binding.swipeRefresh.setOnRefreshListener(this)

        userListAdapter.onItemClickListener = object : IOnItemClickListener<User> {

            override fun onClick(obj: User) {
                userViewModel.getDetailUser(username = obj.login)

                binding.progressCircular.visibility = View.VISIBLE
            }

        }

        val listUserObserver: Observer<NetworkResult<List<User>>> = Observer { result ->
            when (result) {
                is NetworkResult.Loading -> switchRefreshAndList(false)

                is NetworkResult.Loaded -> {
                    result.data?.let { userListAdapter.submitList(it) }

                    switchRefreshAndList(true)
                }

                is NetworkResult.Error -> {
                    result.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }

                    switchRefreshAndList(true)
                }
            }
        }

        userViewModel.requestGetUserList.observe(viewLifecycleOwner, listUserObserver)

        binding.userList.adapter = userListAdapter
    }

    private fun switchRefreshAndList(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = !isRefreshing

        binding.userList.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        userViewModel.getFilteredUserList(UserViewModel.DEFAULT_KEYWORD)
    }
}