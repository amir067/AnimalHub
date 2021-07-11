package com.example.animalhub.ui.Info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.animalhub.R;
import com.example.animalhub.ui.main.Information_Activity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InformationFragment extends Fragment {
    private  final String TAG = "InformationFragment";

    @BindView(R.id.farm)
    CardView farm;

    @BindView(R.id.desert)
    CardView desert;

    @BindView(R.id.bird)
    CardView bird;

    @BindView(R.id.reptile)
    CardView reptile;

    @BindView(R.id.sea)
    CardView sea;

    @BindView(R.id.jungle)
    CardView jungle;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_food__info_, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Information_Activity.class);
                intent.putExtra("detail","Livestock animals, such as cows, sheep," +
                        " goats and chickens, have many roles in the farm ecosystem. " +
                        "They eat corn and hay grown on the farm, they provide milk, eggs, wool and meat for humans," +
                        " and their waste can fertilize the soil." +
                        " Animal manure contains many nutrients that plants can use to grow.");
                startActivity(intent);
            }
        });
        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Information_Activity.class);
                intent.putExtra("detail","Desert animals mostly feed on plants, insects, carcasses and other animals." +
                        "Smaller animals tend to thrive in the desert as compared to those that are larger in size."+
                        "This is because food and water in the desert is scarce while the climatic conditions are quite extreme.");
                startActivity(intent);
            }
        });
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Information_Activity.class);
                intent.putExtra("detail","Bird food or bird seed is food (often varieties of seeds, nuts, or dried fruits) eaten by birds." +
                        " While most bird food is fed to commercial fowl, people also use bird food to feed their pet birds or wild birds.\n" +
                        "\n" +
                        "The various types of bird food reflect the species of bird that can be fed, whether they are carnivores or nectar eating birds.");
                startActivity(intent);
            }
        });
        reptile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Information_Activity.class);
                intent.putExtra("detail","From turtles to snakes, reptiles are surprisingly cute and fun to care for." +
                        " Making sure that your scaly friend has the right food to eat is just as important as watching your own diet." +
                        "There are an estimated 9,500 different species of reptiles on the face of the planet, from the meat-gobbling crocodile to the lettuce-loving tortoise." +
                        " There’s no such thing as a “one size fits all” reptile diet.\n" +
                        "That means it’s important to do your research on your pet. " +
                        "Spend time reading about the wild diet of your reptile’s species, and then find the appropriate mix of fresh food, pellets, and supplements to keep him healthy for a long life.\"");
                startActivity(intent);
            }
        });
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Information_Activity.class);
                intent.putExtra("detail","WHALES eat mostly fish, shrimp, crabs, squid, and krill." +
                        " Bigger whales eat sea lions, walruses, seals, sharks, and sometimes even other whales." +
                        " SWORD FISH eat other ocean fish such as blue fish and mackerel. " +
                        "They slash bigger fish like squids and octopus with their sharp bill to eat.");
                startActivity(intent);
            }
        });
        jungle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Information_Activity.class);
                intent.putExtra("detail","Rainforest animals eat a wide and varied diet, including fruit, leaves, insects, nuts, seeds, bark, grasses and other animals. " +
                        "The rainforest is estimated to contain approximately half the world's animal species. " +
                        "Some of these species are vegetarians, some are carnivores, and some are omnivores.");
                startActivity(intent);
            }
        });


    }


}