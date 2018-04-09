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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText

import br.embrapa.scaffold.dao.item.ItemDAO
import br.embrapa.scaffold.model.Item
import br.embrapa.scaffold.R

class ItemActivity : AppCompatActivity() {

    private var txtNome: EditText? = null
    private var txtDescricao: EditText? = null

    private var itemDAO: ItemDAO? = null
    private var item: Item? = null

    private var codigo = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_seta_esquerda_white)

        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w = window
            w.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
            w.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary, theme))
        } else {

            mToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }

        codigo = intent.extras!!.getInt("codigo", -1)

        itemDAO = ItemDAO(this)

        initializeFilds()
        fillFilds()

    }


    fun initializeFilds() {
        txtNome = findViewById<EditText>(R.id.txt_name)
        txtDescricao = findViewById<EditText>(R.id.txt_descricao)
    }

    fun fillFilds() {
        if (codigo != -1) {
            itemDAO!!.open()
            item = itemDAO!!.getItem(codigo)
            itemDAO!!.close()

            txtNome!!.setText(item!!.nome)
            txtDescricao!!.setText(item!!.descricao)
        } else {
            item = Item()
        }
    }

    fun save(): Boolean {
        item!!.id = codigo

        if (txtNome!!.text.toString() != "") {
            item!!.nome = txtNome!!.text.toString()
        } else {
            mostrarAlertaBuilder(resources.getString(R.string.text_aviso_campo_obrigatorio) + " nome do item!")
            txtNome!!.requestFocus()
            return false
        }

        if (txtDescricao!!.text.toString() != "") {
            item!!.descricao = txtDescricao!!.text.toString()
        } else {
            mostrarAlertaBuilder(resources.getString(R.string.text_aviso_campo_obrigatorio) + " descrição!")
            txtDescricao!!.requestFocus()
            return false
        }

        itemDAO!!.open()
        if (codigo == -1) {
            itemDAO!!.insert(item)
        } else {
            itemDAO!!.update(item, item!!.id)
        }
        itemDAO!!.close()

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_item_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        } else if (item.itemId == R.id.action_adicionar) {
            if (save()) {
                finish()
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun mostrarAlertaBuilder(sms: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(sms)
        dialog.setNeutralButton("OK", null)
        dialog.create().show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
    }

}
