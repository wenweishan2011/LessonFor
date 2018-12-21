package com.wwsdemo.explorerdemo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wwsdemo.explorerdemo.R;
import com.xuexiang.rxutil2.RxBindingUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {


    private static final String MODEL_ID = "id";
    private MainViewModel mViewModel;
    private Unbinder mUnbinder;

    @BindView(R.id.message) TextView message;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final Bundle arguments = getArguments();
        String mid = null;
        if(arguments != null){
            mid = arguments.getString(MODEL_ID);
        }
        mViewModel.init(mid);
        mViewModel.getData().observe(this, s -> message.setText(s));
        RxBindingUtils.setViewClicks(message, o -> {
            mViewModel.getData().postValue("Hello");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
