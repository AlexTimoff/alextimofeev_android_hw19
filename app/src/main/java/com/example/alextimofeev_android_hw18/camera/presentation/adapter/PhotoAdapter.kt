package com.example.alextimofeev_android_hw18.camera.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alextimofeev_android_hw18.databinding.PhotoItemBinding
import com.example.alextimofeev_android_hw18.camera.entity.Photo


//Вспомогательный класс, который создает view  и заполняет данными
class PhotoAdapter(): RecyclerView.Adapter<PhotoViewHolder> (){

    //Создаем изменяемую переменную,  содержащую перечень фото
    private var data : List<Photo> = arrayListOf()


    //Создаем ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotoViewHolder(binding)
    }

    //Получаем нужный элемент из массива данных и устанавливаем во view
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //Из массива получаем элемента по индексу
        val photo = data.getOrNull(position)
        //Устанавливаем фото
       with(holder.binding){
            dateOfPhoto.text=photo?.datePhoto
           photo?.let {
            //Для получения URI из строки можно воспользоваться методом Uri.parse(uriStr), где uriStr — это строка, сохранённая в базе данных.
               Glide.with(newImage.context).load(Uri.parse(photo.uri)).into(newImage)
           }
       }
    }

    //Отображение выбранного фото
    override fun getItemCount(): Int = data.size

    //Возвращает количество элементов, которые нужно отобразить
    fun setData(data: List<Photo>){
        this.data = data
        notifyDataSetChanged()
    }

}