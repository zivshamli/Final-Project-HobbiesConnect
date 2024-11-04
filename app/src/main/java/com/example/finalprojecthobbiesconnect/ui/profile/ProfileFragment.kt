package com.example.finalprojecthobbiesconnect.ui.profile

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.finalprojecthobbiesconnect.MainActivity
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.FragmentProfileBinding
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SharedPreferencesManagerV3
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.checkerframework.checker.units.qual.C

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var selectedHobbies: MutableList<String> =MyActiveUserManager.getUser().hobbies
    private  var profilePhotoUri:String =MyActiveUserManager.getUser().profilePhoto

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding =  FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews()



        return root
    }





    private fun initViews() {
        initTextViews()
        initProfileImage()
        refreshMyHobbiesText()
        initHobbiesChipGroup()
        initUploadPhotoBTN()
        initProfileLogoutBTN()
        initSaveChangesBTN()

    }


    private fun initSaveChangesBTN() {
        binding.profileSaveBTN.setOnClickListener{saveChanges()}
    }

    private fun uploadProfileImage(userId: String, parse: Uri?) {

        // Check if the selected image is the same as the current profile photo
        if (profilePhotoUri != MyActiveUserManager.getUser().profilePhoto) {
            // Upload profile image to Firebase Storage
            val storageRef =
                FirebaseStorage.getInstance().reference.child("profile_images/${userId}.jpg")

            if (parse == null) {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER_PROFILE)
                return
            }

            storageRef.putFile(parse)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveUserChangePhotoToDatabase(userId, uri.toString())

                    }
                }
                .addOnFailureListener {

                    SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER_PROFILE)

                }
        }
        else {
            SignalManager.getInstance().toast(Constants.MESSAGE_NO_CHANGES_PHOTO)
        }
    }

    private fun saveUserChangePhotoToDatabase(userId: String, photoUrl: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child(Constants.USERS_REF).child(userId)

        // Update profile photo in the database
        databaseRef.child(Constants.PROFILE_PHOTO_REF).setValue(photoUrl)
            .addOnSuccessListener {
                MyActiveUserManager.getUser().profilePhoto = photoUrl
                SignalManager.getInstance().toast(Constants.MESSAGE_PHOTO_UPDATED)
            }
            .addOnFailureListener {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER_PROFILE)
            }

    }
    private fun saveUserChangeHobbiesToDatabase(userId: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child(Constants.USERS_REF).child(userId)

        // Update hobbies in the database
        databaseRef.child(Constants.HOBBIES_REF).setValue(selectedHobbies)
            .addOnSuccessListener {
                MyActiveUserManager.getUser().hobbies = selectedHobbies

                SignalManager.getInstance().toast(Constants.MESSAGE_HOBBIES_UPDATED)
            }
            .addOnFailureListener {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_UPDATE_HOBBIES)
            }
    }

    private fun saveChanges() {
        val userId=MyActiveUserManager.getUser().email.replace(".",",")
        uploadProfileImage(userId, Uri.parse(profilePhotoUri))
        saveUserChangeHobbiesToDatabase(userId)



    }

    private fun initProfileLogoutBTN(){
        binding.profileLogoutBTN.setOnClickListener{logout()}
    }


    private fun initProfileImage() {
        val profileUrl = MyActiveUserManager.getUser().profilePhoto
        ImageLoader.getInstance().load(profileUrl, binding.profilePhoto, R.drawable.avatar)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        MyActiveUserManager.logout()
        OtherUserManager.getInstance().logout()
        SharedPreferencesManagerV3.getInstance().clear()
        changeActivityToLogin()
    }

    private fun changeActivityToLogin() {
        val intent = Intent(this.activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    private fun initTextViews() {
        binding.profileNameTextView.text = MyActiveUserManager.getUser().username
       val age=MyActiveUserManager.getUser().getAge()
        binding.profileAgeText.text =buildString {
        append("Age: ")
        append(age.toString())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initHobbiesChipGroup() {
        selectedHobbies=MyActiveUserManager.getUser().hobbies

        Constants.HOBBIES_LIST.forEach { hobby ->
            val chip = Chip(this.activity).apply {
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
            chip.isChecked = selectedHobbies.contains(chip.text.toString())
            if (chip.isChecked) {

                chip.setTextColor(
                    ContextCompat.getColor(
                        this.requireContext(),
                        android.R.color.white
                    )
                )
                chip.chipBackgroundColor =
                    context?.let { ContextCompat.getColorStateList(it, R.color.blue_800) }

            }

            // when i click on a chip
            chip.setOnCheckedChangeListener { _, isChecked ->



                if (isChecked && selectedHobbies.size >=Constants.HOBBIES_LIMIT) {
                    chip.isChecked = false
                    selectedHobbies.remove(chip.text.toString())
                    SignalManager.getInstance().vibrateAndToast(Constants.HOBBIES_LIMIT_MESSAGE)
                }
                else if(isChecked){
                    chip.setTextColor(ContextCompat.getColor(this.requireContext(), android.R.color.white))
                    chip.chipBackgroundColor = ContextCompat.getColorStateList(this.requireContext(), R.color.blue_800)
                    if(!selectedHobbies.contains(chip.text.toString())) {
                        selectedHobbies.add(chip.text.toString())
                        refreshMyHobbiesText()
                    }
                }
                else {
                    chip.setTextColor(ContextCompat.getColor(this.requireContext(), android.R.color.black))
                    chip.chipBackgroundColor = ContextCompat.getColorStateList(this.requireContext(), android.R.color.white)
                    selectedHobbies.remove(chip.text.toString())
                    refreshMyHobbiesText()
                }

            }
            binding.profileChipGroupHobbies.addView(chip)

        }

    }



    private fun refreshMyHobbiesText() {
        binding.profileMyHobbiesText.text = buildString {
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

    private fun initUploadPhotoBTN() {
        binding.profileUploadPhotoBTN.setOnClickListener{uploadPhoto()}
    }

    private fun uploadPhoto() {
        requestStoragePermission()
        if (isPermissionGranted()) {
            openGallery()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ContextCompat.checkSelfPermission(
                this.requireContext(),
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this.requireContext(),
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        requestGalleryLauncher.launch(intent)

    }
    private fun handleImageSelected(it: Uri) {
        ImageLoader.getInstance().load(it,binding.profilePhoto, R.drawable.avatar)

    }

}