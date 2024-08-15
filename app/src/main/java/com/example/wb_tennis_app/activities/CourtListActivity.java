package com.example.wb_tennis_app.activities;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.adapters.CourtAdapter;
import com.example.wb_tennis_app.models.Court;

import java.util.ArrayList;
import java.util.List;

public class CourtListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_list);

        ListView courtListView = findViewById(R.id.courtListView);

        // Mock data for demonstration
        List<Court> courts = new ArrayList<>();
        courts.add(new Court("Grass", "Grass-1"));
        courts.add(new Court("Artificial Grass", "Artificial Grass-1"));

        CourtAdapter adapter = new CourtAdapter(this, courts);
        courtListView.setAdapter(adapter);
    }
}
