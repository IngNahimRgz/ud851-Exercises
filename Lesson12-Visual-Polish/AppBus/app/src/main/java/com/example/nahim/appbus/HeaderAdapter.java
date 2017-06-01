package com.example.nahim.appbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nahim on 16/05/2017.
 */

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> implements Filterable {
    private ArrayList<Header> mDataset;
    private ArrayList<Header> mFilteredList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HeaderAdapter(ArrayList<Header> myDataset) {
        mDataset = myDataset;
        mFilteredList = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
        // set the view's size, margins, paddings and layout parameter
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre_ruta.setText(mFilteredList.get(position).getNombre_ruta());
        holder.tiempo_recorrido.setText("Tiempo de recorrido: " + mFilteredList.get(position).getTiempo_recorrido());
        holder.frecuencia_paso.setText("Frecuencia de paso: " + mFilteredList.get(position).getFrecuencia_paso());
        holder.longitud_ruta.setText("Longitud de ruta: " + mFilteredList.get(position).getLongitud_ruta());

        //set events
        holder.setOnClickListeners();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    //@Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mDataset;
                } else {

                    ArrayList<Header> filteredList = new ArrayList<>();

                    for (Header header : mDataset) {
                        if (header.getNombre_ruta().toLowerCase().contains(charString) ||
                                header.getFrecuencia_paso().toLowerCase().contains(charString) ||
                                header.getLongitud_ruta().toLowerCase().contains(charString) ||
                                header.getTiempo_recorrido().toLowerCase().contains(charString)) {
                            filteredList.add(header);
                        }

                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Header>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Buttons
        Button btn_ver_detalles;
        Button btn_editar_ahora;
        Button btn_ver_ruta;
        Context context;
        // each data item is just a string in this case
        private TextView nombre_ruta;
        private TextView tiempo_recorrido;
        private TextView frecuencia_paso;
        private TextView longitud_ruta;


        public ViewHolder(View v) {
            super(v);

            context = v.getContext();
            //Aqui se vinculan los elementos graficos

            //Textviews
            nombre_ruta = (TextView) v.findViewById(R.id.nombre_ruta);
            tiempo_recorrido = (TextView) v.findViewById(R.id.tiempo_recorrido);
            frecuencia_paso = (TextView) v.findViewById(R.id.frecuencia_paso);
            longitud_ruta = (TextView) v.findViewById(R.id.longitud_ruta);

            //Botones
            btn_ver_detalles = (Button) v.findViewById(R.id.btn_ver_detalles);
            btn_editar_ahora = (Button) v.findViewById(R.id.btn_editar_ahora);
            btn_ver_ruta = (Button) v.findViewById(R.id.btn_ver_ruta);
        }

        void setOnClickListeners() {
            btn_ver_ruta.setOnClickListener(this);
            btn_editar_ahora.setOnClickListener(this);
            btn_ver_detalles.setOnClickListener(this);
        }

        /**
         * En este metodo se abrira un nuevo intento segun la opcion selecionada por el usuario
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            String nombreRuta = nombre_ruta.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("NOMBRE_RUTA", nombreRuta);

            switch (v.getId()) {
                //Mandamos llamar a la pantalla donde se mostraran todos los datos de la ruta seleccionada
                case R.id.btn_ver_detalles:
                    Toast.makeText(context, "Escogiste: " + nombreRuta, Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(context, Ver_Detalles_Activity.class);
                    //context.startActivity(intent);
                    break;

                //Mandamos llamar a la pantalla donde se podra modificar las lista de coordenadas que conforman la ruta,
                //al momento de llamar a la actividad se le manda el nombre de la ruta, para que genere un archivo de texto con el mismo nombre
                //el cual contendra las coordenadas
                case R.id.btn_editar_ahora:
                    Intent intentEditarAhora = new Intent(context, EditarRuta.class);
                    intentEditarAhora.putExtras(bundle);
                    context.startActivity(intentEditarAhora);
                    break;

                //Abre la pantalla donde se muestra la ruta en el mapa,
                case R.id.btn_ver_ruta:
                    Intent intentVerRuta = new Intent(context, MapsActivity.class);
                    intentVerRuta.putExtras(bundle);
                    context.startActivity(intentVerRuta);
                    break;
            }
        }
    }
}