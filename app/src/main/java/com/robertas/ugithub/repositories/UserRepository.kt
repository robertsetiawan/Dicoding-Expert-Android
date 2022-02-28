package com.robertas.ugithub.repositories

import android.annotation.SuppressLint
import android.content.Context
import com.robertas.ugithub.interfaces.IRepository
import com.robertas.ugithub.R
import com.robertas.ugithub.models.User

class UserRepository: IRepository<User> {
    @SuppressLint("Recycle")
    override fun getListItemFromResources(context: Context): List<User> {
        val listUser: ArrayList<User> = arrayListOf()

        with(context.resources){
            val names = getStringArray(R.array.name)

            val usernames = getStringArray(R.array.username)

            val companies = getStringArray(R.array.company)

            val followers = getStringArray(R.array.followers)

            val following = getStringArray(R.array.following)

            val repositories = getStringArray(R.array.repository)

            val avatars = obtainTypedArray(R.array.avatar)

            val locations = getStringArray(R.array.location)

            for (i in 0..9){
                val user = User(
                    username = usernames[i],
                    name = names[i],
                    company = companies[i],
                    repository = repositories[i].toInt(),
                    location = locations[i],
                    followers = followers[i].toInt(),
                    following = following[i].toInt(),
                    avatar = avatars.getResourceId(i, -1)
                )

                listUser.add(user)
            }
        }
        return listUser
    }
}