package com.synckware.fastframeworklite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	//Referente ao banco
	private static final String NOME_DO_BANCO = "database_itens.db";
	private static final int VERSAO_DO_BANCO = 5;

	//Referente a tabela item
	public static final String TABLE_ITENS = "itens";
	public static final String TABLE_ITENS_COLUMN_ID = "_id" ;
	public static final String TABLE_ITENS_COLUMN_NAME = "name";
	public static final String TABLE_ITENS_COLUMN_DESCRIPTION = "description";
	public static final String TABLE_ITENS_COLUMN_CATEGORY = "category";
	public static final String TABLE_ITENS_COLUMN_IS_FAVORITE = "is_favorite";

	// SQL para criaçao da tabela item
	private static final String SQL_CREATE_TABLE_NOTAS = " CREATE TABLE "
			+ TABLE_ITENS + " ( "
			+ TABLE_ITENS_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , "
			+ TABLE_ITENS_COLUMN_NAME + " TEXT NOT NULL , "
			+ TABLE_ITENS_COLUMN_DESCRIPTION + " TEXT NOT NULL , "
			+ TABLE_ITENS_COLUMN_CATEGORY + " TEXT NOT NULL , "
			+ TABLE_ITENS_COLUMN_IS_FAVORITE + " INTEGER NOT NULL ) ; " ;

	//Construtor para com os parametros para sobrescrição da classe SQLiteOpenHelper
	public DatabaseOpenHelper(Context context) {
		super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
	}

	/**
	 * Cria a tabela no banco de dados, caso ela nao exista.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_NOTAS);
	}

	/**
	 * Atualiza a estrutura da tabela no banco de dados, caso sua versão tenha
	 * mudado.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITENS) ;
		onCreate(db);
	}
}