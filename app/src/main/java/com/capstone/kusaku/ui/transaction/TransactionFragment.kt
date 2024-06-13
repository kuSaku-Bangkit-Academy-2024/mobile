package com.capstone.kusaku.ui.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.kusaku.R
import com.capstone.kusaku.databinding.FragmentTransactionBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.main.MainActivity
import com.capstone.kusaku.utils.ProgressBarHelper
import com.capstone.kusaku.utils.Status
import java.util.Calendar

class TransactionFragment : Fragment() { private val viewModel: TransactionViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var progressBarHelper: ProgressBarHelper
    private lateinit var adapter: ArrayAdapter<String>
    private val categories = ArrayList<String>()
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi progressBarHelper di onCreateView
        progressBarHelper = ProgressBarHelper(requireActivity() as MainActivity)
        adapter = ArrayAdapter(requireContext(), R.layout.list_item_category, categories)
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupCategoryDropdown()
    }

    private fun setupView() {
        binding.btnAdd.setOnClickListener {
            addTransaction()
        }

        binding.btnUseAi.setOnClickListener {
            useAiToCategorize()
        }

        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        viewModel.category.observe(viewLifecycleOwner) { category ->
            category?.let {
                if (adapter.getPosition(category) == -1) {
                    adapter.add(category)
                    adapter.notifyDataSetChanged()
                }
                binding.dropdownCategory.setText(category, false)
            }
        }

        viewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
            val message = viewModel.handleTransactionResponse(response)
            when (response.status) {
                Status.SUCCESS -> {
                    progressBarHelper.hide()
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> progressBarHelper.show()
            }
        }
    }

    private fun useAiToCategorize() {
        val description = binding.edtDescription.text.toString()
        viewModel.predictCategory(description).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    progressBarHelper.hide()
                    val category = resource.data?.data?.category
                    if (!category.isNullOrEmpty()) {
                        val adapter = binding.dropdownCategory.adapter as ArrayAdapter<String>
                        if (!isCategoryInAdapter(adapter, category)) {
                            adapter.add(category)
                            adapter.notifyDataSetChanged()
                        }
                        binding.dropdownCategory.setText(category, false)
                    }
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> progressBarHelper.show()
            }
        }
    }

    private fun isCategoryInAdapter(adapter: ArrayAdapter<String>, category: String): Boolean {
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == category) {
                return true
            }
        }
        return false
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            val formattedDate = viewModel.convertDateFormat(selectedDate, "dd/MM/yyyy", "yyyy-MM-dd")
            Toast.makeText(requireContext(), "Selected date: $formattedDate", Toast.LENGTH_SHORT).show()
            binding.btnDatePicker.text = formattedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun setupCategoryDropdown() {
        val categories = listOf("Food", "Transportation", "Apparel", "Household", "Social Life", "Education", "Entertainment", "Health", "other")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_category, categories)
        binding.dropdownCategory.setAdapter(adapter)
    }

    private fun addTransaction() {
        val timestamp = binding.btnDatePicker.text.toString()
        val describe = binding.edtDescription.text.toString()
        val price = binding.edtNominal.text.toString().toIntOrNull() ?: 0
        val category = binding.dropdownCategory.text.toString()

        if (timestamp.isNotEmpty() && describe.isNotEmpty() && price > 0 && category.isNotEmpty()) {
            viewModel.addTransaction(timestamp, describe, price, category).observe(viewLifecycleOwner) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        progressBarHelper.hide()
                        val message = viewModel.handleTransactionResponse(response)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.navigation_home)
                    }
                    Status.ERROR -> {
                        progressBarHelper.hide()
                        val errorMessage = response.message ?: "Failed to add transaction"
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> progressBarHelper.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}