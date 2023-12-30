package com.aydee.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aydee.adminwaveoffood.databinding.ActivityAddItemBinding
import com.aydee.adminwaveoffood.model.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngedients: String
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnAddItem.setOnClickListener {
            // get text from edit text
            foodName = binding.etFoodName.text.toString().trim()
            foodPrice = binding.etFoodPrice.text.toString().trim()
            foodDescription = binding.etFoodDescription.text.toString().trim()
            foodIngedients = binding.etFoodIngridients.text.toString().trim()

            if(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngedients.isBlank()){
                showToast("Fill all the Details")
            }
            else{
                uploadData()
            }
        }

        binding.txtSelectImage.setOnClickListener {
            //pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            pickImage.launch("image/*")
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        // get a reference to the "menu" node in database
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key
        if(foodImageUri != null){
            // get firebase storage reference
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Create a new menu item
                    val newItem = ItemModel(
                        newItemKey,
                        foodName,
                        foodPrice,
                        foodDescription,
                        foodIngedients,
                        uri.toString()
                    )
                    newItemKey?.let { key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            showToast("Data uploaded successfully")
                            finish()
                        }
                            .addOnFailureListener {
                                showToast("Data uploading Failed")
                            }
                    }
                }
                    .addOnFailureListener {
                        showToast("Image url download Failed")
                    }
            }
                .addOnFailureListener {
                    showToast("Image didn't uploaded")
                }
        }
        else {
            showToast("Select the image")
        }
    }

    // pick and show selected image in image view
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            binding.imgSelectedItemImage.setImageURI(uri)
            foodImageUri = uri
        }
    }

//    private val pickImage: ActivityResultLauncher<PickVisualMediaRequest> =
//        registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
//            if(uri != null){
//                binding.imgSelectedItemImage.setImageURI(uri)
//            }
//        }

    // show Toast message
    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}