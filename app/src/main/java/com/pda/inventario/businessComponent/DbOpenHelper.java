package com.pda.inventario.businessComponent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbOpenHelper extends SQLiteOpenHelper {


    public DbOpenHelper(Context context) {
        super(context, "inventario.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.CreateTableEndereco(db);
        this.CreateTableProduto(db);
        this.CreateTableItemColeta(db);
        this.CreateTableDepartamento(db);
        this.CreateTableEnderecoExcluido(db);
        this.CreateTableInventario(db);
        this.CreateTableUser(db);
        this.CreateTableSetor(db);
        this.CreateTableSubatividade(db);
        this.CreateTableTipoAtividade(db);
        this.CreateTableConfiguracao(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            deleteTables(db);
            onCreate(db);
        }
    }


    public void deleteTables(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        while (cursor.moveToNext()) {
            if (!cursor.getString(0).equals("sqlite_sequence")) {
                tables.add(cursor.getString(0));
            }
        }

        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }

    }

    private void CreateTableItemColeta(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_ITEM_COLETA ( ID_COLETA INTEGER PRIMARY KEY AUTOINCREMENT, EAN TEXT, QTDE_CONTAGEM REAL, ID_INVENTARIO INT, ID_ENDERECO INT, DATA_COLETA TEXT, METODO_COLETA INT, " +
                "TIPO_ATIVIDADE INT, EXPORT INT, COD_PROD TEXT, DESC_PROD TEXT, METODO_AUDITORIA INT, ID_USUARIO INT, METODO_LEITURA INT, TIMESTAMP TEXT, PRECO INT, VALIDADE TEXT, QTDE_DIV_PRECO INT, TONALIDADE TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableDepartamento(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_DEPARTAMENTO ( ID_DEPARTAMENTO INTEGER PRIMARY KEY AUTOINCREMENT, DESCRICAO TEXT, METODO_CONTAGEM INT, DESC_METODO_CONTAGEM TEXT, " +
                "METODO_AUDITORIA INT, DESC_METODO_AUDITORIA TEXT, QTDE_MAX_MULTIPLOS INT, ID_INVENTARIO INT, METODO_LEITURA INT, DESC_METODO_LEITURA TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableEndereco(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_ENDERECO (ID_ENDERECO TEXT, COD_ENDERECO TEXT, ID_SETOR INT, DESC_SETOR TEXT, METODO_CONTAGEM INT, DESC_METODO_CONTAGEM TEXT, " +
                "METODO_AUDITORIA INT, DESC_METODO_AUDITORIA TEXT, ID_DEPATARMENTO INT, DESC_DEPARTAMENTO TEXT, QTDE_MAX_MULTIPLOS INT, ID_INVENTARIO INT, METODO_LEITURA INT, DESC_METODO_LEITURA TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableEnderecoExcluido(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_ENDERECO_EXCLUIDO (COD_ENDERECO TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableInventario(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_INVENTARIO (ID_INVENTARIO INTEGER PRIMARY KEY AUTOINCREMENT, NOME_FANTASIA TEXT, OBS TEXT, AUTORIZACAO TEXT, METODO_SCAN INT, " +
                "METODO_TRANSFER INT, METODO_ENDERECAMENTO INT, METODO_LEITURA INT, COD_SCAN_START INT, COD_SCAN_END INT, ZERO_ESQUERDA INT, MODULO INT)";
        db.execSQL(comando);
    }

    private void CreateTableUser(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_USUARIO (ID_USUARIO INT, NOME TEXT, TIPO INTEGER, LOGIN TEXT, PASSWORD TEXT, DATA_ACESSO TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableSetor(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_SETOR ( ID_SETOR INTEGER PRIMARY KEY AUTOINCREMENT, DESCRICAO TEXT, METODO_CONTAGEM INT, DESC_METODO_CONTAGEM TEXT, " +
                "METODO_AUDITORIA INT, DESC_METODO_AUDITORIA TEXT, QTDE_MAX_MULTIPLOS INT, ID_INVENTARIO INT, METODO_LEITURA INT, DESC_METODO_LEITURA TEXT)";
        db.execSQL(comando);
    }

    //na tabela, o atributo preço na verdade não significa o preço do produto.
    //Significa se tem tonalidade ou não, se caso 1 tem tonalidade
    private void CreateTableProduto(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_PRODUTO (ID_PRODUTO INTEGER PRIMARY KEY AUTOINCREMENT, COD_PRODUTO TEXT, EAN TEXT, DESC_PRODUTO TEXT, PRECO INT, UNIDADE_MEDIDA TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableSubatividade(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_SUBATIVIDADE (ID_SUBATIVIDADE INTEGER PRIMARY KEY AUTOINCREMENT, ID_ATIVIDADE INT, DESC_SUBATIVIDADE TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableTipoAtividade(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_TIPO_ATIVIDADE (ID_TIPO_ATIVIDADE INTEGER PRIMARY KEY AUTOINCREMENT, ID_LISTAGEM INT, DESC_ATIVIDADE TEXT)";
        db.execSQL(comando);
    }

    private void CreateTableConfiguracao(SQLiteDatabase db) {
        String comando = "CREATE TABLE PDA_TB_CONFIGURACAO (SERVIDOR TEXT, DIRETORIO_VIRTUAL TEXT, FILIAL TEXT)";
        db.execSQL(comando);
    }
}
