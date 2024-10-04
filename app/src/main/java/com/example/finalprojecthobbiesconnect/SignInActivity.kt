package com.example.finalprojecthobbiesconnect

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar


class SignInActivity : AppCompatActivity() {
    private lateinit var profilePhoto: CircleImageView
    private lateinit var nameTextField: TextInputEditText
    private lateinit var emailTextField: TextInputEditText
    private lateinit var passwordTextField: TextInputEditText
    private lateinit var confirmPasswordTextField: TextInputEditText
    private lateinit var yearPicker: Spinner
    private lateinit var chipGroupHobbies: ChipGroup
    private lateinit var saveBTN: MaterialButton
    private lateinit var uploadPhotoBTN: MaterialButton
    private lateinit var myHobbiesText: MaterialTextView




    private var selectedHobbies: MutableList<String> = mutableListOf()
    private var selectedYear : Int = 0
    private var username:String = ""
    private var email:String = ""
    private var password:String = ""
    private var confirmPassword:String = ""

    private var profilePhotoUri:String = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
            "://" + resources.getResourcePackageName(R.drawable.avatar) +
            '/' + resources.getResourceTypeName(R.drawable.avatar) +
            '/' + resources.getResourceEntryName(R.drawable.avatar)).toString()

    private var friendsList: MutableList<String> = mutableListOf()
    private var pendingFriendsList: MutableList<String> = mutableListOf()
    private var chatList: MutableList<String> = mutableListOf()


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            SignalManager.getInstance().vibrateAndToast("Permission denied")
        }
    }

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                SignalManager.getInstance().toast("Image selected")
                handleImageSelected(it)
                profilePhotoUri=it.toString()
            }
        }
    }

    private fun handleImageSelected(it: Uri) {
        ImageLoader.getInstance().load(it, profilePhoto)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        findViews()
        initViews()
    }



    private fun findViews() {
        profilePhoto = findViewById(R.id.profile_photo)
        nameTextField = findViewById(R.id.name_text_field)
        emailTextField= findViewById(R.id.sign_up_email_text_field)
        passwordTextField = findViewById(R.id.sign_up_password_text_field)
        confirmPasswordTextField = findViewById(R.id.confirm_password_text_field)
        yearPicker = findViewById(R.id.sign_up_year_picker)
        chipGroupHobbies = findViewById(R.id.sign_up_chip_group_hobbies)
        uploadPhotoBTN = findViewById(R.id.upload_photo_BTN)
        myHobbiesText = findViewById(R.id.my_hobbies_text)
        saveBTN = findViewById(R.id.save_BTN)


    }
    private fun initViews() {
        initYearSpinner()
        initHobbiesChipGroup()
        initUploadPhotoBTN()
        initSaveBTN()
    }



    private fun initSaveBTN() {
        saveBTN.setOnClickListener {saveUser()}
    }

    private fun saveUser() {
        if (!validateFields()) {
            return
        }
        val user = User(
            username = username,
            email = email,
            password = password,
            selectedYear = selectedYear,
            profilePhoto = profilePhotoUri,
            friendsList = friendsList,
            pendingFriendsList = pendingFriendsList,
            chatList = chatList
        )
        saveUserToFirebase(user)
          

    }

    private fun saveUserToFirebase(user: User): User {
       return user

    }

    private fun initYearSpinner() {
        val years = mutableListOf<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1921..currentYear) {
            years.add(i.toString())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearPicker.adapter = adapter
    }
    private fun initHobbiesChipGroup() {
        Constants.HOBBIES_LIST.forEach { hobby ->
            val chip = Chip(this).apply {
                text = hobby
                isCheckable = true
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.black
                    )
                )
                chipBackgroundColor =
                    ContextCompat.getColorStateList(context, android.R.color.white)  // רקע לבן

            }

            // when i click on a chip
            chip.setOnCheckedChangeListener { _, isChecked ->



                if (isChecked && selectedHobbies.size >=5) {
                    chip.isChecked = false
                    selectedHobbies.remove(chip.text.toString())
                    SignalManager.getInstance().vibrateAndToast("You can't select more than 5 hobbies")
                }
                else if(isChecked){
                    chip.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    chip.chipBackgroundColor = ContextCompat.getColorStateList(this, R.color.blue_800)
                    selectedHobbies.add(chip.text.toString())
                    refreshMyHobbiesText()
                }
                else {
                    chip.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                    chip.chipBackgroundColor = ContextCompat.getColorStateList(this, android.R.color.white)
                    selectedHobbies.remove(chip.text.toString())
                    refreshMyHobbiesText()
                }

            }
            chipGroupHobbies.addView(chip)

        }
    }
    private fun initUploadPhotoBTN() {
        uploadPhotoBTN.setOnClickListener{uploadPhoto()}
    }

    private fun uploadPhoto() {
        requestStoragePermission()
        if (isPermissionGranted()) {
            openGallery()
        }
    }

    private fun openGallery() {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
        requestGalleryLauncher.launch(intent)


    }
    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }




    private fun requestStoragePermission() {

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
} else {
    requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
}




    }




    private fun refreshMyHobbiesText() {
        myHobbiesText.text = buildString {
            append("My hobbies: \n")
            selectedHobbies.forEach { hobby ->
                if (hobby != selectedHobbies.last())
                    append("$hobby , ")
                else {
                    append(hobby)
                }

            }
        }
    }
        private fun validateFields(): Boolean {
            username = nameTextField.text.toString()
            email = emailTextField.text.toString()
            password = passwordTextField.text.toString()
            confirmPassword = confirmPasswordTextField.text.toString()
            selectedYear = yearPicker.selectedItem.toString().toInt()
            if(username=="") {
                SignalManager.getInstance().vibrateAndToast("Please enter your name")
                return false
            }
            if(email=="") {
                SignalManager.getInstance().vibrateAndToast("Please enter your email")
                return false
            }
            if(password=="") {
                SignalManager.getInstance().vibrateAndToast("Please enter your password")
                return false
            }
            if(confirmPassword=="") {
                SignalManager.getInstance().vibrateAndToast("Please confirm your password")
                return false
            }
            if(password!=confirmPassword) {
                SignalManager.getInstance().vibrateAndToast("Passwords don't match")
                return false
            }
            return true


        }

}





