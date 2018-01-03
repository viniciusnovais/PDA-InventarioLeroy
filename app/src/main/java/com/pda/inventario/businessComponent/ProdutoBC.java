package com.pda.inventario.businessComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pda.inventario.entityObject.EnderecoColetorEO;
import com.pda.inventario.entityObject.ProdutoEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

public class ProdutoBC {
    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public ProdutoBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void insertProdutoFile(List<String> fileName) {
        String[] fileProd;
        //File sdcard = new File(Environment.getExternalStorageDirectory(), "/unzipped/");
        File sdcard = new File(Environment.getExternalStorageDirectory().toString());
        Log.i("ReadFile", "Begin");
        this.DeleteProduto();
        for (int i = 0; i < fileName.size(); i++) {
            // Get the text file
            File file = new File(sdcard, fileName.get(i));

            // Read text from file
            StringBuilder text = new StringBuilder();
            Log.i("ReadFile", file.getName());
            try {
                this.OpenConnection();
                String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?, ?);";
                SQLiteStatement statement = bd.compileStatement(sql);
                bd.beginTransaction();

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    //ProdutoEO produtoEO = new ProdutoEO();
                    fileProd = line.split(";");
                    statement.clearBindings();
                    statement.bindString(2, fileProd[0]);
                    statement.bindString(3, fileProd[1]);
                    statement.bindString(4, fileProd[2]);
                    statement.bindDouble(5, Double.parseDouble(fileProd[3]));
                    statement.bindString(6, fileProd[4]);
                    statement.execute();
                }
                br.close();

                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bd.setTransactionSuccessful();
                bd.endTransaction();
            }
        }
    }

    public void InsertProduto(ProdutoEO produtoEO) {
        try {
            this.DeleteProduto();
            this.OpenConnection();

            String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?);";
            SQLiteStatement statement = bd.compileStatement(sql);
            bd.beginTransaction();
            Log.i("Insert", "Begin");

            statement.clearBindings();
            statement.bindString(2, produtoEO.getCodSku());
            statement.bindString(3, produtoEO.getCodAutomacao());
            statement.bindString(4, produtoEO.getDescSku());
            statement.bindDouble(5, produtoEO.getPreco());
            statement.execute();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bd.setTransactionSuccessful();
            bd.endTransaction();
        }
    }

    public int DeleteProduto() {
        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_PRODUTO", null, null);
        } finally {
            this.CloseConnection();
        }
    }

    public ProdutoEO GetProdByEAN(String codeEAN) {
        try {
            this.OpenConnection();

            ProdutoEO objProduto = new ProdutoEO();
            String[] args = {codeEAN};
            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, "EAN = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objProduto.setIdProduto(cursor.getInt(0));
                objProduto.setCodSku(cursor.getString(1));
                objProduto.setCodAutomacao(cursor.getString(2));
                objProduto.setDescSku(cursor.getString(3));
                objProduto.setPreco(cursor.getInt(4));
                objProduto.setUnidadeMedida(cursor.getString(5));
            }
            cursor.close();

            return objProduto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public ProdutoEO GetProdBySKU(String codeSKU) {
        try {
            this.OpenConnection();

            ProdutoEO objProduto = new ProdutoEO();
            String[] args = {codeSKU};
            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, "COD_PRODUTO = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objProduto.setIdProduto(cursor.getInt(0));
                objProduto.setCodSku(cursor.getString(1));
                objProduto.setCodAutomacao(cursor.getString(2));
                objProduto.setDescSku(cursor.getString(3));
                objProduto.setPreco(cursor.getInt(4));
                objProduto.setUnidadeMedida(cursor.getString(5));
            }
            cursor.close();

            return objProduto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ProdutoEO> GetProd() {
        try {
            this.OpenConnection();

            List<ProdutoEO> objProdutoList = new ArrayList<ProdutoEO>();
            ProdutoEO objProduto = new ProdutoEO();

            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                objProduto = new ProdutoEO();
                objProduto.setIdProduto(cursor.getInt(0));
                objProduto.setCodSku(cursor.getString(1));
                objProduto.setCodAutomacao(cursor.getString(2));
                objProduto.setDescSku(cursor.getString(3));
                objProduto.setPreco(cursor.getInt(4));

                objProdutoList.add(objProduto);
            }
            cursor.close();

            return objProdutoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }
}
