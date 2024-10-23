package com.draccoapp.poker.ui.fragments.tournament

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.AddUpdadeTournament
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentAddUpdateBinding
import com.draccoapp.poker.utils.Constants
import com.draccoapp.poker.viewModel.RegisterViewModel
import com.draccoapp.poker.viewModel.TournamentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddUpdateFragment : Fragment() {

    private var _binding: FragmentAddUpdateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()
    private val viewModelImage: RegisterViewModel by viewModel()

    private val args by navArgs<AddUpdateFragmentArgs>()


    private var currentPhotoPath: String? = null
    private var fotoSelecionada: MultipartBody.Part? = null
    private var uriGaleria: Uri? = null
    private var bitmapCamera: Bitmap? = null

    private var imagemUpload = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tentaFixarImagem()
        onclick()
        setupObservables()

    }

    private fun tentaFixarImagem() {
        //GALERIA
        if (uriGaleria != null) {
            binding.imageView6.setImageURI(uriGaleria)
            bitmapCamera = null
        }

        //CAMERA
        if (bitmapCamera != null) {
            binding.imageView6.setImageBitmap(bitmapCamera)
            uriGaleria = null
        }

        binding.imageView6.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun openImagePicker() {
        val options = arrayOf<CharSequence>(
            //  getString(R.string.camera),
            getString(R.string.galeria),
            getString(R.string.cancelar))
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.selecionar_comprovante))
            .setItems(options) { dialog, item ->
                when {
//                    options[item] == getString(R.string.camera) -> {
//                        checkCameraPermission()
//                    }

                    options[item] == getString(R.string.galeria) -> {
                        val pickPhoto =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        activityResultContract.launch(pickPhoto)
                    }

                    options[item] == getString(R.string.cancelar) -> {
                        dialog.dismiss()
                    }
                }
            }
            .show()
    }

    private fun checkCameraPermission() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // A permissão da câmera já foi concedida, você pode iniciar a câmera
            startCamera()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.camera_permission))
                .setMessage(getString(R.string.camera_permission_required_to_take_pictures))
                .setPositiveButton("OK") { _, _ ->
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()
                .show()
        }
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // A permissão foi concedida, você pode iniciar a câmera
                startCamera()
            } else {
                // A permissão foi negada, você pode exibir uma mensagem para o usuário ou solicitar novamente
                Toast.makeText(requireContext(), getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.draccoapp.fileprovider",
                it
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activityResultContract.launch(takePictureIntent)

        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = path
        }
    }


    private val activityResultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // A imagem foi selecionada pela galeria
//                val uri = result.data?.data
                uriGaleria = result.data?.data
                if (uriGaleria != null) {
                    binding.imageView6.setImageURI(uriGaleria)
                    Log.i("uri", "a URI é $uriGaleria ")
                    binding.imageView6.scaleType = ImageView.ScaleType.CENTER_CROP
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = Glide.with(this@AddUpdateFragment)
                            .asBitmap()
                            .load(uriGaleria)
                            .submit()
                            .get()

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        val requestBody = RequestBody.create("image/jpeg".toMediaType(), stream.toByteArray())
                        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
                        withContext(Dispatchers.Main) {
                            fotoSelecionada = imagePart
                            viewModelImage.uploadArquivo(fotoSelecionada)
                        }
                    }
                } else {
                    // A imagem foi capturada pela câmera
                    lifecycleScope.launch(Dispatchers.IO) {
                        bitmapCamera = Glide.with(this@AddUpdateFragment)
                            .asBitmap()
                            .load(currentPhotoPath)
                            .submit()
                            .get()
                        withContext(Dispatchers.Main) {
                            binding.imageView6.setImageBitmap(bitmapCamera)
                            binding.imageView6.scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val stream = ByteArrayOutputStream()
                        val bitmap = bitmapCamera
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        val requestBody = RequestBody.create("image/jpeg".toMediaType(), stream.toByteArray())
                        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
                        withContext(Dispatchers.Main) {
                            fotoSelecionada = imagePart
                            viewModelImage.uploadArquivo(fotoSelecionada)
                        }
                    }
                }
            }
        }


    private fun setupObservables() {
        viewModel.successAddUpdate.observe(viewLifecycleOwner) {
            findNavController()
                .popBackStack()

        }
        viewModelImage.successUpload.observe(viewLifecycleOwner) {
            if (it != null) {
                Constants.RegisterImageUrl = it.url
                imagemUpload = it.url
            }
        }
    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            imageView6.setOnClickListener {
                    openImagePicker()

            }

            buttonEnter.setOnClickListener {
                if (validade()){
                    viewModel.createUpdate(
                        AddUpdadeTournament(
                            subscriptionId = args.idTournament,
                            title = binding.editName.text.toString(),
                            message = binding.editMessage.text.toString(),
                            proofUrl = imagemUpload
                        )
                    )
                    findNavController()
                }
            }
        }
    }


    private fun validade(): Boolean {
        val title = binding.editName.text.toString()
        val message = binding.editMessage.text.toString()

        if (title.isEmpty() || message.isEmpty()) {
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}