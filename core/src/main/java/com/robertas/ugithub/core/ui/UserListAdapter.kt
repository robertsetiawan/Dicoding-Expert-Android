package com.robertas.ugithub.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robertas.ugithub.core.databinding.UserItemBinding
import com.robertas.ugithub.core.domain.model.UserDomain

class UserListAdapter(private val isTouchableCard: Boolean) :
    ListAdapter<UserDomain, UserListAdapter.ViewHolder>(DiffCallBack) {

    lateinit var onItemClickListener: IOnItemClickListener<UserDomain>

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
        fun bind(user: UserDomain) {
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

    object DiffCallBack : DiffUtil.ItemCallback<UserDomain>() {
        override fun areItemsTheSame(oldItem: UserDomain, newItem: UserDomain): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserDomain, newItem: UserDomain): Boolean =
            oldItem == newItem
    }
}