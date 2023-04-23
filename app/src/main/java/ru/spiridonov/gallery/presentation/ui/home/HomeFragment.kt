package ru.spiridonov.gallery.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.FragmentHomeBinding
import ru.spiridonov.gallery.presentation.adapters.MediaItemAdapter
import ru.spiridonov.gallery.presentation.ui.fullscreen.FullscreenActivity
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding == null")


    private val component by lazy {
        (requireActivity().application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mediaItemAdapter: MediaItemAdapter

    private lateinit var viewModel: HomeViewModel


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        observeViewModel()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadAllMediaInfo()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.media.observe(viewLifecycleOwner) {
            mediaItemAdapter.submitList(it)
            mediaItemAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        binding.rvMediaList.adapter = mediaItemAdapter
        mediaItemAdapter.onItemClickListener = {
            val intent = FullscreenActivity.newIntentImage(requireContext(), it.media_id)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}