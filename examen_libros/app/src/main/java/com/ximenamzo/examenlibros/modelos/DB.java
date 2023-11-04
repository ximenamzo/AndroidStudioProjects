package com.ximenamzo.examenlibros.modelos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;

public class DB {
    public static void buscar(Context context, String datoQueBusco, String tablaDondeBusco, String campoDondeBusco) {
        SQLiteDatabase bd = new Connect(context, Variables.NOMBRE_BD, null, Connect.APPVERSION).getReadableDatabase();
        String consulta = "SELECT " + Variables.CAMPO_ID2[0] + " FROM " + tablaDondeBusco + " WHERE " + campoDondeBusco + " LIKE ?";
        String className;

        try {
            Cursor cursor;
            cursor = bd.rawQuery(consulta, new String[]{"%" + datoQueBusco + "%"});

            if (cursor.getCount() > 1) {
                cursor.close();
                className = "com.ximenamzo.examenlibros.vistas."+tablaDondeBusco+".lista_"+tablaDondeBusco+"_custom";

                    try {
                        Class<?> customClass = Class.forName(className);
                        Intent i = new Intent(context, customClass);
                        i.putExtra("campo", campoDondeBusco);
                        i.putExtra("dato", datoQueBusco);
                        context.startActivity(i);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


            } else if (cursor.getCount() == 1) {

                cursor.moveToFirst();
                Log.d("DEBUGXX", "Id del "+campoDondeBusco+" encontrado: "+cursor.getString(0));
                String id = cursor.getString(0);
                cursor.close();
                String objeto = tablaDondeBusco;
                objeto = objeto.substring(0, objeto.length() - 1);
                className = "com.ximenamzo.examenlibros.vistas."+tablaDondeBusco+".detalle_" + objeto;
                try {
                    Class<?> customClass = Class.forName(className);
                    Intent i = new Intent(context, customClass);
                    i.putExtra("id", id);
                    context.startActivity(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "No hay titulos que coincidan con el "+campoDondeBusco+" '"+datoQueBusco+"'", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al buscar el "+campoDondeBusco+": " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
    }
}
