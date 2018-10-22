package com.nnl.password.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.nnl.password.R
import com.nnl.password.content.DBContentManager
import java.util.*

class RecyclerContentActivity : Activity(),MenuFragment.OnFragmentInteractionListener {

    var contentManager: DBContentManager? = null

    private var recyclerView: RecyclerView? = null
    private var lastClickBackTime = 0L

    enum class ContextMenuItem(var groupID: Int, var itemID: Int,
                               var order: Int, var title: CharSequence){
        DELETE(0, 0, 0, "删除"),
        EDIT(0, 1, 1, "编辑"),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentManager = DBContentManager.getInstance(applicationContext)
        setContentView(R.layout.activity_recycler_content)

        recyclerView = findViewById(R.id.content_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_devider)!!)
        recyclerView?.addItemDecoration(divider)
    }

    override fun onResume() {
        super.onResume()
        recyclerView?.adapter = MyRecyclerViewAdapter()
    }

    inner class MyRecyclerViewAdapter: RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){
        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var tvItemTitle: TextView? = null
        }

        override fun getItemCount(): Int {
            return contentManager!!.getCount()
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tvItemTitle?.setText(contentManager!!.getItem(position)?.title)
            holder.itemView.id = position
            holder.itemView.setOnClickListener {
                var intent = Intent(baseContext, ItemDetailActivity::class.java)
                intent.putExtra(ItemDetailActivity.OPT_DETAIL_INDEX, position)
                startActivity(intent)
            }
            registerForContextMenu(holder.itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var view = LayoutInflater.from(baseContext).inflate(R.layout.recycler_view_item, parent, false)
            var holder = MyViewHolder(view)
            holder.tvItemTitle = view.findViewById(R.id.textView_recycler_view_item)
            return holder
        }


    }
    override fun onFragmentInteraction(uri: Uri){

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var values = ContextMenuItem.values()
        for (value in values) {
            menu?.add(value.groupID, v!!.id, value.order, value.title)
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.order) {
            ContextMenuItem.DELETE.order -> {
                contentManager?.delete(contentManager?.getItem(item.itemId)!!.title!!)
                recyclerView?.adapter = MyRecyclerViewAdapter()
                return true
            }
            ContextMenuItem.EDIT.order -> {
                var intent = Intent(baseContext, AddItemActivity::class.java)
                intent.putExtra(AddItemActivity.OPT_MODIFY_OR_ADD, 1.toByte())
                intent.putExtra(AddItemActivity.OPT_MODIFY_INDEX, item.itemId)
                startActivity(intent)
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        var curTime = Calendar.getInstance().timeInMillis
        if (lastClickBackTime == 0L || curTime - lastClickBackTime > 2000) {
            Toast.makeText(baseContext, "再次点击退出程序", Toast.LENGTH_LONG).show()
            lastClickBackTime = curTime
            return
        }
        super.onBackPressed()
    }
}
