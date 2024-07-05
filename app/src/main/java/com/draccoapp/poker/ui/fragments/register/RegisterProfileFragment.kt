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
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentRegisterProfileBinding
import com.draccoapp.poker.utils.Constants
import com.draccoapp.poker.utils.Constants.Companion.RegisterCity
import com.draccoapp.poker.utils.Constants.Companion.RegisterCountry
import com.draccoapp.poker.utils.Constants.Companion.RegisterDateBirth
import com.draccoapp.poker.utils.Constants.Companion.RegisterEmail
import com.draccoapp.poker.utils.Constants.Companion.RegisterGender
import com.draccoapp.poker.utils.Constants.Companion.RegisterName
import com.draccoapp.poker.utils.Constants.Companion.RegisterState
import com.draccoapp.poker.utils.MaskEditUtil
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.RegisterViewModel
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
    var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupUI()
        setupObservers()

    }

    private fun setupObservers() {
        viewModel.successUpload.observe(viewLifecycleOwner) {
            if (it != null) {
                imageUrl = it.url
                mostrarToast("Foto enviada com sucesso", requireContext())
            }
        }


    }


    private fun setupUI() {
        val termsOfUse = getString(R.string.termos_de_uso)
        val politicOfPrivacy = getString(R.string.pol_tica_de_privacidade)
        val text = buildString {
            append(getString(R.string.declaro_que_concordo_e_aceito_os))
            append(termsOfUse)
            append(getString(R.string.e))
            append(politicOfPrivacy)
        }

        val spannableStringBuilder = SpannableStringBuilder(text)

        val clickableSpanTerms = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController()
                    .navigate(
                        RegisterProfileFragmentDirections
                            .actionRegisterProfileFragmentToTermsUseFragment2()
                    )
            }
        }

        val clickableSpanPolitic = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController()
                    .navigate(
                        RegisterProfileFragmentDirections
                            .actionRegisterProfileFragmentToPoliticPrivacyFragment2()
                    )
            }
        }

        val startTerms = text.indexOf(termsOfUse)
        val endTerms = startTerms + termsOfUse.length

        val startPolitic = text.indexOf(politicOfPrivacy)
        val endPolitic = startPolitic + politicOfPrivacy.length

        spannableStringBuilder.setSpan(clickableSpanTerms, startTerms, endTerms, 0)
        spannableStringBuilder.setSpan(clickableSpanPolitic, startPolitic, endPolitic, 0)

        binding.checkBox.text = spannableStringBuilder
        binding.checkBox.movementMethod = LinkMovementMethod.getInstance()


        mascaraDataNascimento()

        configEditGender()
        configEditState()

        binding.imageCamera.setOnClickListener {
            openImagePicker()
        }


    }

    private fun openImagePicker() {
        val options = arrayOf<CharSequence>("Câmera", "Galeria", "Cancelar")
        AlertDialog.Builder(requireContext())
            .setTitle("Selecionar foto")
            .setItems(options) { dialog, item ->
                when {
                    options[item] == "Câmera" -> {
                        checkCameraPermission()
                    }

                    options[item] == "Galeria" -> {
                        val pickPhoto =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        activityResultContract.launch(pickPhoto)
                    }

                    options[item] == "Cancelar" -> {
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
                .setTitle("Camera permission")
                .setMessage("Camera permission required to take pictures.")
                .setPositiveButton("OK") { _, _ ->
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
                .setNegativeButton("Cancel", null)
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
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT)
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
//                "com.crosoften.eloi.fileprovider",
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
                val uri = result.data?.data
                if (uri != null) {
                    binding.shapeableImageView3.setImageURI(uri)
                    Log.i("uri", "a URI é $uri ")
                    binding.shapeableImageView3.scaleType = ImageView.ScaleType.CENTER_CROP
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = Glide.with(this@RegisterProfileFragment)
                            .asBitmap()
                            .load(uri)
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
                        val bitmap = Glide.with(this@RegisterProfileFragment)
                            .asBitmap()
                            .load(currentPhotoPath)
                            .submit()
                            .get()
                        withContext(Dispatchers.Main) {
                            binding.shapeableImageView3.setImageBitmap(bitmap)
                            binding.shapeableImageView3.scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
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


    private fun configEditState() {
        val listaDeEstados = mutableListOf(
            "AC", // Acre
            "AL", // Alagoas
            "AP", // Amapá
            "AM", // Amazonas
            "BA", // Bahia
            "CE", // Ceará
            "DF", // Distrito Federal
            "ES", // Espírito Santo
            "GO", // Goiás
            "MA", // Maranhão
            "MT", // Mato Grosso
            "MS", // Mato Grosso do Sul
            "MG", // Minas Gerais
            "PA", // Pará
            "PB", // Paraíba
            "PR", // Paraná
            "PE", // Pernambuco
            "PI", // Piauí
            "RJ", // Rio de Janeiro
            "RN", // Rio Grande do Norte
            "RS", // Rio Grande do Sul
            "RO", // Rondônia
            "RR", // Roraima
            "SC", // Santa Catarina
            "SP", // São Paulo
            "SE", // Sergipe
            "TO"  // Tocantins
        )
        val editState = binding.editState
        editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeEstados))
    }

    private fun configEditGender() {
        val listaDeGeneros = mutableListOf("Male", "Female")
        val editGender = binding.editGender
        editGender.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeGeneros))
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
                val gender = editGender
                val country = editCountry
                val state = editState
                val city = editCity
                val checkBox = checkBox

                //Salvando variáveis
                RegisterName = name.text.toString()
                RegisterDateBirth = date.text.toString()
                RegisterGender = gender.text.toString()
                RegisterCountry = country.text.toString()
                RegisterState = state.text.toString()
                RegisterCity = city.text.toString()

                Log.i("registerDados", "Dados do usuario $RegisterName salvos com sucesso")

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

}

fun validateEditTexts(vararg editTexts: EditText): Boolean {
    return editTexts.all { it.text.toString().isNotEmpty() }
}