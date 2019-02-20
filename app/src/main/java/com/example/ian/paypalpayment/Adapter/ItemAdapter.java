package com.example.ian.paypalpayment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.paypalpayment.ItemActivity;
import com.example.ian.paypalpayment.Model.Item;
import com.example.ian.paypalpayment.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ClientHolder>{
    private Context context;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_item_row, viewGroup, false);
        return new ItemAdapter.ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder clientHolder, int i) {
        Item item = items.get(i);
        clientHolder.setDetails(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ClientHolder extends RecyclerView.ViewHolder {
        private ImageView image_profile;
        private TextView name, description, model, price, group_zize;
        public ClientHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.id_profile_icon);
            name = itemView.findViewById(R.id.id_item_name);
            description = itemView.findViewById(R.id.id_item_description);
//            model = itemView.findViewById(R.id.id_item_model);
//            price = itemView.findViewById(R.id.id_item_prize);
//            group_zize = itemView.findViewById(R.id.id_item_group_size);

            image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        Item item = items.get(pos);
                        Intent intent = new Intent(context, ItemActivity.class);
                        intent.putExtra("item_data", item);
                        context.startActivity(intent);
                    }
                }
            });

        }

        public void setDetails(Item item) {

            String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";

            // loading album cover using Glide library
//            Glide.with(context)
//                    .load(item.getImage_path())
//                    //.apply(RequestOptions.circleCropTransform())
//                    .into(image_profile);

            name.setText(item.getName());
            description.setText(item.getDescription());
//            model.setText(item.getModel());
//            price.setText(item.getPrice());
//            group_zize.setText(item.getGroup_size());
        }
    }
}
