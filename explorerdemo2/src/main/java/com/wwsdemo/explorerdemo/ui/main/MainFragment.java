package com.wwsdemo.explorerdemo.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wwsdemo.explorerdemo.R;
import com.wwsdemo.explorerdemo.repository.DocumentInfoRepository;
import com.wwsdemo.explorerdemo.ui.main.bean.DocumentInfo;
import com.wwsdemo.explorerdemo.weight.CustomAdapter;
import com.xuexiang.rxutil2.RxBindingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    public static final String DATA_AUTHORITY = "com.wwsdemo.explorerdemo.storageprovider.documents";
    private String TAG = "wws";

    private static final String MODEL_ID = "id";
    private MainViewModel mViewModel;
    private Unbinder mUnbinder;

    private LiveData<List<DocumentInfo>> mData;

    @BindView(R.id.back)
    Button back;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    private CustomAdapter adapter;

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
        if (arguments != null) {
            mid = arguments.getString(MODEL_ID);
        }
        mViewModel.init(mid, getContext().getContentResolver());
        final File[] dirs = ContextCompat.getExternalFilesDirs(getContext(), null);
        final List<DocumentInfo> value = new ArrayList<>();
        for(File dir : dirs){
            mData = mViewModel.getData(DocumentsContract.buildDocumentUri(DATA_AUTHORITY, "root:" + dir.getName()));
            value.addAll(mData.getValue());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(value, (view, position) -> {
            final int id = view.getId();
            final Uri uri = DocumentsContract.buildDocumentUri(DATA_AUTHORITY, value.get(position).document_id);
            switch (id){
                case R.id.icon:
                    LiveData<List<DocumentInfo>> nextPageData = new DocumentInfoRepository().getDocuments(getContext().getContentResolver(), DocumentsContract.buildChildDocumentsUri(DATA_AUTHORITY, value.get(position).document_id));
                    List<DocumentInfo> list = nextPageData.getValue();
                    if (!list.isEmpty()) {
                        value.clear();
                        value.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.newFile:

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            DocumentsContract.createDocument(getContext().getContentResolver(),uri, "text/plain", "wws_test");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.newDir:
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            DocumentsContract.createDocument(getContext().getContentResolver(), uri, DocumentsContract.Document.MIME_TYPE_DIR, "wws_dir");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        });
        recyclerView.setAdapter(adapter);

        RxBindingUtils.setViewClicks(back, o -> {
            LiveData<List<DocumentInfo>> nextPageData = new DocumentInfoRepository().getDocuments(getContext().getContentResolver(), DocumentsContract.buildChildDocumentsUri(DATA_AUTHORITY, "root:"));
            List<DocumentInfo> list = nextPageData.getValue();
            value.clear();
            value.addAll(list);
            adapter.notifyDataSetChanged();
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
