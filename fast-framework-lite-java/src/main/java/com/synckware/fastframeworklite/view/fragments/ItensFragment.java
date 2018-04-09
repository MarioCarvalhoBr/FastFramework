package com.synckware.fastframeworklite.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.synckware.fastframeworklite.model.Item;
import com.synckware.fastframeworklite.adapter.ItemAdapter;
import com.synckware.fastframeworklite.R;
import com.synckware.fastframeworklite.dao.ItemDAO;
import com.synckware.fastframeworklite.util.Constants;
import com.synckware.fastframeworklite.view.activities.AddOrUpdateItemActivity;

public class ItensFragment extends Fragment {

    // ID da opção "Editar" do menu de contexto
    private static final int MENU_EDITAR = Menu.FIRST + 1;

    // ID da opção "Apagar" do menu de contexto
    private static final int MENU_APAGAR = Menu.FIRST + 0;

    /**
     * Variável de instância do tipo ListView que fará referência à ListView do
     * nosso Resource Layout e será manipulado via código para ser preenchido
     * com os dados consultados no banco de dados.
     */
    private ListView listaPessoas;

    private View view;
    private Context mContext = getActivity();
    final String TAG = "TAG";
    Toolbar mToolbar;

    public ItensFragment() {
        // Required empty public constructor
    }

    public static ItensFragment newInstance(String param1, String param2) {
        ItensFragment fragment = new ItensFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_itens, container, false);


        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle("Lista de Pessoas");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            w.setNavigationBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
        }else{
             //noinspection deprecation
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddOrUpdateItemActivity.class);
                getActivity().startActivity(intent);
            }
        });
        /**
         * Fazemos a ligação entre o pessoa "listaPessoas" e a View
         * ListView do nosso Resource Layout.
         */
        listaPessoas = (ListView) view.findViewById(R.id.lv_list_itens);
        listaPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                // Declarando a Intent que irá invocar a Activity
                // "CadastraPessoaActivity"

                Intent i = new Intent(getActivity(), AddOrUpdateItemActivity.class);

                /**
                 * Passando o objeto selecionado pelo usuário, na ListView, como
                 * parâmetro para a Activity "CadastraPessoaActivity", para
                 * que esta possa editar as informações do registro selecionado.
                 */
                i.putExtra(Constants.ITEM_SELECTED, (Item) getListaObjetosEmprestados().getItemAtPosition(posicao));

                // Invoca a Activity definida em nossa Intent
                getActivity().startActivity(i);

            }
        });

        // Registra o Context Menu para a ListView da nossa tela
        registerForContextMenu(listaPessoas);

        mContext = getActivity();

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }
    /**
     * Retorna o pessoa ListView já preenchido com a lista de pessoas
     * emprestados.
     */
    public ListView getListaObjetosEmprestados() {
        return listaPessoas;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_opcoes_fragments, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sync_toolbar:
                montaListView();
                Toast.makeText(mContext, "Sinconizando os dados!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
     * Cria o menu de contexto.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opçoes");

        // Adiciona a opção "Apagar" ao Context Menu
        menu.add(0, MENU_APAGAR, 0, "Apagar");
        // Adiciona a opção "Editar" ao Context Menu
        menu.add(0, MENU_EDITAR, 0, "Editar");
    }

    /**
     * Monta a ListView com os dados a serem apresentados na tela.
     */
    private void montaListView() {

        // Consulta os items cadastrados no banco de dados através do DAO
        ItemDAO dao = new ItemDAO(
                getActivity());

        // Guarda os items consultados em uma List

        final List<Item> items = dao.getAll();

        // Instancia o Adapter que irá adaptar os dados na ListView
        ArrayAdapter<Item> adapter = new ItemAdapter(
                getActivity(), android.R.layout.simple_list_item_1, items);

        /**
         * Define o Adapter que irá adaptar os dados consultados no banco de
         * dados à nossa ListView do Resource Layout.
         *
         * @param adapter
         */
        listaPessoas.setAdapter(adapter);

    }

    /**
     * Dispara o evento da opção selecionada no menu de contexto.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /**
         * A classe "AdapterView.AdapterContextMenuInfo" irá nos ajudar a
         * extrair algumas informações do nosso Context Menu, como por exemplo a
         * posição da ListView em que a opção do menu foi clicada pelo usuário.
         * Precisaremos exatamente dessa informação para saber qual o item da
         * ListView que o usuário deseja interagir.
         */
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Trata a ação da opção "Apagar" do menu de contexto
        if (item.getItemId() == MENU_APAGAR) {

            // Obtemos o pessoa que o usuário deseja remover através da sua
            // posição na ListView
            Item pessoa = (Item) getListaObjetosEmprestados()
                    .getItemAtPosition(info.position);

            // Chama o método que irá remover o pessoa do banco de dados
            ItemDAO dao = new ItemDAO(getActivity());
            dao.delete(pessoa);

            /**
             * Mostra uma mensagem na tela informando ao usuário que a operação
             * foi realizada com sucesso
             */
            Toast.makeText(getActivity(), "Sucesso ao remover", Toast.LENGTH_LONG).show();
            montaListView();

            /**
             * Após tratar com sucesso o evento de uma opção do menu de
             * contexto, o retorno deve ser sempre "true"
             */
            return true;
        }

        // Trata a ação da opção "Ligar" do menu de contexto
        if (item.getItemId() == MENU_EDITAR) {
            // Declarando a Intent que irá invocar a Activity
            // "CadastraPessoaActivity"

            Intent i = new Intent(getActivity(), AddOrUpdateItemActivity.class);

            /**
             * Passando o objeto selecionado pelo usuário, na ListView, como
             * parâmetro para a Activity "CadastraPessoaActivity", para
             * que esta possa editar as informações do registro selecionado.
             */
            i.putExtra(Constants.ITEM_SELECTED, (Item) getListaObjetosEmprestados().getItemAtPosition(info.position));

            // Invoca a Activity definida em nossa Intent
            getActivity().startActivity(i);

        }


        return super.onContextItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Chama o método que irá montar a ListView na tela.
        montaListView();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener

                    Fragment fragment = null;
                    fragment = new HomeFragment();

                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_content, fragment)
                                .commit();
                    }

                    mToolbar.setTitle("Coffee People");
                    return true;
                }
                return false;
            }
        });
    }


}
