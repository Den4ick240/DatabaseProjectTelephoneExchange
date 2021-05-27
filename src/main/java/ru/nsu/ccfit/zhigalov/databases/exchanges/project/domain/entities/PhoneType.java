package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public enum PhoneType {
    UNOCCUPIED, PUBLIC, PARALLEL, PAIRED, SIMPLE;

    public String russianString() {
        switch (this) {
            case UNOCCUPIED:
                return "��������";
            case PAIRED:
                return "���������";
            case PUBLIC:
                return "��������";
            case SIMPLE:
                return "�������";
            case PARALLEL:
                return "������������";
        }
        return null;
    }
}
