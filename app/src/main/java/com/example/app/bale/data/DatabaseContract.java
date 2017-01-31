package com.example.app.bale.data;

/**
 * Created by 13108306 on 18/08/2016.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;

public class DatabaseContract {
    /* "Content authority" é o nome para o provedor de conteúdo, similiar a relação entre
    um domínio e um site. Uma string conveniente para se usar é o pacote da aplicação por
    ser um valor único. */
    public static final String CONTENT_AUTHORITY = "com.example.app.bale";

    /* Use a "Content authority" para criar a base de todas URIs que o aplicativo usará para
    * se comunicar com o provedor de conteúdo.*/
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_AVALIACOES = "avaliacoes";
    public static final String PATH_IDIOMAS = "idiomas";
    public static final String PATH_PARTICIPANTES = "participantes";
    public static final String PATH_PARTICIPANTES_IDIOMAS = "participantes_idiomas";
    public static final String PATH_PERGUNTAS = "perguntas";
    public static final String PATH_PERGUNTAS_AVALIACOES = "perguntas_avaliacoes";
    public static final String PATH_PESQUISAS = "pesquisas";
    public static final String PATH_RESPOSTAS = "respostas";
    public static final String PATH_USUARIOS = "usuarios";

    // Joins
    // Pesquisas em Andamento Geral
    public static final String PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL = "relationship_join_pesquisasemandamento_geral";
    public static final Uri CONTENT_URI_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL).build();
    public static final String CONTENT_ITEM_TYPE_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL;

    // Pesquisas em Andamneto Por Etapa
    public static final String PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA = "relationship_join_pesquisasemandamento_por_etapa";
    public static final Uri CONTENT_URI_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA).build();
    public static final String RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA;

    // Pesquisas Finalizadas Geral
    public static final String PATH_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL = "relationship_join_pesquisasfinalizadas_geral";
    public static final Uri CONTENT_URI_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL).build();
    public static final String CONTENT_ITEM_TYPE_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL;

    // Distinct
    public static final String PATH_DISTINCT_ETAPA_PERGUNTAS = "distinct_etapa_perguntas";
    public static final Uri CONTENT_URI_DISTINCT_ETAPA_PERGUNTAS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_DISTINCT_ETAPA_PERGUNTAS).build();
    public static final String CONTENT_ITEM_TYPE_DISTINCT_ETAPA_PERGUNTAS =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISTINCT_ETAPA_PERGUNTAS;

    /* Classe interna que define os conteúdos da tabela Avaliações */
    public static final class AvaliacoesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AVALIACOES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AVALIACOES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AVALIACOES;

        /* Nome da tabela */
        public static final String TABLE_NAME = "avaliacoes";

        /* Colunas */
        public static final String COLUMN_CONTEUDO = "conteudo";
        public static final String COLUMN_TIPO = "tipo";

        /* Gera o ID da tabela */
        public static Uri buildAvaliacoesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Idiomas */
    public static final class IdiomasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_IDIOMAS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IDIOMAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IDIOMAS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "idiomas";

        /* Colunas */
        public static final String COLUMN_NOME = "nome";

        /* Gera o ID da tabela */
        public static Uri buildIdiomasUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Participantes */
    public static final class ParticipantesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARTICIPANTES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPANTES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPANTES;

        /* Nome da tabela */
        public static final String TABLE_NAME = "participantes";

        /* Colunas */
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_DATA_DE_NASCIMENTO = "data_de_nascimento";
        public static final String COLUMN_GENERO = "genero";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_ESCOLARIDADE = "escolaridade";
        public static final String COLUMN_DOMINANCIA_MANUAL = "dominancia_manual";
        public static final String COLUMN_TEM_APOSENTADORIA = "tem_aposentadoria";
        public static final String COLUMN_PROFISSAO = "profissao";
        public static final String COLUMN_IDIOMA = "idioma";
        public static final String COLUMN_EXAMINADOR = "examinador";
        public static final String COLUMN_OBSERVACOES = "observacoes";

        /* Gera o ID da tabela */
        public static Uri buildParticipantesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Participantes_Idiomas */
    public static final class ParticipantesIdiomasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARTICIPANTES_IDIOMAS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPANTES_IDIOMAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPANTES_IDIOMAS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "participantes_idiomas";

        /* Colunas */
        public static final String COLUMN_ID_PARTICIPANTE = "id_participante";
        public static final String COLUMN_ID_IDIOMA = "id_idioma";

        /* Gera o ID da tabela */
        public static Uri buildParticipantesIdiomasUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Perguntas */
    public static final class PerguntasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERGUNTAS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERGUNTAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERGUNTAS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "perguntas";

        /* Colunas */
        public static final String COLUMN_CONTEUDO = "conteudo";
        public static final String COLUMN_NUMERACAO = "numeracao";
        public static final String COLUMN_ETAPA = "etapa";
        public static final String COLUMN_ESTA_ATIVA = "esta_ativa";


        /* Gera o ID da tabela */
        public static Uri buildPerguntasUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Perguntas_Avaliacoes */
    public static final class PerguntasAvaliacoesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERGUNTAS_AVALIACOES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERGUNTAS_AVALIACOES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERGUNTAS_AVALIACOES;

        /* Nome da tabela */
        public static final String TABLE_NAME = "perguntas_avaliacoes";

        /* Colunas */
        public static final String COLUMN_ID_PERGUNTA = "id_pergunta";
        public static final String COLUMN_ID_AVALIACAO = "id_avaliacao";

        /* Gera o ID da tabela */
        public static Uri buildPerguntasAvaliacoesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Pesquisas */
    public static final class PesquisasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PESQUISAS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PESQUISAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PESQUISAS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "pesquisas";

        /* Colunas */
        public static final String COLUMN_EXAMINADOR = "examinador";
        public static final String COLUMN_PARTICIPANTE = "participante";
        public static final String COLUMN_DATA_DE_REALIZACAO = "data_de_realizacao";
        public static final String COLUMN_ESTA_FINALIZADA = "esta_finalizada";


        /* Gera o ID da tabela */
        public static Uri buildPesquisasUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Respostas */
    public static final class RespostasEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESPOSTAS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESPOSTAS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESPOSTAS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "respostas";

        /* Colunas */
        public static final String COLUMN_ID_PESQUISA = "id_pessquisa";
        public static final String COLUMN_ID_PERGUNTA = "id_pergunta";
        public static final String COLUMN_RESPOSTA = "resposta";

        /* Gera o ID da tabela */
        public static Uri buildRespostasUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Classe interna que define os conteúdos da tabela Usuarios */
    public static final class UsuariosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USUARIOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USUARIOS;

        /* Nome da tabela */
        public static final String TABLE_NAME = "usuarios";

        /* Colunas */
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_SENHA = "senha";
        public static final String COLUMN_CODIGO = "codigo";
        public static final String COLUMN_E_ADMINISTRADOR = "e_administrador";

        /* Gera o ID da tabela */
        public static Uri buildUsuariosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
