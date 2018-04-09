package com.synckware.fastframeworklite.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.synckware.fastframeworklite.model.Item;

public class ItemDAO {
	//Atributos
	private DatabaseOpenHelper databaseOpenHelper;
	private Context context;

	//Construdo com o contexto da atividade que deseja a conexao com a base de dados
	public ItemDAO(Context context) {
		databaseOpenHelper = new DatabaseOpenHelper(context);
		this.context = context;
	}

	/**
	 * Salva um registro no banco de dados. Caso o registro não exista na base de dados,
	 * ele sera adicionado. Caso o registro exista no base de dados, apenas
	 * atualiza os valores com base no codigo passado.
	 *
	 * @param item
	 */
	public void save(Item item) {
		/**
		 * Se o ID do registro for nulo é porque ele ainda não existe na base de
		 * dados, logo subentende-se que queremos adicionar o registro a base de
		 * dados. Sendo assim, chamaremos o método add() já definido no
		 * DAO.
		 */
		if (getItem(item.getId()) == null) {
			add(item);
		/**
		 * Caso o registro possua um ID é porque ele já existe na base de
		 * dados, logo subentende-se que queremos alterar seus valores na
		 * base de dados. Sendo assim, chamaremos o método update() já
		 * definido no DAO.
		 */
		} else {
			update(item);
		}
	}

