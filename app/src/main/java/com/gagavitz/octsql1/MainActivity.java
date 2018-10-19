package com.gagavitz.octsql1;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ItemAdapter itemAdapter;
    Context thisContext;
    ListView lvGeneral;
    TextView tvProgress;
    Map<String, Double> fruitsMap = new LinkedHashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        lvGeneral = findViewById(R.id.lvGeneral);
        tvProgress = findViewById(R.id.tvProgress);
        thisContext = this;

        tvProgress.setText("");
        Button btn = findViewById(R.id.btnGetData);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });
    }

    private class GetData extends AsyncTask<String, String, String> {

        String msg = "";
        static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://" + DbStrings.DATABASE_URL + "/" + DbStrings.DATABASE_NAME;

        @Override
        protected void onPreExecute() {
            tvProgress.setText("Connecting to the Database...");
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);

                stmt = conn.createStatement();
                String sql = "SELECT * FROM fruits";
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");

                    fruitsMap.put(name, price);
                }

                msg = "Process complete!";

                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException connError) {
                msg = "An exception was thrown for JDBC.";
                connError.printStackTrace();
            } catch (ClassNotFoundException e) {
                msg = "A class not found exception was thrown.";
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            tvProgress.setText(this.msg);

            if (fruitsMap.size() > 0) {

                itemAdapter = new ItemAdapter(thisContext, fruitsMap);
                lvGeneral.setAdapter(itemAdapter);
            }
        }
    }
} //End of Main Activity
