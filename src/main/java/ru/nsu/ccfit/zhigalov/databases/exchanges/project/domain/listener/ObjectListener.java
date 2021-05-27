package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener;

public interface ObjectListener<T>{
    void onReceived(T object);
}
