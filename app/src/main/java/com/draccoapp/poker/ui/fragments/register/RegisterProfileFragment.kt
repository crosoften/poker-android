package com.draccoapp.poker.ui.fragments.register

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
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentRegisterProfileBinding
import com.draccoapp.poker.ui.fragments.tournament.TournamentFragment.Companion.TAG
import com.draccoapp.poker.utils.Constants.Companion.RegisterCity
import com.draccoapp.poker.utils.Constants.Companion.RegisterCountry
import com.draccoapp.poker.utils.Constants.Companion.RegisterDateBirth
import com.draccoapp.poker.utils.Constants.Companion.RegisterGender
import com.draccoapp.poker.utils.Constants.Companion.RegisterImageUrl
import com.draccoapp.poker.utils.Constants.Companion.RegisterName
import com.draccoapp.poker.utils.Constants.Companion.RegisterState
import com.draccoapp.poker.utils.MaskEditUtil
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.Validation.validateEditTexts
import com.draccoapp.poker.utils.converterDataNasc
import com.draccoapp.poker.utils.mostrarToast
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


class RegisterProfileFragment : Fragment() {

    private var _binding: FragmentRegisterProfileBinding? = null
    private val binding get() = _binding!!

    private var currentPhotoPath: String? = null
    private var fotoSelecionada: MultipartBody.Part? = null
    private val viewModel: RegisterViewModel by viewModel()
    private val viewModelEstados: TournamentViewModel by viewModel()
    private var uriGaleria: Uri? = null
    private var bitmapCamera: Bitmap? = null

