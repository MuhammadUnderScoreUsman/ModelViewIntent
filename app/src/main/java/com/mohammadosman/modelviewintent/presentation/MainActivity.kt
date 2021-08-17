package com.mohammadosman.modelviewintent.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.mohammadosman.modelviewintent.R
import com.mohammadosman.modelviewintent.databinding.ActivityMainBinding
import com.mohammadosman.modelviewintent.presentation.ui.AccountFragment
import com.mohammadosman.modelviewintent.presentation.ui.NewFeedsFragment
import com.mohammadosman.modelviewintent.presentation.ui.ProfileFragment
import com.mohammadosman.modelviewintent.presentation.util.TAG

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var newFeedsFragment: NewFeedsFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var accountFragment: AccountFragment


    private val fragments: Array<Fragment>
        get() = arrayOf(
            newFeedsFragment,
            profileFragment,
            accountFragment
        )

    private var selectedIdx = 0

    private val selectedFragment get() = fragments[selectedIdx]

    private fun selectFragment(selectedFragment: Fragment) {

        var transaction = supportFragmentManager.beginTransaction()

        fragments.forEachIndexed { idx, itmFragment ->
            if (selectedFragment == itmFragment) {
                Log.d(
                    TAG,
                    "selectedFragment: $selectedFragment || fragment: $itmFragment || fragIdx: $selectedIdx"
                )
                transaction = transaction.attach(itmFragment)
                selectedIdx = idx
            } else {
                transaction = transaction.detach(itmFragment)
            }
        }

        transaction.commit()

        title = when (selectedFragment) {
            is AccountFragment -> getString(R.string.title_account)
            is ProfileFragment -> getString(R.string.title_profile)
            is NewFeedsFragment -> getString(R.string.title_new_feeds)
            else -> ""
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            newFeedsFragment = NewFeedsFragment()
            accountFragment = AccountFragment()
            profileFragment = ProfileFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.frag_cont, newFeedsFragment, TAG_NEW_FEEDS_FRAGMENT)
                .add(R.id.frag_cont, profileFragment, TAG_PROFILE_FRAGMENT)
                .add(R.id.frag_cont, accountFragment, TAG_ACCOUNT_FRAGMENT)
                .commit()

        } else {
            newFeedsFragment = supportFragmentManager.findFragmentByTag(
                TAG_NEW_FEEDS_FRAGMENT
            ) as NewFeedsFragment

            accountFragment = supportFragmentManager.findFragmentByTag(
                TAG_ACCOUNT_FRAGMENT
            ) as AccountFragment

            profileFragment = supportFragmentManager.findFragmentByTag(
                TAG_PROFILE_FRAGMENT
            ) as ProfileFragment


            selectedIdx = savedInstanceState.getInt(KEY_SELECTED_IDX, 0)
        }

        selectFragment(selectedFragment)

        binding.botNav.setOnNavigationItemSelectedListener { itm ->
            val fragment = when (itm.itemId) {
                R.id.action_newFeeds -> newFeedsFragment
                R.id.action_profile -> profileFragment
                R.id.action_account -> accountFragment
                else -> throw IllegalAccessException("Unexpected itmId")
            }

            selectFragment(fragment)
            true
        }
    }

    override fun onBackPressed() {
        if (selectedIdx != 0) {
            binding.botNav.selectedItemId = R.id.action_newFeeds
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_IDX, selectedIdx)
    }

    companion object{
        const val TAG_NEW_FEEDS_FRAGMENT = "TAG_NEW_FEEDS_FRAGMENT"
        const val TAG_ACCOUNT_FRAGMENT = "TAG_ACCOUNT_FRAGMENT"
        const val TAG_PROFILE_FRAGMENT = "TAG_PROFILE_FRAGMENT"
        const val KEY_SELECTED_IDX = "KEY_SELECTED_IDX"
    }
}
