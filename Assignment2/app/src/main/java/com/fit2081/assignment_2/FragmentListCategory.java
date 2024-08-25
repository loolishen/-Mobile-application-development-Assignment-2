package com.fit2081.assignment_2;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.provider.ViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    SharedPreferences sP;
    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CategoryRecyclerAdapter adapter;

    Gson gson = new Gson();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewModel viewModel;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        eventCategories = new ArrayList<>(); // Initialize the ArrayList

        viewModel.getAllEventCategoriesLiveData().observe(this, new Observer<List<EventCategory>>() {
            @Override
            public void onChanged(List<EventCategory> categories) {
                eventCategories.clear();
                eventCategories.addAll(categories);
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        sP = getActivity().getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);
//        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        eventCategories = gson.fromJson(eventCategoriesString, type);

        //Toast.makeText(getContext(), "Size" + eventCategories.size(), Toast.LENGTH_SHORT).show();

        View v = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new CategoryRecyclerAdapter();

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        // Observe LiveData from ViewModel
        viewModel.getAllEventCategoriesLiveData().observe(getViewLifecycleOwner(), newData -> {
            adapter.setData(new ArrayList<>(newData));
            adapter.notifyDataSetChanged();
        });

        adapter.setData(eventCategories);


        recyclerView.setAdapter(adapter);

        return v;

    }
}