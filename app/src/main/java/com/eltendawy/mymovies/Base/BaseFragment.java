package com.eltendawy.mymovies.Base;

import android.content.Context;
import android.support.v4.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/13/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public class BaseFragment extends Fragment {

    protected BaseActivity activity;
    private int main_fragment_container;

    public BaseFragment() {

        main_fragment_container = -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;


    }

    public void setMain_fragment_container(int main_fragment_container) {
        this.main_fragment_container = main_fragment_container;
    }

    protected void showFragment(Fragment fragment) throws Exception {
        if (main_fragment_container == -1) throw new Exception("set main fragment container first");
        getChildFragmentManager().beginTransaction().replace(main_fragment_container, fragment).commit();

    }

}
