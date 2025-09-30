package com.example.kotlinfirstless

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var actionBtn: Button
    private lateinit var switchModeTv: TextView

    private var isLoginMode = true
    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        db = UserDatabase.getDatabase(this)

        usernameEt = findViewById(R.id.et_username)
        passwordEt = findViewById(R.id.et_password)
        confirmPasswordEt = findViewById(R.id.et_confirm_password)
        actionBtn = findViewById(R.id.btn_action)
        switchModeTv = findViewById(R.id.tv_switch_mode)

        updateUI()

        actionBtn.setOnClickListener {
            if (isLoginMode) login() else register()
        }
        switchModeTv.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
        }
    }

    private fun updateUI() {
        if (isLoginMode) {
            confirmPasswordEt.visibility = View.GONE
            actionBtn.text = "Войти"
            switchModeTv.text = "Нет аккаунта? Зарегистрироваться"
        } else {
            confirmPasswordEt.visibility = View.VISIBLE
            actionBtn.text = "Зарегистрироваться"
            switchModeTv.text = "Уже есть аккаунт? Войти"
        }
    }

    private fun login() {
        val username = usernameEt.text.toString().trim()
        val password = passwordEt.text.toString().trim()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = db.userDao().login(username, password)
            runOnUiThread {
                if (user != null) {
                    // сохраняем id в SharedPreferences
                    val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putInt("current_user_id", user.id).apply()

                    Toast.makeText(this@AuthActivity, "Добро пожаловать, $username!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AuthActivity, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun register() {
        val username = usernameEt.text.toString().trim()
        val password = passwordEt.text.toString().trim()
        val confirmPassword = confirmPasswordEt.text.toString().trim()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val existingUser = db.userDao().getUser(username)
            if (existingUser != null) {
                runOnUiThread {
                    Toast.makeText(this@AuthActivity, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
                }
            } else {
                val newId = db.userDao().insertUser(User(username = username, password = password)).toInt()
                // сохраняем id как вошедшего пользователя
                val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit().putInt("current_user_id", newId).apply()

                runOnUiThread {
                    Toast.makeText(this@AuthActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                    isLoginMode = true
                    updateUI()
                    finish() // можно автоматически закрыть и считать пользователя залогиненным
                }
            }
        }
    }
}
