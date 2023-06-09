package ru.spiridonov.gallery.presentation.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.FragmentProfileBinding
import ru.spiridonov.gallery.presentation.activity.account.LoginActivity
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")


    private val component by lazy {
        (requireActivity().application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        observeViewModel()
        buttonClickListener()
    }

    private fun buttonClickListener() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().popBackStack()
            requireActivity().startActivity(LoginActivity.newIntent(requireContext()))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadData()
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null)
                binding.accountItem = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}