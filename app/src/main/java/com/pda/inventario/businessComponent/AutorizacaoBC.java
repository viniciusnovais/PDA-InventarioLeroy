package com.pda.inventario.businessComponent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AutorizacaoBC {

    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public AutorizacaoBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void limpaBase() {
        try {
            this.OpenConnection();
            bd.delete("PDA_TB_USUARIO", null, null);
            bd.delete("PDA_TB_PRODUTO", null, null);
            bd.delete("PDA_TB_ENDERECO", null, null);
            bd.delete("PDA_TB_SETOR", null, null);
            bd.delete("PDA_TB_DEPARTAMENTO", null, null);
            bd.delete("PDA_TB_ITEM_COLETA", null, null);
            bd.delete("PDA_TB_INVENTARIO", null, null);



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.CloseConnection();
        }
    }
}
