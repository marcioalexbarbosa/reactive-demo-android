package com.example.android.reactor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.reactor.R
import com.example.android.reactor.model.Dummy

class DummyAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Dummy>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    fun updateDataSource() {
        this.notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_item_dummy, parent, false)

            holder = ViewHolder()
            holder.id = view.findViewById(R.id.dummy_list_title) as TextView
            holder.name = view.findViewById(R.id.dummy_list_subtitle) as TextView

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val titleTextView = holder.id
        val subtitleTextView = holder.name

        val dummy = getItem(position) as Dummy

        titleTextView.text = dummy.id
        subtitleTextView.text = dummy.name
        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    private class ViewHolder {
        lateinit var id: TextView
        lateinit var name: TextView
    }
}
