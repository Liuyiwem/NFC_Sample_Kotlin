package com.example.nfc_sample_kotlin.view


import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nfc_sample_kotlin.base.BaseActivity
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.viewmodel.ActivityViewModel
import com.example.nfc_sample_kotlin.databinding.ActivityMainBinding
import com.example.nfc_sample_kotlin.logi
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController
    private var mNfcAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private val viewModel by viewModel<ActivityViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        checkNFCAdapter()
        logi("onCreate")
    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.enableForegroundDispatch(this, mPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        logi("onDestroy")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
            viewModel.setNewIntent(intent)
            logi("onNewIntent: ${intent.hashCode()}")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(super.binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNav
        bottomNavigationView.outlineProvider = null
        bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.scanFragment,R.id.writeFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    private fun checkNFCAdapter() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter == null) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("This device doesn't support NFC.")
            builder.setPositiveButton("Cancel", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        }

        if (!mNfcAdapter!!.isEnabled) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("NFC Disabled")
            builder.setMessage("Plesae Enable NFC")
            builder.setPositiveButton("Settings") { _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
            builder.setNegativeButton("Cancel", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        }

        if (mNfcAdapter!!.isEnabled) {
            mPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, this.javaClass)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
        }
    }
}