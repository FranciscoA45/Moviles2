package com.example.x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.x.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciar.setOnClickListener { validarDatos() }
        binding.passwordIcon.setOnClickListener { togglePasswordVisibility() }
      //  binding.textViewForgotPassword.setOnClickListener { onForgotPasswordClicked() }
    }

    private fun validarDatos() {
        val email = binding.emailET.text.toString()
        val contraseña = binding.passwordET.text.toString()
        val patternEmail = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        var isValid = true

        if (email.isBlank()) {
            binding.emailET.error = "Por favor, ingresa tu correo electrónico"
            isValid = false
        } else if (!patternEmail.matcher(email).matches()) {
            binding.emailET.error = "Por favor, ingresa un correo electrónico válido"
            isValid = false
        } else {
            binding.emailET.error = null
        }

        if (contraseña.isBlank()) {
            binding.passwordET.error = "Por favor, ingresa tu contraseña"
            isValid = false
        } else if (!(contraseña.length in 8..16)) {
            binding.passwordET.error = "La contraseña debe tener entre 8 y 16 caracteres"
            isValid = false
        } else {
            binding.passwordET.error = null
        }

        if (isValid) {
            Toast.makeText(this, "Su login ha sido exitoso", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun togglePasswordVisibility() {
        val editText = binding.passwordET
        val imageView = binding.passwordIcon
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageView.setImageResource(R.drawable.visible_password)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.hide_password)
        }
        editText.setSelection(editText.text.length)
    }

    private fun onForgotPasswordClicked() {
        startActivity(Intent(this, Recuperacion::class.java))
    }

    fun RegisterInClicked(view: View) {
        startActivity(Intent(this, Register::class.java))
    }
}
