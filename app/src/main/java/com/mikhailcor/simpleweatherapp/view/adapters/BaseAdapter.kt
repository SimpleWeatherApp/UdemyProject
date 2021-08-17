package com.mikhailcor.simpleweatherapp.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<D> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>(){

    private val _mData by lazy { mutableListOf<D>() }
    protected val mData : List<D> = _mData

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount() = _mData.size


    fun updateData(data: List<D>){
        if(_mData.isEmpty() && (data.isNotEmpty())){
            _mData.addAll(data)
        }else {
            _mData.clear()
            _mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view){

        abstract fun bindView(position: Int)

    }
}