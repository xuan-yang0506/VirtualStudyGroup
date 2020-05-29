package com.example.virtualstudygroup.chatActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.virtualstudygroup.R
import com.example.virtualstudygroup.model.UserChat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_message.chat_toolbar
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object {
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        // set up back button
        setSupportActionBar(chat_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Select A New Chat"

        // set up recycler view
        val adapter = GroupAdapter<GroupieViewHolder>()

        new_message_list.adapter = adapter

        fetchUsers()

    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.i("Diana", it.toString())
                    val user = it.getValue(UserChat::class.java)
                    user?.let {
                        adapter.add(
                            UserItem(
                                user
                            )
                        )
                    }
                }
                
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)

                    finish()
                }
                
                new_message_list.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return super.onSupportNavigateUp()
    }
}

class UserItem(val user: UserChat): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // show email instead of user name b/c username info missing
        if (user.name == "") {
            viewHolder.itemView.tvChatUserName.text = user.email
        } else {
            viewHolder.itemView.tvChatUserName.text = user.name
        }

        if (user.photoURL.startsWith("https:")) {
            Picasso.get().load(user.photoURL)?.into(viewHolder.itemView.ivChatUserImage)
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row_message
    }
}