package com.example.app.bale.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.bale.data.DatabaseContract.AvaliacoesEntry;
import com.example.app.bale.data.DatabaseContract.IdiomasEntry;
import com.example.app.bale.data.DatabaseContract.ParticipantesEntry;
import com.example.app.bale.data.DatabaseContract.ParticipantesIdiomasEntry;
import com.example.app.bale.data.DatabaseContract.PerguntasEntry;
import com.example.app.bale.data.DatabaseContract.PerguntasAvaliacoesEntry;
import com.example.app.bale.data.DatabaseContract.PesquisasEntry;
import com.example.app.bale.data.DatabaseContract.RespostasEntry;
import com.example.app.bale.data.DatabaseContract.UsuariosEntry;

/**
 * Created by 13108306 on 18/08/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "bale.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /* Tabelas principais */
        final String SQL_CREATE_AVALIACOES_TABLE = "CREATE TABLE " + AvaliacoesEntry.TABLE_NAME + " (" +
                AvaliacoesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AvaliacoesEntry.COLUMN_CONTEUDO + " VARCHAR(200) UNIQUE NOT NULL," +
                AvaliacoesEntry.COLUMN_TIPO + " VARCHAR(20) NOT NULL " +
                ");";

        final String SQL_CREATE_IDIOMAS_TABLE = "CREATE TABLE " + IdiomasEntry.TABLE_NAME + " (" +
                IdiomasEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IdiomasEntry.COLUMN_NOME + " VARCHAR(50) UNIQUE NOT NULL " +
                ");";

        final String SQL_CREATE_PARTICIPANTES_TABLE = "CREATE TABLE " + ParticipantesEntry.TABLE_NAME + " (" +
                ParticipantesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ParticipantesEntry.COLUMN_NOME + " VARCHAR(200) NOT NULL," +
                ParticipantesEntry.COLUMN_DATA_DE_NASCIMENTO + " DATE NOT NULL," +
                ParticipantesEntry.COLUMN_GENERO + " VARCHAR(10)," +
                ParticipantesEntry.COLUMN_TELEFONE + " VARCHAR(11) NOT NULL," +
                ParticipantesEntry.COLUMN_ESCOLARIDADE + " NUMBER(2,0) NOT NULL," +
                ParticipantesEntry.COLUMN_DOMINANCIA_MANUAL + " CHAR(1) NOT NULL," +
                ParticipantesEntry.COLUMN_TEM_APOSENTADORIA + " BOOLEAN NOT NULL," +
                ParticipantesEntry.COLUMN_PROFISSAO + " VARCHAR(100) NOT NULL," +
                ParticipantesEntry.COLUMN_IDIOMA + " INTEGER NOT NULL," +
                ParticipantesEntry.COLUMN_EXAMINADOR + " VARCHAR(5) NOT NULL," +
                ParticipantesEntry.COLUMN_OBSERVACOES + " VARCHAR(5000)," +
                "FOREIGN KEY(" + ParticipantesEntry.COLUMN_IDIOMA + ") REFERENCES " +
                IdiomasEntry.TABLE_NAME + "(" + IdiomasEntry._ID + ")," +
                "FOREIGN KEY(" + ParticipantesEntry.COLUMN_EXAMINADOR + ") REFERENCES " +
                UsuariosEntry.TABLE_NAME + "(" + UsuariosEntry._ID + ") " +
                ");";

        final String SQL_CREATE_PERGUNTAS_TABLE = "CREATE TABLE " + PerguntasEntry.TABLE_NAME + " (" +
                PerguntasEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PerguntasEntry.COLUMN_CONTEUDO + " VARCHAR(1500) NOT NULL," +
                PerguntasEntry.COLUMN_NUMERACAO + " NUMBER(2,0) NOT NULL," +
                PerguntasEntry.COLUMN_ETAPA + " VARCHAR(100) NOT NULL," +
                PerguntasEntry.COLUMN_ESTA_ATIVA + " BOOLEAN NOT NULL " +
                ");";

        final String SQL_CREATE_PESQUISAS_TABLE = "CREATE TABLE " + PesquisasEntry.TABLE_NAME + " (" +
                PesquisasEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PesquisasEntry.COLUMN_EXAMINADOR +  " VARCHAR(5) NOT NULL," +
                PesquisasEntry.COLUMN_PARTICIPANTE + " VARCHAR(10) UNIQUE," +
                PesquisasEntry.COLUMN_DATA_DE_REALIZACAO + " DATE NOT NULL," +
                PesquisasEntry.COLUMN_ESTA_FINALIZADA + " BOOLEAN NOT NULL," +
                "FOREIGN KEY(" + PesquisasEntry.COLUMN_EXAMINADOR+ ") REFERENCES " +
                UsuariosEntry.TABLE_NAME + "(" + UsuariosEntry._ID + ")," +
                "FOREIGN KEY(" + PesquisasEntry.COLUMN_PARTICIPANTE + ") REFERENCES " +
                ParticipantesEntry.TABLE_NAME + "(" + ParticipantesEntry._ID + ") " +
                ");";

        final String SQL_CREATE_USUARIOS_TABLE = "CREATE TABLE " + UsuariosEntry.TABLE_NAME + " (" +
                UsuariosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UsuariosEntry.COLUMN_NOME + " VARCHAR(100) NOT NULL," +
                UsuariosEntry.COLUMN_EMAIL + " VARCHAR(100) UNIQUE NOT NULL," +
                UsuariosEntry.COLUMN_SENHA + " VARCHAR(15) NOT NULL," +
                UsuariosEntry.COLUMN_CODIGO + " VARCHAR(6) NULL," +
                UsuariosEntry.COLUMN_E_ADMINISTRADOR + " BOOLEAN NOT NULL " +
                ");";

        /* Tabelas intermediárias */
        final String SQL_CREATE_PARTICIPANTES_IDIOMA_TABLE = "CREATE TABLE " + ParticipantesIdiomasEntry.TABLE_NAME + " (" +
                ParticipantesIdiomasEntry.COLUMN_ID_PARTICIPANTE + " INTEGER NOT NULL," +
                ParticipantesIdiomasEntry.COLUMN_ID_IDIOMA + " INTEGER NOT NULL," +
                "PRIMARY KEY(" + ParticipantesIdiomasEntry.COLUMN_ID_PARTICIPANTE + ", " +
                ParticipantesIdiomasEntry.COLUMN_ID_IDIOMA + ")," +
                "FOREIGN KEY(" + ParticipantesIdiomasEntry.COLUMN_ID_PARTICIPANTE + ") REFERENCES " +
                ParticipantesEntry.TABLE_NAME + "(" + ParticipantesEntry._ID + ")," +
                "FOREIGN KEY(" + ParticipantesIdiomasEntry.COLUMN_ID_IDIOMA + ") REFERENCES " +
                IdiomasEntry.TABLE_NAME + "(" + IdiomasEntry._ID + ") " +
                ");";

        final String SQL_CREATE_PERGUNTAS_AVALIACOES_TABLE = "CREATE TABLE " + PerguntasAvaliacoesEntry.TABLE_NAME + " (" +
                PerguntasAvaliacoesEntry.COLUMN_ID_PERGUNTA + " INTEGER NOT NULL," +
                PerguntasAvaliacoesEntry.COLUMN_ID_AVALIACAO + " INTEGER NOT NULL," +
                "PRIMARY KEY(" + PerguntasAvaliacoesEntry.COLUMN_ID_PERGUNTA + ", " +
                PerguntasAvaliacoesEntry.COLUMN_ID_AVALIACAO + ")," +
                "FOREIGN KEY(" + PerguntasAvaliacoesEntry.COLUMN_ID_PERGUNTA + ") REFERENCES " +
                PerguntasEntry.TABLE_NAME + "(" + PerguntasEntry._ID + ")," +
                "FOREIGN KEY(" + PerguntasAvaliacoesEntry.COLUMN_ID_AVALIACAO + ") REFERENCES " +
                AvaliacoesEntry.TABLE_NAME + "(" + AvaliacoesEntry._ID + ") " +
                ");";

        final String SQL_CREATE_RESPOSTAS_TABLE = "CREATE TABLE " + RespostasEntry.TABLE_NAME + " (" +
                RespostasEntry.COLUMN_ID_PESQUISA + " INTEGER NOT NULL," +
                RespostasEntry.COLUMN_ID_PERGUNTA + " INTEGER NOT NULL," +
                "PRIMARY KEY(" + RespostasEntry.COLUMN_ID_PESQUISA + ", " +
                RespostasEntry.COLUMN_ID_PERGUNTA + ")," +
                "FOREIGN KEY(" + RespostasEntry.COLUMN_ID_PESQUISA + ") REFERENCES " +
                PesquisasEntry.TABLE_NAME + "(" + PesquisasEntry._ID + ")," +
                "FOREIGN KEY(" + RespostasEntry.COLUMN_ID_PERGUNTA + ") REFERENCES " +
                PerguntasEntry.TABLE_NAME + "(" + PerguntasEntry._ID + ") " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_AVALIACOES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_IDIOMAS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PERGUNTAS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USUARIOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PARTICIPANTES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PARTICIPANTES_IDIOMA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PERGUNTAS_AVALIACOES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PESQUISAS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RESPOSTAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RespostasEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PesquisasEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PerguntasAvaliacoesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParticipantesIdiomasEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParticipantesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UsuariosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PerguntasEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IdiomasEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AvaliacoesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
