package com.capstone.kusaku.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.kusaku.databinding.FragmentProfileBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.login.LoginActivity
import com.capstone.kusaku.ui.main.MainActivity
import com.capstone.kusaku.utils.ProgressBarHelper
import com.capstone.kusaku.utils.RupiahFormatter
import com.capstone.kusaku.utils.Status

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var progressBarHelper: ProgressBarHelper
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        progressBarHelper = ProgressBarHelper(requireActivity() as MainActivity)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(){
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        viewModel.getUserDetail().observe(viewLifecycleOwner){
            when (it.status){
                Status.SUCCESS -> {
                    progressBarHelper.hide()
                    binding.valueEmail.text = it.data?.email ?: "-"
                    binding.valueUsername.text = it.data?.username ?: "-"
                    binding.valueIncome.text = it.data?.income?.toLong()
                        ?.let { it1 -> RupiahFormatter.format(it1) }
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> progressBarHelper.show()
            }
        }
    }
}