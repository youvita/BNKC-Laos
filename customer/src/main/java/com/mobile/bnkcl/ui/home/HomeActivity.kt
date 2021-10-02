package com.mobile.bnkcl.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.app.Constants.ANIMATE_LEFT
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.dialog.SystemDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.ActivityHomeBinding
import com.mobile.bnkcl.ui.dialog.LanguageDialog
import com.mobile.bnkcl.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

//    @Inject
//    lateinit var systemDialog: SystemDialog

    private var title = "Notice"
    private var message = "Under Construction"
    private var button = "Confirm"

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.color_ffffff))
        setAnimateType(ANIMATE_LEFT)
        super.onCreate(savedInstanceState)

        initLanguage()

        updateLocalize(Locale.getDefault().language)

        binding.clLease.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.clLoan.setOnClickListener {
            systemDialog =
                SystemDialog.newInstance(R.drawable.ic_badge_error, title, message, button)
            systemDialog.show(supportFragmentManager, systemDialog.tag)
        }
    }

    private fun initLanguage() {
        binding.clChangeLanguage.setOnClickListener {
            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, languageDialog.tag)
            languageDialog.onLangSelected {
                updateLocalize(it)
            }
        }
    }

    /**
     * update localize directly
     */
    private fun updateLocalize(language: String) {
        when (language) {
            "lo" -> {
                binding.load = "ທ່ານຕ້ອງການເງິນກູ້ບໍ?"
                binding.leas = "ທ່ານຕ້ອງການເຊົ່າສິນເຊື່ອບໍ?"
                binding.desc = "(ສິນເຊື່ອລົດໃຫຍ່, ລົດຈັກ, ໂທລະສັບ ແລະ ອື່ນໆ)"

                title = "ແຈ້ງການ"
                message = "Under Construction"
                button = "Confirm"
            }
            else -> {
                binding.load = "Need a Loan?"
                binding.leas = "Need a Leasing?"
                binding.desc = "(car, motorcycle, mobile phone etc)"

                title = "Notice"
                message = "Under Construction"
                button = "Confirm"
            }
        }
        binding.localeCode = language
    }
}