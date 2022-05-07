package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.parstagram.LoginActivity
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

class ComposeFragment : Fragment() {

    lateinit var ivPreview: ImageView
    lateinit var composeDescription: EditText

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set onclickListeners and setup logic

        ivPreview = view.findViewById(R.id.imageView)
        composeDescription = view.findViewById<EditText>(R.id.etDescription)

        //Log out user
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener{
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser() // this will now be null

            val intent = Intent(requireContext(), LoginActivity::class.java)
            Toast.makeText(requireContext(), "Successfully logged out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        //Send a new post to server
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            //Get the description they have inputted
            val description = composeDescription.text.toString()
            val user = ParseUser.getCurrentUser()
            if(photoFile != null){
                submitPost(description, user, photoFile!!)
            } else {
                //TODO Print error log message and show a toast to user
            }
        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            //Launch camera to take a picture
            onLaunchCamera()
        }
    }

    //Send a post to our Parse server
    fun submitPost(description: String, user: ParseUser, file: File){
        //Create the Post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground {exception ->
            if(exception != null){
                //Something went wrong
                Log.e(MainActivity.TAG, "Error while saving post")
                exception.printStackTrace()
                //TODO: Show a toast
            } else {
                Log.i(MainActivity.TAG, "Successfully saved post")
                composeDescription.setText("")

                //https://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-android - Fran Marzoa
                ivPreview.setImageResource(android.R.color.transparent)

                Toast.makeText(requireContext(), "Successfully created a new post", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //val ivPreview: ImageView = view.findViewById()
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}