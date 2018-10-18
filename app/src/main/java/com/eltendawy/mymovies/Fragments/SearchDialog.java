package com.eltendawy.mymovies.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.eltendawy.mymovies.Activities.MainActivity;
import com.eltendawy.mymovies.R;

import java.util.Objects;

public class SearchDialog extends DialogFragment {
    View view;

    TextInputEditText searchText;
    Button search,cancel;
    public final String TAG="search";
    MainActivity parent;
    public SearchDialog setParent(MainActivity parent) {
        this.parent = parent;
        return this;
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.customdialog);
    }
    @Override public void onStart() {
        super.onStart();
        Objects.requireNonNull(getDialog().getWindow())
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_dialog_search,container,false);
        try {
            if(getDialog().getWindow()!=null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
        }catch (Exception ignored)
        {
        }
        initViews();
        return view;
    }

    private void initViews() {
        searchText=view.findViewById(R.id.search_edit_text);
        search=view.findViewById(R.id.search);
        cancel=view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText()!=null)
                    parent.fetchMovies(searchText.getText().toString());
                dismiss();
            }
        });
    }
}
