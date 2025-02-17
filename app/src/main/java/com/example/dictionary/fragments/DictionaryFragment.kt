package com.example.dictionary.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.databinding.FragmentDictionaryBinding
import com.example.dictionary.models.DictionaryTerm
import com.example.dictionary.repository.DictionaryRepository
import com.example.dictionary.adapters.DictionaryAdapter
import com.google.android.material.chip.Chip

class DictionaryFragment : Fragment() {
    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dictionaryRepository: DictionaryRepository
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private var currentLetter: Char? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        
        // Repository'yi initialize et
        dictionaryRepository = DictionaryRepository(requireContext())
        
        // RecyclerView'ı hemen ayarla
        setupRecyclerView()
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupLetterFilters()
        loadInitialTerms()
    }

    private fun setupLetterFilters() {
        // Sadece mevcut harfleri kullan
        val availableLetters = dictionaryRepository.getAvailableLetters()
        
        val letterChips = availableLetters.map { letter ->
            Chip(requireContext()).apply {
                text = letter.toString()
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        currentLetter = letter
                        loadTermsByLetter(letter)
                        uncheckOtherChips(letter)
                    } else if (currentLetter == letter) {
                        currentLetter = null
                        loadInitialTerms()
                    }
                }
            }
        }

        letterChips.forEach { binding.letterChipGroup.addView(it) }
    }

    private fun uncheckOtherChips(selectedLetter: Char) {
        for (i in 0 until binding.letterChipGroup.childCount) {
            val chip = binding.letterChipGroup.getChildAt(i) as? Chip
            if (chip?.text.toString() != selectedLetter.toString()) {
                chip?.isChecked = false
            }
        }
    }

    private fun loadTermsByLetter(letter: Char) {
        try {
            Log.d("DictionaryFragment", "Loading terms for letter: $letter")
            val terms = dictionaryRepository.getTermsByLetter(letter)
            Log.d("DictionaryFragment", "Found ${terms.size} terms for letter $letter")
            displayTerms(terms)
        } catch (e: Exception) {
            Log.e("DictionaryFragment", "Error loading terms for letter $letter", e)
            Toast.makeText(context, "Terimler yüklenirken hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    currentLetter = null
                    // Arama yapılırken tüm chip'leri unchecked yap
                    uncheckAllChips()
                    val searchResults = dictionaryRepository.searchTerms(query)
                    displayTerms(searchResults)
                } else if (currentLetter != null) {
                    // Eğer harf filtresi aktifse, o harfe göre göster
                    loadTermsByLetter(currentLetter!!)
                } else {
                    loadInitialTerms()
                }
            }
        })
    }

    private fun uncheckAllChips() {
        for (i in 0 until binding.letterChipGroup.childCount) {
            val chip = binding.letterChipGroup.getChildAt(i) as? Chip
            chip?.isChecked = false
        }
    }

    private fun setupRecyclerView() {
        dictionaryAdapter = DictionaryAdapter(emptyList()) { term ->
            Toast.makeText(context, "${term.word} seçildi", Toast.LENGTH_SHORT).show()
        }

        binding.dictionaryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dictionaryAdapter
            setHasFixedSize(true) // Performans için
        }
    }

    private fun loadInitialTerms() {
        try {
            Log.d("DictionaryFragment", "Loading initial terms...")
            val terms = dictionaryRepository.getAllTerms()
            Log.d("DictionaryFragment", "Loaded ${terms.size} terms")
            
            if (terms.isNotEmpty()) {
                Log.d("DictionaryFragment", "First term: ${terms.first().word}")
                Log.d("DictionaryFragment", "Last term: ${terms.last().word}")
                displayTerms(terms)
            } else {
                Log.w("DictionaryFragment", "No terms found!")
                binding.emptyStateText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e("DictionaryFragment", "Error loading terms", e)
            Toast.makeText(context, "Terimler yüklenirken hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayTerms(terms: List<DictionaryTerm>) {
        Log.d("DictionaryFragment", "Displaying ${terms.size} terms")
        binding.emptyStateText.visibility = if (terms.isEmpty()) View.VISIBLE else View.GONE
        dictionaryAdapter.updateTerms(terms)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 