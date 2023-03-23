package com.example.bretterverse.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bretterverse.databinding.FragmentResultsBinding
import com.example.bretterverse.models.ResultViewModel
import com.example.bretterverse.network.WisperService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsFragment : Fragment() {

    private lateinit var resultViewModel: ResultViewModel
    private lateinit var binding: FragmentResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultViewModel = ViewModelProvider(requireActivity()).get(ResultViewModel::class.java)

        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        // get results from server and update the view
        getResults()
    }

    private fun getResults() {
        WisperService.wisperApi.getResults().enqueue(object : Callback<List<Result>> {
            override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                if (response.isSuccessful) {
                    response.body()?.let { results ->
                        resultViewModel.setResults(results)
                        binding.rvResults.adapter = ResultsAdapter(results)
                    }
                } else {
                    // show error message if response is unsuccessful
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "Error loading results"
                }
            }

            override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                // show error message if request fails
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = "Error loading results: ${t.message}"
            }
        })
    }
}
