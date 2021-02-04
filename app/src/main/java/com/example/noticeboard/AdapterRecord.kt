package com.example.noticeboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class AdapterRecord() : RecyclerView.Adapter<AdapterRecord.HolderRecord>() {

    private var context: Context?=null
    private var recordList:ArrayList<ModelRecord>?=null

    constructor(context: Context?, recordList: ArrayList<ModelRecord>?):this(){
        this.context = context
        this.recordList = recordList
    }

    inner class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView){
        var profileIv : ImageView = itemView.findViewById<ImageView>(R.id.profileIv)
        var nameTv : TextView = itemView.findViewById<TextView>(R.id.nameTv)
        var addressTv : TextView = itemView.findViewById<TextView>(R.id.addressTv)
        var timeTv : TextView = itemView.findViewById<TextView>(R.id.timeTv)
        var moreBtn : ImageButton = itemView.findViewById<ImageButton>(R.id.moreBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.row_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        val model = recordList!!.get(position)

        val id = model.id
        val image = model.image
        val name = model.name
        val address = model.address
        val time = model.time
        val contentt = model.contentt
        val addedTime = model.addedTime
        val updatedTime = model.updatedTime

        holder.nameTv.text = name
        holder.addressTv.text = address
        holder.timeTv.text = time

        if(image == "null"){
            holder.profileIv.setImageResource(R.drawable.ic_person_black)
        }else
            holder.profileIv.setImageURI(Uri.parse(image))

        holder.itemView.setOnClickListener{
            val intent = Intent(context, RecordDetailActivity::class.java)
            intent.putExtra("RECORD_ID",id)
            context!!.startActivity(intent)
        }

        holder.moreBtn.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
       return recordList!!.size
    }
}