package com.pda.inventario;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pda_inventario.R;
import com.pda.inventario.entityObject.ProdutoEO;

import java.util.List;

/**
 * Created by PDA on 13/07/2017.
 */

public class ContagemAdapter extends BaseAdapter {

    List<ProdutoEO> lista;
    Context context;
    private IMyEventListener mEventListener;

    public ContagemAdapter(List<ProdutoEO> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    public interface IMyEventListener {
        void onEventAccured();
    }

    public void setEventListener(IMyEventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_contagem_lista, null);

        TextView tvNomeProduto, tvCodProduto, tvQtde;

        tvNomeProduto = (TextView) view.findViewById(R.id.nomeProduto);
        tvCodProduto = (TextView) view.findViewById(R.id.cod);
        tvQtde = (TextView) view.findViewById(R.id.qtde);

        tvCodProduto.setText(lista.get(position).getCodSku());
        tvNomeProduto.setText(lista.get(position).getDescSku());

        String qtde = lista.get(position).getQuantidade() + "";
        qtde = qtde.replaceAll("[.]", ",");
        tvQtde.setText(qtde);

        return view;
    }
}
