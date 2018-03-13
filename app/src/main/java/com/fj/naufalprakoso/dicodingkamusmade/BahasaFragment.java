package com.fj.naufalprakoso.dicodingkamusmade;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.fj.naufalprakoso.dicodingkamusmade.model.Kamus;
import com.fj.naufalprakoso.dicodingkamusmade.presenter.KamusPresenter;
import com.fj.naufalprakoso.dicodingkamusmade.view.KamusView;

import java.util.ArrayList;
import java.util.List;

public class BahasaFragment extends Fragment implements KamusView {

    private OnFragmentInteractionListener mListener;
    private KamusPresenter presenter;
    private TextView textViewKata;
    private TextView textViewKeterangan;
    private ArrayAdapter<Kamus> adapter;
    private List<Kamus> kamusList = new ArrayList<>();
    public BahasaFragment() {
        // Required empty public constructor
    }

    public static BahasaFragment newInstance() {
        BahasaFragment fragment = new BahasaFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            presenter = new KamusPresenter(this, getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kamus, container, false);
        textViewKata = view.findViewById(R.id.text_kata);
        textViewKeterangan = view.findViewById(R.id.text_keterangan);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, kamusList);
        AutoCompleteTextView textView = view.findViewById(R.id.edit_auto_complete);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kamus result = (Kamus) adapterView.getItemAtPosition(i);
                textViewKata.setText(result.getNama());
                textViewKeterangan.setText(result.getKeterangan());
            }
        });

        presenter.loadData(true);
        textView.setHint(R.string.indonesia_english);
        textView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLoadDataSuccess(List<Kamus> result) {
        if (kamusList.size() != 0) {
            kamusList.clear();
        }

        kamusList.addAll(result);
        adapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
