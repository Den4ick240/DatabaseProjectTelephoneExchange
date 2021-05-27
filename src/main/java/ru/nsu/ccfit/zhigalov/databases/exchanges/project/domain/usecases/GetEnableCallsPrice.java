package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class GetEnableCallsPrice {
    private final DatabaseController databaseController;

    public GetEnableCallsPrice(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public void getLocalCallsEnablePrice(Subscription subscription, ObjectListener<Double> listener, ErrorListener errorListener) {
        runFunction(Consts.getSchemaName() + "get_local_calls_enable_price", subscription, listener, errorListener);
    }
    public void getLdCallsEnablePrice(Subscription subscription, ObjectListener<Double> listener, ErrorListener errorListener) {
        runFunction(Consts.getSchemaName() + "get_ld_calls_enable_price", subscription, listener, errorListener);
    }

    private void runFunction(String functionName, Subscription subscription, ObjectListener<Double> listener, ErrorListener errorListener) {
        try {
            Connection conn = databaseController.getConnection();
            CallableStatement cstmt = null;
            cstmt = conn.prepareCall("{? = call " + functionName + "(?)}");
            cstmt.registerOutParameter(1, Types.DOUBLE);
            cstmt.setInt(2, subscription.getId());
            cstmt.executeUpdate();
            Double price = cstmt.getDouble(1);
            cstmt.close();
            listener.onReceived(price);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            errorListener.onError(throwables.getMessage());
        }
    }
}
