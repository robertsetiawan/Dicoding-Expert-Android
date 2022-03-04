package com.robertas.ugithub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robertas.ugithub.databinding.ActivityMainBinding
import com.robertas.ugithub.interfaces.IRepository
import com.robertas.ugithub.models.domain.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var userRepository: IRepository<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

}