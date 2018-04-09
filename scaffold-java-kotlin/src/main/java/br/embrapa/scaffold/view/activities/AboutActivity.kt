/*
 * Informações sobre a criação do arquivo.
 * Autor: Mário de Araújo Carvalho
 * E-mail: mariodearaujocarvalho@gmail.com
 * GitHub: https://github.com/MarioDeAraujoCarvalho
 * Ano: 13/5/2017
 * Entrar em contado para maiores informações.
 */

package br.embrapa.scaffold.view.activities

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import br.embrapa.scaffold.util.Util
import br.embrapa.scaffold.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //Configurando o toolbar
        toolbar.setNavigationIcon(R.mipmap.ic_menu_seta_esquerda_white)
        toolbar.title = resources.getString(R.string.text_activity_about_title_toolbar)
        setSupportActionBar(toolbar)

        //Configurando NavigationBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w = window
            w.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
            w.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        //Setendo a licença na caixa de diálogo
        tv_license_use.setOnClickListener { view ->
            Util.mostrarAlertaBuilder(resources.getString(R.string.text_activity_about_license_use_title),resources.getString(R.string.text_activity_about_license_use_title_body),this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
            }
            R.id.nav_avalie_play_store -> {
                val url = resources.getString(R.string.link_download_app_or_download_play_store)
                Util.abrirURL(this@AboutActivity, url)
            }
            R.id.nav_share -> {
                val texto = resources.getString(R.string.text_share_app)

                Util.compartilhar(this@AboutActivity, texto)
            }
            R.id.nav_feedback -> Util.enviarEmail(this@AboutActivity, resources.getString(R.string.text_email_contact), resources.getString(R.string.text_send_email_subject), "")
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
    }
}