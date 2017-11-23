package com.utils;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.billphoto.CardGetSet;
import com.billphoto.R;

import java.util.List;

/**
 * Created by Administrator on 15/06/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
List<CardGetSet> data;
    static OnItemClickListener listener;

    public RVAdapter(List<CardGetSet> data) {
        this.data = data;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return    new PersonViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.Invoiceno.setText(data.get(position).InvoiceNo);
        holder.Id.setText(data.get(position).Id);
        holder.InvocieDate.setText(data.get(position).InvoiceDate);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public  class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView Id;
        TextView Invoiceno;
        TextView InvocieDate;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            Invoiceno = (TextView) itemView.findViewById(R.id.tvInvoiceno);
            InvocieDate = (TextView) itemView.findViewById(R.id.tvInvoiceDate);
            Id = (TextView) itemView.findViewById(R.id.tvId);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener!=null)
                listener.onItemClick(Id.getText().toString());
        }


    }



        public interface OnItemClickListener{
            public void onItemClick(String Id);
        }


    public void setOnItemClickListener(final OnItemClickListener listener){
        this.listener = listener;
    }

}