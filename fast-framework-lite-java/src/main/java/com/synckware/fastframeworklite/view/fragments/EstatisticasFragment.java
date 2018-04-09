package com.synckware.fastframeworklite.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.synckware.fastframeworklite.R;
import com.synckware.fastframeworklite.dao.ItemDAO;
import com.synckware.fastframeworklite.model.Item;
import com.synckware.fastframeworklite.view.activities.AddOrUpdateItemActivity;

import java.util.List;

public class EstatisticasFragment extends Fragment {

    private TextView txtRelatorioCafe;

    private View view;
    private Context mContext = getActivity();
    final String TAG = "TAG";
    Toolbar mToolbar;

    public EstatisticasFragment() {
        // Required empty public constructor
    }

    public static EstatisticasFragment newInstance(String param1, String param2) {
        EstatisticasFragment fragment = new EstatisticasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categorys, container, false);


        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle("Estatisticas");

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
         * Fazemos a ligação entre o pessoa "txtRelatorioCafe" e a View
         * ListView do nosso Resource Layout.
         */
        txtRelatorioCafe = (TextView) view.findViewById(R.id.txt_relatorio_cafe);

        carregarEstatisticas();

        mContext = getActivity();

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }

    private void carregarEstatisticas() {
        ItemDAO dao = new ItemDAO(getActivity());
        List<Item> cafeA = dao.getItemForCategory("A");
        List<Item> cafeB = dao.getItemForCategory("B");
        List<Item> cafeC = dao.getItemForCategory("C");
        List<Item> cafeD = dao.getItemForCategory("D");
        List<Item> cafeE = dao.getItemForCategory("E");

        String texto = "Relatorio de preferencia de cafe:\n" +
                "A: "+cafeA+" pessoas\n" +
                "B: "+cafeB+" pessoas\n" +
                "C: "+cafeC+" pessoas\n" +
                "D: "+cafeD+" pessoas\n" +
                "E: "+cafeE+" pessoas\n" +
                "";

        txtRelatorioCafe.setText(texto);
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
    public void onResume() {
        super.onResume();
        // Chama o método que irá montar a ListView na tela.
        carregarEstatisticas();

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
