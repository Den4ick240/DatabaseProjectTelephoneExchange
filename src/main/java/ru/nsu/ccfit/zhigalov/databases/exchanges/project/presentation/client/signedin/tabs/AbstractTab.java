package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;

public abstract class AbstractTab implements View {
    @FXML
    private Node node;

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    private String tabName;

    @Override
    public String toString() {
        return tabName;
    }

    public Node getNode() {
        return node;
    }
}
