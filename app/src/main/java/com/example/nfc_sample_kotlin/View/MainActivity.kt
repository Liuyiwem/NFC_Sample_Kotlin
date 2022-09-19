package com.example.nfc_sample_kotlin.View


import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nfc_sample_kotlin.Base.BaseActivity
import com.example.nfc_sample_kotlin.R
import com.example.nfc_sample_kotlin.TAG
import com.example.nfc_sample_kotlin.ViewModel.ActivityViewModel
import com.example.nfc_sample_kotlin.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController
    private var mNfcAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private val viewModel: ActivityViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        checkNFCAdapter()
    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.enableForegroundDispatch(this, mPendingIntent, null, null)

    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
//           val rawNdefMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            viewModel.setNewIntent(intent)

            Log.d(TAG, "onNewIntent: ${intent.hashCode()}")

        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(super.binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNav
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