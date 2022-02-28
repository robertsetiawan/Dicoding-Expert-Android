package com.robertas.ugithub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertas.ugithub.R
import com.robertas.ugithub.databinding.UserItemBinding
import com.robertas.ugithub.models.User

class UserListAdapter (private val listUser: List<User>): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    lateinit var onItemClickListener: IOnItemClickListener

    interface IOnItemClickListener{
        fun onItemClick(user: User)
    }

    inner class ViewHolder(private val binding: UserItemBinding, private val context: Context): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.apply {
                this.profileImage.setImageResource(user.avatar)

                this.userFollowersTv.text = context.getString(R.string.follower_placeholder, user.followers.toString())

                this.userFollowingTv.text = context.getString(R.string.following_placeholder, user.following.toString())

                this.userTitleTv.text = user.name

                this.userItem.setOnClickListener {
                    onItemClickListener.onItemClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int  = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }
}