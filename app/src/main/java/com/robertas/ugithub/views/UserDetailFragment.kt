package com.robertas.ugithub.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.robertas.ugithub.R
import com.robertas.ugithub.adapters.UserTabAdapter
import com.robertas.ugithub.databinding.FragmentUserDetailBinding
import com.robertas.ugithub.utils.extension.setTextOrHide
import com.robertas.ugithub.viewmodels.UserViewModel


class UserDetailFragment : Fragment() {
    private lateinit var binding: FragmentUserDetailBinding

    private lateinit var navController: NavController

    private val navArgs by navArgs<UserDetailFragmentArgs>()

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setupToolbarNavigation()

        setupUserDetailFragment()

        setupViewPager()
    }

    private fun setupUserDetailFragment() {
        val user = navArgs.user

        binding.apply {
            Glide.with(profileImage).load(user.avatarUrl).into(profileImage)

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
        }
    }

    private fun setupToolbarNavigation() {

        binding.toolbarFragment.apply {
            setupWithNavController(navController)

            setNavigationOnClickListener {

                with(userViewModel) {
                    doneLoadingFollowingList()

                    doneLoadingFollowerList()
                }

                navController.navigateUp()
            }
        }
    }

    private fun setupViewPager() {
        val userTabAdapter = UserTabAdapter(this@UserDetailFragment)

        binding.apply {
            pager.adapter = userTabAdapter

            TabLayoutMediator(tabLayout, pager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.follower)
                    else -> tab.text = getString(R.string.following)
                }
            }.attach()

            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when (position) {
                        0 -> userViewModel.getFollowerList(navArgs.user.login)
                        else -> userViewModel.getFollowingList(navArgs.user.login)
                    }
                }
            })
        }
    }
}
