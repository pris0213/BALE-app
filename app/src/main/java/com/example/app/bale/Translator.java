package com.example.app.bale;

import android.app.Activity;

/**
 * Created by 13108306 on 25/10/2016.
 */
public final class Translator {

    private Translator() {
    }

    // Converte o "month" para um mês (Converte o mês em inglês para português)
    public static String toMes(String month, Activity activity) {
        String mes = "";
        switch (month) {
            case "Jan":
            case "1":
                mes = activity.getResources().getString(R.string.mes_janeiro);
                break;
            case "Feb":
            case "2":
                mes = activity.getResources().getString(R.string.mes_fevereiro);
                break;
            case "Mar":
            case "3":
                mes = activity.getResources().getString(R.string.mes_marco);
                break;
            case "Apr":
            case "4":
                mes = activity.getResources().getString(R.string.mes_abril);
                break;
            case "May":
            case "5":
                mes = activity.getResources().getString(R.string.mes_maio);
                break;
            case "Jun":
            case "6":
                mes = activity.getResources().getString(R.string.mes_junho);
                break;
            case "Jul":
            case "7":
                mes = activity.getResources().getString(R.string.mes_julho);
                break;
            case "Aug":
            case "8":
                mes = activity.getResources().getString(R.string.mes_agosto);
                break;
            case "Sep":
            case "9":
                mes = activity.getResources().getString(R.string.mes_setembro);
                break;
            case "Oct":
            case "10":
                mes = activity.getResources().getString(R.string.mes_outubro);
                break;
            case "Nov":
            case "11":
                mes = activity.getResources().getString(R.string.mes_novembro);
                break;
            case "Dez":
            case "12":
                mes = activity.getResources().getString(R.string.mes_dezembro);
                break;
            default:
                mes = "";
        }
        return mes;
    }

}