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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ian.paypalpayment.ClientDetailActivity;
import com.example.ian.paypalpayment.Model.Client;
import com.example.ian.paypalpayment.R;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder>
{
    private Context context;
    private ArrayList<Client> clients;

    public ClientAdapter(Context context, ArrayList<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup, false);
        return new ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder clientHolder, int i) {
        Client client = clients.get(i);
        clientHolder.setDetails(client);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ClientHolder extends RecyclerView.ViewHolder {
        private TextView txt_first_name, txt_middle_name, txt_last_name, txt_email, txt_phone;
        private ImageView image_profile;
        public ClientHolder(View itemView) {
            super(itemView);
            txt_first_name = itemView.findViewById(R.id.id_txt_first_name);
            //txt_middle_name = itemView.findViewById(R.id.id_txt_middle_name);
            //txt_last_name = itemView.findViewById(R.id.id_txt_last_name);
            txt_email = itemView.findViewById(R.id.id_txt_email);
            txt_phone = itemView.findViewById(R.id.id_txt_phone);
            image_profile = itemView.findViewById(R.id.id_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        Client client = clients.get(pos);
                        Intent intent = new Intent(context, ClientDetailActivity.class);
                        intent.putExtra("client_detail", client);
                        context.startActivity(intent);

                        //Toast.makeText(context, "Hello World"+client.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        public void setDetails(Client client) {
            txt_first_name.setText(client.getFirst_name()+" "+client.getMiddle_name()+" "+client.getLast_name());
            //txt_middle_name.setText(client.getMiddle_name());
            //txt_last_name.setText(client.getLast_name());
            txt_email.setText(client.getEmail());
            txt_phone.setText(client.getPhone());

            String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";

            // loading album cover using Glide library
            Glide.with(context)
                    .load(client.getProfile())
                    .apply(RequestOptions.circleCropTransform())
                    .into(image_profile);
        }


    }
}