    private lateinit var preferences: Preferences
    private lateinit var listaDeEstadosBrasileiros: List<String>
    private lateinit var listaDeEstadosEUA: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterProfileBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializaListaDeEstados()
        onclick()
        setupUI()
        setupObservers()
        tentaFixarImagem()
        configEditCountry()
        configEditState()
    }

    private fun tentaFixarImagem() {
        //GALERIA
        if (uriGaleria != null) {
            binding.shapeableImageView3.setImageURI(uriGaleria)
            bitmapCamera = null
        }

        //CAMERA
        if (bitmapCamera != null) {
            binding.shapeableImageView3.setImageBitmap(bitmapCamera)
            uriGaleria = null
        }

        binding.shapeableImageView3.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun setupObservers() {
        viewModel.successUpload.observe(viewLifecycleOwner) {
            if (it != null) {
                RegisterImageUrl = it.url
            }
        }
    }

    private fun setupUI() {
        binding.txtTermosDeUso.setOnClickListener {
            findNavController()
                .navigate(
                    RegisterProfileFragmentDirections
                        .actionRegisterProfileFragmentToTermsUseFragment2()
                )
        }

        binding.txtPolDePrivacidade.setOnClickListener {
            findNavController()
                .navigate(
                    RegisterProfileFragmentDirections
                        .actionRegisterProfileFragmentToPoliticPrivacyFragment2()
                )
        }
        binding.checkBox.movementMethod = LinkMovementMethod.getInstance()


        mascaraDataNascimento()

        configEditGender()

        binding.imageCamera.setOnClickListener {
            openImagePicker()
        }


    }

    private fun configEditState() {
        val listaCorreta = if (binding.editCountry.text.toString() == "Brasil" || binding.editCountry.text.toString() == "Brazil") {
            listaDeEstadosBrasileiros
        } else {
            listaDeEstadosEUA
        }


        val editState = binding.editState
    //    editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaCorreta))
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
                    binding.shapeableImageView3.setImageURI(uriGaleria)
                    Log.i("uri", "a URI é $uriGaleria ")
                    binding.shapeableImageView3.scaleType = ImageView.ScaleType.CENTER_CROP
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = Glide.with(this@RegisterProfileFragment)
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
                            viewModel.uploadArquivo(fotoSelecionada)
                        }
                    }
                } else {
                    // A imagem foi capturada pela câmera
                    lifecycleScope.launch(Dispatchers.IO) {
                        bitmapCamera = Glide.with(this@RegisterProfileFragment)
                            .asBitmap()
                            .load(currentPhotoPath)
                            .submit()
                            .get()
                        withContext(Dispatchers.Main) {
                            binding.shapeableImageView3.setImageBitmap(bitmapCamera)
                            binding.shapeableImageView3.scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val stream = ByteArrayOutputStream()
                        val bitmap = bitmapCamera
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        val requestBody = RequestBody.create("image/jpeg".toMediaType(), stream.toByteArray())
                        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
                        withContext(Dispatchers.Main) {
                            fotoSelecionada = imagePart
                            viewModel.uploadArquivo(fotoSelecionada)
                        }
                    }
                }
            }
        }

    private fun configEditGender() {
        val language = preferences.getLanguage()

        val editGender = binding.editGender

        if (language == "pt") {
            val listaDeGenerosPT = mutableListOf("Masculino", "Feminino")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeGenerosPT)
            editGender.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        } else {
            val listaDeGenerosEN = mutableListOf("Male", "Female")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeGenerosEN)
            editGender.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }

    }

    private fun mascaraDataNascimento() {
        binding.editDate.addTextChangedListener(MaskEditUtil.mask(binding.editDate, MaskEditUtil.FORMAT_DATE))
    }

    private fun onclick() {
        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            buttonEnter.setOnClickListener {
                val name = editName
                val date = editDate
                val dataFormatted = converterDataNasc(editDate.text.toString())
                val gender = editGender
                val country = editCountry
                val state = editState
                val city = editCity
                val checkBox = checkBox

                //Salvando variáveis
                RegisterName = name.text.toString()
                RegisterDateBirth = dataFormatted
                RegisterGender = gender.text.toString()
                RegisterCountry = country.text.toString()
                RegisterState = state.text.toString()
                RegisterCity = city.text.toString()

                Log.i("registerDados", "Dados do usuario $RegisterName salvos com sucesso e adata é $dataFormatted")

                val allFieldsFilled = validateEditTexts(name, gender, country, state, city)

                if (!checkBox.isChecked) {
                    val stringNecessarioTermos = getString(R.string.necessario_termos)
                    mostrarToast(stringNecessarioTermos, requireContext())
                } else if (!allFieldsFilled) {
                    val stringPreencherCampos = getString(R.string.campos_necessarios)
                    mostrarToast(stringPreencherCampos, requireContext())
                } else if (date.text?.length != 10) {
                    val stringDataInvalida = getString(R.string.data_invalida)
                    mostrarToast(stringDataInvalida, requireContext())
                } else {
                    findNavController().navigate(
                        RegisterProfileFragmentDirections.actionRegisterProfileFragmentToRegisterContactFragment()
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun configEditCountry() {
        val language = preferences.getLanguage()
        Log.i(TAG, "configMoedaAtual: A linguagem no preferences atual é   $language")

        val editCountry = binding.editCountry

        if (language == "pt") {
            val listaDePaisesPT = mutableListOf("Brasil", "Estados Unidos da América")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDePaisesPT)
           // editCountry.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        } else {
            val listaDePaisesEN = mutableListOf("Brazil", "United States of America")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDePaisesEN)
          //  editCountry.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }


        binding.editCountry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val country = s.toString()
                val listaCorreta = if (country == "Brasil" || country == "Brazil") {
                    listaDeEstadosBrasileiros
                } else {
                    listaDeEstadosEUA
                }
                atualizarListaEstados(listaCorreta)
            }
        })

    }

    private fun inicializaListaDeEstados() {
        listaDeEstadosBrasileiros = viewModelEstados.listaDeEstadosBrasileiros
        listaDeEstadosEUA = viewModelEstados.listaDeEstadosEUA
    }

    fun atualizarListaEstados(lista: List<String>) {
        val editState = binding.editState
       // editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lista))
    }


}


