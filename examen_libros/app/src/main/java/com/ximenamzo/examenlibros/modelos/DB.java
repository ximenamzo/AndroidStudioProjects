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
        Log.d("DEBUG_DBMODEL.15", "ENTRAMOS A BUSCAR EN LA DB...");
        SQLiteDatabase bd = new Connect(context, Variables.NOMBRE_BD, null, Connect.APPVERSION).getReadableDatabase();
        String consulta = "SELECT " + Variables.CAMPO_IDS[0] + " FROM " + tablaDondeBusco + " WHERE " + campoDondeBusco + " LIKE ?";
        String className;

        try {
            Cursor cursor;
            cursor = bd.rawQuery(consulta, new String[]{"%" + datoQueBusco + "%"});

            if (cursor.getCount() > 1) {
                Log.d("DEBUG_DBMODEL.25", "Más de 1 dato encontrado");
                cursor.close();
                className = "com.ximenamzo.examenlibros.vistas."+tablaDondeBusco+".lista_"+tablaDondeBusco+"_custom";

                    try {
                        Class<?> customClass = Class.forName(className);
                        Intent i = new Intent(context, customClass);
                        i.putExtra("campo", campoDondeBusco);
                        i.putExtra("dato", datoQueBusco);
                        bd.close();
                        context.startActivity(i);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


            } else if (cursor.getCount() == 1) {

                cursor.moveToFirst();
                Log.d("DEBUG_DBMODEL.44", "Id del "+campoDondeBusco+" encontrado: "+cursor.getString(0));
                String id = cursor.getString(0);
                cursor.close();
                String objeto = tablaDondeBusco;
                Log.d("DEBUG_DBMODEL.48", "Objeto: "+objeto);
                objeto = objeto.substring(0, objeto.length() - 1);
                Log.d("DEBUG_DBMODEL.50", "Objeto: "+objeto);
                className = "com.ximenamzo.examenlibros.vistas."+tablaDondeBusco+".detalle_" + objeto;
                Log.d("DEBUG_DBMODEL.52", "ClassName: "+className);
                try {
                    Class<?> customClass = Class.forName(className);
                    Intent i = new Intent(context, customClass);
                    i.putExtra("id", id);
                    Log.d("DEBUG_DBMODEL.57", "Id del "+objeto+": "+id);
                    bd.close();
                    context.startActivity(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "No hay "+tablaDondeBusco+" que coincidan con el "+campoDondeBusco+" '"+datoQueBusco+"'", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al buscar el "+campoDondeBusco+": " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
    }
}
