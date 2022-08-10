package com.robertas.ugithub.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.robertas.ugithub.R
import com.robertas.ugithub.home.UserTabAdapter
import com.robertas.ugithub.core.utils.extension.setTextOrHide
import com.robertas.ugithub.databinding.FragmentUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetailFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val navArgs by navArgs<UserDetailFragmentArgs>()

    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupToolbarNavigation()
        setupUserDetailFragment()
        setupViewPager()
    }

    private fun setupUserDetailFragment() {
        val user = navArgs.user
        binding.apply {
            Glide.with(profileImage).load(user.avatarUrl).into(profileImage)
            repositoryTv.text =
                getString(R.string.repository_placeholder, user.publicRepos.toString())
            connectionTv.text = getString(
                R.string.connection_placeholder,
                user.followers.toString(),
                user.following.toString()
            )
            locationTv.setTextOrHide(user.location)
            bioTv.setTextOrHide(user.bio)
            companyTv.setTextOrHide(user.company)
            usernameTv.text = user.login
            nameTv.text = user.name
            webTv.setTextOrHide(user.blog)
            openBtn.setOnClickListener(this@UserDetailFragment)
        }
    }

    private fun setupToolbarNavigation() {
        binding.toolbarFragment.apply {
            inflateMenu(R.menu.detail_menu)
            setupWithNavController(navController)
            setNavigationOnClickListener {
                navController.navigateUp()
            }
        }

        val likeIcon = binding.toolbarFragment.menu.findItem(R.id.like)
        val likedIcon = binding.toolbarFragment.menu.findItem(R.id.liked)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            userDetailViewModel.getFavouriteUser(navArgs.user.id).collect{
                if (it != null) {
                    likeIcon.isVisible = false
                    likedIcon.isVisible = true

                } else {
                    likeIcon.isVisible = true
                    likedIcon.isVisible = false
                }
            }
        }

        binding.toolbarFragment.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.like -> {
                    viewLifecycleOwner.lifecycleScope.launchWhenResumed { userDetailViewModel.addFavouriteUser(navArgs.user) }
                    Snackbar.make(
                        binding.root,
                        getString(R.string.user_added_to_favourites),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }

                R.id.liked -> {
                    viewLifecycleOwner.lifecycleScope.launchWhenResumed { userDetailViewModel.deleteFavouriteUser(navArgs.user) }
                    Snackbar.make(
                        binding.root,
                        getString(R.string.user_removed_from_favourites),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPager() {
        val userTabAdapter = UserTabAdapter(this@UserDetailFragment, navArgs.user.login)
        binding.apply {
            pager.adapter = userTabAdapter
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.follower)
                    else -> tab.text = getString(R.string.following)
                }
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/${navArgs.user.login}")
        )
        startActivity(intent)
    }
}
