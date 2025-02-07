package com.example.dictionary.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.dictionary.CategoryAdapter
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentHomeBinding
import com.example.dictionary.models.Category
import com.example.dictionary.models.News
import com.example.dictionary.adapters.NewsSliderAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.dictionary.api.NewsApiClient
import android.util.Log
import com.example.dictionary.api.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var newsAdapter: NewsSliderAdapter
    
    // Coroutine scope'u fragment lifecycle'ına bağlayalım
    private val fragmentScope = lifecycleScope

    private val searchTerms = listOf(
        "siber güvenlik",
        "siber saldırı",
        "hack",
        "hacker",
        "zararlı yazılım",
        "fidye yazılımı",
        "veri sızıntısı",
        "siber suç",
        "ağ güvenliği",
        "bilgi güvenliği"
    ).joinToString(" OR ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setupRecyclerView()
        loadNews()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(getCategoryList()) { category ->
            // Kategori tıklama işlemleri
            Toast.makeText(context, "${category.title} seçildi", Toast.LENGTH_SHORT).show()
        }
        
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }

    private fun getCategoryList(): List<Category> {
        return listOf(
            Category(
                1,
                "Puanım",
                "Toplam kazandığın puanlar",
                R.drawable.ic_score,
                1250
            ),
            Category(
                2,
                "Sıralama",
                "Genel sıralamadaki yerin",
                R.drawable.ic_leaderboard,
                15 // Sıralama numarası
            ),
            Category(
                3,
                "Sözlük",
                "Tüm terimleri keşfet",
                R.drawable.ic_dictionary
            ),
            Category(
                4,
                "Favoriler",
                "Kaydettiğin terimler",
                R.drawable.ic_favorite
            )
        )
    }

    private fun loadNews() {
        fragmentScope.launch {
            try {
                val newsList = fetchNews()
                if (newsList.isEmpty()) {
                    Log.w("NewsAPI", "News list is empty!")
                    return@launch
                }

                // View hala attached mı kontrol edelim
                if (!isAdded) return@launch

                newsAdapter = NewsSliderAdapter(newsList) { news ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                    startActivity(intent)
                }

                binding.newsViewPager.apply {
                    adapter = newsAdapter
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }

                binding.dotsIndicator.setViewPager2(binding.newsViewPager)

                // Otomatik kaydırma için
                startAutoScroll(newsList.size)

            } catch (e: Exception) {
                Log.e("NewsAPI", "Error loading news", e)
            }
        }
    }

    private fun startAutoScroll(listSize: Int) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (!isAdded) return // Fragment hala attached mı kontrol et
                
                binding.newsViewPager.let { viewPager ->
                    val nextItem = (viewPager.currentItem + 1) % listSize
                    viewPager.setCurrentItem(nextItem, true)
                    handler.postDelayed(this, 5000)
                }
            }
        }

        binding.newsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

        handler.postDelayed(runnable, 5000)
    }

    private suspend fun fetchNews(): List<News> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("NewsAPI", "Fetching news with query: $searchTerms")
                val response = NewsApiClient.service.getNews(
                    query = searchTerms,
                    pageSize = 5,
                    apiKey = NewsApiClient.API_KEY
                )

                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    Log.d("NewsAPI", "Found ${articles?.size ?: 0} articles")
                    
                    articles?.forEach { article ->
                        Log.d("NewsAPI", """
                            Article:
                            Title: ${article.title}
                            Has Image: ${article.urlToImage != null}
                            Has Description: ${article.description != null}
                        """.trimIndent())
                    }

                    response.body()?.articles
                        ?.filter { it.urlToImage != null && it.description != null }
                        ?.map { article ->
                            News(
                                title = article.title,
                                description = article.description!!.take(150).let { if (it.length == 150) "$it..." else it },
                                imageUrl = article.urlToImage!!,
                                url = article.url
                            )
                        } ?: emptyList()
                } else {
                    Log.e("NewsAPI", """
                        API Error:
                        Code: ${response.code()}
                        Message: ${response.message()}
                        Error Body: ${response.errorBody()?.string()}
                    """.trimIndent())
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("NewsAPI", "Error fetching news", e)
                emptyList()
            }
        }
    }

    override fun onDestroyView() {
        // View binding'i temizle
        _binding = null
        super.onDestroyView()
    }
} 