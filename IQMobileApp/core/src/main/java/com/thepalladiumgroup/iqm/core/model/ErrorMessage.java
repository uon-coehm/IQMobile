package com.thepalladiumgroup.iqm.core.model;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/05/07]
 */
public class ErrorMessage {

    public static String getDatabaseError(SQLException exception) {

        return getSQLException(exception);
    }

    private static String getSQLException(SQLException ex) {

        String message = "";
        String cause = "";
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                message = e.getMessage();
                Throwable t = ex.getCause();
                while (t != null) {
                    cause = t.toString();
                    t = t.getCause();
                }
            }
        }
        return message + "" + cause;
    }
}
