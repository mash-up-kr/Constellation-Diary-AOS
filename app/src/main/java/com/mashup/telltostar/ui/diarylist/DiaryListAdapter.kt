package com.mashup.telltostar.ui.diarylist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.telltostar.R
import com.mashup.telltostar.data.ModelDate
import java.util.*

class DiaryListAdapter: RecyclerView.Adapter<DiaryListAdapter.BaseViewHolder<*>>() {
    var dataList : ArrayList<Any> = arrayListOf()

    companion object{
        private val TYPE_CONTENTS = 0
        private  val TYPE_DATE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            TYPE_CONTENTS ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_list, parent, false)
                DiaryListContentsViewHolder(view)
            }
            TYPE_DATE ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_diary_date, parent, false)
                DateViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        dataList[position].let{
            when(holder){
                is DiaryListContentsViewHolder -> {
                    holder.bind(it as Temp_diary,position)
                }
                is DateViewHolder -> holder.bind(it as ModelDate,position)
                else -> throw IllegalArgumentException()
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            for(payload in payloads){
                if(payload is Boolean) {
                    if (holder is DiaryListContentsViewHolder) {
                        holder.changeVisibleData(payload)
                    }
                }
            }
        }
    }

    fun setData(item: ArrayList<Any>)= dataList.addAll(item)

    override fun getItemViewType(position: Int): Int {
        dataList[position].let{
            return when (it) {
                is Temp_diary -> TYPE_CONTENTS
                is ModelDate -> TYPE_DATE
                else -> throw IllegalArgumentException("Invalid type of data " + position)
            }
        }
    }

    //viewHolder
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T,pos: Int)
    }


    class DateViewHolder(view: View) : BaseViewHolder<ModelDate>(view){
        val diaryMonth : TextView = itemView.findViewById(R.id.diaryListHeaderDateTV)
        override fun bind(item: ModelDate,pos:Int) {
            diaryMonth.text = item.month.toString()+"년 "+item.day.toString()+"월"
        }
    }
}