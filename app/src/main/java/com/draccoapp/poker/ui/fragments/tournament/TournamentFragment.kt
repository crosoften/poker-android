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
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.databinding.FragmentTournamentBinding
import com.draccoapp.poker.utils.DecimalDigitsFilter
import com.draccoapp.poker.utils.MaskEditUtil
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.utils.showSnackbarGreen
import com.draccoapp.poker.utils.showSnackbarRed
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
import kotlin.math.roundToInt



class TournamentFragment : Fragment() {

    private var _binding: FragmentTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()

    private lateinit var preferences: Preferences
    private  var proofUrl: String = ""

    private var currentPhotoPath: String? = null
    private var fotoSelecionada: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTournamentBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupObservers()
        configMoedaAtual()
        configCasasDecimaisPreco()
        mascaraDataNascimento()

    }

    private fun mascaraDataNascimento() {
        binding.editStartDate.addTextChangedListener(MaskEditUtil.mask(binding.editStartDate, MaskEditUtil.FORMAT_DATE))
        binding.editFinishDate.addTextChangedListener(MaskEditUtil.mask(binding.editFinishDate, MaskEditUtil.FORMAT_DATE))
        binding.editTime.addTextChangedListener(MaskEditUtil.mask(binding.editTime, MaskEditUtil.FORMAT_HOUR))
    }

    private fun configMoedaAtual() {
        val language = preferences.getLanguage()
        Log.i(TAG, "configMoedaAtual: A linguagem no preferences atual é   $language")
        val moedaAtual = if (language == "pt") "(R$)" else "($)"
        binding.txtMoedaAtual.text = moedaAtual
    }


    private fun setup() {
        binding.imageView6.setOnClickListener {
            openImagePicker()
        }

        binding.buttonConfirmar.setOnClickListener {

            val editName = binding.editName.text.toString()
            val editAward = binding.editAward.text.toString()
            var editAwardInt: Int = 0
            editAward?.let {
                editAwardInt = arredondarValor(editAward)
            }

            val editStart = binding.editStartDate.text.toString()
            val editEnd = binding.editFinishDate.text.toString()
            val editTime = binding.editTime.text.toString()
            val editdescription = binding.editDescripiton.text.toString()
            val editCountry = binding.editCountry.text.toString()
            val editState = binding.editState.text.toString()
            val editCity = binding.editCity.text.toString()
            val editNeighborood = binding.editNeighborhood.text.toString()
            val editStreet = binding.editNeighborhood.text.toString()
            val editNumber = binding.editNumber.text.toString()
            val editZipcode = binding.editZipcode.text.toString()
            val editLink = binding.editLink.text.toString()

            val body = TournamentBodyNew(
                title = editName,
                prize = editAwardInt,
                startDatetime = editStart,
                finalDatetime = editEnd,
                time = editTime,
                description = editdescription,
                country = editCountry,
                state = editState,
                city = editCity,
                neighborhood = editNeighborood,
                street = editStreet,
                number = editNumber,
                zipCode = editZipcode,
                eventUrl = if (editLink.isNullOrEmpty()) "null" else editLink,
                proofUrl = if (proofUrl.isNullOrEmpty()) "null" else proofUrl,
            )
             if (!todosOsCamposForamPreenchidos()) {
                binding.root.showSnackbarRed(getString(R.string.necessario_preencher_todos_os_campos_acima))
            } else {
                viewModel.createTournament(body = body)
            }

        }
    }

    private fun setupObservers() {
        viewModel.successCreateTournament.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.root.showSnackbarGreen(getString(R.string.torneio_criado_com_sucesso))
                findNavController().popBackStack(R.id.homeFragment, false)

            }
        }

        viewModel.successUploadFile.observe(viewLifecycleOwner) {
            if (it != null) {
                proofUrl = it.url
                binding.imageView6.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            mostrarToast(it, requireContext())
        }
    }


    private fun todosOsCamposForamPreenchidos(): Boolean {

        val editName = binding.editName
        val editAward = binding.editAward
        val editCountry = binding.editCountry
        val editState = binding.editState
        val editCity = binding.editCity


        return editAward.text.toString().isNotEmpty() &&
                editName.text.toString().isNotEmpty() &&
                editAward.text.toString().isNotEmpty() &&
                editCountry.text.toString().isNotEmpty() &&
                editState.text.toString().isNotEmpty() &&
                editCity.text.toString().isNotEmpty()

    }

    private fun openImagePicker() {
        val options = arrayOf<CharSequence>(getString(R.string.camera), getString(R.string.galeria), getString(R.string.cancelar))
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.selecionar_comprovante))
            .setItems(options) { dialog, item ->
                when {
                    options[item] == getString(R.string.camera) -> {
                        checkCameraPermission()
                    }

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
                val uriGaleria = result.data?.data
                if (uriGaleria != null) {
                    Log.i(TAG, "a URI é $uriGaleria ")
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = Glide.with(this@TournamentFragment)
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
                            Log.i(TAG, "Chamando uploadFile da viewModel: ")
                            viewModel.uploadFile(fotoSelecionada)
                        }
                    }
                } else {
                    // A imagem foi capturada pela câmera
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmapCamera = Glide.with(this@TournamentFragment)
                            .asBitmap()
                            .load(currentPhotoPath)
                            .submit()
                            .get()
                        withContext(Dispatchers.Main) {
                        }
                        val stream = ByteArrayOutputStream()
                        val bitmap = bitmapCamera
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        val requestBody = RequestBody.create("image/jpeg".toMediaType(), stream.toByteArray())
                        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
                        withContext(Dispatchers.Main) {
                            fotoSelecionada = imagePart
                            Log.i(TAG, "Chamando uploadFile da viewModel: ")
                            viewModel.uploadFile(fotoSelecionada)
                        }
                    }
                }
            }
        }


    fun arredondarValor(valor: String): Int {
        return if (valor.isNullOrBlank()) 0 else valor.toDouble().roundToInt()
    }



    private fun configCasasDecimaisPreco() {
        val editAward = binding.editAward

        // Aplicar o DecimalDigitsInputFilter para limitar a 2 casas decimais
        editAward.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            DecimalDigitsFilter(2).filter(source, start, end, dest, dstart, dend)
        })
    }

    override fun onResume() {
        super.onResume()
         }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "TournamentFragment"
    }

}