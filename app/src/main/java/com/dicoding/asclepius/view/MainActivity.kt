package com.dicoding.asclepius.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.utils.Utils.convertToPercent
import com.dicoding.asclepius.utils.Utils.showToast
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.news.NewsActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import com.yalantis.ucrop.UCrop

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                // nothing happen :D
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        historyRecyclerView = binding.historyRecyclerView
        historyAdapter = HistoryAdapter(viewModel)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.setHasFixedSize(true)
        
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        if (!allPermissionsGranted()) {
            requestPermission.launch(REQUIRED_PERMISSION)
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = this@MainActivity,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(this@MainActivity, error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        binding.progressIndicator.visibility = View.GONE
                        try {
                            results?.let {
                                val category = it[0].categories[0].label
                                val confidence = it[0].categories[0].score

                                currentImageUri?.let { uri ->
                                    this@MainActivity.contentResolver.openInputStream(uri)?.use { inputStream ->
                                        val bitmap = BitmapFactory.decodeStream(inputStream)
                                        binding.previewImageView.setImageBitmap(bitmap)
                                    }
                                }

                                val history = currentImageUri?.let { uri ->
                                    this@MainActivity.contentResolver.openInputStream(uri)?.use { inputStream ->
                                        val imageBytes = inputStream.readBytes()
                                        HistoryEntity(
                                            category = category,
                                            confidenceScore = confidence,
                                            dateTime = System.currentTimeMillis().toString(),
                                            image = imageBytes
                                        )
                                    }
                                }
                                
                                if (history != null) {
                                    viewModel.insertHistory(history).observe(this@MainActivity) { results ->
                                        if (results != null) {
                                            when (results) {
                                                is Result.Loading -> {
                                                    binding.progressIndicator.visibility = View.VISIBLE
                                                }
                                                is Result.SuccessMessage -> {
                                                    binding.progressIndicator.visibility = View.GONE
                                                    showToast(this@MainActivity, results.message)
                                                }
                                                is Result.Error -> {
                                                    binding.progressIndicator.visibility = View.GONE
                                                    showToast(this@MainActivity, results.error)
                                                }
                                                else -> {
                                                    binding.progressIndicator.visibility = View.GONE
                                                    showToast(this@MainActivity, "Something went wrong")
                                                }
                                            }
                                        }
                                    }
                                }
                                val confidencePercentage = convertToPercent(confidence)
                                moveToResult(
                                    "Dikategorikan sebagai: $category, dengan tingkat kepastian (confidence): $confidencePercentage"
                                )
                            }
                        } catch (e: Exception) {
                            onError(e.message.toString())
                        }
                    }
                }
            }
        )
        observeHistory()
    }

    private fun startGallery() {
        binding.previewImageView.setImageBitmap(null)
        currentImageUri = null
        if (!allPermissionsGranted()) {
            requestPermission.launch(REQUIRED_PERMISSION)
        }
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    
    private fun observeHistory() {

        viewModel.getAllHistory().observe(this) { historyList ->
            if (historyList != null){
                when (historyList) {
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        historyAdapter.setHistoryList(historyList.data.take(2))
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        showToast(this, historyList.error)
                    }
                    is Result.SuccessMessage -> {
                        showToast(this, historyList.message)
                    }
                }
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(File(cacheDir, "temp_cropped_img.jpg")))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
                .start(this)
        } else {
            Log.d("Image Picker", "No media selected")
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError: Throwable? = UCrop.getError(data!!)
            Log.e("Crop Error", "onActivityResult: ", cropError)
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri != null) {
            binding.progressIndicator.visibility = View.VISIBLE
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } else {
            showToast(this,getString(R.string.empty_image_warning))
        }
    }

    private fun moveToResult(prediction: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri)
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, prediction)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        observeHistory()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}