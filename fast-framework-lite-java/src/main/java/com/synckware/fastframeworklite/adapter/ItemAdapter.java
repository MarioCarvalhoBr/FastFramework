package com.synckware.fastframeworklite.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.synckware.fastframeworklite.R;
import com.synckware.fastframeworklite.model.Item;

public class ItemAdapter extends ArrayAdapter<Item> {
	//Atributos
	private final List<Item> items; // Lista de Itens
	private final Activity activity; // Atividade com o contexto da lista a ser adaptada

	//Construtor com os parametros de entrada

	/**
	 * @Description: Construtor mínimo para fazer a adaptação dos dados
	 * @param activity
	 * @param textViewResourceId
	 * @param items
	 */
	public ItemAdapter(Activity activity, int textViewResourceId, List<Item> items) {
		//Chamada do metodo pai
		super(activity, textViewResourceId, items);
		//Insersao da atividade
		this.activity = activity;
		//Lista de itens do tipo Item recebida como parâmetro pelo construtor
		this.items = items;
	}

	/**
	 * As views definidas no layout "model_item_list.xml" são setadas com
	 * os dados da lista passada por parâmetro (carregadas do banco de dados).
	 * Este Resource Layout será retornado como um item do tipo View
	 * para ser adicionado na ListView do definida no layout
	 * "fragment_activity_lista_itens.xml". Este método será chamado para cada
	 * item da lista passada por parametro, fazendo com que a ListView
	 * mostre todos os itens passados.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**
		 * Obtemos cada item do tipo Item dentro da List recebida
		 * pelo Construtor, de acordo com a sua posição, para ser mostrado na
		 * mesma posição da ListView, ou seja, o primeiro item da List vai
		 * para a primeira posição da ListView e assim sucessivamente.
		 */
		Item item = items.get(position);

		/**
		 * Referenciamos o Resource Layout "model_item_list" em
		 * um item do tipo View, que será o item de retorno deste método.
		 * Esta é a View que será adaptada e retornada para ser apresentada na
		 * ListView.
		 */
		View view = activity.getLayoutInflater().inflate(
				R.layout.model_item_list, null);

		/**
		 * Injeta o valor do campo referente ao nome do item emprestado, do
		 * registro consultado no banco de dados, na TextView de ID
		 * "item_nome".
		 */
		TextView txtNomeDoItem = (TextView) view.findViewById(R.id.txt_item_nome);
		txtNomeDoItem.setText(item.getName());

		/**
		 * Injeta o valor do campo referente ao nome da item para quem o
		 * item foi emprestado, do registro consultado no banco de dados, na
		 * TextView de ID "txt_item_descricao".
		 */
		TextView txtDescricao = (TextView) view.findViewById(R.id.txt_item_descricao);
		txtDescricao.setText(item.getDescription());

		// Retorna a View já adaptada para ser apresentada na ListView
		return view;
	}

	/**
	 * Retorna o ID de um determinado item da ListView, de acordo com a sua
	 * posição.
	 */
	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	/**
	 * Retorna o número de itens que serão mostrados na ListView.
	 */
	@Override
	public int getCount() {
		return super.getCount();
	}

	/**
	 * Retorna um determinado item da ListView, de acordo com a sua posição.
	 */
	@Override
	public Item getItem(int position) {
		return items.get(position);
	}

}
