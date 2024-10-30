package com.example.finalprojecthobbiesconnect

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar
import java.util.Locale


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
    private lateinit var loadingSaveAnimation: com.airbnb.lottie.LottieAnimationView




    private var selectedHobbies: MutableList<String> = mutableListOf()
    private var selectedYear : Int = 0
    private var username:String = ""
    private var email:String = ""
    private var password:String = ""
    private var confirmPassword:String = ""
    private var profilePhotoUri:String =""
    private var friendsList: MutableList<String> = mutableListOf()
    private var pendingFriendsList: MutableList<String> = mutableListOf()
    private var chatList: MutableList<String> = mutableListOf()
    private var isReadPend:Boolean = true


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
        ImageLoader.getInstance().load(it, profilePhoto, R.drawable.avatar)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        profilePhotoUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + resources.getResourcePackageName(R.drawable.avatar) +
                    '/' + resources.getResourceTypeName(R.drawable.avatar) +
                    '/' + resources.getResourceEntryName(R.drawable.avatar)).toString()
        findViews()
        FuncUtlis.setupUI(this, findViewById(android.R.id.content))
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
        loadingSaveAnimation = findViewById(R.id.loading_save_animation)


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
        val user = User(username, email, selectedYear, profilePhotoUri, friendsList, pendingFriendsList, chatList,selectedHobbies,isReadPend)
        loadingSaveAnimation.visibility = View.VISIBLE
        saveBTN.visibility = View.GONE
        saveUserToFirebase(user)



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
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"

            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }
        requestGalleryLauncher.launch(intent)
    }
    private fun isPermissionGranted(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // Android 14 and above
                ContextCompat.checkSelfPermission(
                    this,
                    READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13
                ContextCompat.checkSelfPermission(
                    this,
                    READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }
            else -> {
                // Below Android 13
                ContextCompat.checkSelfPermission(
                    this,
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }




    private fun requestStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // Android 14 and above
                requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13
                requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
            }
            else -> {
                // Below Android 13
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
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
        email = emailTextField.text.toString().lowercase(Locale.ROOT)
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
    private fun saveUserToFirebase(user: User) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userEmail = auth.currentUser?.email
                    if (userEmail != null) {
                        // Upload profile image to Firebase Storage + save user details to Firebase Realtime Database
                        uploadProfileImage(userEmail, Uri.parse(user.profilePhoto))
                    }
                } else {
                    saveBTN.visibility = View.VISIBLE
                    loadingSaveAnimation.visibility = View.GONE
                    SignalManager.getInstance().vibrateAndToast("Failed to register: ${task.exception?.message}")
                }
            }
    }

    private fun uploadProfileImage(userEmail: String, parse: Uri?) {
        val userId = userEmail.replace(".", ",")

        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/${userId}.jpg")

        if (parse == null) {
            SignalManager.getInstance().vibrateAndToast("Failed to upload profile image")
            return
        }

        storageRef.putFile(parse)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveUserDetailsToDatabase(userId, uri.toString())
                }
            }
            .addOnFailureListener {
                saveBTN.visibility = View.VISIBLE
                loadingSaveAnimation.visibility = View.GONE
                SignalManager.getInstance().vibrateAndToast("Failed to upload profile image")
            }
    }

    private fun saveUserDetailsToDatabase(userId: String, profilePhotoUrl: String) {

        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        val user = User(
            username = username,
            email = email,
            birthyear = selectedYear,
            profilePhoto = profilePhotoUrl,
            friendsList = friendsList,
            pendingFriendsList = pendingFriendsList,
            chatList = chatList,
            hobbies = selectedHobbies,
            isReadPend = isReadPend
        )

        databaseRef.setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    MyActiveUserManager.setUser(user)
                    SignalManager.getInstance().toast("User registered successfully")
                    loadingSaveAnimation.visibility = View.GONE
                    saveBTN.visibility = View.VISIBLE
                    changeActivity()
                } else {
                    SignalManager.getInstance().vibrateAndToast("Failed to save user details")
                }
            }
    }

    private fun changeActivity() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }




}







