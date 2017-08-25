package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ContagemColetorEO implements KvmSerializable {
    public int Inventario;
    public String CodigoBarra;
    public String CodigoProduto;
    public float QtdeContagem;
    public String Endereco;
    public int Usuario;
    public int Atividade;
    public int TTimeStamp;
    public String Tonalidade;

    public ContagemColetorEO() {
        Inventario = 0;
        CodigoBarra = "";
        CodigoProduto = "";
        QtdeContagem = 0;
        Endereco = "";
        Usuario = 0;
        Atividade = 0;
        TTimeStamp = 0;
        Tonalidade = "";
    }

    public ContagemColetorEO(int inventario, String codigoBarra, String codigoProduto, float qtdeContagem, String endereco, int usuario, int atividade, int tTimeStamp, String tonalidade) {
        Inventario = inventario;
        CodigoBarra = codigoBarra;
        CodigoProduto = codigoProduto;
        QtdeContagem = qtdeContagem;
        Endereco = endereco;
        Usuario = usuario;
        Atividade = atividade;
        TTimeStamp = tTimeStamp;
        Tonalidade = tonalidade;
    }

    public Object getProperty(int arg0) {

        switch (arg0) {
            case 0:
                return Inventario;
            case 1:
                return CodigoBarra;
            case 2:
                return CodigoProduto;
            case 3:
                return QtdeContagem;
            case 4:
                return Endereco;
            case 5:
                return Usuario;
            case 6:
                return Atividade;
            case 7:
                return TTimeStamp;
            case 8:
                return Tonalidade;

        }
        return null;
    }

    public int getPropertyCount() {
        return 9;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Inventario";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CodigoBarra";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CodigoProduto";
                break;
            case 3:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "QtdeContagem";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Endereco";
                break;
            case 5:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Usuario";
                break;
            case 6:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Atividade";
                break;
            case 7:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "TTimeStamp";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Tonalidade";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                Inventario = Integer.parseInt(value.toString());
                break;
            case 1:
                CodigoBarra = value.toString();
                break;
            case 2:
                CodigoProduto = value.toString();
                break;
            case 3:
                QtdeContagem = Float.parseFloat(value.toString());
                break;
            case 4:
                Endereco = value.toString();
                break;
            case 5:
                Usuario = Integer.parseInt(value.toString());
                break;
            case 6:
                Atividade = Integer.parseInt(value.toString());
                break;
            case 7:
                TTimeStamp = Integer.parseInt(value.toString());
                break;
            case 8:
                Tonalidade = value.toString();
                break;
            default:
                break;
        }
    }
}
