package com.example.agenda.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.entidades.Contacto;

import java.util.ArrayList;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ContactoViewHolder> {
    //Agregamos Array
    ArrayList<Contacto>listaContactos;
    //Hacemos 1
    public ListaContactosAdapter(ArrayList<Contacto>listaContactos)
    {
        this.listaContactos=listaContactos;
    }
    
    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_contacto,
                null, false);

        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.viewNombre.setText(listaContactos.get(position).getNombre());
        holder.viewTelefono.setText(listaContactos.get(position).getTelefono());
        holder.viewEmail.setText(listaContactos.get(position).getEmail());
    }

    //Hacer 2
    @Override
    public int getItemCount() {
        return listaContactos.size();
    }
    //Hacemos 3
    public class ContactoViewHolder extends RecyclerView.ViewHolder{

        TextView viewNombre, viewTelefono, viewEmail;
        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre=itemView.findViewById(R.id.tvNombre);
            viewTelefono= itemView.findViewById(R.id.tvTelefono);
            viewEmail=itemView.findViewById(R.id.tvCorreo);

        }
    }
}