	/**
	 * Adiciona um registro a base de dados.
	 */
	public void add(Item item) {
		/**
		 * Adiciona a um {@link ContentValues} os dados do {@link Item} que foi recebido
		 * por parametro
		 */
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME, item.getName());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_DESCRIPTION, item.getDescription());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_CATEGORY, item.getCategory());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_IS_FAVORITE, 0); // Ao adicionar um item ele entra como nao favorito

		// Instancia uma conexão com o banco de dados, em modo de gravação
		SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

		// Insere o registro na base de dados na tabela definida
		long idInsert =  db.insert(DatabaseOpenHelper.TABLE_ITENS, null, values);

		//Inseri o ID de retorno no objeto passado por parametro
		item.setId(idInsert);

		//Verificacao de insersao
		if(idInsert != -1){
			Toast.makeText(context, "Sucesso ao inserir!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "Erro ao inserir!", Toast.LENGTH_SHORT).show();
		}

		// Encerra a conexão com o banco de dados
		db.close();
	}

	/**
	 * Inseri uma copia de um item no banco de dados.
	 */
	public int insertCopy(Item item){
		/**
		 * Adiciona a um {@link ContentValues} os dados do {@link Item} que foi recebido
		 * por parametro
		 */
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME, item.getName());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_DESCRIPTION, item.getDescription());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_CATEGORY, item.getCategory());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_IS_FAVORITE, item.getIsFavorite()); // Ao adicionar um item ele entra como nao favorito

		// Instancia uma conexão com o banco de dados, em modo de gravação
		SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

		// Insere o registro na base de dados na tabela definida
		long idInsert =  db.insert(DatabaseOpenHelper.TABLE_ITENS, null, values);

		//Inseri o ID de retorno no objeto passado por parametro
		item.setId(idInsert);

		// Encerra a conexão com o banco de dados
		db.close();

		//Verificacao de insersao
		if(idInsert != -1){
			Toast.makeText(context, "Sucesso ao copiar!", Toast.LENGTH_SHORT).show();
			return 1;
		}else{
			Toast.makeText(context, "Erro ao copiar!", Toast.LENGTH_SHORT).show();
			return -1;
		}
	}

	/**
	 * Altera o registro no banco de dados.
	 */
	public void update(Item item) {
		/**
		 * Adiciona a um {@link ContentValues} os dados do {@link Item} que foi recebido
		 * por parametro
		 */
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID, item.getId());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME, item.getName());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_DESCRIPTION, item.getDescription());
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_CATEGORY, item.getCategory());

		// Instancia uma conexão com o banco de dados, em modo de gravação
		SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

		// Atualiza o registro no banco de dados
		long idInsert = db.update(DatabaseOpenHelper.TABLE_ITENS, values, DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID +"=?", new String[]{String.valueOf(item.getId())});

		//Verificacao de atualizacao
		if(idInsert != -1){
			Toast.makeText(context, "Sucesso ao atualizar!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
		}

		// Encerra a conexão com o banco de dados
		db.close();
	}

	/**
	 * Retorna um {@link Item} com o resultado da pesquisa na base de dados de acordo com um codigo passado
	 * @Obs: Equivalente ao comando SQL: SELECT * FROM table WHERE id = x;
	 */
	public Item getItem(long id) {

		//Novo item nulo
		Item item = null;

		// Instancia uma nova conexão com o banco de dados em modo leitura
		SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

		// Executa a consulta no banco de dados
		Cursor cursor = db.query(DatabaseOpenHelper.TABLE_ITENS, null, DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID +"=?", new String[]{String.valueOf(id)}, null, null,
				DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME +" ASC");

		/**
		 * Percorre o Cursor, injetando os dados consultados em um objeto do tipo {@link Item}
		 */
		try {
			while (cursor.moveToNext()) {
				//Instancia o Item com os dados do cursor
				item = cursorForItem(cursor);

				break;
			}

		} finally {
			// Encerra o Cursor
			cursor.close();
		}

		// Encerra a conexão com o banco de dados
		db.close();

		// Retorna um objeto do tipo Item (Caso ele exista, senao retorna nullo).
		return item;
	}

	/**
	 * Retorna uma {@link List<Item>} com o resultado da pesquisa na base de dados para selecionar todos os dados
	 * @Obs: Equivalente ao comando SQL: SELECT * FROM table;
	 */
	public List<Item> getAll() {
		//Cria uma item para servir de modelo para a populacao da lista
		Item item = null;
		// Cria um List guardar os items consultados no banco de dados
		List<Item> items = new ArrayList<Item>();

		// Instancia uma nova conexão com o banco de dados em modo leitura
		SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

		// Executa a consulta no banco de dados
		Cursor cursor = db.query(DatabaseOpenHelper.TABLE_ITENS, null, null, null, null, null,
				DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME +" ASC");

		/**
		 * Percorre o Cursor, injetando os dados consultados em um pessoa do
		 * tipo Item e adicionando-os na List
		 */
		try {
			while (cursor.moveToNext()) {
				//Instancia o Item com os dados do cursor
				item = cursorForItem(cursor);
				//Adiciona o item a Lista
				items.add(item);
			}

		} finally {
			// Encerra o Cursor
			cursor.close();
		}

		// Encerra a conexão com o banco de dados
		db.close();

		// Retorna uma lista com os items consultados
		return items;
	}

	/**
	 * Retorna um {@link Item} com o resultado da pesquisa na base de dados de acordo com um codigo passado
	 * @Obs: Equivalente ao comando SQL: SELECT * FROM table WHERE category = x;
	 */
	public List<Item> getItemForCategory(String category) {

		//Cria uma item para servir de modelo para a populacao da lista
		Item item = null;
		// Cria um List guardar os items consultados no banco de dados
		List<Item> items = new ArrayList<Item>();

		// Instancia uma nova conexão com o banco de dados em modo leitura
		SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

		Cursor cursor = db.query(DatabaseOpenHelper.TABLE_ITENS, null, DatabaseOpenHelper.TABLE_ITENS_COLUMN_CATEGORY +"=?", new String[]{String.valueOf(category)}, null, null,
				DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME +" ASC");

		/**
		 * Percorre o Cursor, injetando os dados consultados em um pessoa do
		 * tipo Item e adicionando-os na List
		 */
		try {
			while (cursor.moveToNext()) {
				//Instancia o Item com os dados do cursor
				item = cursorForItem(cursor);
				//Adiciona o item a Lista
				items.add(item);
			}

		} finally {
			// Encerra o Cursor
			cursor.close();
		}

		// Encerra a conexão com o banco de dados
		db.close();

		// Retorna uma lista com os items consultados
		return items;
	}

	/**
	 * Remove um registro no banco de dados.
	 */
	public void delete(Item item) {
		// Instancia uma conexão com o banco de dados, em modo de gravação
		SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

		// Remove o registro no banco de dados
		long idInsert = db.delete(DatabaseOpenHelper.TABLE_ITENS, DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID +"=?", new String[]{String.valueOf(item.getId())});
		//Verificacao de atualizacao
		if(idInsert != -1){
			Toast.makeText(context, "Sucesso ao remover o item %s!"+item.getName(), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "Sucesso ao remover o item %s!"+item.getName(), Toast.LENGTH_SHORT).show();
		}
		// Encerra a conexão com o banco de dados
		db.close();
	}

	/**
	 * @Description: Metodo para percorrer um cursor e retornar os dados em um objeto do tipo {@link Item}
	 * @param cursor
	 * @return
	 */
	public Item cursorForItem(Cursor cursor){
		//Item que servira como modelo para inserir os dados da consulta na lista
		Item item  = new Item();

		//Insersao dos dados do Curso para um objeto do tipo Item
		item.setId(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID)));
		item.setName(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME)));
		item.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TABLE_ITENS_COLUMN_DESCRIPTION)));
		item.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TABLE_ITENS_COLUMN_CATEGORY)));
		item.setIsFavorite(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.TABLE_ITENS_COLUMN_IS_FAVORITE)));
		//Retorna o item com os dados do cursor
		return item;
	}

	/**
	 * Seta um {@link Item} como favorito
	 */
	public boolean updateIsFavorite(Item item) {
		/**
		 * Adiciona a um {@link ContentValues} os dados do {@link Item} que foi recebido
		 * por parametro
		 */
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.TABLE_ITENS_COLUMN_IS_FAVORITE,  item.getIsFavorite());

		// Instancia uma conexão com o banco de dados, em modo de gravação
		SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

		// Atualiza o registro no banco de dados
		long idInsert = db.update(DatabaseOpenHelper.TABLE_ITENS, values, DatabaseOpenHelper.TABLE_ITENS_COLUMN_ID +"=?", new String[]{String.valueOf(item.getId())});

		// Encerra a conexão com o banco de dados
		db.close();

		//Verificacao de atualizacao
		if(idInsert != -1){
			return  true;
		}else{
			return false;
		}
	}
	/**
	 * Retorna todos os itens favoritos.
	 */
	public List<Item> getAllItensFavorites(){
		//Cria uma item para servir de modelo para a populacao da lista
		Item item = null;

		// Cria um List guardar os items consultados no banco de dados
		List<Item> items = new ArrayList<Item>();

		//1 para favorito e 0 para não favorito.
		int favorite = 1;

		// Instancia uma nova conexão com o banco de dados em modo leitura
		SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

		Cursor cursor = db.query(DatabaseOpenHelper.TABLE_ITENS, null, DatabaseOpenHelper.TABLE_ITENS_COLUMN_IS_FAVORITE +"=?", new String[]{String.valueOf(favorite)}, null, null,
				DatabaseOpenHelper.TABLE_ITENS_COLUMN_NAME +" ASC");

		/**
		 * Percorre o Cursor, injetando os dados consultados em um pessoa do
		 * tipo Item e adicionando-os na List
		 */
		try {
			while (cursor.moveToNext()) {
				//Instancia o Item com os dados do cursor
				item = cursorForItem(cursor);
				//Adiciona o item a Lista
				items.add(item);
			}

		} finally {
			// Encerra o Cursor
			cursor.close();
		}

		// Encerra a conexão com o banco de dados
		db.close();

		// Retorna uma lista com os items consultados
		return items;
	}

}
