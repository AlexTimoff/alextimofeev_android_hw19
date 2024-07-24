package com.example.alextimofeev_android_hw18.map.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alextimofeev_android_hw18.databinding.FragmentShowAttractionBinding
import com.example.alextimofeev_android_hw18.map.entity.AttractionItem
import java.util.zip.Inflater


class ShowAttractionFragment : Fragment() {

    private var _binding: FragmentShowAttractionBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowAttractionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val marker = arguments?.getParcelable<AttractionItem>(KEY_MARKER)
        marker?.let {
            val name = it.tags["name:en"] ?: "Unknown"
            val latitude = it.lat
            val longitude = it.lon
            binding.head.text = name
            binding.latitude.text = "Широта:\n$latitude"
            binding.longitude.text = "Долгота:\n$longitude"
        }
    }

    companion object {
        const val KEY_MARKER = "KEY_MARKER"
    }

}