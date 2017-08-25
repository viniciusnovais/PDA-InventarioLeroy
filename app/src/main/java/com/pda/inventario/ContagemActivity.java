package com.pda.inventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.ColetaBC;
import com.pda.inventario.businessComponent.EnderecoBC;
import com.pda.inventario.businessComponent.ProdutoBC;
import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.ContagemColetorEO;
import com.pda.inventario.entityObject.EnderecoColetorEO;
import com.pda.inventario.entityObject.InventarioEO;
import com.pda.inventario.entityObject.ProdutoEO;
import com.pda.inventario.entityObject.UsuarioEO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContagemActivity extends Activity {
    private AlertDialog alerta, dialogTonalidade;
    private String CURRENT_SELECTION, NEW_SELECTION, tonalidade = "", usuarioaLider = "", senhaLider = "";
    private int iCurrentSelection, countTentativa = 0, TIPO_ATIVIDADE = 0;
    private EditText etEndProd, etLoginLider, etPasswordLider, etQuantidade, etInteiro, etFracionado, editTonalidade;
    private TextView tvEndereco, tvDepartamento, tvSetor, tvProdDetail, tvTitulo;
    private Button btnFechar, btnLoginLider, btnOkMult, btnExcluir, btnZerar;
    private ImageView btnProcurar;
    private ListView lvColeta;
    private Spinner spinMetodoContagem;
    private ArrayAdapter adp;
    private EnderecoColetorEO objEndAberto = new EnderecoColetorEO();
    private List<ProdutoEO> objProdutoList = new ArrayList<ProdutoEO>();
    private UsuarioEO objUsuarioLogado = new UsuarioEO();
    private InventarioEO objInventarioCorrente = new InventarioEO();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contagem);

        Intent intent = getIntent();
        objUsuarioLogado = (UsuarioEO) intent.getSerializableExtra("UsuarioEO");
        objInventarioCorrente = (InventarioEO) intent.getSerializableExtra("InventarioEO");
        TIPO_ATIVIDADE = Integer.parseInt(intent.getSerializableExtra("TIPO_ATIVIDADE").toString());

        etEndProd = (EditText) findViewById(R.id.etEndProd);
        etEndProd.requestFocus();
        etEndProd.setText("");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        tvDepartamento = (TextView) findViewById(R.id.tvDepto);
        tvSetor = (TextView) findViewById(R.id.tvSetor);
        //tvProdDetail = (TextView) findViewById(R.id.tvProdDetail);
        tvTitulo = (TextView) findViewById(R.id.tvTituloTela);

        //btnProcurar = (ImageView) findViewById(R.id.btnProcurar);
        btnFechar = (Button) findViewById(R.id.btnFechar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);
        btnZerar = (Button) findViewById(R.id.btnZerar);

        lvColeta = (ListView) findViewById(R.id.lvColeta);
        lvColeta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ProdutoEO p = (ProdutoEO) parent.getItemAtPosition(position);
                Toast.makeText(ContagemActivity.this, "Produto selecionado: " + p.getDescSku(), Toast.LENGTH_SHORT).show();

                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ContagemActivity.this);
                        builder.setTitle("Deseja excluir produto: " + p.getDescSku() + " ?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ColetaBC repository = new ColetaBC(ContagemActivity.this);
                                        //na lista que me retorna o objeto produto, set id produtocod estou passando o id da coleta para deletar a coleta
                                        //só q estou utilizando o metodo set idprouto para mandar o id da coleta
                                        repository.DeleteProdutoByCod(p.getIdProduto());
                                        populaListViewColeta();
                                    }
                                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                });

            }
        });

        spinMetodoContagem = (Spinner) findViewById(R.id.spinMetodoContagem);

        this.setTituloTela();
        this.populaListViewColeta();
        this.populaSpinnerMetodoContagem();

        etEndProd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= etEndProd.getRight() - etEndProd.getTotalPaddingRight()) {
                        if (!etEndProd.getText().toString().equals("")) {
                            validaColeta();
                        } else {
                            Toast.makeText(ContagemActivity.this, StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        etEndProd.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!etEndProd.getText().toString().equals("")) {
                        validaColeta();
                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        btnFechar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!objProdutoList.isEmpty()) {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0
                            || spinMetodoContagem.getSelectedItemPosition() == 1
                            || spinMetodoContagem.getSelectedItemPosition() == 4) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    } else {
                        fecharVerificacao();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.COLET_ZERO, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != 0) {
                        ColetaBC repository = new ColetaBC(ContagemActivity.this);
                        EnderecoBC repositoryEnd = new EnderecoBC(ContagemActivity.this);

                        int ret = repository.getQtdeContagemByEndereco(String.valueOf(objEndAberto.IdEndereco));
                        if (ret == 0) {
                            repositoryEnd.DeleteEnderecoById(String.valueOf(objEndAberto.IdEndereco));
                        } else {
                            Toast.makeText(ContagemActivity.this, StringUtils.END_CONT, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnZerar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != 0) {
                        ColetaBC repository = new ColetaBC(ContagemActivity.this);
                        repository.deleteColetaEnd(String.valueOf(objEndAberto.IdEndereco));
                        populaListViewColeta();
                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        CURRENT_SELECTION = spinMetodoContagem.getSelectedItem().toString();

        iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();
        spinMetodoContagem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != 0) {
                        if (iCurrentSelection != position) {
                            NEW_SELECTION = spinMetodoContagem.getSelectedItem().toString();
                            spinMetodoContagem.setSelection(adp.getPosition(CURRENT_SELECTION));
                            autorizacaoLider();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    private void setTituloTela() {
        try {
            switch (TIPO_ATIVIDADE) {
                case 2:
                    tvTitulo.setText("Contagem");
                    break;
                case 3:
                    tvTitulo.setText("Auditoria");
                    break;
                case 4:
                    tvTitulo.setText("Contagem Final");
                    break;
                case 5:
                    tvTitulo.setText("Divergência");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populaListViewColeta() {
        ProdutoBC repositoryProd = new ProdutoBC(this);
        ColetaBC repository = new ColetaBC(this);
        List<ColetaEO> objColetaList = new ArrayList<ColetaEO>();

        try {
            if (objEndAberto != null) {
                if (objEndAberto.IdEndereco != 0) {
                    List<ProdutoEO> values = repository.getColetaListViewByEndereco(String.valueOf(objEndAberto.IdEndereco));

                    ContagemAdapter adapter = new ContagemAdapter(values, this);
                    lvColeta.setAdapter(adapter);

                    if (values != null) {

                        if (values.size() > 0) {
                            if (objProdutoList.isEmpty()) {
                                objColetaList = repository.GetColetaByEndereco(String.valueOf(objEndAberto.IdEndereco));

                                for (ColetaEO obj : objColetaList) {
                                    objProdutoList.add(repositoryProd.GetProdByEAN(obj.CodigoBarra));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populaSpinnerMetodoContagem() {
        try {

            String[] values = new String[]{
                    "Peça a peça",
                    "Múltiplos",
                    "Peça a peça com verificação",
                    "Múltiplos com verificação"

                    //quando existir o fracionado
                    //"Multiplos Fracionados"
            };

            adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
            adp.setDropDownViewResource(R.layout.spinner_row);
            spinMetodoContagem.setAdapter(adp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verificaEndereco() {
        EnderecoBC repository = new EnderecoBC(this);
        try {
            if (objEndAberto != null) {
                if (objEndAberto.IdEndereco == 0) {
                    objEndAberto = repository.GetEnderecoByCode(this.etEndProd.getText().toString());

                    if (objEndAberto.IdEndereco == 0) {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_NOK, Toast.LENGTH_SHORT).show();
                        etEndProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                etEndProd.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputMethodManager.showSoftInput(etEndProd, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                });
                            }
                        });

                        etEndProd.requestFocus();
                        etEndProd.setText("");
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_OPEN, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ContagemActivity.this, StringUtils.FAIL_END, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean verificaProduto() {
        ProdutoBC repository = new ProdutoBC(this);
        ProdutoEO objProduto = new ProdutoEO();

        try {
            objProduto = repository.GetProdByEAN(this.etEndProd.getText().toString());

            if (objProduto.getIdProduto() == 0) {
                objProduto = repository.GetProdBySKU(this.etEndProd.getText().toString());

                if (objProduto.getIdProduto() == 0) {
                    Toast.makeText(ContagemActivity.this, StringUtils.PROD_NOK, Toast.LENGTH_SHORT).show();
                    etEndProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            etEndProd.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.showSoftInput(etEndProd, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                        }
                    });

                    etEndProd.requestFocus();
                    etEndProd.setText("");
                    return false;
                } else {

                    if (objProduto.getPreco() == 1) {
                        popupTonalidade(objProduto);
                    } else {

                        if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                            objProduto.setColeta(1);
                            objProdutoList.add(objProduto);

                            this.insertColetaComTonalidade(objProduto, tonalidade);

                        } else if (spinMetodoContagem.getSelectedItemPosition() == 1
                                || spinMetodoContagem.getSelectedItemPosition() == 3
                                || spinMetodoContagem.getSelectedItemPosition() == 4) {
                            this.contagemMult(objProduto, tonalidade);
                        }
                    }
                }
            } else {

                if (objProduto.getPreco() == 1) {
                    popupTonalidade(objProduto);
                } else {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                        objProduto.setColeta(1);
                        objProdutoList.add(objProduto);

                        this.insertColetaComTonalidade(objProduto, tonalidade);

                    } else if (spinMetodoContagem.getSelectedItemPosition() == 1
                            || spinMetodoContagem.getSelectedItemPosition() == 3
                            || spinMetodoContagem.getSelectedItemPosition() == 4) {
                        this.contagemMult(objProduto, tonalidade);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //uso esse metodo por precisei verificar a tonalidade
    private void verificaProduto2(ProdutoEO objProduto, String tonalidade) {

        try {

            if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                objProduto.setColeta(1);
                objProdutoList.add(objProduto);

                this.insertColetaComTonalidade(objProduto, tonalidade);

            } else if (spinMetodoContagem.getSelectedItemPosition() == 1
                    || spinMetodoContagem.getSelectedItemPosition() == 3
                    || spinMetodoContagem.getSelectedItemPosition() == 4) {
                this.contagemMult(objProduto, tonalidade);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void insertColetaComTonalidade(ProdutoEO objProduto, String tonalidade) {
        ColetaBC repository = new ColetaBC(this);
        ColetaEO objColeta;

        try {
            objColeta = new ColetaEO(0, objProduto.getCodAutomacao(), objProduto.getColeta(), objInventarioCorrente.getIdInventario(), objEndAberto.IdEndereco, "30-05-2016", 1, TIPO_ATIVIDADE, 0,
                    objProduto.getCodSku(), objProduto.getDescSku(), 1, String.valueOf(new Random().nextInt() + 1), 0, "", 0, objUsuarioLogado.getCodigo(), 1, tonalidade);

            repository.CreateColeta(objColeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaColeta() {
        if (objEndAberto != null) {
            if (objEndAberto.IdEndereco == 0) {
                if (verificaEndereco()) {
                    tvEndereco.setText(StringUtils.END + objEndAberto.Endereco);
                    tvDepartamento.setText(StringUtils.DPTO + objEndAberto.Departamento);
                    tvSetor.setText(StringUtils.SETOR + objEndAberto.Setor);

                    this.populaListViewColeta();

                    spinMetodoContagem.setSelection(adp.getPosition(objEndAberto.MetodoContagem));
                    iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();
                    CURRENT_SELECTION = spinMetodoContagem.getSelectedItem().toString();

                    etEndProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            etEndProd.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.showSoftInput(etEndProd, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                        }
                    });

                    etEndProd.requestFocus();
                    etEndProd.setText("");
                }
            } else {
                if (verificaProduto()) {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                        tvProdDetail.setText(StringUtils.PROD + objProdutoList.get(objProdutoList.size() - 1).getDescSku());

                        Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
                        this.populaListViewColeta();

                        etEndProd.requestFocus();
                        etEndProd.setText("");
                    }
                }
            }
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Integer, Void> {
        private final ProgressDialog dialog = new ProgressDialog(ContagemActivity.this);

        @Override
        protected void onPreExecute() {
            Log.i("Response", "onPreExecute");
            this.dialog.setMessage(StringUtils.CLOSING_END);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Response", "doInBackground");
            setContagem();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("Response", "onPostExecute");
            Toast.makeText(getApplicationContext(), StringUtils.CLOSING_END_OK, Toast.LENGTH_LONG).show();
            fechaEnderecoControls();
            this.dialog.dismiss();
        }
    }

    private void fechaEnderecoControls() {
        try {
            etEndProd.setText("");
            etEndProd.requestFocus();
            lvColeta.setAdapter(null);
            objEndAberto = new EnderecoColetorEO();
            objProdutoList = new ArrayList<ProdutoEO>();
            tvEndereco.setText(StringUtils.END);
            tvDepartamento.setText(StringUtils.DPTO);
            tvSetor.setText(StringUtils.SETOR);
            tvProdDetail.setText(StringUtils.PROD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContagem() {

        String SOAP_ACTION = "http://tempuri.org/SetColeta";
        String METHOD_NAME = "SetColeta";
        String NAMESPACE = "http://tempuri.org/";
        //String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";

        try {
            ColetaBC repository = new ColetaBC(this);
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            List<ContagemColetorEO> obj = new ArrayList<ContagemColetorEO>();

            obj = repository.GetContagemColetorByEndereco(String.valueOf(objEndAberto.IdEndereco));

            SoapObject soapEntity = new SoapObject(NAMESPACE, "entity");

            for (ContagemColetorEO i : obj) {
                soapEntity.addProperty("ContagemColetorEO", i);
            }
            Request.addSoapObject(soapEntity);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            MarshalFloat mdFloat = new MarshalFloat();
            mdFloat.register(soapEnvelope);

            soapEnvelope.addMapping(NAMESPACE, "ContagemColetorEO", new ContagemColetorEO().getClass());

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            repository.UpdateColetaExport(String.valueOf(objEndAberto.IdEndereco));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaAutorizacaoLider() {
        try {
            if (etLoginLider.getText().toString().equals("") || etPasswordLider.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
            } else {
                usuarioaLider = etLoginLider.getText().toString();
                senhaLider = etPasswordLider.getText().toString();
                AsyncCallWSAutenticacao task = new AsyncCallWSAutenticacao();
                task.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autorizacaoLider() {
        LayoutInflater li = getLayoutInflater();

        View view = li.inflate(R.layout.login_lider_novo, null);
        etLoginLider = (EditText) view.findViewById(R.id.etUserLider);
        etPasswordLider = (EditText) view.findViewById(R.id.etPasswordLider);
        btnLoginLider = (Button) view.findViewById(R.id.btnOk);

        btnLoginLider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                validaAutorizacaoLider();
            }
        });

        etLoginLider.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    etPasswordLider.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etPasswordLider.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    validaAutorizacaoLider();
                    return true;
                }
                return false;
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(StringUtils.ALERT_TITLE);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    private void validaContMult(final ProdutoEO prod, String tonalidade) {

        try {
            //pega oq foi digitado
            String qtde = etInteiro.getText().toString().concat(".").concat(etFracionado.getText().toString());

            if ((etInteiro.getText().toString().equals("") && etFracionado.getText().toString().equals(""))
                    || Float.parseFloat(qtde) < 0) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD_ZERO, Toast.LENGTH_SHORT).show();
            } else {
                prod.setColeta(Float.parseFloat(qtde));
                objProdutoList.add(prod);
                insertColetaComTonalidade(prod, tonalidade);

                Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
                populaListViewColeta();

                etEndProd.requestFocus();
                etEndProd.setText("");

                alerta.dismiss();
            }


        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        try {
//            String qtde = etQuantidade.getText().toString().replaceAll("[,]", ".");
//
//            if (etQuantidade.getText().toString().equals("") || Float.parseFloat(qtde) < 0) {
//                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD_ZERO, Toast.LENGTH_SHORT).show();
//            } else {
//                prod.setColeta(Float.parseFloat(qtde));
//                objProdutoList.add(prod);
//                insertColetaComTonalidade(prod, tonalidade);
//
//                //tvProdDetail.setText(StringUtils.PROD + objProdutoList.get(objProdutoList.size() - 1).getDescSku());
//
//                Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
//                populaListViewColeta();
//
//                etEndProd.requestFocus();
//                etEndProd.setText("");
//
//                alerta.dismiss();
//            }
//
//
//        } catch (Exception e) {
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    private void contagemMult(ProdutoEO objProduto, final String tonalidade) {
        try {

            final ProdutoEO prod = objProduto;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(ContagemActivity.this, R.layout.unidade_medida, null);
            etInteiro = (EditText) view.findViewById(R.id.etInteiro);
            etFracionado = (EditText) view.findViewById(R.id.etDecimal);
            //etQuantidade = (EditText) view.findViewById(R.id.etColeta);
            btnOkMult = (Button) view.findViewById(R.id.btnEntrar);

            //ainda não faz nada
//            verificaQuantidade(objProduto.getUnidadeMedida());
            builder.setTitle(StringUtils.ALERT_TITLE_TYPE_COUNT + objProduto.getUnidadeMedida());

            builder.setView(view);
            alerta = builder.create();
            alerta.show();

            btnOkMult.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    validaContMult(prod, tonalidade);
                }
            });

            etInteiro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    etInteiro.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(etInteiro, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            });

            etInteiro.requestFocus();

            etInteiro.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        etFracionado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                etFracionado.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputMethodManager.showSoftInput(etFracionado, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                });
                            }
                        });

                        etFracionado.requestFocus();
                        return true;
                    }
                    return false;
                }
            });

            etFracionado.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validaContMult(prod, tonalidade);
                        return true;
                    }
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWSAutenticacao extends AsyncTask<Void, Void, Void> {
        String resultString;
        private final ProgressDialog dialog = new ProgressDialog(ContagemActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(StringUtils.AUTHENTICATION);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultString = getLogin(usuarioaLider, senhaLider);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (resultString.compareTo(StringUtils.AUTHENTICATION_OK) == 0) {
                alerta.dismiss();
                CURRENT_SELECTION = NEW_SELECTION;
            }
            spinMetodoContagem.setSelection(adp.getPosition(CURRENT_SELECTION));
            iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();

            showResult(resultString);
            this.dialog.dismiss();
        }
    }

    public void showResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public String getLogin(String user, String password) {
        String SOAP_ACTION = "http://tempuri.org/GetAutenticacao";
        String METHOD_NAME = "GetAutenticacao";
        String NAMESPACE = "http://tempuri.org/";
        //String URL = "http://179.184.159.52/wsandroid/autenticacao.asmx";
        String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("login_", user);
            Request.addProperty("senha_", password);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoap = (SoapObject) soapEnvelope.getResponse();

            if (!objSoap.hasProperty("mensagemErro")) {
                if (Integer.parseInt(objSoap.getProperty("CodigoPerfil").toString()) == 1) {
                    return StringUtils.AUTHENTICATION_OK;
                } else {
                    return StringUtils.PERMISSION_NOK;
                }
            } else {
                return objSoap.getProperty("mensagemErro").toString();
            }

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    public void popupTonalidade(final ProdutoEO objProduto) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v = View.inflate(ContagemActivity.this, R.layout.tonalidade, null);
            editTonalidade = (EditText) v.findViewById(R.id.etTonalidade);
            Button btConfirma = (Button) v.findViewById(R.id.btnEntrar);
            builder.setTitle("Tonalidade");
            builder.setView(v);
            dialogTonalidade = builder.create();
            dialogTonalidade.show();

            editTonalidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    editTonalidade.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager inputMethodManager = (InputMethodManager) ContagemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(editTonalidade, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            });

            editTonalidade.requestFocus();

            editTonalidade.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!editTonalidade.getText().toString().equals("")) {
                            tonalidade = editTonalidade.getText().toString();
                            dialogTonalidade.dismiss();
                            verificaProduto2(objProduto, tonalidade);
                            tonalidade = "";

                        } else {
                            Toast.makeText(ContagemActivity.this, "Insira a tonalidade", Toast.LENGTH_SHORT).show();
                            editTonalidade.setText("");
                            editTonalidade.requestFocus();
                        }
                        return true;
                    }
                    return false;
                }
            });


            btConfirma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editTonalidade.getText().toString().equals("")) {
                        tonalidade = editTonalidade.getText().toString();
                        dialogTonalidade.dismiss();
                        verificaProduto2(objProduto, tonalidade);
                        tonalidade = "";

                    } else {
                        Toast.makeText(ContagemActivity.this, "Insira a tonalidade", Toast.LENGTH_SHORT).show();
                        editTonalidade.setText("");
                        editTonalidade.requestFocus();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaFecharEndMult(final ColetaBC repository) {
        try {
            int ret = 0;
            if (etQuantidade.getText().toString().equals("") || etQuantidade.getText().toString().equals("0")) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD_ZERO, Toast.LENGTH_SHORT).show();
            } else {
                ret = repository.getQtdeContagemByEndereco(String.valueOf(objEndAberto.IdEndereco));

                if (ret == Integer.parseInt(etQuantidade.getText().toString())) {
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                    alerta.dismiss();
                    countTentativa = 0;
                } else {
                    Toast.makeText(getApplicationContext(), StringUtils.WRONG_QUANTITY, Toast.LENGTH_SHORT).show();
                    countTentativa++;
                    etQuantidade.setText("");
                    etQuantidade.requestFocus();

                    if (countTentativa == 3) {
                        countTentativa = 0;
                        repository.deleteColetaEnd(String.valueOf(objEndAberto.IdEndereco));
                        alerta.dismiss();
                        etEndProd.setText("");
                        etEndProd.requestFocus();
                        lvColeta.setAdapter(null);
                        objProdutoList = new ArrayList<ProdutoEO>();
                        tvProdDetail.setText(StringUtils.PROD);
                        Toast.makeText(getApplicationContext(), StringUtils.END_ZERO, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fecharVerificacao() {
        try {
            final ColetaBC repositoryColeta = new ColetaBC(this);

            LayoutInflater li = getLayoutInflater();

            View view = li.inflate(R.layout.multiplos_novo, null);
            etQuantidade = (EditText) view.findViewById(R.id.etColeta);
            btnOkMult = (Button) view.findViewById(R.id.btnEntrar);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(StringUtils.ALERT_TITLE_VERIFY);
            builder.setView(view);
            alerta = builder.create();
            alerta.show();

            btnOkMult.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    validaFecharEndMult(repositoryColeta);
                }
            });

            etQuantidade.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validaFecharEndMult(repositoryColeta);
                        return true;
                    }
                    return false;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
