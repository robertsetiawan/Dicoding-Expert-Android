package com.robertas.ugithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.robertas.ugithub.databinding.ActivityUserDetailBinding
import com.robertas.ugithub.models.User

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var user: User

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setupDetailUser(binding, user)
    }

    private fun setupDetailUser(binding: ActivityUserDetailBinding, user: User){

        val (username, name, avatar, company, location, repository, followers, following) = user

        binding.apply {
            profileImage.setImageResource(avatar)

            userTitleTv.text = name

            usernameButton.text = this@UserDetailActivity.getString(R.string.github_placeholder, username)

            usernameButton.setOnClickListener(this@UserDetailActivity)

            followerTv.text = followers.toString()

            followingTv.text = following.toString()

            companyTv.text = company

            locationTv.text = location

            repositoryTv.text = repository.toString()
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                R.id.username_button -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, this@UserDetailActivity.getString(R.string.github_placeholder, user.username))
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)

                    startActivity(shareIntent)
                } else -> {}
            }
        }
    }
}