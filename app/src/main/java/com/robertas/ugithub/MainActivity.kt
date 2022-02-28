package com.robertas.ugithub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robertas.ugithub.adapters.UserListAdapter
import com.robertas.ugithub.databinding.ActivityMainBinding
import com.robertas.ugithub.interfaces.IRepository
import com.robertas.ugithub.models.User
import com.robertas.ugithub.repositories.UserRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var userRepository: IRepository<User>

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        userRepository = UserRepository()

        setContentView(binding.root)

        setupUserList(binding)
    }

    private fun setupUserList(binding: ActivityMainBinding){

        userListAdapter = UserListAdapter(userRepository.getListItemFromResources(this))

        userListAdapter.onItemClickListener = object : UserListAdapter.IOnItemClickListener{

            override fun onItemClick(user: User) {

                val detailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)

                detailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)

                startActivity(detailIntent)
            }
        }

        binding.userList.apply {
            setHasFixedSize(true)

            adapter = userListAdapter
        }
    }

}