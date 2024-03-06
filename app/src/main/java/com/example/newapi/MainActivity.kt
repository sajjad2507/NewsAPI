package com.example.newapi

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val apiKey = "5c269cf1a1034266af4657b8dac4cd13"
    private var selectedStartDate = ""
    private var selectedEndDate = ""
    var switchOn = false
    var ch = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedStartDate = getCurrentDate()
        selectedEndDate = getCurrentDate()

        binding.findBtn.setOnClickListener {

            if (checkPermission()) {

                val query = binding.mainSearchView.query.toString()
                searchNews(query)

            } else {
                requestPermission()
            }

        }

        binding.arrowImg.setOnClickListener {

            if (ch == 1) {
                binding.arrowImg.setImageResource(R.drawable.up)
                binding.btnDate.visibility = View.VISIBLE
                switchOn = true
                ch = 2
            } else {
                binding.arrowImg.setImageResource(R.drawable.arrow)
                binding.btnDate.visibility = View.GONE
                switchOn = false
                ch = 1
            }

        }

        binding.startDateBtn.setOnClickListener {

            onStartDateButtonPressed()

        }

        binding.ednDateBtn.setOnClickListener {

            onEndDateButtonPressed()

        }

    }

    private fun openDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                if (isStartDate) {
                    selectedStartDate = handleSelectedDate(year, month, day)
                } else {
                    selectedEndDate = handleSelectedDate(year, month, day)
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun handleSelectedDate(year: Int, month: Int, day: Int): String {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(selectedCalendar.time)
    }

    // Call these methods when corresponding buttons are pressed
    private fun onStartDateButtonPressed() {
        openDatePickerDialog(isStartDate = true)
    }

    private fun onEndDateButtonPressed() {
        openDatePickerDialog(isStartDate = false)
    }

    private fun searchNews(query: String) {

        val call = if (switchOn) {

            val startDate = selectedStartDate
            val endDate = selectedEndDate
            RetrofitClient.instance.getNews(apiKey, query, startDate, endDate)
        } else {

            RetrofitClient.instance.getNews(apiKey, query)
        }

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        logNewsData(it)
                    }
                } else {
                    Log.e("API Error", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("API Failure", "Failure: ${t.message}")
            }
        })
    }

    private fun logNewsData(newsResponse: NewsResponse) {
        val recyclerView: RecyclerView = findViewById(R.id.mainRcv)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = NewsAdapter(newsResponse.articles, this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()


        calendar.add(Calendar.DAY_OF_MONTH, -1)


        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedPreviousDate = dateFormat.format(calendar.time)

        return formattedPreviousDate
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
