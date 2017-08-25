package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ColetaEO implements KvmSerializable {

    public int IdColeta;
    public String CodigoBarra;
    public float QuantidadeContagem;
    public int IdInventario;
    public int IdEndereco;
    public String DataColeta;
    public int MetodoColeta;
    public int TipoAtividade;
    public int Export;
    public String CodigoProduto;
    public String DescricaoProduto;
    public int MetodoAuditoria;
    public String TimeStamp;
    public int Preco;
    public String Validade;
    public int QtdeDivPreco;
    public int IdUsuario;
    public int MetodoLeitura;
    public String Tonalidade;

    public ColetaEO() {
        IdColeta = 0;
        CodigoBarra = "";
        QuantidadeContagem = 0;
        IdInventario = 0;
        IdEndereco = 0;
        DataColeta = "";
        MetodoColeta = 0;
        TipoAtividade = 0;
        Export = 0;
        CodigoProduto = "";
        DescricaoProduto = "";
        MetodoAuditoria = 0;
        TimeStamp = "";
        Preco = 0;
        Validade = "";
        QtdeDivPreco = 0;
        IdUsuario = 0;
        MetodoLeitura = 0;
        Tonalidade = "";
    }

    public ColetaEO(int idColeta, String codigoBarra, float quantidadeContagem, int idInventario, int idEndereco, String dataColeta, int metodoColeta, int tipoAtividade, int export, String codigoProduto,
                    String descricaoProduto, int metodoAuditoria, String timeStamp, int preco, String validade, int qtdeDivPreco, int idUsuario, int metodoLeitura, String tonalidade) {
        IdColeta = idColeta;
        CodigoBarra = codigoBarra;
        QuantidadeContagem = quantidadeContagem;
        IdInventario = idInventario;
        IdEndereco = idEndereco;
        DataColeta = dataColeta;
        MetodoColeta = metodoColeta;
        TipoAtividade = tipoAtividade;
        Export = export;
        CodigoProduto = codigoProduto;
        DescricaoProduto = descricaoProduto;
        MetodoAuditoria = metodoAuditoria;
        TimeStamp = timeStamp;
        Preco = preco;
        Validade = validade;
        QtdeDivPreco = qtdeDivPreco;
        IdUsuario = idUsuario;
        MetodoLeitura = metodoLeitura;
        Tonalidade = tonalidade;
    }

    public Object getProperty(int arg0) {

        switch (arg0) {
            case 0:
                return IdColeta;
            case 1:
                return CodigoBarra;
            case 2:
                return QuantidadeContagem;
            case 3:
                return IdInventario;
            case 4:
                return IdEndereco;
            case 5:
                return DataColeta;
            case 6:
                return MetodoColeta;
            case 7:
                return TipoAtividade;
            case 8:
                return Export;
            case 9:
                return CodigoProduto;
            case 10:
                return DescricaoProduto;
            case 11:
                return MetodoAuditoria;
            case 12:
                return TimeStamp;
            case 13:
                return Preco;
            case 14:
                return Validade;
            case 15:
                return QtdeDivPreco;
            case 16:
                return IdUsuario;
            case 17:
                return MetodoLeitura;
            case 18:
                return Tonalidade;
        }
        return null;
    }

    public int getPropertyCount() {
        return 19;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdColeta";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CodigoBarra";
                break;
            case 2:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "QuantidadeContagem";
                break;
            case 3:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdInventario";
                break;
            case 4:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdEndereco";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DataColeta";
                break;
            case 6:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "MetodoColeta";
                break;
            case 7:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "TipoAtividade";
                break;
            case 8:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Export";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "CodigoProduto";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DescricaoProduto";
                break;
            case 11:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "MetodoAuditoria";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TimeStamp";
                break;
            case 13:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Preco";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Validade";
                break;
            case 15:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "QtdeDivPreco";
                break;
            case 16:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdUsuario";
                break;
            case 17:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "MetodoLeitura";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "tonalidade";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                IdColeta = Integer.parseInt(value.toString());
                break;
            case 1:
                CodigoBarra = value.toString();
                break;
            case 2:
                QuantidadeContagem = Float.parseFloat(value.toString());
                break;
            case 3:
                IdInventario = Integer.parseInt(value.toString());
                break;
            case 4:
                IdEndereco = Integer.parseInt(value.toString());
                break;
            case 5:
                DataColeta = value.toString();
                break;
            case 6:
                MetodoColeta = Integer.parseInt(value.toString());
                break;
            case 7:
                TipoAtividade = Integer.parseInt(value.toString());
                break;
            case 8:
                Export = Integer.parseInt(value.toString());
                break;
            case 9:
                CodigoProduto = value.toString();
                break;
            case 10:
                DescricaoProduto = value.toString();
                break;
            case 11:
                MetodoAuditoria = Integer.parseInt(value.toString());
                break;
            case 12:
                TimeStamp = value.toString();
                break;
            case 13:
                Preco = Integer.parseInt(value.toString());
                break;
            case 14:
                Validade = value.toString();
                break;
            case 15:
                QtdeDivPreco = Integer.parseInt(value.toString());
                break;
            case 16:
                IdUsuario = Integer.parseInt(value.toString());
                break;
            case 17:
                MetodoLeitura = Integer.parseInt(value.toString());
                break;
            case 18:
                Tonalidade = value.toString();
                break;
            default:
                break;
        }
    }
}
