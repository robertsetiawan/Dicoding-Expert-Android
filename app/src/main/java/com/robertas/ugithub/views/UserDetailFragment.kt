package com.robertas.ugithub.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.robertas.ugithub.R
import com.robertas.ugithub.adapters.UserTabAdapter
import com.robertas.ugithub.databinding.FragmentUserDetailBinding
import com.robertas.ugithub.utils.extension.setTextOrHide


class UserDetailFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentUserDetailBinding

    private lateinit var navController: NavController

    private val navArgs by navArgs<UserDetailFragmentArgs>()

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
            setupWithNavController(navController)

            setNavigationOnClickListener {
                navController.navigateUp()
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

    override fun onClick(view: View?) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/${navArgs.user.login}")
        )
        startActivity(intent)
    }
}
