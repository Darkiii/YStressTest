package com.example.ystresstest;

import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.ystresstest.fragments.BaseFragment;
import com.example.ystresstest.fragments.BasicFragment;
import com.example.ystresstest.fragments.ChartFragment;
import com.example.ystresstest.fragments.DisplayFragment;
import com.example.ystresstest.fragments.MovieFragment;
import com.example.ystresstest.fragments.TerrainFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StressApplication extends Application {

    public enum Category {
        GENERAL("General");

        private String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final Map<Category, ExampleItem[]> ITEMS = new HashMap<>();
    public static final ArrayList<TeamMember> TEAM_MEMBERS = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        ITEMS.put(Category.GENERAL, new ExampleItem[] {
                new ExampleItem("Terrain", TerrainFragment.class)
//                , new ExampleItem("Getting Started", BasicFragment.class)
                , new ExampleItem("Display test", DisplayFragment.class)
                , new ExampleItem("Movie test", MovieFragment.class)
                , new ExampleItem("Result Chart", ChartFragment.class)
        });
    }

    public static final class ExampleItem {
        public final Class<? extends Fragment> actionClass;
        public final String title;
        public final String url;

        public ExampleItem(String title, Class<? extends Fragment> actionClass) {
            this.title = title;
            this.actionClass = actionClass;
            this.url = actionClass.getSimpleName() + ".java";
        }
    }

    public static final class TeamMember {
        public int photo;
        public String name;
        public String favoriteBeer;
        public String link;

        protected TeamMember(int photo, String name, String about, String link) {
            this.photo = photo;
            this.name = name;
            this.favoriteBeer = about;
            this.link = link;
        }
    }
}
