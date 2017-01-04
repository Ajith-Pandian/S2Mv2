package com.example.uilayer.manage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.domainlayer.models.Schools;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectSchoolActivity extends AppCompatActivity {
    @BindView(R.id.recycler_school)
    RecyclerView schoolRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        schoolRecycler.setLayoutManager(layoutManager);
        schoolRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1));
        schoolRecycler.setAdapter(new SchoolsAdapter(this, getSchools()));

    }

    ArrayList<Schools> getSchools() {
        ArrayList<Schools> schoolsArrayList = new ArrayList<>();
        Schools school1 = new Schools();
        school1.setName("Diwan-BalluBhai School");
        school1.setAddress("Sector 28 , Faridabad");
        school1.setLogo("http://www.civil-site.com/wp-content/uploads/2015/11/MJ-High-School-11.jpg");
        schoolsArrayList.add(school1);
        Schools school2 = new Schools();
        school2.setName("Indian Academy");
        school2.setLogo("http://www.civil-site.com/wp-content/uploads/2015/11/MJ-High-School-11.jpg");
        school2.setAddress("Bannergatta, Bangalore");
        schoolsArrayList.add(school2);
        Schools school3 = new Schools();
        school3.setName("The Shri Ram School");
        school3.setLogo("https://image.freepik.com/free-vector/school-building_23-2147515924.jpg");
        school3.setAddress("M.G.Road, Gurgaon");
        schoolsArrayList.add(school3);
        return schoolsArrayList;
    }
}
