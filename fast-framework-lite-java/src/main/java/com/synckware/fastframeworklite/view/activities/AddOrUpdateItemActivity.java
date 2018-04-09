package com.synckware.fastframeworklite.view.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.synckware.fastframeworklite.model.Item;
import com.synckware.fastframeworklite.R;
import com.synckware.fastframeworklite.dao.ItemDAO;
import com.synckware.fastframeworklite.util.Constants;

public class AddOrUpdateItemActivity extends AppCompatActivity {

	private Item item = null;

	private EditText txtName;
	private EditText txtDescription;
	private Spinner spCategorys;

	private Button btnSave;
	private Button btnCancel;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Define o Layout Resource da Activity
		setContentView(R.layout.activity_add_or_update_item);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setNavigationIcon(R.mipmap.ic_menu_seta_esquerda_white);

		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window w = getWindow();
			w.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
			w.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
		}else{
			//noinspection deprecation
			mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		}

		txtName = (EditText) findViewById(R.id.txt_field_name);
		txtDescription = (EditText) findViewById(R.id.txt_field_description);
		spCategorys = (Spinner) findViewById(R.id.txt_field_category);

		btnSave = (Button) findViewById(R.id.cadastra_pessoa_botao_salvar);
		btnCancel = (Button) findViewById(R.id.cadastra_pessoa_botao_cancelar);

		String[] categories =  {"A","B","C","D","E"};
		ArrayAdapter adapter = new ArrayAdapter(AddOrUpdateItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
		spCategorys.setAdapter(adapter);

		spCategorys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				item.setCategory((String) spCategorys.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		/**
		 * Recebe o objeto recebido como parâmetro da ListView para edição.
		 */
		item = (Item) getIntent().getSerializableExtra(
				Constants.ITEM_SELECTED);

		if (item == null) {
			// Instancia um novo objeto do tipo Item
			item = new Item();
		} else {
			txtName.setText(item.getName());
			txtDescription.setText(item.getDescription());

			if(item.getCategory().equals("A")){
				spCategorys.setSelection(0);
			}else if(item.getCategory().equals("B")){
				spCategorys.setSelection(1);
			}else if(item.getCategory().equals("C")){
				spCategorys.setSelection(2);
			}else if(item.getCategory().equals("D")){
				spCategorys.setSelection(3);
			}else if(item.getCategory().equals("E")){
				spCategorys.setSelection(4);
			}
		}

		/**
		 * O botao salvar irá salvar o objeto no banco de dados.
		 */
		btnSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Injeta no objeto "item" os dados informados pelo usuário
				item.setName(txtName.getText().toString());
				item.setDescription(txtDescription.getText().toString());

				// Instancia o DAO para persistir o objeto
				ItemDAO dao = new ItemDAO(
						getApplicationContext());
				// Salva o objeto no banco de dados
				dao.save(item);
				finish();

			}

		});

		/**
		 * O botao cancelar apenas finaliza a Activity.
		 */
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}


}
