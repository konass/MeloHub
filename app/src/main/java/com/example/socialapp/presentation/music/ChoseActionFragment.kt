package com.example.socialapp.presentation.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentChoseActionBinding

class ChoseActionFragment : Fragment() {
lateinit var binding: FragmentChoseActionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentChoseActionBinding.inflate(layoutInflater,  container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSearch.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_choseActionFragment_to_searchMusicFragment
            )
        }
            binding.btnListen.setOnClickListener {
                view.findNavController().navigate(
                    R.id.action_choseActionFragment_to_musicFragment
                )
            }
    }



}