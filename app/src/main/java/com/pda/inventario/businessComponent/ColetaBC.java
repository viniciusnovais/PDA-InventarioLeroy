package com.pda.inventario.businessComponent;

import java.util.ArrayList;
import java.util.List;

import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.ContagemColetorEO;
import com.pda.inventario.entityObject.ProdutoEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ColetaBC {
    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public ColetaBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void CreateColeta(ColetaEO objColeta) {
        try {
            this.OpenConnection();

            ContentValues values = new ContentValues();
            values.put("EAN", objColeta.CodigoBarra);
            values.put("QTDE_CONTAGEM", objColeta.QuantidadeContagem);
            values.put("ID_INVENTARIO", objColeta.IdInventario);
            values.put("ID_ENDERECO", objColeta.IdEndereco);
            values.put("DATA_COLETA", objColeta.DataColeta);
            values.put("METODO_COLETA", objColeta.MetodoColeta);
            values.put("TIPO_ATIVIDADE", objColeta.TipoAtividade);
            values.put("EXPORT", objColeta.Export);
            values.put("COD_PROD", objColeta.CodigoProduto);
            values.put("DESC_PROD", objColeta.DescricaoProduto);
            values.put("METODO_AUDITORIA", objColeta.MetodoAuditoria);
            values.put("ID_USUARIO", objColeta.IdUsuario);
            values.put("METODO_LEITURA", objColeta.MetodoLeitura);
            values.put("TIMESTAMP", objColeta.TimeStamp);
            values.put("PRECO", objColeta.Preco);
            values.put("VALIDADE", objColeta.Validade);
            values.put("QTDE_DIV_PRECO", objColeta.QtdeDivPreco);
            values.put("TONALIDADE", objColeta.Tonalidade);

            bd.insert("PDA_TB_ITEM_COLETA", null, values);
        } finally {
            this.CloseConnection();
        }
    }

    public List<ColetaEO> GetColetaByEndereco(String idEndereco) {
        try {
            this.OpenConnection();

            List<ColetaEO> objColetaList = new ArrayList<ColetaEO>();
            ColetaEO objColeta;
            String[] argsWhere = {idEndereco, "0"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", null, "ID_ENDERECO = ? AND EXPORT = ?", argsWhere, null, null, null);
            while (cursor.moveToNext()) {
                objColeta = new ColetaEO();
                objColeta.IdEndereco = cursor.getInt(0);
                objColeta.CodigoBarra = cursor.getString(1);
                objColeta.QuantidadeContagem = cursor.getInt(2);
                objColeta.IdInventario = cursor.getInt(3);
                objColeta.IdEndereco = cursor.getInt(4);
                objColeta.DataColeta = cursor.getString(5);
                objColeta.MetodoColeta = cursor.getInt(6);
                objColeta.TipoAtividade = cursor.getInt(7);
                objColeta.Export = cursor.getInt(8);
                objColeta.CodigoProduto = cursor.getString(9);
                objColeta.DescricaoProduto = cursor.getString(10);
                objColeta.MetodoAuditoria = cursor.getInt(11);
                objColeta.IdUsuario = cursor.getInt(12);
                objColeta.MetodoLeitura = cursor.getInt(13);
                objColeta.TimeStamp = cursor.getString(14);
                objColeta.Preco = cursor.getInt(15);
                objColeta.Validade = cursor.getString(16);
                objColeta.QtdeDivPreco = cursor.getInt(17);
                objColeta.Tonalidade = cursor.getString(18);

                objColetaList.add(objColeta);
            }
            cursor.close();

            return objColetaList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ProdutoEO> getColetaListViewByEndereco(String idEndereco) {
        try {
            this.OpenConnection();

            List<ProdutoEO> lista = new ArrayList<>();
            String[] argsWhere = {idEndereco, "0"};
            String[] argsColumns = {"COD_PROD", "DESC_PROD", "QTDE_CONTAGEM", "ID_COLETA"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", argsColumns, "ID_ENDERECO = ? AND EXPORT = ?", argsWhere, null, null, null);
            String[] arrayColeta = new String[cursor.getCount()];

            while (cursor.moveToNext()) {
                ProdutoEO p = new ProdutoEO();
                p.setCodSku(cursor.getString(0));
                p.setDescSku(cursor.getString(1));
                p.setQuantidade(cursor.getFloat(2));
                //estou usando o getidproduto para pegar o id da coleta para deletar
                p.setIdProduto(cursor.getInt(3));
                lista.add(p);
            }
            cursor.close();

            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ContagemColetorEO> GetContagemColetorByEndereco(String idEndereco) {
        try {
            this.OpenConnection();

            List<ContagemColetorEO> objColetaList = new ArrayList<ContagemColetorEO>();
            ContagemColetorEO objColeta;
            String[] args = {idEndereco, "0"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", null, "ID_ENDERECO = ? AND EXPORT = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objColeta = new ContagemColetorEO();

                objColeta.CodigoBarra = cursor.getString(1);
                objColeta.QtdeContagem = cursor.getFloat(2);
                objColeta.Inventario = cursor.getInt(3);
                objColeta.Endereco = String.valueOf(cursor.getInt(4));
                objColeta.Atividade = cursor.getInt(7);
                objColeta.CodigoProduto = cursor.getString(9);
                objColeta.Usuario = cursor.getInt(12);
                objColeta.TTimeStamp = cursor.getInt(14);
                objColeta.Tonalidade = cursor.getString(18);

                objColetaList.add(objColeta);
            }
            cursor.close();

            return objColetaList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ContagemColetorEO> GetContagemColetorPendente(String idInventario) {
        try {
            this.OpenConnection();

            List<ContagemColetorEO> objColetaList = new ArrayList<ContagemColetorEO>();
            ContagemColetorEO objColeta;
            String[] args = {idInventario, "0"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", null, "ID_INVENTARIO = ? AND EXPORT = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objColeta = new ContagemColetorEO();

                objColeta.CodigoBarra = cursor.getString(1);
                objColeta.QtdeContagem = cursor.getInt(2);
                objColeta.Inventario = cursor.getInt(3);
                objColeta.Endereco = String.valueOf(cursor.getInt(4));
                objColeta.Atividade = cursor.getInt(7);
                objColeta.CodigoProduto = cursor.getString(9);
                objColeta.Usuario = cursor.getInt(12);
                objColeta.TTimeStamp = cursor.getInt(14);

                objColetaList.add(objColeta);
            }
            cursor.close();

            return objColetaList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ColetaEO> GetColetaPendente(String idInventario) {
        try {
            this.OpenConnection();

            List<ColetaEO> objColetaList = new ArrayList<ColetaEO>();
            ColetaEO objColeta;
            String[] argsWhere = {idInventario, "0"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", null, "ID_INVENTARIO = ? AND EXPORT = ?", argsWhere, null, null, null);
            while (cursor.moveToNext()) {
                objColeta = new ColetaEO();
                objColeta.IdEndereco = cursor.getInt(0);
                objColeta.CodigoBarra = cursor.getString(1);
                objColeta.QuantidadeContagem = cursor.getInt(2);
                objColeta.IdInventario = cursor.getInt(3);
                objColeta.IdEndereco = cursor.getInt(4);
                objColeta.DataColeta = cursor.getString(5);
                objColeta.MetodoColeta = cursor.getInt(6);
                objColeta.TipoAtividade = cursor.getInt(7);
                objColeta.Export = cursor.getInt(8);
                objColeta.CodigoProduto = cursor.getString(9);
                objColeta.DescricaoProduto = cursor.getString(10);
                objColeta.MetodoAuditoria = cursor.getInt(11);
                objColeta.IdUsuario = cursor.getInt(12);
                objColeta.MetodoLeitura = cursor.getInt(13);
                objColeta.TimeStamp = cursor.getString(14);
                objColeta.Preco = cursor.getInt(15);
                objColeta.Validade = cursor.getString(16);
                objColeta.QtdeDivPreco = cursor.getInt(17);
                objColeta.Tonalidade = cursor.getString(18);

                objColetaList.add(objColeta);
            }
            cursor.close();

            return objColetaList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public int DeleteColeta() {
        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_ITEM_COLETA", null, null);
        } finally {
            this.CloseConnection();
        }
    }

    public int deleteColetaEnd(String idEndereco) {
        try {
            String[] argsWhere = {idEndereco, "0"};

            this.OpenConnection();
            return bd.delete("PDA_TB_ITEM_COLETA", "ID_ENDERECO = ? AND EXPORT = ?", argsWhere);
        } finally {
            this.CloseConnection();
        }
    }

    public int UpdateColetaExport(String idEndereco) {
        try {
            String[] argsWhere = {idEndereco, "0"};

            ContentValues values = new ContentValues();
            values.put("EXPORT", "1");

            this.OpenConnection();
            return bd.update("PDA_TB_ITEM_COLETA", values, "ID_ENDERECO = ? AND EXPORT = ?", argsWhere);
        } finally {
            this.CloseConnection();
        }
    }

    public int UpdateColetaExportInventario(String idInventario) {
        try {
            String[] argsWhere = {idInventario, "0"};

            ContentValues values = new ContentValues();
            values.put("EXPORT", "1");

            this.OpenConnection();
            return bd.update("PDA_TB_ITEM_COLETA", values, "ID_INVENTARIO = ? AND EXPORT = ?", argsWhere);
        } finally {
            this.CloseConnection();
        }
    }

    public int getQtdeContagemByEndereco(String idEndereco) {
        try {
            this.OpenConnection();
            int ret = 0;
            String[] args = {idEndereco, "0"};
            String[] argsColumns = {"SUM(QTDE_CONTAGEM)"};
            Cursor cursor = bd.query("PDA_TB_ITEM_COLETA", argsColumns, "ID_ENDERECO = ? AND EXPORT = ?", args, "EAN, ID_INVENTARIO, ID_ENDERECO", null, null);
            while (cursor.moveToNext()) {
                ret = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            this.CloseConnection();
        }
    }
    //na verdade estou deletando a coleta que contem o produto seleciona
    public int DeleteProdutoByCod(int idColeta) {

        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_ITEM_COLETA", "ID_COLETA = ?", new String[]{idColeta + ""});
        } finally {
            this.CloseConnection();
        }
    }
}