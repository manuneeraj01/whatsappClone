package com.example.whatsappclone

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.whatsappclone.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.Timer
import java.util.TimerTask


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    private lateinit var db:DocumentReference
    private lateinit var uid:String
    private lateinit var storageReference:StorageReference
    private lateinit var image: ByteArray

    val register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        uploadImage(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        uid = auth.currentUser?.uid.toString()
        storageReference = FirebaseStorage.getInstance().reference.child("$uid/profilePhoto")
        db = fStore.collection("users").document(uid)
        db.addSnapshotListener{value, error->
            if(error != null){
                Log.d(TAG, "Error : Unable to process data")
            }
            else{
                binding.tvUserName.text = value?.getString("userName")
                binding.tvUserEmail.text = value?.getString("userEmail")
                binding.tvUserStatus.text = value?.getString("userStatus")
                Picasso.get().load(value?.getString("userProfilePhoto")).error(R.drawable.user).into(binding.ivProfile)
            }

        }
        binding.apply {
            btnUpdate.visibility = View.VISIBLE
            /*tvUserName.text = "Name is Noob"
            tvUserEmail.text = "Email is NoobEmail"
            tvUserStatus.text = "Code sleep and repeat"*/
            btnUpdate.setOnClickListener {
                btnSave.visibility = View.VISIBLE
                tilUserName.visibility = View.VISIBLE
                tilUserEmail.visibility = View.VISIBLE
                tilUserStatus.visibility = View.VISIBLE
                btnUpdate.visibility = View.GONE
                tvUserName.visibility = View.GONE
                tvUserEmail.visibility = View.GONE
                tvUserStatus.visibility = View.GONE
                etUserName.text = Editable.Factory.getInstance().newEditable(tvUserName.text.toString())
                etUserEmail.text = Editable.Factory.getInstance().newEditable(tvUserEmail.text.toString())
                etUserStatus.text = Editable.Factory.getInstance().newEditable(tvUserStatus.text.toString())
            }
            btnSave.setOnClickListener {
                btnSave.visibility = View.GONE
                tilUserName.visibility = View.GONE
                tilUserEmail.visibility = View.GONE
                tilUserStatus.visibility = View.GONE
                btnUpdate.visibility = View.VISIBLE
                tvUserName.visibility = View.VISIBLE
                tvUserEmail.visibility = View.VISIBLE
                tvUserStatus.visibility = View.VISIBLE
                
               val obj = mutableMapOf<String, String>()
                obj["userName"] = etUserName.text.toString()
                obj["userEmail"] = etUserEmail.text.toString()
                obj["userStatus"] = etUserStatus.text.toString()

                db.set(obj).addOnSuccessListener {
                    Log.d(TAG, "Success : Data successfully updated")
                }
            }
            ivProfileAdd.setOnClickListener{
                capturePhoto()
            }

        }

    }
    private fun capturePhoto(){
       /* val register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
            uploadImage(it)
        }*/
        register.launch(null)
    }
    private fun uploadImage(it:Bitmap?){
        val baos = ByteArrayOutputStream()
        it?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        image = baos.toByteArray()
        storageReference.putBytes(image).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                val obj = mutableMapOf<String, String>()
                obj["userProfilePhoto"] = it.toString()
                db.update(obj as Map<String, Any>).addOnSuccessListener {
                    Log.d(TAG, "onSuccess : ProfilePictureUploaded")
                }
            }
        }

    }
    companion object{
        private val TAG = ProfileFragment::class.java.simpleName
    }
}