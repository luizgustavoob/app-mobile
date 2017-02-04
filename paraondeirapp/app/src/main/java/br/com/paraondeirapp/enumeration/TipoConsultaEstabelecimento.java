package br.com.paraondeirapp.enumeration;

import br.com.paraondeirapp.view.ListaActivity;

public enum TipoConsultaEstabelecimento {

    SINCRONIZACAO {
        @Override
        public void executar(ListaActivity activity) {
            activity.consultarNoServidor();
        }
    },

    BDLOCAL {
        @Override
        public void executar(ListaActivity activity) {
            activity.consultarNoBancoLocal();
        }
    },

    SOLICITACAO {
        @Override
        public void executar(ListaActivity activity) {
            activity.solicitarIndicacoes();
        }
    };

    public abstract void executar(ListaActivity activity);
}
