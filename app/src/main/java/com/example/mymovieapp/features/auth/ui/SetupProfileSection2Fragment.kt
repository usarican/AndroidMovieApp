package com.example.mymovieapp.features.auth.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.activityViewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentSetupProfileSection2Binding
import com.example.mymovieapp.utils.FileUtils
import com.example.mymovieapp.utils.PathHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class SetupProfileSection2Fragment :
    BaseFragment<FragmentSetupProfileSection2Binding>(R.layout.fragment_setup_profile_section2) {

    val viewModel: AuthenticationViewModel by activityViewModels()

    @Inject
    lateinit var pathHelper: PathHelper

    override fun setUpUI() {
        binding.apply {
            val countryList = pathHelper.getCountryPhoneCode() ?: emptyList()
            userPhoneEditView.setCountryList(countryList)
        }
        setupGenreListEditText()
    }

    override fun setUpListeners() {
        binding.buttonChangeProfilePhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupGenreListEditText() {
        val genreList = listOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.user_genre_list_item, genreList)
        (binding.setupSection2UserGenre.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                FileUtils(context).getPath(uri)?.let {
                    fileToBitmap(it)?.let { b ->
                        binding.setupSection2ProfileImage.setImageBitmap(b)
                    }
                }
            } else {
                Timber.tag("PhotoPicker").d("No media selected")
            }
        }

    private fun fileToBitmap(filepath: String): Bitmap? {
        var bitmap: Bitmap? = null
        val imageFile = File(filepath)
        val path = imageFile.absolutePath
        val bounds = BitmapFactory.Options()
        bounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bounds)
        val opts = BitmapFactory.Options()
        val bm = BitmapFactory.decodeFile(path, opts)
        val exif: ExifInterface
        try {
            exif = ExifInterface(path)
            val orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation = orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
            var rotationAngle = 0
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270
            val matrix = Matrix()
            matrix.setRotate(
                rotationAngle.toFloat(),
                bm.width.toFloat() / 2,
                bm.height.toFloat() / 2
            )
            bitmap = Bitmap.createBitmap(
                bm,
                0,
                0,
                bounds.outWidth,
                bounds.outHeight,
                matrix,
                true
            )
            val scaleTo = 2048
            return if (max(bm.width, bm.height) > scaleTo)
                bitmap?.scale(scaleTo)
            else bitmap

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun Bitmap.scale(maxWidthAndHeight: Int): Bitmap {
        val newWidth: Int
        val newHeight: Int

        if (this.width >= this.height) {
            val ratio: Float = this.width.toFloat() / this.height.toFloat()

            newWidth = maxWidthAndHeight
            // Calculate the new height for the scaled bitmap
            newHeight = Math.round(maxWidthAndHeight / ratio)
        } else {
            val ratio: Float = this.height.toFloat() / this.width.toFloat()

            // Calculate the new width for the scaled bitmap
            newWidth = Math.round(maxWidthAndHeight / ratio)
            newHeight = maxWidthAndHeight
        }

        return Bitmap.createScaledBitmap(
            this,
            newWidth,
            newHeight,
            false
        )
    }


}