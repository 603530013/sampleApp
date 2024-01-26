package com.mobiledrivetech.external.sample.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobiledrivetech.external.sample.R
import com.mobiledrivetech.external.sample.presentation.ui.fragments.CommandsListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CommandsListFragment.newInstance())
                .commitNow()
        }
    }
}
