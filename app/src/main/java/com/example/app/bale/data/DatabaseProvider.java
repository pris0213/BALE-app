package com.example.app.bale.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by 13108306 on 19/08/2016.
 */
public class DatabaseProvider extends ContentProvider {

    // UriMatcher usada por este provedor de conteúdo
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    static final int AVALIACOES = 100;
    static final int IDIOMAS = 200;
    static final int PARTICIPANTES = 300;
    static final int PARTICIPANTES_IDIOMAS = 400;
    static final int PERGUNTAS = 500;
    static final int PERGUNTAS_AVALIACOES = 600;
    static final int PESQUISAS = 700;
    static final int RESPOSTAS = 800;
    static final int USUARIOS = 900;
    // Joins
    static final int RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL = 701;
    static final int RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA = 702;
    static final int RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL = 750;
    // Distinct
    static final int DISTINCT_ETAPA_PERGUNTAS = 501;

    static UriMatcher buildUriMatcher() {

        /* Todos caminhos (paths) adicionados à UriMatcher têm um código correspondente que será
        retornado quando seu par for encontrado. O código passado dentro do construtor representa o códigio
        a ser retornado para a URI raiz. É comum usar NO_MATCH como o código nesse caso.*/
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        /* Para cada tipo de URI que você deseja adicionar, crie um código correspondente.*/
        matcher.addURI(authority, DatabaseContract.PATH_AVALIACOES, AVALIACOES);
        matcher.addURI(authority, DatabaseContract.PATH_IDIOMAS, IDIOMAS);
        matcher.addURI(authority, DatabaseContract.PATH_PARTICIPANTES, PARTICIPANTES);
        matcher.addURI(authority, DatabaseContract.PATH_PARTICIPANTES_IDIOMAS, PARTICIPANTES_IDIOMAS);
        matcher.addURI(authority, DatabaseContract.PATH_PERGUNTAS, PERGUNTAS);
        matcher.addURI(authority, DatabaseContract.PATH_PERGUNTAS_AVALIACOES, PERGUNTAS_AVALIACOES);
        matcher.addURI(authority, DatabaseContract.PATH_PESQUISAS, PESQUISAS);
        matcher.addURI(authority, DatabaseContract.PATH_RESPOSTAS, RESPOSTAS);
        matcher.addURI(authority, DatabaseContract.PATH_USUARIOS, USUARIOS);
        // Joins
        matcher.addURI(authority, DatabaseContract.PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL + "/",
                RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL);
        matcher.addURI(authority, DatabaseContract.PATH_RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA + "/",
                RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA);
        matcher.addURI(authority, DatabaseContract.PATH_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL + "/",
                RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL);
        // Distinct
        matcher.addURI(authority, DatabaseContract.PATH_DISTINCT_ETAPA_PERGUNTAS + "/",
                DISTINCT_ETAPA_PERGUNTAS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case AVALIACOES:
                return DatabaseContract.AvaliacoesEntry.CONTENT_ITEM_TYPE;
            case IDIOMAS:
                return DatabaseContract.IdiomasEntry.CONTENT_TYPE;
            case PARTICIPANTES:
                return DatabaseContract.ParticipantesEntry.CONTENT_TYPE;
            case PARTICIPANTES_IDIOMAS:
                return DatabaseContract.ParticipantesIdiomasEntry.CONTENT_TYPE;
            case PERGUNTAS:
                return DatabaseContract.PerguntasEntry.CONTENT_TYPE;
            case PERGUNTAS_AVALIACOES:
                return DatabaseContract.PerguntasAvaliacoesEntry.CONTENT_TYPE;
            case PESQUISAS:
                return DatabaseContract.PesquisasEntry.CONTENT_TYPE;
            case RESPOSTAS:
                return DatabaseContract.RespostasEntry.CONTENT_TYPE;
            case USUARIOS:
                return DatabaseContract.UsuariosEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case AVALIACOES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.AvaliacoesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case IDIOMAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.IdiomasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARTICIPANTES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.ParticipantesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARTICIPANTES_IDIOMAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PERGUNTAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.PerguntasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PERGUNTAS_AVALIACOES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PESQUISAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.PesquisasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RESPOSTAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.RespostasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USUARIOS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.UsuariosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_GERAL:
            {
               retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                       "SELECT " +
                       DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID + ", " +
                       DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_DATA_DE_REALIZACAO + ", " +
                       DatabaseContract.ParticipantesEntry.TABLE_NAME + "." + DatabaseContract.ParticipantesEntry.COLUMN_NOME + " AS PARTICIPANTE_NOME, "+
                       DatabaseContract.UsuariosEntry.TABLE_NAME + "." + DatabaseContract.UsuariosEntry.COLUMN_NOME + " AS EXAMINADOR_NOME" +
                       " FROM " + DatabaseContract.PesquisasEntry.TABLE_NAME +
                       " INNER JOIN " + DatabaseContract.ParticipantesEntry.TABLE_NAME +
                       " ON " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_PARTICIPANTE +
                       " = " + DatabaseContract.ParticipantesEntry.TABLE_NAME + "." + DatabaseContract.ParticipantesEntry._ID +
                       " INNER JOIN " + DatabaseContract.UsuariosEntry.TABLE_NAME +
                       " ON " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR +
                       " = " + DatabaseContract.UsuariosEntry.TABLE_NAME + "." + DatabaseContract.UsuariosEntry._ID +
                       // " WHERE NOT(" + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_ESTA_FINALIZADA + ")",
                       " WHERE " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_ESTA_FINALIZADA  + " = 0" +
                       " AND " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR + " = ?",
                       selectionArgs
                );
                break;
            }
            case RELATIONSHIP_JOIN_PESQUISASEMANDAMENTO_POR_ETAPA:
            {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                      "SELECT " +
                      DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID + " AS PESQUISA_ID, " +
                      DatabaseContract.RespostasEntry.TABLE_NAME + "." + DatabaseContract.RespostasEntry.COLUMN_ID_PERGUNTA
                              + " AS PERGUNTA_PARTICIPANTE_ID, " +
                      DatabaseContract.PerguntasEntry.TABLE_NAME + "." + DatabaseContract.PerguntasEntry._ID + " AS PERGUNTA_ID, " +
                      DatabaseContract.PerguntasEntry.TABLE_NAME + "." + DatabaseContract.PerguntasEntry.COLUMN_ETAPA +
                      " FROM " + DatabaseContract.PerguntasEntry.TABLE_NAME +
                      " LEFT OUTER JOIN " + DatabaseContract.RespostasEntry.TABLE_NAME +
                      " ON " + DatabaseContract.PerguntasEntry.TABLE_NAME + "." + DatabaseContract.PerguntasEntry._ID +
                      " = " + DatabaseContract.RespostasEntry.TABLE_NAME + "." + DatabaseContract.RespostasEntry.COLUMN_ID_PERGUNTA +
                      " INNER JOIN " + DatabaseContract.PesquisasEntry.TABLE_NAME +
                      " ON " + DatabaseContract.RespostasEntry.TABLE_NAME + "." + DatabaseContract.RespostasEntry.COLUMN_ID_PESQUISA +
                      " = " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID +
                      " WHERE " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR + " = ?" +
                      " ORDER BY " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID,
                      selectionArgs
                );
                break;
            }
            case DISTINCT_ETAPA_PERGUNTAS:
            {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                      "SELECT DISTINCT " +
                      DatabaseContract.PerguntasEntry.TABLE_NAME + "." + DatabaseContract.PerguntasEntry.COLUMN_ETAPA +
                      " FROM " + DatabaseContract.PerguntasEntry.TABLE_NAME +
                      // 0 = false, 1 = true
                      " WHERE " + DatabaseContract.PerguntasEntry.COLUMN_ESTA_ATIVA + " = 1",
                      // + " GROUP BY " + DatabaseContract.PerguntasEntry.TABLE_NAME + "." + DatabaseContract.PerguntasEntry.COLUMN_ETAPA,
                      selectionArgs
                );
                break;
            }
            case RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL:
            {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                        "SELECT " +
                                DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID + ", " +
                                DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_DATA_DE_REALIZACAO + ", " +
                                DatabaseContract.ParticipantesEntry.TABLE_NAME + "." + DatabaseContract.ParticipantesEntry.COLUMN_NOME + " AS PARTICIPANTE_NOME, "+
                                DatabaseContract.UsuariosEntry.TABLE_NAME + "." + DatabaseContract.UsuariosEntry.COLUMN_NOME + " AS EXAMINADOR_NOME" +
                                " FROM " + DatabaseContract.PesquisasEntry.TABLE_NAME +
                                " INNER JOIN " + DatabaseContract.ParticipantesEntry.TABLE_NAME +
                                " ON " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_PARTICIPANTE +
                                " = " + DatabaseContract.ParticipantesEntry.TABLE_NAME + "." + DatabaseContract.ParticipantesEntry._ID +
                                " INNER JOIN " + DatabaseContract.UsuariosEntry.TABLE_NAME +
                                " ON " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR +
                                " = " + DatabaseContract.UsuariosEntry.TABLE_NAME + "." + DatabaseContract.UsuariosEntry._ID +
                                // " WHERE NOT(" + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_ESTA_FINALIZADA + ")",
                                " WHERE " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_ESTA_FINALIZADA  + " = 1" +
                                " AND " + DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR + " = ?",
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case AVALIACOES: {
                long _id = db.insert(DatabaseContract.AvaliacoesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.AvaliacoesEntry.buildAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case IDIOMAS: {
                long _id = db.insert(DatabaseContract.IdiomasEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.IdiomasEntry.buildIdiomasUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case PARTICIPANTES: {
                long _id = db.insert(DatabaseContract.ParticipantesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.ParticipantesEntry.buildParticipantesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case PARTICIPANTES_IDIOMAS: {
                long _id = db.insert(DatabaseContract.ParticipantesIdiomasEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.ParticipantesIdiomasEntry.buildParticipantesIdiomasUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case PERGUNTAS: {
                long _id = db.insert(DatabaseContract.PerguntasEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.PerguntasEntry.buildPerguntasUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case PERGUNTAS_AVALIACOES: {
                long _id = db.insert(DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.PerguntasAvaliacoesEntry.buildPerguntasAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case PESQUISAS: {
                long _id = db.insert(DatabaseContract.PesquisasEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.PesquisasEntry.buildPesquisasUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case RESPOSTAS: {
                long _id = db.insert(DatabaseContract.RespostasEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.RespostasEntry.buildRespostasUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case USUARIOS: {
                long _id = db.insert(DatabaseContract.UsuariosEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.UsuariosEntry.buildUsuariosUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case AVALIACOES: {
                rowsDeleted = db.delete(
                        DatabaseContract.AvaliacoesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case IDIOMAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.IdiomasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PARTICIPANTES: {
                rowsDeleted = db.delete(
                        DatabaseContract.ParticipantesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PARTICIPANTES_IDIOMAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.ParticipantesIdiomasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PERGUNTAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.PerguntasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PERGUNTAS_AVALIACOES: {
                rowsDeleted = db.delete(
                        DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PESQUISAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.PesquisasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case RESPOSTAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.RespostasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case USUARIOS: {
                rowsDeleted = db.delete(
                        DatabaseContract.UsuariosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case AVALIACOES:
                rowsUpdated = db.update(DatabaseContract.AvaliacoesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case IDIOMAS:
                rowsUpdated = db.update(DatabaseContract.IdiomasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PARTICIPANTES:
                rowsUpdated = db.update(DatabaseContract.ParticipantesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PARTICIPANTES_IDIOMAS:
                rowsUpdated = db.update(DatabaseContract.ParticipantesIdiomasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PERGUNTAS:
                rowsUpdated = db.update(DatabaseContract.PerguntasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PERGUNTAS_AVALIACOES:
                rowsUpdated = db.update(DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PESQUISAS:
                rowsUpdated = db.update(DatabaseContract.PesquisasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RESPOSTAS:
                rowsUpdated = db.update(DatabaseContract.RespostasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USUARIOS:
                rowsUpdated = db.update(DatabaseContract.UsuariosEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case AVALIACOES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.AvaliacoesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case IDIOMAS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.IdiomasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PARTICIPANTES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.ParticipantesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PARTICIPANTES_IDIOMAS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.AvaliacoesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PERGUNTAS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.PerguntasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PERGUNTAS_AVALIACOES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.PerguntasAvaliacoesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PESQUISAS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.PesquisasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case RESPOSTAS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.RespostasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case USUARIOS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.UsuariosEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }



    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
