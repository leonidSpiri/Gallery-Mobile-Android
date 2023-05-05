package ru.spiridonov.gallery.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.spiridonov.gallery.GalleryApp
import ru.spiridonov.gallery.R
import ru.spiridonov.gallery.databinding.ActivityMainBinding
import ru.spiridonov.gallery.presentation.activity.account.LoginActivity
import ru.spiridonov.gallery.presentation.activity.add_media.AddMediaActivity
import ru.spiridonov.gallery.presentation.viewmodels.MainViewModel
import ru.spiridonov.gallery.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val component by lazy {
        (application as GalleryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        checkIfUserLoggedIn()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrentUser()
    }

    private fun checkIfUserLoggedIn() =
        viewModel.user.observe(this) {
            if (it == null)
                startActivity(LoginActivity.newIntent(this))
            else
                setupView()
        }

    private fun setupView() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_add -> {
                startActivity(AddMediaActivity.newIntent(this))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}