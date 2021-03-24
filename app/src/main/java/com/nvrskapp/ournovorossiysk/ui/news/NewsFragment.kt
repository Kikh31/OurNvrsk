package com.nvrskapp.ournovorossiysk.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nvrskapp.ournovorossiysk.R
import com.nvrskapp.ournovorossiysk.data.NewsStore.allNews
import com.nvrskapp.ournovorossiysk.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val binding = DataBindingUtil.inflate<FragmentNewsBinding>(
            inflater,
            R.layout.fragment_news,
            container,
            false
        ).apply {
            feed.adapter = NewsAdapter(allNews)
        }
        return binding.root
    }
}