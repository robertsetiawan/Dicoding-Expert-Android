package com.robertas.ugithub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robertas.ugithub.databinding.UserItemBinding
import com.robertas.ugithub.interfaces.IOnItemClickListener
import com.robertas.ugithub.models.domain.User

class UserListAdapter(private val isTouchableCard: Boolean) :
    ListAdapter<User, UserListAdapter.ViewHolder>(DiffCallBack) {

    lateinit var onItemClickListener: IOnItemClickListener<User>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UserItemBinding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)

        holder.bind(user)
    }

    inner class ViewHolder(private val userBinding: UserItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(userBinding.root) {
        fun bind(user: User) {
            userBinding.apply {
                Glide.with(context).load(user.avatarUrl).into(this.profileImage)

                this.userTitleTv.text = user.login

                this.userCard.apply {
                    setOnClickListener { onItemClickListener.onClick(user) }

                    isCheckable = isTouchableCard

                    isClickable = isTouchableCard

                    isFocusable = isTouchableCard
                }
            }
        }
    }

    object DiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
}