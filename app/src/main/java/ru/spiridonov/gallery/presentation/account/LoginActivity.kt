package ru.spiridonov.gallery.presentation.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.databinding.ActivityLoginBinding
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val component by lazy {
        (application as GalleryApp).component
    }
    private var isModeLogin = true

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        buttonListener()
        addTextChangeListeners()
        observeViewModel()
    }

    private fun buttonListener() = with(binding) {
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel!!.login(
                viewModel!!.parseStroke(etEmail.text.toString()),
                viewModel!!.parseStroke(etPassword.text.toString())
            )
        }
        register.setOnClickListener {
            if (isModeLogin) {
                tilUsername.visibility = View.VISIBLE
                isModeLogin = false
                login.visibility = View.GONE
            } else {
                loading.visibility = View.VISIBLE
                viewModel!!.register(
                    User(
                        email = viewModel!!.parseStroke(etEmail.text.toString()),
                        passwordHash = viewModel!!.parseStroke(etPassword.text.toString()),
                        username = viewModel!!.parseStroke(etUsername.text.toString()),
                        user_id = "",
                        dateCreated = "",
                        accessToken = ""
                    )
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) {
            binding.loading.visibility = View.GONE
            if (it != null) {
                Toast.makeText(this, "Успешно, ${it.username}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.error.observe(this) {
            binding.loading.visibility = View.GONE
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorInputEmail.observe(this) {
            if (it != null)
                binding.loading.visibility = View.GONE
        }
        viewModel.errorInputPassword.observe(this) {
            if (it != null)
                binding.loading.visibility = View.GONE
        }
        viewModel.errorInputUsername.observe(this) {
            if (it != null)
                binding.loading.visibility = View.GONE
        }
    }

    private fun addTextChangeListeners() =
        with(binding) {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputEmail()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputPassword()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputUsername()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}